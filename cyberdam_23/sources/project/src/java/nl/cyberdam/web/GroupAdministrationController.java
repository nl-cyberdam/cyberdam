package nl.cyberdam.web;

import java.util.List;
import java.util.Locale;

import org.springframework.beans.support.PagedListSourceProvider;

public class GroupAdministrationController extends GameListController {

	// attribute used to store list in session
    public static final String GROUPS_ATTR = "groups";
    
    public GroupAdministrationController() {
    	super(GROUPS_ATTR, "groupadministration", null , GroupsFilter.class);
    	//setFilter(new GroupsFilter());
    	setListSourceProvider(new GroupsProvider());
    	
    }

    private class GroupsProvider implements PagedListSourceProvider {
        
        public List loadList(Locale loc, Object filter) {
        	GroupsFilter gf = (GroupsFilter) filter;
            return (List) userManager.findallGroups(gf.getName());
        }
    }
}
