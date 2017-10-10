package nl.cyberdam.web;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.User;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 */
public class UserAdministrationController extends AbstractGameController {
    
    // attribute used to store list in session
    private static final String USERS_ATTR = "users";
    
    /** Creates a new instance of UserAdministrationController */
    public UserAdministrationController() {
        setRequireSession(true);
    }
    
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        RefreshablePagedListHolder listHolder =
                (RefreshablePagedListHolder) request.getSession(true).getAttribute(USERS_ATTR);
        if (listHolder == null) {
            logger.debug("creating new listHolder object");
            listHolder = new RefreshablePagedListHolder();
            listHolder.setPageSize(gameManager.loadSystemParameters().getDefaultRows());
            listHolder.setSourceProvider(new UsersProvider());
            listHolder.setFilter(new TextFilter ());
            MutableSortDefinition sortDefinition = new MutableSortDefinition("lastName", true, true);
            sortDefinition.setToggleAscendingOnProperty(true);
            listHolder.setSort(sortDefinition);
            request.getSession(true).setAttribute(USERS_ATTR, listHolder);
        }
        
        ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder, USERS_ATTR);
        binder.bind(request);
        
        listHolder.setLocale(RequestContextUtils.getLocale(request));
        boolean forceRefresh = request.getParameter("forceRefresh") != null;
        listHolder.refresh(forceRefresh);
        
        ModelAndView mav = new ModelAndView("useradministration", binder.getBindingResult().getModel());
        mav.addObject("filter", listHolder.getFilter ());
        return mav;
    }
    
    private class UsersProvider implements PagedListSourceProvider {
        
        public List<User> loadList(Locale loc, Object filter) {
	    TextFilter textFilter = (TextFilter) filter;
            return userManager.getFilteredUsers(textFilter.getSearchText());
        }
    }
}
