package nl.cyberdam.web;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.multilanguage.MultiLanguageMessage;
import nl.cyberdam.multilanguage.MultiLanguageSource;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 *
 */
public class MultilanguageController extends AbstractGameController {

//    // attribute used to store list in session
    private static final String MULTILANGUAGELIST_ATTR = "multilanguagelist";
    private MultiLanguageSource messageSource;
//    private Map<String,Set<MultiLanguageMessage>> messages;
//    private Properties messageProperties;

    public MultilanguageController() {
        setRequireSession(true);
    }
    

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Long languagePackId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(request, "languagePack"));
        RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request.getSession(true).getAttribute(MULTILANGUAGELIST_ATTR + languagePackId.toString());
        
        if (listHolder == null) {
            logger.debug("creating new listHolder object");
            listHolder = new RefreshablePagedListHolder();
            listHolder.setPageSize(gameManager.loadSystemParameters().getDefaultRows());
            listHolder.setSourceProvider(new MultilanguageProvider(languagePackId));
            listHolder.setFilter(new TextFilter());
            request.getSession(true).setAttribute(MULTILANGUAGELIST_ATTR+ languagePackId.toString(), listHolder);
        }
        ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder, MULTILANGUAGELIST_ATTR+ languagePackId.toString());
        binder.bind(request);

        listHolder.setLocale(RequestContextUtils.getLocale(request));
        boolean forceRefresh = request.getParameter("forceRefresh") != null;
        listHolder.refresh(forceRefresh);

        ModelAndView mav = new ModelAndView("multilanguage", MULTILANGUAGELIST_ATTR, listHolder);
        mav.addObject("languagePack", messageSource.getLanguagePack(languagePackId));
        mav.addObject("filter", listHolder.getFilter ());
        return mav;
    }

    private class MultilanguageProvider implements PagedListSourceProvider {
    	
    	private Long languagePackId;
    	
    	public MultilanguageProvider(Long languagePackId) {
			this.languagePackId = languagePackId;
		}

		public List<MultiLanguageMessage> loadList(Locale locale, Object filter) {
			TextFilter textFilter = (TextFilter) filter;
			List<MultiLanguageMessage> list = messageSource.getMessagesForLanguage(languagePackId, textFilter.getSearchText(), null);
			return list;
		}
    }
    
    public void setMessageSource(MultiLanguageSource messageSource) {
        this.messageSource = messageSource;
    }
}