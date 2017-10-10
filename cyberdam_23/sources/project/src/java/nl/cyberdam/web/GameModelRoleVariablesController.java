package nl.cyberdam.web;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.ItemInUseException;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 */
public class GameModelRoleVariablesController extends AbstractController {

	private GameManager gameManager;
	private MessageSource messageSource;
	private Role role;
	public static final String ROLE_ATTR = GameModelDetailController.ROLE_ATTR;
	public static final String VARIABLE_ATTR = GameModelDetailController.VARIABLE_ATTR;
	public static final String ROLEVARIABLES_ATTR = "roleVariables";

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long roleId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
		role = gameManager.loadRole(roleId);
		request.getSession(true).setAttribute(ROLE_ATTR, role);

		RefreshablePagedListHolder variables = 
			getListHolder(request, ROLEVARIABLES_ATTR + roleId.toString(), new VariableProvider());

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(ROLEVARIABLES_ATTR, variables);

		String action = ServletRequestUtils.getStringParameter(request, "action");
		if (action == null && ServletRequestUtils.getStringParameter(request, "sort.property") != null) {
			action = "sort";
		}
		if (action == null && ServletRequestUtils.getBooleanParameter(request, "forceRefresh") != null) {
			action = "refresh";
		}
		if (action != null) {
			if (action.equals("refresh")) {
				variables.refresh(true);
			} else if (action.equals("sort")) {
				variables.resort();
			} else if (action.equals("delete")) {
				Long objectId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "objectId"));
				try {
					role.removeVariableById(objectId);
					gameManager.save(role);
					data.put("forceRefresh", "true");
					data.put("id", roleId);
					return new ModelAndView(new RedirectView("gamemodelrolevariables.htm"), data);
				} catch (ItemInUseException iiue) {
					Locale userLocale = GameUtil.getCurrentUser().getLocale();
					String error = messageSource.getMessage(iiue.getMultiLanguageKey(), null, userLocale );
					data.put("iiuerror", MessageFormat.format(error, iiue.getUsedBy()));
				}
			} else if (action.equals("add")) {
				Variable v = new Variable();
				role.addVariable(v);
				request.getSession(true).setAttribute(VARIABLE_ATTR, v);
				return new ModelAndView(new RedirectView("gamemodelvariableedit.htm"));
			} else if (action.equals("edit")) {
				Long objectId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "objectId"));
				Variable v = role.getVariableById(objectId);
				request.getSession(true).setAttribute(VARIABLE_ATTR, v);
				return new ModelAndView(new RedirectView("gamemodelvariableedit.htm"));
			}
		}
		return new ModelAndView("gamemodelrolevariables", data);
	}

	private RefreshablePagedListHolder getListHolder(HttpServletRequest request, String attr, PagedListSourceProvider sp) {
		RefreshablePagedListHolder listHolder =
			(RefreshablePagedListHolder) request.getSession(true).getAttribute(attr);
		if (listHolder == null) {
			listHolder = new RefreshablePagedListHolder();
			listHolder.setPageSize(gameManager.loadSystemParameters().getDefaultRows());
			listHolder.setSourceProvider(sp);
			// populate
			listHolder.refresh(true);
			request.getSession(true).setAttribute(attr, listHolder);
		}
		ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder);
		binder.bind(request);
		listHolder.setLocale(RequestContextUtils.getLocale(request));
		return listHolder;
	}

	private class VariableProvider implements PagedListSourceProvider, Serializable {
		@SuppressWarnings("unchecked")
		public List loadList(Locale loc, Object filter) {
			try {
				// use copy because original is unmodifiable
				return new ArrayList<Variable>(role.getVariables());
			} catch (org.springframework.orm.ObjectRetrievalFailureException e) {
				logger.debug("error retrieving  list of role variables", e);
				// return empty list on error
				return new ArrayList();
			}
		}
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
