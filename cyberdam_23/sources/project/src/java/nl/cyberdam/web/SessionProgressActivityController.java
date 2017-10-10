package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.User;
import nl.cyberdam.multilanguage.MultiLanguageSource;
import nl.cyberdam.service.MailService;
import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.web.util.ActivityHelper;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class SessionProgressActivityController extends AbstractGameController {
    
    private MailService               mailService;
    private MenuUtil                  menuUtil;
    protected SessionReportLogService sessionReportLogService;
    private ScriptManager scriptManager;
    private MultiLanguageSource messageSource;
    
    public void setSessionReportLogService(
            SessionReportLogService sessionReportLogService) {
        this.sessionReportLogService = sessionReportLogService;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long activityId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(request,
                "activityId"));
        Long participantId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(
                request, "participantId"));
        Participant participant = getGameManager().loadParticipant(
                participantId);
        
        final GameSession gameSession = participant.getGameSession ();
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU, gameSession.getManifest());
        
        ProgressActivity progress = getGameManager().loadProgressActivity(
                activityId);
        Map<String, Object> data = new HashMap<String, Object>();
        
        String doCancel = ServletRequestUtils.getStringParameter(request,
                "_cancel");
        if (doCancel != null) {
            // redirect
            data.put("participantId", participant.getId());
            return new ModelAndView(new RedirectView("session.htm", true), data);
        }

        ModelAndView disabledRedirect = ActivityHelper.RedirectDisabledActivity(participant, progress, "session.htm", scriptManager);
        if (disabledRedirect != null)
        {
            return disabledRedirect;
        }

        // if this is a submit '_nextstep' is set
        String doNextStep = ServletRequestUtils.getStringParameter(request, "_nextstep");
        String scriptError = null;
        if (doNextStep != null) {
            Long nextStepId = new Long(ServletRequestUtils
                    .getRequiredLongParameter(request, "nextstep"));
            
            // mark activity as completed. This must be done before gotoStep() 
            // since gotoStep() will reset the complete state of all activities of 
            // the nextStep.
            participant.getCompletedActivities().add(progress);

            // do next step
            // XXX this might not be the best way to do this - first loading the
            // step, then setting it
            // in a model that already has this step lying around somewhere.
            StepOfPlay nextStep = getGameManager().loadStepOfPlay(nextStepId);
            gameSession.setCurrentStepOfPlay(nextStep);
            
            getScriptManager().CompletedActivity(progress, participant, null);
            if (gameSession.getCurrentStepOfPlay() == nextStep)
            {
            	getScriptManager ().RunScriptForStep (nextStep, participant, null);
            }
            gameSession.gotoStep(gameSession.getCurrentStepOfPlay());
            // log: game session name, role name, activity name
            getGameManager().getGameLogService().addLog(
            		LogEntry.Module.SYSTEM,
            		"progress activity",
            		gameSession.getName()
            		+ ", "
            		+ participant.getRoleAndPlayground().getRole()
            		.getName() + ", " + progress.getName());
            // Session report log
            sessionReportLogService.logProgressActivity(progress, participant);

            // XXX I don't think this save should be necessary - but the
            // activity is not added if I leave this out
            getGameManager().save(participant);

            // XXX now we have to save the session?
            getGameManager().save(gameSession);

            // notify participants
            for (Participant p : gameSession.getParticipants()) {
            	for (User u : p.getUsers()) {
					if (u.isDefaultNotifyNewStepOfPlay()) {
						SimpleMailMessage message = new SimpleMailMessage();
						message.setTo(u.getEmail());
						// message.setSubject("new step of play");
						message.setSubject(messageSource.getMessage("email.newstepofplay.subject", null, u.getLocale()));
						Object[] bodyOptions = { u.getFullName(), gameSession.getName(), nextStep.getName() };
						message.setText(messageSource.getMessage("email.newstepofplay.body", bodyOptions, u.getLocale()));
						message.setFrom(gameManager.loadSystemParameters().getEmail());
						mailService.send(message);
					}
            	}
            }
            data.put("participantId", participant.getId());
            return new ModelAndView(new RedirectView("session.htm", true), data);
        }

        data.put("progressActivity", progress);
        data.put("participant", participant);
        data.put("substitutedInstructions", gameManager.substituteVariablesInText (participant, progress.getInstructions()));
        data.put("statusText", SessionTools.getStatusText(participant, gameManager));
    	data.put("scriptError", scriptError);

        // data.put("gamesession", participant.getGameSession());
        return new ModelAndView("sessionprogressactivity", data);
    }
    
    /**
     * should be injected
     */
    public void setMailService(MailService mailService) {
        this.mailService = mailService;
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

	public void setMessageSource(MultiLanguageSource messageSource) {
		this.messageSource = messageSource;
	}
}
