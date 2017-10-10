package nl.cyberdam.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * this controller presents a list of gamemodels and the option to view,
 * edit and create them.  LCMS Administrator or System Administrator will see all games, others only
 * their own and public ones.
 */
public class GameAuthorController extends AbstractController {

    private GameManager gameManager;
    public static final String GAMEMODEL_ATTR = "gamemodels";

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request.getSession(true).getAttribute(GAMEMODEL_ATTR);
        
       if (listHolder == null) {
            logger.debug("creating new listHolder object");
            listHolder = new RefreshablePagedListHolder();
            listHolder.setPageSize(gameManager.loadSystemParameters().getDefaultRows());
            listHolder.setSourceProvider(new GamesProvider());
            listHolder.setFilter(new TextFilter ());
            request.getSession(true).setAttribute(GAMEMODEL_ATTR, listHolder);
        }

        ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder, GAMEMODEL_ATTR);
        binder.bind(request);

        listHolder.setLocale(RequestContextUtils.getLocale(request));
        boolean forceRefresh = request.getParameter("forceRefresh") != null;
        listHolder.refresh(forceRefresh);

        ModelAndView mav = new ModelAndView("gameauthor", binder.getBindingResult().getModel());
        mav.addObject("filter", listHolder.getFilter ());
        return mav;
    }

    private class GamesProvider implements PagedListSourceProvider, Serializable {

        public List<GameModel> loadList(Locale loc, Object filter) {
	    TextFilter textFilter = (TextFilter) filter;
        	try {
				if (GameUtil.getCurrentUser().getGameAuthorities().isSystemAdministrator() || 
					GameUtil.getCurrentUser().getGameAuthorities().isLcmsAdministrator()) {
					return gameManager.findAll(textFilter.getSearchText ());
				} else {
					return gameManager.findAllForUser(GameUtil.getCurrentUser(), textFilter.getSearchText ());
				}
			} catch (org.springframework.orm.ObjectRetrievalFailureException e) {
				logger.debug("error retrieving  list of gamemodels", e);
				// return empty list on error
				return new ArrayList<GameModel>();
			}
        }
    }
}
