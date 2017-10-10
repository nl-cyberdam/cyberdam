package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.support.PagedListSourceProvider;


public class BrowseGroupsController extends GameListController {

	public BrowseGroupsController(String attributeName, String viewName) {
		super("groupslist", "browsegroups");
		setListSourceProvider(new GroupsProvider());
	}
	
	private class GroupsProvider implements PagedListSourceProvider, Serializable {

        public List loadList(Locale loc, Object filter) {
        	return (List) getUserManager().findallGroups();
        }
    }

}
