package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.service.GameManager;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 */
public class GameModelVariableEditController extends CancellableFormController {

	private GameModel gameModel;
	private Role role;
	private GameManager gameManager;
	public static final String GAMEMODEL_ATTR = GameModelDetailController.GAMEMODEL_ATTR;
	public static final String VARIABLE_ATTR = GameModelDetailController.VARIABLE_ATTR;
	public static final String ROLE_ATTR = GameModelDetailController.ROLE_ATTR;

	@Override
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		gameModel = (GameModel) request.getSession(true).getAttribute(GAMEMODEL_ATTR);
		role = (Role) request.getSession(true).getAttribute(ROLE_ATTR);
		return request.getSession(true).getAttribute(VARIABLE_ATTR);
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
	throws Exception {
		Variable variable = (Variable) command;
		// check for unique name
		List <Variable> lv = new ArrayList<Variable>();
		if (role != null) {
			lv = role.getVariables();
		} else {
			lv = gameModel.getVariables();
		}
		boolean b = false;
		for (Variable v: lv) {
			if (!v.equals(variable) && v.getName().equals(variable.getName())) b = true;
		}
		if (b == true) {
			errors.reject("variable.alreadytaken");
		} else {
			Pattern p = Pattern.compile("\\s*");
			Matcher m = p.matcher(variable.getInitialValue());
			if (m.matches()) {
				variable.setInitialValue(null);
			}
		}
	}
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		try {
			if (role != null) {
				gameManager.save(role);
			} else {
				gameManager.save(gameModel);
			}
		} catch (DataIntegrityViolationException dive) {
			errors.reject(dive.getMessage());
			return showForm(request, response, errors);
		}
		Map<String, Object> data = new HashMap<String, Object>();
		String view;
		if (role != null) {
			data.put("id", role.getId());
			view = "gamemodelrolevariables.htm";
		} else {
			data.put("id", gameModel.getId());
			view = "gamemodeldetail.htm#variables_section";
		}
		data.put("forceRefresh", new Boolean(true));
		return new ModelAndView(new RedirectView(view), data);
	}

	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String view;
		if (role != null) {
			data.put("id", role.getId());
			view = "gamemodelrolevariables.htm";
		} else {
			data.put("id", gameModel.getId());
			view = "gamemodeldetail.htm#variables_section";
		}
		return new ModelAndView(new RedirectView(view), data);
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
