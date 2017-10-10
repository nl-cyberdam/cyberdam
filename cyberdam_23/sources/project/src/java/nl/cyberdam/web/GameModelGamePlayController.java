package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.ItemInUseException;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.RoleActivity;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.web.customeditors.StepOfPlayEditor;

import org.json.JSONObject;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 * 
 */
public class GameModelGamePlayController extends CancellableFormController {

	private GameManager gameManager;
	private MessageSource messageSource;
	private GameModel gameModel;
	public static final String GAMEMODEL_ATTR = GameModelDetailController.GAMEMODEL_ATTR;
	public static final String GRIDDATA_ATTR = GameModelDetailController.GRIDDATA_ATTR;
	public static final String ACTIVITIES_ATTR = GameModelDetailController.ACTIVITIES_ATTR;

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
		throws Exception {
		binder.registerCustomEditor(StepOfPlay.class, "initialStepOfPlay", new StepOfPlayEditor(gameModel));
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		gameModel = (GameModel) request.getSession(true).getAttribute(GAMEMODEL_ATTR);
		return gameModel;
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request)
			throws Exception {
		Map<String, Object> referenceData = new HashMap<String, Object>();
		List<Activity> activityList = new ArrayList<Activity>(gameModel.getActivities());
		Collections.sort(activityList, new Comparator<Activity>() {
			public int compare(Activity a1, Activity a2) {
				return a1.getName().compareTo(a2.getName());
			}
		});
		referenceData.put("activityList", activityList);
		return referenceData;
	}

	protected void onBind(HttpServletRequest request, Object command, BindException errors)
	throws Exception {
		String gridData = ServletRequestUtils.getRequiredStringParameter(request, "gridData");
		//System.err.printf("incoming json string: [%s]\n", gridData);
		JSONObject jsonin = new JSONObject(gridData);
		gameModel.mapFromGridData(jsonin);
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
	throws Exception {
		String action = ServletRequestUtils.getStringParameter(request, "action");
		if ("".equals(action)) {
			Pattern p = Pattern.compile("\\s*");
			for (Role r: gameModel.getRoles()) {
				Matcher m = p.matcher(r.getName());
				if (m.matches()) {
					errors.reject("empty.role");
					break;
				}
			}
			for (StepOfPlay s: gameModel.getStepsOfPlay()) {
				Matcher m = p.matcher(s.getName());
				if (m.matches()) {
					errors.reject("empty.step");
					break;
				}
			}
			if (gameModel.getInitialStepOfPlay() == null && gameModel.getStepsOfPlay().size() > 0) {
				errors.reject("empty.initialstep");
			}
		} else if ("deleteRole".equals(action) || "addRole".equals(action)) {
			if (!gameModel.getGameManifests().isEmpty()) {
				errors.reject("gamemodel.inuse.by.gamemanifest");
			}
		}
		if (errors.hasErrors()) {
			Long firstIndex = new Long(ServletRequestUtils.getRequiredLongParameter(request, "firstIndex"));
			JSONObject jsonout = gameModel.mapToGridData();
			jsonout.put("firstIndex", firstIndex);
			request.setAttribute(GRIDDATA_ATTR, this.gameModel.escapeQuote(jsonout.toString()));
		}
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		String action = ServletRequestUtils.getRequiredStringParameter(request, "action");
		Long firstIndex = new Long(ServletRequestUtils.getRequiredLongParameter(request, "firstIndex"));
		Map<String, Object> data = new HashMap<String, Object>();
		if (!"".equals(action)) {
			Locale userLocale = GameUtil.getCurrentUser().getLocale();
			try {
				if (action.equals("deleteRole")) {
					int index = ServletRequestUtils.getRequiredIntParameter(request, "deleteIndex");
					gameModel.removeRoleByIndex(index);
				} else if (action.equals("deleteStep")) {
					int deleteIndex = ServletRequestUtils.getRequiredIntParameter(request, "deleteIndex");
					int index = gameModel.getRoles().indexOf(gameModel.getInitialStepOfPlay());
					if (index == deleteIndex) {
						gameModel.setInitialStepOfPlay(null);
					}
					gameModel.removeStepOfPlayByIndex(deleteIndex);
				} else if (action.equals("deleteActivity")) {
					Long activityId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "activityId"));
					int stepIndex = ServletRequestUtils.getRequiredIntParameter(request, "stepIndex");
					int roleIndex = ServletRequestUtils.getRequiredIntParameter(request, "roleIndex");
					StepOfPlay step = gameModel.getStepsOfPlay().get(stepIndex);
					Role role = gameModel.getRoles().get(roleIndex);
					step.removeRoleActivity(role, activityId);
				} else if (action.equals("addRole")) {
					Role r = new Role();
					String name = messageSource.getMessage("new.role", null, userLocale );
					r.setName(name);
					gameModel.addRole(r);
				} else if (action.equals("addStep")) {
					StepOfPlay s = new StepOfPlay();
					String name = messageSource.getMessage("new.stepofplay", null, userLocale );
					s.setName(name);
					gameModel.addStepOfPlay(s);
				} else if (action.equals("addActivity")) {
					Long activityId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "activityId"));
					int stepIndex = ServletRequestUtils.getRequiredIntParameter(request, "stepIndex");
					int roleIndex = ServletRequestUtils.getRequiredIntParameter(request, "roleIndex");
					Activity activity = gameModel.getActivityById(activityId);
					StepOfPlay step = gameModel.getStepsOfPlay().get(stepIndex);
					Role role = gameModel.getRoles().get(roleIndex);
					RoleActivity ra = new RoleActivity(role, activity);
					step.addRoleActivity(ra);
				} else if (action.equals("changeRoleOrder")) {
					int oldIndex = ServletRequestUtils.getRequiredIntParameter(request, "oldIndex");
					int newIndex = ServletRequestUtils.getRequiredIntParameter(request, "newIndex");
					gameModel.changeRoleOrder(oldIndex, newIndex);
				} else if (action.equals("changeStepOrder")) {
					int oldIndex = ServletRequestUtils.getRequiredIntParameter(request, "oldIndex");
					int newIndex = ServletRequestUtils.getRequiredIntParameter(request, "newIndex");
					gameModel.changeStepOrder(oldIndex, newIndex);
				} else {
					errors.reject("Unknown action: " + action);
				}
			} catch (DataIntegrityViolationException dive) {
				errors.reject(dive.getMessage());
			} catch (ItemInUseException iiue) {
				errors.reject(iiue.getMultiLanguageKey(), new String[]{iiue.getUsedBy()}, null);
			}
			JSONObject jsonout = gameModel.mapToGridData();
			jsonout.put("firstIndex", firstIndex);
			if (action.equals("addStep")) jsonout.put("addStep", true);
			data.put(GRIDDATA_ATTR, this.gameModel.escapeQuote(jsonout.toString()));
			return showForm(request, response, errors, data);
		}
		gameManager.save(gameModel);
		data.put("id", gameModel.getId());
		return new ModelAndView(getSuccessView(), data);
	}

	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		return new ModelAndView(getCancelView(), "id", gameModel.getId());
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
