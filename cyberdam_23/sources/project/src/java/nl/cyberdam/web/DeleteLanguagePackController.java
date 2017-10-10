package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.multilanguage.LanguagePack;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class DeleteLanguagePackController extends AbstractGameController {
    
    private static final String LANGUAGEPACK_KEY    = "languagepack";
    private static final String DELETE_LANGUAGEPACK = "delete Languagepack";
    private static final String LANGUAGE_PACK_ID    = "languagePackId";
    
    private String              successRedirect;
    private String              cancelRedirect;
    private String              view;
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        ModelAndView returnValue;
        Long id = new Long(ServletRequestUtils.getRequiredLongParameter(
                request, LANGUAGE_PACK_ID));      
        LanguagePack languagePack = getGameManager().loadLanguagePack(id);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put(LANGUAGEPACK_KEY, languagePack);
        
        // on POST
        if (METHOD_POST.equals(request.getMethod())) {
            if (languagePack != null && WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
                getGameManager().delete(languagePack);
                getGameManager().getGameLogService().addLog(
                        LogEntry.Module.SYSTEM, DELETE_LANGUAGEPACK,
                        languagePack.getLocale());
                // Deleted
                returnValue = new ModelAndView(new RedirectView(
                        successRedirect, true));
            } else {
                // Canceled
                returnValue = new ModelAndView(new RedirectView(cancelRedirect,
                        true));
            }
        } else {
            // Show form
            returnValue = new ModelAndView(view, m);
        }
        
        return returnValue;
    }
    
    public void setSuccessRedirect(String successRedirect) {
        this.successRedirect = successRedirect;
    }
    
    public void setCancelRedirect(String cancelRedirect) {
        this.cancelRedirect = cancelRedirect;
    }
    
    public void setView(String view) {
        this.view = view;
    }
    
}
