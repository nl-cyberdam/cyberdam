package nl.cyberdam.web;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 */
public class PlaygroundDetailController extends AbstractController {

	private GameManager gameManager;
	public static final String PLAYGROUNDOBJECTS_ATTR = "playgroundObjects";

	/** Creates a new instance of PlaygroundDetialController */
	public PlaygroundDetailController() {
		setRequireSession(true);
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long playgroundId = ServletRequestUtils.getLongParameter(request, "id");
		Playground playground;
		if (playgroundId != null) {
			playground = gameManager.loadPlayground(playgroundId);
		} else {
			playground = new Playground();
			// set url with unique string
			playground.setUriId("url" + (new Random()).nextInt());
			User currentUser = (User) SecurityContextHolder.getContext()
										.getAuthentication().getPrincipal();
			playground.setLastModifier(currentUser);
			playground.setLastModified(new Date());
			gameManager.save(playground);
			playgroundId = playground.getId();
			playground.setName("Playground" + playgroundId.toString());
			playground.setUriId("url" + playgroundId.toString());
			gameManager.save(playground);
		}

		RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request
		.getSession(true).getAttribute(
				PLAYGROUNDOBJECTS_ATTR + playgroundId.toString());

		if (listHolder == null) {
			logger.debug("creating new listHolder object");
			listHolder = new RefreshablePagedListHolder();
			listHolder.setPageSize(getGameManager().loadSystemParameters()
					.getDefaultRows());
			listHolder.setSourceProvider(new PlaygroundObjectsProvider(
					playgroundId));

			listHolder.setFilter(new TextFilter());
			request.getSession(true).setAttribute(
					PLAYGROUNDOBJECTS_ATTR + playgroundId.toString(),
					listHolder);
		}

		ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder);
		binder.bind(request);

		listHolder.setLocale(RequestContextUtils.getLocale(request));
		boolean forceRefresh = request.getParameter("forceRefresh") != null;
		listHolder.refresh(forceRefresh);
		
		ModelAndView mav = new ModelAndView("playgrounddetail");
		if (ServletRequestUtils.getStringParameter(request, "sort.property") != null ||
				ServletRequestUtils.getIntParameter(request, "page") != null) {
			mav = new ModelAndView(
					new RedirectView("playgrounddetail.htm#playgroundobjects_section"));
		}
		
		mav.addObject("id", playgroundId);
		mav.addObject("filter", listHolder.getFilter());
		mav.addObject("playground", playground);
		mav.addObject(PLAYGROUNDOBJECTS_ATTR, listHolder);
		return mav;
	}

	private class PlaygroundObjectsProvider implements PagedListSourceProvider,
	Serializable {

		Long playgroundId;

		public PlaygroundObjectsProvider(Long playgroundId) {
			this.playgroundId = playgroundId;
		}

		public List<?> loadList(Locale loc, Object filter) {
			TextFilter textFilter = (TextFilter) filter;
			if (GameUtil.getCurrentUser().getGameAuthorities()
					.isSystemAdministrator()
					|| GameUtil.getCurrentUser().getGameAuthorities()
					.isLcmsAdministrator()) {
				return getGameManager().findAllPlaygroundObjects(playgroundId,
						textFilter.getSearchText());
			} else {
				return getGameManager().findAllPlaygroundObjectsForUser(
						GameUtil.getCurrentUser(), playgroundId,
						textFilter.getSearchText());
			}
		}
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
