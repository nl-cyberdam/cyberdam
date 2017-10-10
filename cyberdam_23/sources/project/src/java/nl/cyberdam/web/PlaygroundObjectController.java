package nl.cyberdam.web;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.RoleToPlaygroundMapping;
import nl.cyberdam.domain.SessionPlaygroundObject;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * controller for displaying a directory page of a playground
 * object
 *
 */
public class PlaygroundObjectController extends AbstractGameController {

	private MenuUtil menuUtil;
	
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
    		
    		Long playgroundObjectId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(request, "playgroundObjectId"));
            PlaygroundObject po = getGameManager().loadPlaygroundObject(playgroundObjectId);
            
            Map<String, Object> data = new HashMap<String, Object>();
            data.put("description", po.getDescription());
            
            GameManifest manifest = null;
            String idString = ServletRequestUtils.getStringParameter(request,"participantId");
            if (idString != null && idString.length() != 0)
            {
            	Long participantId = new Long (idString);
            	Participant participant = getGameManager().loadParticipant(participantId);
            	data.put("participant", participant);
            	manifest = participant.getGameSession().getManifest();
                if (participant.getRoleAndPlayground().getPlaygroundObject() == po)
                {
                    data.put("canEdit", new Boolean (true));
                }
                for (SessionPlaygroundObject sessionObject: po.getSessionPlaygroundObjects ())
                {
                	if (sessionObject.getGameSession() == participant.getGameSession())
                	{
                		data.put("description", sessionObject.getDescription());
                	}
                }

                // merge all uploads by all participants
                Set < SessionResource > resources = new HashSet < SessionResource > ();
                List<RoleToPlaygroundMapping> roleMappings = participant.getGameSession().getManifest().getRolesToPlaygroundObjects();
                for (RoleToPlaygroundMapping mapping: roleMappings)
                {
                	if (mapping.getPlaygroundObject() == po)
                	{
                		Role role = mapping.getRole();
                		for (Participant roleParticipant : participant.getGameSession().getParticipants())
                		{
                			if (roleParticipant.getRoleAndPlayground().getRole() == role)
                			{
                				for (SessionResource resource : roleParticipant.getSessionResources())
                				{
                					if (resource.isPublished())
                					{
                						resources.add(resource);
                					}
                				}
                			}
                		}
                	}
                }
                data.put("sessionResources", resources);
            }
            menuUtil.generateMenu(request, MenuUtil.PLAYGROUND_MENU, manifest);

            data.put("playgroundobject", po);
            
            return new ModelAndView("playgroundobject", data);
    }

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}

}
