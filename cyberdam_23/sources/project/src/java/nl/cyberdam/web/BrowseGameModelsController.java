package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.util.GameUtil;

import org.springframework.beans.support.PagedListSourceProvider;

public class BrowseGameModelsController extends GameListController {

	private static String ATTR = "gamemodelslist";
	private static String VIEW = "browsegamemodels";
	public BrowseGameModelsController() {
		super(ATTR, VIEW, null, TextFilter.class);
		setListSourceProvider(new GameModelsProvider());
	}

	private class GameModelsProvider implements PagedListSourceProvider, Serializable {

        public List<GameModel> loadList(Locale loc, Object filter) {
        	TextFilter textFilter = (TextFilter) filter;
        	return (List<GameModel>) getGameManager().findAllReady(GameUtil.getCurrentUser(), textFilter.getSearchText());
        }
    }

}
