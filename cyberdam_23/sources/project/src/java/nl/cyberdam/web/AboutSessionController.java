package nl.cyberdam.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.RoleToPlaygroundMapping;

/**
 * 
 */
public class AboutSessionController extends SessionController {
	
	public AboutSessionController() {
		viewName = "aboutsession";
	}

	protected Map<?, ?> additionalModelData(HttpServletRequest request, HttpServletResponse arg1,
		Participant participant) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
	    GameSession gameSession = participant.getGameSession();
	    // make unique list of playground names
	    Set<String> set = new HashSet<String>();
	    for (RoleToPlaygroundMapping map: gameSession.getManifest().getRolesToPlaygroundObjects()) {
	    	set.add(map.getPlayground().getName());
	    }
	    data.put("playgroundNames", set);
	    return data;
	}
}
