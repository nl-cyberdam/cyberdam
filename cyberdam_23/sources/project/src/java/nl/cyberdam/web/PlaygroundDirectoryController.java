package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * show the playground directory
 */
public class PlaygroundDirectoryController extends AbstractGameController {
    private MenuUtil            menuUtil;
    
    private static final String PLAYGROUNDOBJECTS_ATTR = "playgroundobjects";
    
    public static class Filter implements java.io.Serializable
    {
    	public String searchText;
		public Playground playground;

		public Filter ()
		{
		}
		
	    public boolean equals(Object object)
	    {
	    	boolean equal = false;
	    	if (object == this)
	    	{
	    	    equal = true;
	    	}
	    	else if (object instanceof Filter)
	    	{
	    		Filter filter = (Filter) object;
	    		equal = (searchText == null && filter.searchText == null) || (searchText != null && searchText.equals (filter.searchText));
				equal = equal && (playground != null && filter.playground != null && filter.playground.getId().equals(playground.getId()) || (playground == null && filter.playground == null));
	    	}
	    	return equal;
		}

		public String getSearchText() {
			return searchText;
		}
		public void setSearchText(String filterText) {
			this.searchText = filterText;
		}
		public Playground getPlayground() {
			return playground;
		}
		public void setPlayground(Playground playground) {
			this.playground = playground;
		}
    }
 
    public PlaygroundDirectoryController() {
        setRequireSession(true);
    }
    
    private class PlaygroundDirectoryProvider implements
            PagedListSourceProvider, Serializable {
        
        public List<PlaygroundObject> loadList(Locale loc, Object filter) {
            Playground playground = null;
            String text = null;
            if (filter != null) {
            	Filter directoryFilter = (Filter) filter;
            	playground = directoryFilter.getPlayground();
            	text = directoryFilter.getSearchText();
            }
            return getGameManager().findAllPlaygroundObjectsForDirectory(
                    playground, text);
        }
    }
    
    public void setMenuUtil(MenuUtil menuUtil) {
        this.menuUtil = menuUtil;
    }
    
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        GameManifest manifest = null;
        String idString = ServletRequestUtils.getStringParameter(request,"participantId");
        if (idString != null && idString.length() != 0)
        {
        	Long participantId = new Long (idString);
        	Participant participant = getGameManager().loadParticipant(participantId);
        	request.setAttribute("participant", participant);
        	manifest = participant.getGameSession().getManifest();
        }
        menuUtil.generateMenu(request, MenuUtil.PLAYGROUND_MENU, manifest);
        
        Long playgroundId = ServletRequestUtils.getLongParameter(request,
                "playgroundId");
        
        Playground playground = gameManager.loadPlayground(playgroundId);
        request.setAttribute("playground", playground);
        
		RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request
		.getSession(true).getAttribute(PLAYGROUNDOBJECTS_ATTR);
		if (listHolder == null)
		{
			listHolder = new RefreshablePagedListHolder();
			listHolder.setPageSize(gameManager.loadSystemParameters()
					.getDefaultRows());
			listHolder.setSourceProvider(new PlaygroundDirectoryProvider());
	        request.getSession ().setAttribute(PLAYGROUNDOBJECTS_ATTR, listHolder);
		}
		
        Filter filter = new Filter ();
        filter.setPlayground(playground);
		listHolder.setFilter(filter);

		ServletRequestDataBinder binder = new ServletRequestDataBinder(
                listHolder, PLAYGROUNDOBJECTS_ATTR);
        binder.bind(request);
        listHolder.setLocale(RequestContextUtils.getLocale(request));
		boolean forceRefresh = request.getParameter("forceRefresh") != null;
		listHolder.refresh(forceRefresh);
        
        ModelAndView mav = new ModelAndView("playgrounddirectory", binder
                .getBindingResult().getModel());
        mav.addObject("filter", listHolder.getFilter ());
        return mav;
    }
}
