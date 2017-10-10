package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.SessionPlaygroundObject;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.web.customeditors.PlaygroundEditor;
import nl.cyberdam.web.util.MenuUtil;

import org.acegisecurity.AccessDeniedException;
import org.acegisecurity.AcegiSecurityException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

public class PlaygroundObjectOwnerController extends CancellableFormController {

    private GameManager gameManager;
    private MenuUtil menuUtil;

    public PlaygroundObjectOwnerController() {
    	setCommandClass(PlaygroundObjectSessionDataWrapper.class);
    }

    public void setGameManager(GameManager gameManager) {
    	this.gameManager = gameManager;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
    		HttpServletResponse response) throws Exception {
    	
    	// check if the current user is really the owner of this playground object
    	Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
		Participant participant = gameManager.loadParticipant(participantId);
		
		Long playgroundObjectId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(request, "id"));
        PlaygroundObject po = gameManager.loadPlaygroundObject(playgroundObjectId);
		
		if (participant.getRoleAndPlayground().getPlaygroundObject() != po) {
			throw new AccessDeniedException("Current participant is not the owner of this playground object");
		}
		
		User currentUser = GameUtil.getCurrentUser();
		boolean userIsParticipant = false;
		for (Iterator iterator = participant.getUsers().iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			if (user.equals(currentUser)) {
				userIsParticipant = true;
				break;
			}
		}
		
		if (!userIsParticipant) {
			throw new AccessDeniedException("Current user is not allowed to edit this page");
		}
    	
    	return super.handleRequestInternal(request, response);
    }

    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
    throws Exception {

		binder.registerCustomEditor(Playground.class, "playground", new PlaygroundEditor(
			gameManager));
    }

    @Override
    protected ModelAndView showForm(HttpServletRequest request, HttpServletResponse response,
	    BindException errors) throws Exception {
		Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
		Participant participant = gameManager.loadParticipant(participantId);
		menuUtil.generateMenu(request, MenuUtil.PLAYGROUND_MENU, participant.getGameSession()
			.getManifest());
		return super.showForm(request, response, errors);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
		Long playgroundObjectId = ServletRequestUtils.getLongParameter(request, "id");
		PlaygroundObject playgroundObject = gameManager.loadPlaygroundObject(playgroundObjectId);
		Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
		Participant participant = gameManager.loadParticipant(participantId);
	
		PlaygroundObjectSessionDataWrapper sessionData = new PlaygroundObjectSessionDataWrapper();
		sessionData.participant = participant;
		for (SessionPlaygroundObject sessionObject : playgroundObject.getSessionPlaygroundObjects()) {
		    if (sessionObject.getGameSession() == participant.getGameSession()) {
			sessionData.sessionObject = sessionObject;
		    }
		}
		if (sessionData.sessionObject == null) {
		    sessionData.sessionObject = new SessionPlaygroundObject();
		    sessionData.sessionObject.setPlaygroundObject(playgroundObject);
		    sessionData.sessionObject.setGameSession(participant.getGameSession());
		    sessionData.sessionObject.setDescription(playgroundObject.getDescription());
		}
		return sessionData;
    }

    @SuppressWarnings("boxing")
    @Override
    public Map<String, Object> referenceData(HttpServletRequest request) throws ServletRequestBindingException {
		Map <String, Object> data = new HashMap <String, Object> ();
		long playgroundObjectId = ServletRequestUtils.getRequiredLongParameter(request, "id");
		PlaygroundObject playgroundObject = gameManager.loadPlaygroundObject(playgroundObjectId);
		data.put("playgroundobject", playgroundObject);
    	Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
		Participant participant = gameManager.loadParticipant(participantId);
		data.put("participant", participant);
		data.put("defaultDescription", playgroundObject.getDescription());
		return data;
    }

    @Override
    protected ModelAndView onSubmit(Object command) throws Exception {
		PlaygroundObjectSessionDataWrapper wrapper = (PlaygroundObjectSessionDataWrapper) command;
	
		gameManager.save(wrapper.sessionObject);
		gameManager.save(wrapper.participant);
	
		Map <String, Object> data = new HashMap <String, Object> ();
		data.put("participantId", wrapper.participant.getId());
		data.put("playgroundObjectId", wrapper.getSessionObject().getPlaygroundObject().getId());
		return new ModelAndView (getSuccessView(), data);
    }

    @Override
    protected ModelAndView onCancel(
	    HttpServletRequest request, HttpServletResponse response, Object command) throws Exception {
		PlaygroundObjectSessionDataWrapper wrapper = (PlaygroundObjectSessionDataWrapper) command;
	
		Map <String, Object> data = new HashMap <String, Object> ();
		data.put("participantId", wrapper.participant.getId());
		data.put("playgroundObjectId", wrapper.getSessionObject().getPlaygroundObject().getId());
		return new ModelAndView (getSuccessView(), data);
    }

    public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
    }

    public class PlaygroundObjectSessionDataWrapper implements java.io.Serializable {
		SessionPlaygroundObject sessionObject;
		Participant participant;
	
		public PlaygroundObjectSessionDataWrapper() {
		}
	
		public SessionPlaygroundObject getSessionObject() {
		    return sessionObject;
		}
	
		public void setSessionObject(SessionPlaygroundObject sessionObject) {
		    this.sessionObject = sessionObject;
		}
	
		public Participant getParticipant() {
		    return participant;
		}
	
		public void setParticipant(Participant participant) {
		    this.participant = participant;
		}
    }

}
