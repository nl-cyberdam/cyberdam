package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Playground;
import nl.cyberdam.service.GameManager;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 
 */
public class PlaygroundListPageController extends AbstractController {

	private GameManager gameManager;
	public static final String PLAYGROUNDS_ATTR = "playgrounds";

	/** Creates a new instance of PlaygroundListPageController */
	public PlaygroundListPageController() {
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request
				.getSession(true).getAttribute(PLAYGROUNDS_ATTR);

		if (listHolder == null) {
			logger.debug("creating new listHolder object");
			listHolder = new RefreshablePagedListHolder();
			listHolder.setPageSize(gameManager.loadSystemParameters()
					.getDefaultRows());
			listHolder.setSourceProvider(new PlaygroundsProvider());
			listHolder.setFilter(new TextFilter ());
			request.getSession(true).setAttribute(PLAYGROUNDS_ATTR, listHolder);
		}

		ServletRequestDataBinder binder = new ServletRequestDataBinder(
				listHolder, PLAYGROUNDS_ATTR);
		binder.bind(request);

		listHolder.setLocale(RequestContextUtils.getLocale(request));
		boolean forceRefresh = request.getParameter("forceRefresh") != null;
		listHolder.refresh(forceRefresh);

		ModelAndView mav = new ModelAndView("playgroundlistpage", binder.getBindingResult()
				.getModel());
	        mav.addObject("filter", listHolder.getFilter ());
	        return mav;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	private class PlaygroundsProvider implements PagedListSourceProvider,
			Serializable {

		public List<Playground> loadList(Locale loc, Object filter) {
		    TextFilter textFilter = (TextFilter) filter;
			return gameManager.findAllPlaygrounds(textFilter.getSearchText());
		}
	}
}
