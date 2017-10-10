package nl.cyberdam.web;

import nl.cyberdam.multilanguage.MultiLanguageSource;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.MailService;
import nl.cyberdam.service.UserManager;

import org.acegisecurity.providers.dao.UserCache;
import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.springframework.web.servlet.mvc.CancellableFormController;

public class AbstractUserController extends CancellableFormController {

	private UserManager userManager;
	protected GameManager gameManager;
	protected MultiLanguageSource messageSource;
	protected PasswordEncoder passwordEncoder;
	protected MailService mailService;
	protected UserCache acegiCache;

	public AbstractUserController() {
		super();
	}

	public UserManager getUserManager() {
	    return userManager;
	}

	public void setUserManager(UserManager userManager) {
	    this.userManager = userManager;
	}

	public MultiLanguageSource getMessageSource() {
	    return messageSource;
	}

	public void setMessageSource(MultiLanguageSource messageSource) {
	    this.messageSource = messageSource;
	}

	public void setAcegiCache(UserCache acegiCache) {
		this.acegiCache = acegiCache;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

}