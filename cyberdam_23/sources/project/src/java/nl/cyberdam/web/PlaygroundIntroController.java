/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 */
public class PlaygroundIntroController extends AbstractGameController {

	private MenuUtil menuUtil;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
        Map<String, Object> data = new HashMap<String, Object>();
        
        GameManifest manifest = null;
        String idString = ServletRequestUtils.getStringParameter(request,"participantId");
        if (idString != null && idString.length() != 0)
        {
        	Long participantId = new Long (idString);
        	Participant participant = getGameManager().loadParticipant(participantId);
        	data.put("participant", participant);
    		data.put("gamesession", participant.getGameSession());
    		data.put("gamemanifest", manifest);
    		data.put("gamemodel", participant.getGameSession().getManifest()
    				.getGameModel());
        	manifest = participant.getGameSession().getManifest();
        }
        menuUtil.generateMenu(request, MenuUtil.PLAYGROUND_MENU, manifest);

        Long playgroundId = ServletRequestUtils.getLongParameter(request, "playgroundId");
        Playground playground = getGameManager().loadPlayground(playgroundId);
		data.put("playground", playground);
		return new ModelAndView("playgroundintro", data);
	}

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}

}
