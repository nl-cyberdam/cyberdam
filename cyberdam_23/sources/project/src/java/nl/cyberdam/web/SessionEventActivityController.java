package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.EventActivity;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.web.util.ActivityHelper;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

//We are not a form controller yet, but I have a distinct feeling we'll be one in the future
public class SessionEventActivityController extends AbstractController {

    private GameManager gameManager;

    private MenuUtil menuUtil;

    private SessionReportLogService sessionReportLogService;

    private ScriptManager scriptManager;

	public void setSessionReportLogService(SessionReportLogService sessionReportLogService) {
		if (this.sessionReportLogService != sessionReportLogService) {
			this.sessionReportLogService = sessionReportLogService;
		}
	}

    public void setGameManager(GameManager gameManager) {
	this.gameManager = gameManager;
    }

    private GameManager getGameManager() {
	return gameManager;
    }

    @SuppressWarnings("boxing")
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
	    HttpServletResponse response) throws Exception {
	Long activityId = ServletRequestUtils.getRequiredLongParameter(request, "activityId");
	EventActivity activity = gameManager.loadEventActivity(activityId);
	Long participantId = ServletRequestUtils.getRequiredLongParameter(request, "participantId");
	Participant participant = gameManager.loadParticipant(participantId);

	// check for _cancel
	String doCancel = ServletRequestUtils.getStringParameter(request, "_cancel");
	if (doCancel != null) {
	    return new ModelAndView(new RedirectView("session.htm", true), "participantId",
		    participantId);
	}

        ModelAndView disabledRedirect = ActivityHelper.RedirectDisabledActivity(participant, activity, "session.htm", scriptManager);
        if (disabledRedirect != null)
        {
            return disabledRedirect;
        }

	String doSubmit = ServletRequestUtils.getStringParameter(request, "_ok");
    String scriptError = null;
	if (doSubmit != null) {
		getScriptManager().CompletedActivity(activity, participant, null);
		// Session report log
		participant.getCompletedActivities().add(activity);
		getGameManager().save(participant);
		sessionReportLogService.logEventActivity(activity, participant);
		return new ModelAndView(new RedirectView("session.htm", true), "participantId",
				participantId);
	}
	
	Map<String, Object> data = new HashMap<String, Object>();
	menuUtil.generateMenu(request, MenuUtil.GAME_MENU, participant.getGameSession()
		.getManifest());

	data.put("activity", activity);
	data.put("substitutedInstructions", gameManager.substituteVariablesInText(participant,
		activity.getInstructions()));
	data.put("participant", participant);
	data.put("statusText", SessionTools.getStatusText(participant, gameManager));
	data.put("scriptError", scriptError);

	return new ModelAndView("sessioneventactivity", data);
    }

    public void setMenuUtil(MenuUtil menuUtil) {
	this.menuUtil = menuUtil;
    }

    public void setScriptManager(ScriptManager scriptManager) {
	this.scriptManager = scriptManager;
    }

    public ScriptManager getScriptManager() {
	return scriptManager;
    }
}
