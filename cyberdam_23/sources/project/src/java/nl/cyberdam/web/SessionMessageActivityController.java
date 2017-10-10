package nl.cyberdam.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.MessageActivity;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.web.customeditors.RoleToParticipantMappingEditor;
import nl.cyberdam.web.customeditors.SessionResourceEditor;
import nl.cyberdam.web.util.ActivityHelper;
import nl.cyberdam.web.util.MenuUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

/**
 * Send message from within the contect of an activity - for general message
 * sending see SendMessageController.
 */
public class SessionMessageActivityController extends CancellableFormController {
    
    Log                               logger = LogFactory.getLog(getClass());
    GameManager                       gameManager;
    protected SessionReportLogService sessionReportLogService;
    private MenuUtil                  menuUtil;
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
    
    public SessionMessageActivityController() {
        setCommandClass(Message.class);
        // Dont do session form - it will cause hibernate errors if we don't
        // reattach the objects
        // setSessionForm(true);
    }
    
    @Override
    protected void initBinder(HttpServletRequest request,
            ServletRequestDataBinder binder) throws Exception {
        // gets gameModels by id
        binder.registerCustomEditor(Participant.class, "recipients",
                new RoleToParticipantMappingEditor(gameManager));
        binder.registerCustomEditor(SessionResource.class, "attachments",
                new SessionResourceEditor(gameManager));
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
        Long activityId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(request,
                "activityId"));
        MessageActivity messageActivity = getGameManager().loadMessageActivity(
                activityId);
        
        List<Participant> r2pMappingList = participant.getGameSession()
                .getParticipants();
        // put list of possible recipients
        referenceData.put("recipients",
                participant.getGameSession().getRolesToParticipantsForRoles(
                        messageActivity.getRecipients()));
        referenceData.put("from", participant);
        // if there is a related Action
        referenceData.put("messageActivity", messageActivity);
        
        // all session pages need the "participant"
        referenceData.put("participant", participant);

        referenceData.put("substitutedInstructions", gameManager.substituteVariablesInText (participant, messageActivity.getInstructions()));
        referenceData.put("statusText", SessionTools.getStatusText(participant, gameManager));
        
        return referenceData;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        
        Long activityId = new Long(ServletRequestUtils.getRequiredLongParameter(request,
                "activityId"));
        MessageActivity messageActivity = getGameManager().loadMessageActivity(
                activityId);
        
        Long participantId = ServletRequestUtils.getLongParameter(request,
                "participantId");
        Participant sender = gameManager.loadParticipant(participantId);
        Message m = new Message();
        m.setSender(sender);
        
        // set sender and recipients
        m.setRecipients(sender.getGameSession().getRolesToParticipantsForRoles(
                messageActivity.getRecipients()));
        
        // if reply set subject, recipients, body
        if (WebUtils.hasSubmitParameter(request, "_reply")) {
            // get original message
            Long messageId = new Long(ServletRequestUtils
                    .getRequiredLongParameter(request, "messageId"));
            Message original = getGameManager().loadMessage(messageId);
            m.setSubject("Re: " + original.getSubject());
            m.getRecipients().add(original.getSender());
            // XXX or quoting per line?
            m.setBody("\n\n---- original ----\n\n" + original.getBody());
        } else {
            // fill body with default message text
            m.setBody(messageActivity.getDefaultMessageText());
        }
        return m;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        Long activityId = new Long(ServletRequestUtils.getRequiredLongParameter(request,
                "activityId"));
        MessageActivity messageActivity = getGameManager().loadMessageActivity(
                activityId);
        Message m = (Message) command;
        Participant sender = m.getSender();

        ModelAndView disabledRedirect = ActivityHelper.RedirectDisabledActivity(sender, messageActivity, "session.htm", scriptManager);
        if (disabledRedirect != null)
        {
            return disabledRedirect;
        }

        gameManager.deliverMessage(m);
        getScriptManager().CompletedActivity(messageActivity, sender, null);
        // log
        getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM,
                "sending message for activity", messageActivity.getName());
        // set activity to completed
        sender.addCompletedActivity(messageActivity);
        // XXX is a save actually needed?
        getGameManager().save(sender);
        // SessionReport
        sessionReportLogService.logMessageActivity(messageActivity, m, null);
        return new ModelAndView(new RedirectView(getSuccessView(), true),
                "participantId", m.getSender().getId());
    }
    
    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
            HttpServletResponse response, Object command) throws Exception {
        Message m = (Message) command;
        return new ModelAndView(new RedirectView(getCancelView(), true),
                "participantId", m.getSender().getId());
    }

    @SuppressWarnings("boxing")
    @Override
    protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse response, BindException errors)
            throws Exception {
        
        // activityId is only supplied if the upload is part of an activity
        Long activityId = ServletRequestUtils.getLongParameter(request,
                "activityId");
        Long participantId = ServletRequestUtils.getRequiredLongParameter(
                request, "participantId");
        Participant participant = getGameManager().loadParticipant(
                participantId);
        MessageActivity activity = getGameManager ().loadMessageActivity (activityId);
        
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

    public void setScriptManager(ScriptManager scriptManager) {
	this.scriptManager = scriptManager;
    }

    public ScriptManager getScriptManager() {
	return scriptManager;
    }
}
