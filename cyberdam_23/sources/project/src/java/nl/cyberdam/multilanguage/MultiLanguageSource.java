package nl.cyberdam.multilanguage;

import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.springframework.context.MessageSource;

public interface MultiLanguageSource extends MessageSource {

	public List<MultiLanguageMessage> getMessages();

	public List<MultiLanguageMessage> getMessagesByCode(String code);

	/**
	 * @param language 2 letter language abbrev.
	 */
	public List<MultiLanguageMessage> getMessagesForLanguage(String language);
	public List<MultiLanguageMessage> getMessagesForLanguage(Long languagePackId, String code, String message);

	public MultiLanguageMessage getMessage(String language, String code);

	public MultiLanguageMessage getMessage(Long id);

	public void save(MultiLanguageMessage message);
	public void delete(MultiLanguageMessage message);

	public void setSessionFactory(SessionFactory sessionFactory);

	/**
	 * A list of languages supported by the system - these can be
	 * used in the gui to show languages that the user can choose / edit
	 */
	public List<LanguagePack> supportedLanguages();

	public Properties getSystemProperties();

	public void setSystemProperties(Properties systemProperties);

	public LanguagePack getLanguagePack(Long id);
}