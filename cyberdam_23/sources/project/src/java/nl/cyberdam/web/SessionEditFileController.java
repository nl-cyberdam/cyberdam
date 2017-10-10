package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class SessionEditFileController extends CancellableFormGameController {
	
	private MenuUtil menuUtil;
	
	public SessionEditFileController() {
		setCommandClass(SessionResource.class);
	}
	    	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Long resourceId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
		Long participantId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "participantId"));
		Participant participant = getGameManager()
				.loadParticipant(participantId);
		SessionResource file = getGameManager().loadSessionResource(resourceId);		
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU,participant.getGameSession().getManifest());
		
		return file;
	}
	
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> referenceData = new HashMap<String, Object>();
        Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
        Participant participant = getGameManager().loadParticipant(participantId);
        referenceData.put("participant", participant);
        referenceData.put("statusText", SessionTools.getStatusText(participant, getGameManager()));

        return referenceData;
    }
    
	@Override
    protected ModelAndView onSubmit(HttpServletRequest request,
    		HttpServletResponse response, Object command, BindException errors)
    		throws Exception {
		Long participantId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "participantId"));
		Participant participant = getGameManager().loadParticipant(participantId);
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU,participant.getGameSession().getManifest());
		SessionResource resource = (SessionResource) command;
		getGameManager().save(resource);
		return new ModelAndView(new RedirectView(getSuccessView(), true), "participantId", participantId);
	}
	
    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
    		HttpServletResponse response, Object command) throws Exception {
    	Long participantId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "participantId"));
    	return new ModelAndView(new RedirectView(getCancelView(), true), "participantId", participantId);
    }

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
}
