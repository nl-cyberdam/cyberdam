package nl.cyberdam.web;

import java.util.List;
import java.util.Locale;

import nl.cyberdam.domain.User;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.beans.support.PagedListSourceProvider;

/**
 *
 */
public class GameParticipantController extends GameListController {
    
	// attribute used to store list in session
    public static final String PARTICIPANTS_ATTR = "gameparticipantlist";
    
    
    public GameParticipantController() {
    	super(PARTICIPANTS_ATTR, "gameparticipant", null, TextFilter.class);
    	setListSourceProvider(new ParticipantsProvider());
    }

    private class ParticipantsProvider implements PagedListSourceProvider {
        
        public List loadList(Locale loc, Object filter) {
	    TextFilter textFilter = (TextFilter) filter;
        	User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return (List) gameManager.findParticipantsForUser(currentUser, textFilter.getSearchText ());
        }
    }
}
