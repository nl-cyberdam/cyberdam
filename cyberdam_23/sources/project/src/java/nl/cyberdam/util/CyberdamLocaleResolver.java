package nl.cyberdam.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.LocaleResolver;

/**
 * If a real cyberdam user is logged in use the locale
 * set for that user, otherwise fall back to the default
 * one.
 */
public class CyberdamLocaleResolver implements LocaleResolver {
	
	Log logger = LogFactory.getLog(getClass());
	GameManager gameManager;

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public Locale resolveLocale(HttpServletRequest request) {
		logger.debug("resolveLocale()");
		User u = GameUtil.getCurrentUser();
		if (u != null) {
			return u.getLocale();
		}
		return getDefaultLocale();
	}

	/**
	 * empty implementation.
	 */
	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		throw new UnsupportedOperationException("Setting the locale should be done on the User object.");
	}
	
	private Locale getDefaultLocale() {
		logger.debug("getDefaultLocale()");
		if (gameManager != null) {
			logger.debug("getDefaultLocale() return default language");
			return new Locale(gameManager.loadSystemParameters().getDefaultLanguageCode());
		} else {
			logger.debug("getDefaultLocale() return null");
			return null;
		}
		
	}

}
