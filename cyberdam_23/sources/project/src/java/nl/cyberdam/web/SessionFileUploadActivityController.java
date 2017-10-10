package nl.cyberdam.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.FileUploadActivity;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.web.util.ActivityHelper;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

/**
 * controller for uploading a file in a game session
 */
public class SessionFileUploadActivityController extends AbstractFormController {
    
    private GameManager             gameManager;
    
    private MenuUtil                menuUtil;
    private ScriptManager scriptManager;
    
    private SessionReportLogService sessionReportLogService;
    
    public SessionFileUploadActivityController() {
        setCommandClass(SessionFileUploadBean.class);
    }
    
    public void setSessionReportLogService(
            SessionReportLogService sessionReportLogService) {
        this.sessionReportLogService = sessionReportLogService;
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    private GameManager getGameManager() {
        return gameManager;
    }
    
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        
        logger.info("process form submission");
        // cast the bean
        SessionFileUploadBean bean = (SessionFileUploadBean) command;
        
        // activityId is not required
        Long activityId = bean.getActivityId();
        Long participantId = bean.getParticipantId();
        Participant participant = getGameManager().loadParticipant(
                participantId);
        
        // check for _cancel
        if (WebUtils.hasSubmitParameter(request, "_cancel")) {
            if (activityId != null) {
                return new ModelAndView(
                        new RedirectView("activities.htm", true),
                        "participantId", participantId);
            } else {
                return new ModelAndView(new RedirectView("filedirectory.htm",
                        true), "participantId", participantId);
            }
        }
        
		if (activityId != null) {
			FileUploadActivity activity = getGameManager ().loadFileUploadActivity (activityId);
			ModelAndView disabledRedirect = ActivityHelper.RedirectDisabledActivity(participant, activity, "session.htm", scriptManager);
			if (disabledRedirect != null)
			{
				return disabledRedirect;
			}
		}

        // check is there were errors - if so: display the upload form again
        if (errors.hasErrors()) {
            return showForm(request, response, errors);
        }
        
        MultipartFile file = bean.getFile();
        SessionResource r = new SessionResource();
        r.setContent(file.getBytes());
        r.setName(file.getOriginalFilename());
        r.setFileSize((int) file.getSize());
        r.setCreated(new Date());
        r.setStepOfPlay(participant.getGameSession().getCurrentStepOfPlay());
        r.setParticipant (participant);
        
        // XXX is a save action needed after this?
        participant.getSessionResourceBox().getResources().add(r);
        
        // check if upload was part of an activity
        if (activityId != null) {
            FileUploadActivity activity = getGameManager ().loadFileUploadActivity (activityId);
            getScriptManager().CompletedActivity(activity, participant, null);
            // mark activity as completed.
            participant.getCompletedActivities().add(activity);
            // XXX I don't think this save should be necessary - but the
            // activity is not added if I leave this out
            getGameManager().save(participant);
            // Session report log
            getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM,
                    "fileupload activity", activity.getName());
            sessionReportLogService.logFileUploadActivity(activity, file,
					participant, r);
            // redirect to the session page
            return new ModelAndView(new RedirectView("session.htm", true), "participantId", participantId);
        }
        
        getGameManager().save(participant);
        getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM,
                "fileupload", "");
		sessionReportLogService.logFileUploadActivity(null, file,
				  participant, r);
        return new ModelAndView(new RedirectView("session.htm", true),
                "participantId", participantId);
    }
    
    @Override
    protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse response, BindException errors)
            throws Exception {
        
        // activityId is only supplied if the upload is part of an activity
        Long activityId = ServletRequestUtils.getLongParameter(request,
                "activityId");
        Long participantId = new Long(ServletRequestUtils.getRequiredLongParameter(
                request, "participantId"));
        Participant participant = getGameManager().loadParticipant(
                participantId);
        if (activityId != null) {
            FileUploadActivity activity = getGameManager ().loadFileUploadActivity (activityId);
        	ModelAndView disabledRedirect = ActivityHelper.RedirectDisabledActivity(participant, activity, "session.htm", scriptManager);
        	if (disabledRedirect != null)
        	{
        		return disabledRedirect;
        	}
        }

        Map<String, Object> data = new HashMap<String, Object>();
        menuUtil.generateMenu(request, MenuUtil.GAME_MENU, participant
                .getGameSession().getManifest());
        String doCancel = ServletRequestUtils.getStringParameter(request, "_cancel");
        if (doCancel != null) {
            // redirect
            data.put("participantId", participantId);
            return new ModelAndView(new RedirectView("session.htm", true), data);
        }
        
        if (activityId != null) {
			FileUploadActivity activity = getGameManager ().loadFileUploadActivity (activityId);
            data.put("activity", activity);
            data.put("substitutedInstructions", gameManager.substituteVariablesInText (participant, activity.getInstructions()));
        }
        data.put("participant", participant);
        data.put("statusText", SessionTools.getStatusText(participant, gameManager));
        if (errors.hasErrors()) {
            // if we had any errors: add them to the 'errors' list?
            data.put("errors", errors.getGlobalErrors());
        }
        return new ModelAndView("sessionfileuploadactivity", data);
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
