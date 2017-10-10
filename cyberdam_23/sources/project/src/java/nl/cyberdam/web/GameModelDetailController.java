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

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.EventActivity;
import nl.cyberdam.domain.FileUploadActivity;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.ItemInUseException;
import nl.cyberdam.domain.MessageActivity;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.Resource;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;

import org.acegisecurity.context.SecurityContextHolder;
import org.json.JSONObject;
import org.springframework.beans.support.MutableSortDefinition;
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
public class GameModelDetailController extends AbstractController {

	private GameManager gameManager;
	private MessageSource messageSource;
	private GameModel gameModel;
	public static final String GAMEMODEL_ATTR = "gameModel";
	public static final String RESOURCES_ATTR = "resources";
	public static final String VARIABLES_ATTR = "variables";
	public static final String STEPOFPLAY_ATTR = "stepofplay";
	public static final String ACTIVITIES_ATTR = "activities";
	public static final String VARIABLE_ATTR = "variable";
	public static final String ACTIVITY_ATTR = "activity";
	public static final String GRIDDATA_ATTR  = "gridData";
	public static final String ROLE_ATTR  = "role";

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long gameModelId = ServletRequestUtils.getLongParameter(request, "id");
		if (gameModelId == null) {
			gameModel = new GameModel();
			Locale userLocale = GameUtil.getCurrentUser().getLocale();
			gameModel.setName(messageSource.getMessage("default.newmodel", null, userLocale ));
			User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			gameModel.setLastModifier(currentUser);
			gameModel.setOwner(currentUser);
			gameManager.save(gameModel);
			gameModelId = gameModel.getId();
		} else {
			gameModel = gameManager.load(gameModelId);
			gameModel.initialize();
		}
		request.getSession(true).setAttribute(GAMEMODEL_ATTR, gameModel);
		JSONObject json = gameModel.mapToGridData();
		request.getSession(true).setAttribute(GRIDDATA_ATTR, this.gameModel.escapeQuote(json.toString()));

		RefreshablePagedListHolder resources = 
			getListHolder(request, RESOURCES_ATTR + gameModelId.toString(), new ResourceProvider());
		RefreshablePagedListHolder variables = 
			getListHolder(request, VARIABLES_ATTR + gameModelId.toString(), new VariableProvider());
		RefreshablePagedListHolder activities = 
			getListHolder(request, ACTIVITIES_ATTR + gameModelId.toString(), new ActivityProvider());

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(RESOURCES_ATTR, resources);
		data.put(VARIABLES_ATTR, variables);
		data.put(ACTIVITIES_ATTR, activities);

		String action = ServletRequestUtils.getStringParameter(request, "action");
		if (action == null && ServletRequestUtils.getStringParameter(request, "sort.property") != null) {
			action = "sort";
		}
		if (action == null && ServletRequestUtils.getBooleanParameter(request, "forceRefresh") != null) {
			action = "refresh";
		}
		if (action != null) {
			String object = ServletRequestUtils.getStringParameter(request, "object");
			if (action.equals("refresh")) {
				resources.refresh(true);
				variables.refresh(true);
				activities.refresh(true);
			} else if (action.equals("sort")) {
				if (object.equals(RESOURCES_ATTR)) resources.resort();
				else if (object.equals(VARIABLES_ATTR)) variables.resort();
			} else if (action.equals("copy")) {
				Long activityId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "objectId"));
				Activity a = gameModel.getActivityById(activityId);
				Locale userLocale = GameUtil.getCurrentUser().getLocale();
				String copyOf = messageSource.getMessage("copy.of", null, userLocale);
				gameModel.addCopyOfActivity(a, copyOf + ": " + a.getName());
				gameManager.save(gameModel);
				data.put("forceRefresh","true");
				data.put("id", gameModelId);
				return new ModelAndView(new RedirectView("gamemodeldetail.htm#activities_section"), data);
			} else if (action.equals("delete")) {
				Long objectId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "objectId"));
				try {
					if (object.equals(RESOURCES_ATTR)) gameModel.removeResourceById(objectId);
					else if (object.equals(VARIABLES_ATTR)) gameModel.removeVariableById(objectId);
					else if (object.equals(ACTIVITIES_ATTR)) gameModel.removeActivityById(objectId);
					gameManager.save(gameModel);
					data.put("forceRefresh","true");
					data.put("id", gameModelId);
					return new ModelAndView(new RedirectView("gamemodeldetail.htm#" + object + "_section"), data);
				} catch (ItemInUseException iiue) {
					Locale userLocale = GameUtil.getCurrentUser().getLocale();
					String error = messageSource.getMessage(iiue.getMultiLanguageKey(), null, userLocale );
					data.put(object + "iiuerror", MessageFormat.format(error, iiue.getUsedBy()));
				}
			} else if (action.equals("add")) {
				if (object.equals(VARIABLES_ATTR)) {
					Variable v = new Variable();
					gameModel.addVariable(v);
					request.getSession(true).setAttribute(VARIABLE_ATTR, v);
					request.getSession(true).removeAttribute(ROLE_ATTR);
					return new ModelAndView(new RedirectView("gamemodelvariableedit.htm"));
				} else if (object.equals(ACTIVITIES_ATTR)) {
					String type = ServletRequestUtils.getRequiredStringParameter(request, "type");
					Activity activity;
					if (type.equals("fileupload")) {
						activity = new FileUploadActivity();
					} else if (type.equals("message")) {
						activity = new MessageActivity();
					} else if (type.equals("progress")) {
						activity = new ProgressActivity();
					} else if (type.equals("form")) {
						activity = new FormActivity();
					} else if (type.equals("event")) {
						activity = new EventActivity();
					} else {
						throw new IllegalArgumentException("unknown type: " + type);
					}
					activity.setGameModel(gameModel);
					gameModel.addActivity(activity);
					request.getSession(true).setAttribute(ACTIVITY_ATTR, activity);
					return new ModelAndView(new RedirectView("gamemodelactivityedit.htm"));
				}
			} else if (action.equals("edit")) {
				Long objectId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "objectId"));
				if (object.equals(VARIABLES_ATTR)) {
					Variable v = gameManager.loadVariable(objectId);
					request.getSession(true).setAttribute(VARIABLE_ATTR, v);
					request.getSession(true).removeAttribute(ROLE_ATTR);
					return new ModelAndView(new RedirectView("gamemodelvariableedit.htm"));
				} else if (object.equals(ACTIVITIES_ATTR)) {
					Activity a = gameModel.getActivityById(objectId);
					request.getSession(true).setAttribute(ACTIVITY_ATTR, a);
					return new ModelAndView(new RedirectView("gamemodelactivityedit.htm"));
				} else if (object.equals(STEPOFPLAY_ATTR)) {
					StepOfPlay step = gameModel.getStepsOfPlay().get(objectId.intValue());
					request.getSession(true).setAttribute(STEPOFPLAY_ATTR, step);
					return new ModelAndView(new RedirectView("gamemodelstepdetails.htm"));
				}
			} else if (action.equals("view")) {
				Long objectId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "objectId"));
				if (object.equals(VARIABLES_ATTR)) {
				} else if (object.equals(ACTIVITIES_ATTR)) {
				} else if (object.equals(RESOURCES_ATTR)) {
					Resource resource = gameModel.getResourceById(objectId);
					data.clear();
					data.put("resource", resource);
					return new ModelAndView("gamemodelresourceviewer", data);
				}
			}
		}
		return new ModelAndView("gamemodeldetail", data);
	}

	private RefreshablePagedListHolder getListHolder(HttpServletRequest request, String attr, PagedListSourceProvider sp) {
		RefreshablePagedListHolder listHolder =
			(RefreshablePagedListHolder) request.getSession(true).getAttribute(attr);
		if (listHolder == null) {
			listHolder = new RefreshablePagedListHolder(sp);
			listHolder.setPageSize(Integer.MAX_VALUE);
			MutableSortDefinition msd = new MutableSortDefinition("name", true, true);
			msd.setToggleAscendingOnProperty(true);
			listHolder.setSort(msd);
			// populate
			listHolder.refresh(true);
			request.getSession(true).setAttribute(attr, listHolder);
		}
		ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder);
		binder.bind(request);
		listHolder.setLocale(RequestContextUtils.getLocale(request));
		return listHolder;
	}

	private class ResourceProvider implements PagedListSourceProvider, Serializable {
		@SuppressWarnings("unchecked")
		public List loadList(Locale loc, Object filter) {
			try {
				// use copy because original is unmodifiable
				return new ArrayList<Resource>(gameModel.getResources());
			} catch (org.springframework.orm.ObjectRetrievalFailureException e) {
				logger.debug("error retrieving  list of resources", e);
				// return empty list on error
				return new ArrayList();
			}
		}
	}

	private class VariableProvider implements PagedListSourceProvider, Serializable {
		@SuppressWarnings("unchecked")
		public List loadList(Locale loc, Object filter) {
			try {
				// use copy because original is unmodifiable
				return new ArrayList<Variable>(gameModel.getVariables());
			} catch (org.springframework.orm.ObjectRetrievalFailureException e) {
				logger.debug("error retrieving  list of variables", e);
				// return empty list on error
				return new ArrayList();
			}
		}
	}

	private class ActivityProvider implements PagedListSourceProvider, Serializable {
		@SuppressWarnings("unchecked")
		public List loadList(Locale loc, Object filter) {
			try {
				// use copy because original is unmodifiable
				return new ArrayList<Activity>(gameModel.getActivities());
			} catch (org.springframework.orm.ObjectRetrievalFailureException e) {
				logger.debug("error retrieving  list of activities", e);
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
