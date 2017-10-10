package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.multilanguage.MultiLanguageMessage;
import nl.cyberdam.multilanguage.MultiLanguageSource;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class DeleteMultilanguageMessageController extends AbstractGameController {

    private MultiLanguageSource multilanguageManager;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
		MultiLanguageMessage message = multilanguageManager.getMessage(id);
		Long languagePackId = message.getLanguagePack().getId();
		multilanguageManager.delete(message);
		
		// redirect
		return new ModelAndView(new RedirectView("multilanguage.htm?forceRefresh=true", true), "languagePack", languagePackId);
	}

    public void setMessageSource(MultiLanguageSource mm) {
        multilanguageManager = mm;
    }
}
