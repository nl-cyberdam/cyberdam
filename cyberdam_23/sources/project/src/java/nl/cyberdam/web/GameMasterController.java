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
public class GameMasterController extends AbstractController {
    
    private GameManager gameManager;
    
    /** Creates a new instance of GameMasterController */
    public GameMasterController() {
    }
    
    public static final String GAMESESSIONS_ATTR = "gamesessions";

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request.getSession(true).getAttribute(GAMESESSIONS_ATTR);
        
        if (listHolder == null) {
            logger.debug("creating new listHolder object");
            listHolder = new RefreshablePagedListHolder();
            listHolder.setPageSize(gameManager.loadSystemParameters().getDefaultRows());
            listHolder.setSourceProvider(new GameSessionsProvider());
            listHolder.setFilter(new TextFilter ());
            request.getSession(true).setAttribute(GAMESESSIONS_ATTR, listHolder);
        }

        ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder, GAMESESSIONS_ATTR);
        binder.bind(request);

        listHolder.setLocale(RequestContextUtils.getLocale(request));
        boolean forceRefresh = request.getParameter("forceRefresh") != null;
        listHolder.refresh(forceRefresh);

        ModelAndView mav = new ModelAndView("gamemaster", binder.getBindingResult().getModel());
        mav.addObject("filter", listHolder.getFilter ());
        return mav;
    }

    private class GameSessionsProvider implements PagedListSourceProvider, Serializable {

        public List loadList(Locale loc, Object filter) {
	    TextFilter textFilter = (TextFilter) filter;
            
            if (GameUtil.getCurrentUser().getGameAuthorities().isSystemAdministrator() || 
            	GameUtil.getCurrentUser().getGameAuthorities().isLmsAdministrator() ||
            	GameUtil.getCurrentUser().getGameAuthorities().isVleAdministrator()) {
            	return gameManager.findAllGameSessions(textFilter.getSearchText ());
        	} else {
        		return gameManager.findAllGameSessionsForUser(GameUtil.getCurrentUser(), textFilter.getSearchText ());
        	}
        }
    }
    
}
