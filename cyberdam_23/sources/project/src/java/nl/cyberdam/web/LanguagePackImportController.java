package nl.cyberdam.web;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import nl.cyberdam.domain.ObjectFactory;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.multilanguage.LanguagePack;
import nl.cyberdam.multilanguage.MultiLanguageMessage;
import nl.cyberdam.util.NoCloseZipInputStream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.validator.InvalidValue;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;
import org.xml.sax.SAXException;

public class LanguagePackImportController extends AbstractFormController {
    
    public static final String PARSE_ERROR           = "languagepack.parse.error";
    public static final String FORMAT_ERROR           = "languagepack.format.error";
    public static final String BUTTON_OK             = "_OK";
    public static final String BUTTON_UPLOAD         = "_upload";
    public static final String BUTTON_CANCEL         = "_cancel";
    public static final String KEY_ERRORS            = "errors";
    public static final String KEY_AREYOUSURE        = "areyousure";
    public static final String KEY_NUMBER_OF_OBJECTS = "numberOfObjects";
    public static final String KEY_LANGUAGEPACK      = "languagePack";
    protected SessionFactory   sessionFactory;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        return new SessionFileUploadBean();
    }
    
    @Override
    protected ModelAndView showForm(HttpServletRequest request,
            HttpServletResponse resoponse, BindException errors)
            throws Exception {
        return new ModelAndView("languagepackimport");
    }
    
    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ModelAndView returnValue = new ModelAndView(new RedirectView(
                "languagepacks.htm", true));
        Map<String, Object> data = new HashMap<String, Object>();
        if (WebUtils.hasSubmitParameter(request, BUTTON_CANCEL)) {
            ;
        } else if (WebUtils.hasSubmitParameter(request, BUTTON_UPLOAD)) {
            LanguagePack languagePack;
            try {
                languagePack = importFromXML((SessionFileUploadBean) command);
            } catch (Exception e) {
                // parse error (import)
                errors.reject(PARSE_ERROR);
                data.put(KEY_ERRORS, errors.getGlobalErrors());
                return new ModelAndView("languagepackimport", data);
            }
            prepareData(data, languagePack);
            if (null != LanguagePack.findLocale(sessionFactory
                    .getCurrentSession(), languagePack.getLocale())) {
                // this locale exists (are you sure?)
                data.put(KEY_AREYOUSURE, Integer.valueOf(1));
                request.getSession().setAttribute(KEY_LANGUAGEPACK,
                        languagePack);
                returnValue = new ModelAndView("languagepackimport", data);
            } else {
                // new locale (summary)
            	try {
            		save(languagePack);
            		returnValue = new ModelAndView("languagepacksummary", data);
            	} catch (org.hibernate.validator.InvalidStateException ise) {
            		InvalidValue[] invalidValues = ise.getInvalidValues();
            		for (InvalidValue iv : invalidValues) {
            			errors.reject(FORMAT_ERROR + "." + iv.getPropertyName());
            		}
                    data.put(KEY_ERRORS, errors.getGlobalErrors());
            		return new ModelAndView("languagepackimport", data);
            	}
            }
        } else if (WebUtils.hasSubmitParameter(request, BUTTON_OK)) {
            // summary
            LanguagePack languagePack = (LanguagePack) request.getSession()
                    .getAttribute(KEY_LANGUAGEPACK);
            request.getSession().setAttribute(KEY_LANGUAGEPACK, null);
            prepareData(data, languagePack);
            deleteAndSave(languagePack);
            returnValue = new ModelAndView("languagepacksummary", data);
        }
        return returnValue;
    }
    
    public void prepareData(Map<String, Object> data, LanguagePack languagePack) {
        data.put(KEY_LANGUAGEPACK, languagePack);
        data.put(KEY_NUMBER_OF_OBJECTS, Integer.valueOf(languagePack.getMessages().size()));
    }
    
    public String getNext(NoCloseZipInputStream inputStream) throws IOException {
        ZipEntry entry = null;
        if ((entry = inputStream.getNextEntry()) != null) {
            return entry.getName();
        }
        return null;
    }
    
    public LanguagePack importFromXML(
            SessionFileUploadBean sessionFileUploadBean) throws IOException,
            JAXBException, SAXException {
        NoCloseZipInputStream is = new NoCloseZipInputStream(
                sessionFileUploadBean.getFile().getInputStream());
        JAXBContext jaxbContext = JAXBContext.newInstance("nl.cyberdam.domain");
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setEventHandler(new XmlTransformException());
        SchemaFactory sf = SchemaFactory
                .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
        unmarshaller.setSchema((sf.newSchema(new File(getServletContext()
                .getRealPath(SystemParameters.XMLSCHEMA)))));
        String xmlName = getNext(is);
        LanguagePack languagePack = (new ObjectFactory()).createLanguagePack();
        JAXBElement<?> element = (JAXBElement<?>) unmarshaller.unmarshal(is);
        languagePack = (LanguagePack) element.getValue();
        
        // Prepare for Hibernate
        languagePack.setId(null);
        for (MultiLanguageMessage message : languagePack.getMessages()) {
            message.setLanguagePack(languagePack);
            message.setId(null);
        }
        
        return languagePack;
    }
    
    @Transactional
    protected void save(LanguagePack languagePack) {
        sessionFactory.getCurrentSession().saveOrUpdate(languagePack);
    }
    
    @Transactional
    protected void deleteAndSave(LanguagePack languagePack) {
        Session session = sessionFactory.getCurrentSession();
        LanguagePack origLanguagePack = LanguagePack.findLocale(session,
                languagePack.getLocale());
        Long id = origLanguagePack.getId();
        origLanguagePack = (LanguagePack) session.load(LanguagePack.class, id);
        if (null != origLanguagePack) {
            session.delete(origLanguagePack);
            session.flush();
        }
        session.save(languagePack);
    }
    
}
