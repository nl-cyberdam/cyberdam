package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.web.customeditors.RoleToParticipantMappingEditor;
import nl.cyberdam.web.customeditors.SessionResourceEditor;
import nl.cyberdam.web.util.MenuUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

/**
 * Controller for sending messages outside of an activity - see SessionMessageActivityController for
 * activity based sending of messages.
 */
public class SendMessageController extends CancellableFormController {

	Log logger = LogFactory.getLog(getClass());
    GameManager gameManager;
    protected SessionReportLogService sessionReportLogService;
    MenuUtil menuUtil;
    
	private MessageSource messageSource;

    
    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    public void setSessionReportLogService(
            SessionReportLogService sessionReportLogService) {
        this.sessionReportLogService = sessionReportLogService;
    }
    
    public SendMessageController() {
        setCommandClass(Message.class);
        // Dont do session form - it will cause hibernate errors if we don't reattach the objects
        // setSessionForm(true); 
    }
    
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        // gets gameModels by id
    	binder.registerCustomEditor(Participant.class, "recipients", new RoleToParticipantMappingEditor(gameManager));
    	binder.registerCustomEditor(SessionResource.class, "attachments", new SessionResourceEditor(gameManager));
    }
    
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> referenceData = new HashMap<String, Object>();
        Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
        Participant participant = gameManager.loadParticipant(participantId);
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU,participant.getGameSession().getManifest());
        List<Participant> r2pMappingList = participant.getGameSession().getParticipants();
        // put list of possible recipients
        referenceData.put("recipientOptions", r2pMappingList);
        referenceData.put("from", participant);
        // if there is a related Action
        referenceData.put("messageaction", "");
        // attachment options
        Set<SessionResource> attachmentOptions = participant.getSessionResourceBox().getResources();
        referenceData.put("attachmentOptions", attachmentOptions);
        referenceData.put("participant", participant);
        referenceData.put("statusText", SessionTools.getStatusText(participant, gameManager));

        return referenceData;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
    		throws Exception {
    	Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
        Participant sender = gameManager.loadParticipant(participantId);
    	Message m = new Message();
    	m.setSender(sender);
    	// if reply set subject, recipients, body
    	if (WebUtils.hasSubmitParameter(request,"_reply")) {
    		// get original message
    		Long messageId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "messageId"));
    		Message original = getGameManager().loadMessage(messageId);
    		m.setSubject("Re: " + original.getSubject());
    		m.getRecipients().add(original.getSender());
    		// XXX or quoting per line?
    		
    		m.setBody("\n\n"+messageSource.getMessage("message.originaltextseparator", null, GameUtil.getCurrentUser().getLocale()) + "\n\n" + original.getBody());
    	}
    	return m;
    }

    @Override
    protected ModelAndView onSubmit(Object command, BindException arg1) throws Exception {
        
        Message m = (Message) command;
        // log
        getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM, "sending message", "");
        gameManager.deliverMessage(m);
        StringBuffer buffer = new StringBuffer("");
        for (Iterator<Participant> it = m.getRecipients().iterator();it.hasNext();) {
        	Participant participant = it.next();
        	buffer.append(participant.getRoleAndPlayground().getRole().getName());
        	if (it.hasNext()) buffer.append(", ");
        }
        sessionReportLogService.logMessageActivity(null, m, buffer.toString());
        return new ModelAndView(new RedirectView(getSuccessView(), true), "participantId", m.getSender().getId());
    }
    
    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
    		HttpServletResponse response, Object command) throws Exception {
    	Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
    	return new ModelAndView(new RedirectView(getCancelView(), true), "participantId", participantId);
    }

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
	
    /**
     * should be injected 
     */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
