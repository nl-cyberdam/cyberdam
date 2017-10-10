package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import nl.cyberdam.domain.User;

import org.springframework.beans.support.PagedListSourceProvider;

public class BrowseUsersController  extends GameListController {

	private static String ATTR = "userslist";
	private static String VIEW = "bowseusers";
	public BrowseUsersController() {
		super(ATTR, VIEW, null, TextFilter.class);
		setListSourceProvider(new UsersProvider());
	}

	private class UsersProvider implements PagedListSourceProvider, Serializable {

		public List<User> loadList(Locale loc, Object filter) {
			TextFilter textFilter = (TextFilter) filter;
			return userManager.getFilteredUsers(textFilter.getSearchText());
		}
	}
}
