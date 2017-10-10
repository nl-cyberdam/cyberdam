package nl.cyberdam.multilanguage;

import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * This MessageSource searches for a message in the database, and of none is
 * found falls back on another MessageSource.
 *
 */
@Repository
@Transactional
public class MessageSource extends AbstractMessageSource implements MultiLanguageSource {

    private SessionFactory sessionFactory;
    private Properties systemProperties;
    Log logger = LogFactory.getLog(getClass());

    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#getMessages()
	 */
    @SuppressWarnings(value = "unchecked")
    public List<MultiLanguageMessage> getMessages() {
        return (List<MultiLanguageMessage>) sessionFactory.getCurrentSession().createQuery("from MultiLanguageMessage m").list();
    }

    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#getMessagesByCode(java.lang.String)
	 */
    @SuppressWarnings("unchecked")
    public List<MultiLanguageMessage> getMessagesByCode(String code) {
        return (List<MultiLanguageMessage>) sessionFactory.getCurrentSession().createQuery("from MultiLanguageMessage m where m.code is :code").setString("code", code).list();
    }
    
    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#getMessagesForLanguage(java.lang.String)
	 */
    @SuppressWarnings(value = "unchecked")
    public List<MultiLanguageMessage> getMessagesForLanguage(String language) {
    	Query q = sessionFactory.getCurrentSession().createQuery("select m from MultiLanguageMessage as m, LanguagePack lp where lp.locale is :locale and m member of lp.messages");
        q.setString("locale", language);
        return (List<MultiLanguageMessage>) q.list();
    }
    
    /**
     * default order by code
     */
	@SuppressWarnings("unchecked")
	public List<MultiLanguageMessage> getMessagesForLanguage(Long languagePackId, String textFilter, String no_use) {
	    textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
	    String filterCriteria = (textFilter == null) ? ":textFilter is null" : "lower(m.code || m.message) like :textFilter ";
		Query q = sessionFactory.getCurrentSession().createQuery(
				"select m from MultiLanguageMessage as m, LanguagePack lp where lp.id is :id and " +
				filterCriteria + " and m member of lp.messages ORDER BY m.code ")
				.setParameter ("textFilter", textFilter)
				.setLong("id", languagePackId.longValue());
        return (List<MultiLanguageMessage>) q.list();
	}
    
    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#getMessage(java.lang.String, java.lang.String)
	 */
    public MultiLanguageMessage getMessage(String language, String code) {
        Query q = sessionFactory.getCurrentSession().createQuery("from MultiLanguageMessage m where m.language is :language and m.code is :code");
        q.setString("code", code);
        q.setString("language", language);
        try {
        	return (MultiLanguageMessage) q.uniqueResult();        	
        } catch (HibernateException he) {
        	logger.error("Exception while retrieving code '" + code + "' in language '" + language + "'", he);
        	return null;
        }
    }
    
    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#getMessage(java.lang.Long)
	 */
    public MultiLanguageMessage getMessage(Long id) {
        return (MultiLanguageMessage) sessionFactory.getCurrentSession().load(MultiLanguageMessage.class, id);
    }

    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#save(nl.cyberdam.multilanguage.MultiLanguageMessage)
	 */
    public void save(MultiLanguageMessage message) {
        sessionFactory.getCurrentSession().saveOrUpdate(message);
    }
    
    
    public void delete(MultiLanguageMessage message) {
    	sessionFactory.getCurrentSession().delete(message);
    }

    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#setSessionFactory(org.hibernate.SessionFactory)
	 */
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     *
     */
    @Transactional(propagation = Propagation.SUPPORTS)
    protected MessageFormat resolveCode(String code, Locale locale) {
        logger.debug("resolveCode(): '" + code + "' for locale: " + locale);
        // NOTE: this is a 'hack' to make it possible to see the codes on the front end
        // if the user specifies language 'xx' the multilanguagekeys will be returned (between '[' and ']'
        // to make them easily identifyable
        if ("xx".equals(locale.getLanguage())) {
        	return new MessageFormat("[" + code + "]");
        }
// XXX this function does not do a full lookup like the default resolver - currently that behavior is only used by the Form Activity, so we special-case it
    	if (code.startsWith ("activityvariable.mandatory"))
    	{
    	    code = "activityvariable.mandatory";
    	}
        Query q = sessionFactory.getCurrentSession().createQuery("select m from MultiLanguageMessage as m, LanguagePack lp where m.code is :code and lp.locale is :locale and m member of lp.messages");
        q.setString("code", code);
        q.setString("locale", locale.getLanguage());
        MultiLanguageMessage m;
		try {
			m = (MultiLanguageMessage) q.uniqueResult();
		} catch (HibernateException he) {
        	logger.error("Exception while retrieving code '" + code + "' in locale '" + locale + "'", he);
        	return null;
        }

        if (m != null) {
            return new MessageFormat(m.getMessage());
        } else {
            // fallback to language = ""
            q.setString("code", code);
            q.setString("locale", "");
            try {
				m = (MultiLanguageMessage) q.uniqueResult();
			} catch (HibernateException he) {
	        	logger.error("Exception while retrieving code '" + code + "' in locale '" + locale + "'", he);
	        	return null;
	        }
            if (m != null) {
                return new MessageFormat(m.getMessage());
            } else {
                return null;
            }
        }
    }

    // XXX maybe later implement faster version
    @Override
    protected String resolveCodeWithoutArguments(String code, Locale locale) {
    	logger.debug("resolveCodeWithoutArguments(): '" + code + "' for locale: " + locale);
// XXX this function does not do a full lookup like the default resolver - currently that behavior is only used by the Form Activity, so we special-case it
    	if (code.startsWith ("activityvariable.mandatory"))
    	{
    	    code = "activityvariable.mandatory";
    	}
        MessageFormat messageFormat = resolveCode(code, locale);
        if (messageFormat != null) {
            synchronized (messageFormat) {
                return messageFormat.format(new Object[0]);
            }
        }
        return null;
    }
    
    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#supportedLanguages()
	 */
    @SuppressWarnings("unchecked")
	public List<LanguagePack> supportedLanguages() {
    	List<LanguagePack> languagePacks = (List<LanguagePack>) sessionFactory.getCurrentSession().createQuery("from LanguagePack").list();
        return languagePacks;
    }

    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#getSystemProperties()
	 */
    public Properties getSystemProperties() {
        return systemProperties;
    }

    /* (non-Javadoc)
	 * @see nl.cyberdam.multilanguage.MultiLanguageSource#setSystemProperties(java.util.Properties)
	 */
    public void setSystemProperties(Properties systemProperties) {
        this.systemProperties = systemProperties;
    }

	public LanguagePack getLanguagePack(Long id) {
		return (LanguagePack) sessionFactory.getCurrentSession().load(LanguagePack.class, id);
	}
}