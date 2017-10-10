package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.multilanguage.LanguagePack;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

public class LanguagePackEditorController extends CancellableFormController {
    
    protected static final String ALREADYTAKEN     = "alreadytaken";
    protected static final String LOCALE           = "locale";
    protected static final String LANGUAGE_PACK_ID = "languagePackId";
    private SessionFactory        sessionFactory;
    
    public LanguagePackEditorController() {
        setCommandClass(LanguagePack.class);
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        
        Long languagePackId = ServletRequestUtils.getLongParameter(request,
                LANGUAGE_PACK_ID);
        LanguagePack languagePack;
        if (languagePackId != null) {
            languagePack = (LanguagePack) getSessionFactory()
                    .getCurrentSession().load(LanguagePack.class,
                            languagePackId);
        } else {
            languagePack = new LanguagePack();
        }
        return languagePack;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ModelAndView returnValue = new ModelAndView(getSuccessView());
        LanguagePack languagePack = (LanguagePack) command;
        getValidator().validate(languagePack, errors);
        if (errors.hasErrors()) {
            returnValue =  showForm(request, response, errors);
        } else {
            try {
                getSessionFactory().getCurrentSession().saveOrUpdate(languagePack);
                getSessionFactory().getCurrentSession().flush();
            } catch (ConstraintViolationException ce) {
                errors.rejectValue(LOCALE, ALREADYTAKEN);
                returnValue =  showForm(request, response, errors);               
            }
        }
        return returnValue;
    }
    
    protected ModelAndView onCancel(HttpServletRequest request,
            HttpServletResponse response, Object command) throws Exception {
        return new ModelAndView(getCancelView());
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    // Should be injected
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
}
