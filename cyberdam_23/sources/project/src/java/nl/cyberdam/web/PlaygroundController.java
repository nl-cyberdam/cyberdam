package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.web.util.MenuUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 */
public class PlaygroundController extends AbstractGameController {
    
    protected final Log logger = LogFactory.getLog(getClass());
    
    private MenuUtil menuUtil;
    private String swfPath;
    
    /** Creates a new instance of PlaygroundController */
    public PlaygroundController() {
    }
    
    /**
     * defaults to playground 1 if no id is specified
     * 
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws java.lang.Exception
     */
    protected ModelAndView handleRequestInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        ModelAndView mav = new ModelAndView();
        Map<String, Object> data = new HashMap<String, Object>();
        
        GameManifest manifest = null;
        String idString = ServletRequestUtils.getStringParameter(httpServletRequest,"participantId");
        if (idString != null && idString.length() != 0)
        {
        	Long participantId = new Long (idString);
        	Participant participant = getGameManager().loadParticipant(participantId);
        	data.put("participant", participant);
        	manifest = participant.getGameSession().getManifest();
        }
        menuUtil.generateMenu(httpServletRequest, MenuUtil.PLAYGROUND_MENU, manifest);

        Long playgroundId = ServletRequestUtils.getLongParameter(httpServletRequest, "playgroundId");
        Playground playground = getGameManager().loadPlayground(playgroundId);
        data.put("playground", playground);
        data.put("playgroundLink", playground.getLink());
        Boolean externalSwf = Boolean.FALSE;
        if (playground != null) {
        	if (playground.getLink().startsWith("http://") && ! playground.getLink().matches("(.*\\.jpg$)|(.*\\.JPG$)|(.*\\.jpeg$)|(.*\\.JPEG$)|(.*\\.png$)|(.*\\.PNG$)|(.*\\.gif$)|(.*\\.GIF$)")) {
        		externalSwf = Boolean.TRUE;
        	}
        }
        data.put("externalSwf", externalSwf);
        data.put("swfPath", swfPath);
        
        mav.addAllObjects(data);
        
        return mav;
    }

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
	
	// Injected by Spring
	public void setSwfPath(String swfPath) {
		logger.info("setSwfPath: " + swfPath);
		this.swfPath = swfPath;
	}
}
