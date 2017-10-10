package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 */
public class GameManifestComposerController extends AbstractController {
    
    private GameManager gameManager;
    public static final String GAMEMANIFESTS_ATTR = "gamemanifests";
    public static final String GAMEMANIFEST_ATTR = GameManifestController.GAMEMANIFEST_ATTR;
    
    /** Creates a new instance of GameManifestComposerController */
    public GameManifestComposerController() {
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request.getSession(true).getAttribute(GAMEMANIFESTS_ATTR);
        
        if (listHolder == null) {
            logger.debug("creating new listHolder object");
            listHolder = new RefreshablePagedListHolder();
            listHolder.setPageSize(gameManager.loadSystemParameters().getDefaultRows());
            listHolder.setSourceProvider(new GameManifestsProvider());
            listHolder.setFilter(new TextFilter ());
            request.getSession(true).setAttribute(GAMEMANIFESTS_ATTR, listHolder);
        }
        // reset the session
        request.getSession(true).removeAttribute(GAMEMANIFEST_ATTR);

        ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder, GAMEMANIFESTS_ATTR);
        binder.bind(request);

        listHolder.setLocale(RequestContextUtils.getLocale(request));
        boolean forceRefresh = request.getParameter("forceRefresh") != null;
        listHolder.refresh(forceRefresh);

        ModelAndView mav = new ModelAndView("gamemanifestcomposer", binder.getBindingResult().getModel());
        mav.addObject("filter", listHolder.getFilter ());
        return mav;
    }
    
    private class GameManifestsProvider implements PagedListSourceProvider, Serializable {

	@SuppressWarnings("unchecked")
        public List loadList(Locale loc, Object filter) {
	    TextFilter textFilter = (TextFilter) filter;
        	if (GameUtil.getCurrentUser().getGameAuthorities().isSystemAdministrator() || 
            		GameUtil.getCurrentUser().getGameAuthorities().isLcmsAdministrator()) {
        		return gameManager.findAllManifests(textFilter.getSearchText ());
        	} else {
        		return gameManager.findAllManifestsForUser(GameUtil.getCurrentUser(), textFilter.getSearchText ());
        	}
        }
    }
    
}
