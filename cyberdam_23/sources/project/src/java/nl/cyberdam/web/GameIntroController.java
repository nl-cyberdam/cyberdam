package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class GameIntroController extends AbstractGameController {

	// XXX this class is nearly identical to AboutSessionController.java
	private MenuUtil menuUtil;
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Long participantId = ServletRequestUtils.getLongParameter(request, "participantId");
		Participant participant = getGameManager().loadParticipant(participantId);
		GameSession gameSession = participant.getGameSession();
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU,gameSession.getManifest());
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("participant", participant);
		data.put("gamesession", gameSession);
		data.put("gamemanifest", gameSession.getManifest());
		data.put("gamemodel", gameSession.getManifest().getGameModel());
		data.put("substitutedDescription", gameManager.substituteVariablesInText (participant, gameSession.getManifest().getGameModel().getDescription()));
		data.put("statusText", SessionTools.getStatusText(participant, gameManager));

		return new ModelAndView("gameintro", data);
    }
    
	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}

}
