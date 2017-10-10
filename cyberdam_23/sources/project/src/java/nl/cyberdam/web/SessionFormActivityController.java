package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.ActivityVariable;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.domain.VariableSessionValue;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.web.util.ActivityHelper;
import nl.cyberdam.web.util.MenuUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Send message from within the contect of an activity - for general message
 * sending see SendMessageController.
 */
public class SessionFormActivityController extends
		CancellableFormController {
	public class ListWrapper implements java.io.Serializable {
		public ListWrapper() {
		}

		public List<VariableSessionValue> variableValues;
		public List<Boolean> enabledList;
		
		public List<VariableSessionValue> getVariableValues () {
			return variableValues;
		}

		public List<Boolean> getEnabledList() {
			return enabledList;
		}
	}

	Log logger = LogFactory.getLog(getClass());
	GameManager gameManager;
	protected SessionReportLogService sessionReportLogService;
	private MenuUtil menuUtil;
	private SessionFactory sessionFactory;
	private ScriptManager scriptManager;

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setSessionReportLogService(
			SessionReportLogService sessionReportLogService) {
		this.sessionReportLogService = sessionReportLogService;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public SessionFormActivityController() {
		setCommandClass(ListWrapper.class);
		setCommandName("varList");
		// Dont do session form - it will cause hibernate errors if we don't
		// reattach the objects
		// setSessionForm(true);
	}

	@Override
	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {
		// gets gameModels by id
		// binder.registerCustomEditor(Participant.class, "recipients",
		// new RoleToParticipantMappingEditor(gameManager));
		// binder.registerCustomEditor(SessionResource.class, "attachments",
		// new SessionResourceEditor(gameManager));
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request)
			throws Exception {
		Map<String, Object> referenceData = new HashMap<String, Object>();
		Long participantId = ServletRequestUtils.getLongParameter(request,
				"participantId");
		Participant participant = gameManager.loadParticipant(participantId);
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU, participant
				.getGameSession().getManifest());
		Long activityId = ServletRequestUtils.getLongParameter(request,
				"activityId");
		FormActivity activity = getGameManager().loadFormActivity(
				activityId);
		// if there is a related Action
		referenceData.put("activity", activity);

		// all session pages need the "participant"
		referenceData.put("participant", participant);
		referenceData.put("substitutedInstructions", gameManager.substituteVariablesInText (participant, activity.getInstructions()));
		referenceData.put("statusText", SessionTools.getStatusText(participant, gameManager));
		
		return referenceData;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		Long participantId = ServletRequestUtils.getLongParameter(request,
				"participantId");
		Participant participant = gameManager.loadParticipant(participantId);
		GameSession session = participant.getGameSession();

		Long activityId = ServletRequestUtils.getLongParameter(request,
				"activityId");
		FormActivity activity = getGameManager().loadFormActivity(
				activityId);
		List<ActivityVariable> activityVariables = activity.getActivityVariables();
		Collections.sort(activityVariables);
		List<VariableSessionValue> values = gameManager
				.findVariableValuesForSession(session);
		Long roleId = participant.getRoleAndPlayground().getRole().getId();
		ArrayList<VariableSessionValue> valuesArray = new ArrayList<VariableSessionValue>();
		ArrayList<Boolean> enabledList = new ArrayList<Boolean>();
		for (ActivityVariable activityVariable : activityVariables) {
			Variable variable = activityVariable.getVariable();
			VariableSessionValue value = null;
			for (VariableSessionValue oldValue : values) {
				if (activityVariable.getVariable ().getId().equals(oldValue.getVariable ().getId())) {
					value = oldValue;
				}
			}
			if (value == null) {
				// create a value from the variable's initialValue now
				value = new VariableSessionValue();
				value.setVariable(variable);
				value.setValue(variable.getInitialValue());
				value.setGameSession (session);
			}
			value.setActivityVariable(activityVariable);
			valuesArray.add(value);
			if (variable.getRole() == null) {
				enabledList.add(new Boolean(true));
			} else {
				enabledList.add(new Boolean(roleId.equals(variable.getRole().getId())));
			}
		}
		ListWrapper wrapper = new ListWrapper();
		wrapper.variableValues = valuesArray;
		wrapper.enabledList = enabledList;
		return wrapper;
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		Long activityId = ServletRequestUtils.getLongParameter(request,
				"activityId");
		FormActivity activity = getGameManager().loadFormActivity(
				activityId);
		Long participantId = ServletRequestUtils.getLongParameter(request,
				"participantId");
		Participant participant = gameManager.loadParticipant(participantId);

		ModelAndView disabledRedirect = ActivityHelper.RedirectDisabledActivity(participant, activity, "session.htm", scriptManager);
		if (disabledRedirect != null) {
			return disabledRedirect;
		}

		ListWrapper listWrapper = (ListWrapper) command;
		List<VariableSessionValue> variableValues = listWrapper.getVariableValues();
		List<Boolean> enabledList = listWrapper.getEnabledList();
		for (int valueIndex = 0 ; valueIndex < variableValues.size() ; ++valueIndex) {
			if (enabledList.get(valueIndex).booleanValue()) {
				VariableSessionValue variableValue = variableValues.get(valueIndex);
	   			Pattern p = Pattern.compile("\\s*");
	   			Matcher m = p.matcher(variableValue.getValue());
	   			if (m.matches()) {
	   				variableValue.setValue(null);
	   			}
				getSessionFactory().getCurrentSession().saveOrUpdate(variableValue);
			}
		}
		getScriptManager().CompletedActivity(activity, participant, null);
		// log
		getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM,
				"setting variables for activity", activity.getName());
		// Session report log
		sessionReportLogService.logFormActivity(activity, variableValues,
				participant);
		// set activity to completed
		participant.addCompletedActivity(activity);
		getGameManager().save(participant);
		getScriptManager().CompletedActivity(activity, participant, null);
		return new ModelAndView(new RedirectView(getSuccessView(), true),
				"participantId", participantId);
	}

	@Override
	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		Long participantId = ServletRequestUtils.getLongParameter(request,
				"participantId");
		return new ModelAndView(new RedirectView(getCancelView(), true),
				"participantId", participantId);
	}

	@SuppressWarnings("boxing")
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
		HttpServletResponse response, BindException errors)
	throws Exception {
	    Long activityId = ServletRequestUtils.getLongParameter(request,
	    "activityId");
	    Long participantId = ServletRequestUtils.getRequiredLongParameter(
		    request, "participantId");
	    Participant participant = getGameManager().loadParticipant(
		    participantId);
	    Activity activity = getGameManager ().loadFormActivity (activityId);
	    ModelAndView disabledRedirect = ActivityHelper.RedirectDisabledActivity(participant, activity, "session.htm", scriptManager);
	    if (disabledRedirect != null)
	    {
		return disabledRedirect;
	    }
	    return super.showForm(request, response, errors);
	}


	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// Should be injected
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void setScriptManager(ScriptManager scriptManager) {
	    this.scriptManager = scriptManager;
	}

	public ScriptManager getScriptManager() {
	    return scriptManager;
	}

}
