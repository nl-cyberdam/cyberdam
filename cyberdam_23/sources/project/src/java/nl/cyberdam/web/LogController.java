package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.support.PagedListSourceProvider;

/**
 *
 */
public class LogController extends GameListController {
    
    /** Creates a new instance of LogController */
    public LogController() {
		super("logentries", "log", null, TextFilter.class);
		setListSourceProvider(new LogEntriesProvider());
    	
    }
    
    private class LogEntriesProvider implements PagedListSourceProvider, Serializable {

        public List loadList(Locale loc, Object filter) {
			TextFilter textFilter = (TextFilter) filter;
			return getGameManager().getGameLogService().findAllLogEntries(textFilter.getSearchText ());
        }
    }
    
}
