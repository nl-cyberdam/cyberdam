package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class PlaygroundWindowController extends AbstractGameController {
   
	MenuUtil menuUtil;
	    
    public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
    

    public PlaygroundWindowController()
    {
    	super ();
    }


	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
		GameManifest manifest = null;
		List<Playground> playgrounds = null;
		if (participantId != null)
		{
			Participant participant = getGameManager().loadParticipant(participantId);
			request.setAttribute("participant", participant);
			manifest = participant.getGameSession().getManifest();
			playgrounds = new ArrayList<Playground>(gameManager.findAllPlaygroundsFormanifest(manifest));
		}
		else
		{
			playgrounds = gameManager.findAllPlaygroundsForUser(GameUtil.getCurrentUser());
		}
		menuUtil.generateMenu(request, MenuUtil.PLAYGROUND_MENU, manifest);

		
		ModelAndView mav = new ModelAndView ("playgroundwindow");

		Map<String, Object> additionalData = new HashMap<String, Object>();
		mav.addObject("playgrounds", playgrounds);
		if (additionalData != null) {
			mav.addAllObjects(additionalData);
		}
				
		return mav;
	}
}
