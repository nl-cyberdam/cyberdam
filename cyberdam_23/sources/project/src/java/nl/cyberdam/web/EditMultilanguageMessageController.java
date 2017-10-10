package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.multilanguage.LanguagePack;
import nl.cyberdam.multilanguage.MultiLanguageMessage;
import nl.cyberdam.multilanguage.MultiLanguageSource;
import nl.cyberdam.web.customeditors.LanguagePackEditor;

import org.hibernate.Hibernate;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

public class EditMultilanguageMessageController extends CancellableFormController {

    public static String MULTILANGUAGEMESSAGE = "multilanguagemessage";
    private MultiLanguageSource multilanguageManager;

    public EditMultilanguageMessageController() {
        setCommandName(MULTILANGUAGEMESSAGE);
        setSessionForm(true);
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long id = ServletRequestUtils.getLongParameter(request, "id");
        MultiLanguageMessage message;
        if (id != null) {
            message = multilanguageManager.getMessage(id);
        } else {
            message = new MultiLanguageMessage();
            ServletRequestDataBinder binder = new ServletRequestDataBinder(message);
            binder.registerCustomEditor(LanguagePack.class, "languagePack", new LanguagePackEditor(multilanguageManager));
            logger.debug("request languagePack: " + ServletRequestUtils.getIntParameter(request, "languagePack"));
            binder.bind(request);
            // evaluate binding errors?
            logger.debug(binder.getErrors());
        }
        return message;
    }
    
    @Override
    protected Map referenceData(HttpServletRequest request, Object command,
    		Errors errors) throws Exception {
    	Map<String, String> ref = new HashMap<String, String>();
    	Long languagePackId = ServletRequestUtils.getLongParameter(request, "languagePack");
    	if (languagePackId != null) {
    	} else if (command != null) {
    		MultiLanguageMessage message = (MultiLanguageMessage) command;
    		languagePackId = message.getLanguagePack().getId();
    	}
    	String languagePackName = multilanguageManager.getLanguagePack(languagePackId).getName();
    	ref.put("languagePackName", languagePackName);
    	return ref;
    }
    
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(LanguagePack.class, "languagePack", new LanguagePackEditor(multilanguageManager));
    }
    
    /**
     * since this method is overridden the doSubmitAction is no longer Called!
     */
    @Override
    protected ModelAndView onSubmit(
			HttpServletRequest request,	HttpServletResponse response, Object command,	BindException errors)
	throws Exception {
    	MultiLanguageMessage message = (MultiLanguageMessage) command;
		Long languagePackId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "languagePack"));
		Hibernate.initialize(multilanguageManager.getMessagesForLanguage(multilanguageManager.getLanguagePack(languagePackId).getName()));
    	
    	try {
			multilanguageManager.save(message);
		} catch (DataIntegrityViolationException e) {
			// signal there is an error with the language code (most probably an attempt to
			// use a code that already is in the db)
    		errors.rejectValue("code", "error");
    		return showForm(request, response, errors);
		}
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("languagePack", message.getLanguagePack().getId());
    	params.put("forceRefresh", "true");
    	return new ModelAndView(new RedirectView("multilanguage.htm", true), params);
    }
    
    
    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
    		HttpServletResponse response, Object command) throws Exception {
    	MultiLanguageMessage message = (MultiLanguageMessage) command;
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("languagePack", message.getLanguagePack().getId());
    	params.put("forceRefresh", "true");
    	return new ModelAndView(new RedirectView("multilanguage.htm", true), params);
    }

    /**
     * should be injected
     */
    public void setMessageSource(MultiLanguageSource mm) {
        multilanguageManager = mm;
    }
}