package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.util.GameUtil;

import org.springframework.beans.support.PagedListSourceProvider;

public class BrowseGameManifestsController extends GameListController {

	private static String ATTR = "gamemanifestslist";
	private static String VIEW = "browsegamemanifests";

	public BrowseGameManifestsController() {
		super(ATTR, VIEW, null, TextFilter.class);
		setListSourceProvider(new GameManifestsProvider());
	}

	private class GameManifestsProvider implements PagedListSourceProvider, Serializable {

		public List<GameManifest> loadList(Locale loc, Object filter) {
			TextFilter textFilter = (TextFilter) filter;
			return (List<GameManifest>) gameManager.findAllReadyManifests(GameUtil.getCurrentUser(), textFilter.getSearchText());
		}
	}

}
