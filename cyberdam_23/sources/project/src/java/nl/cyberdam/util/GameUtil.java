package nl.cyberdam.util;

import nl.cyberdam.domain.User;

import org.acegisecurity.context.SecurityContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GameUtil {
	
	static Log logger = LogFactory.getLog(GameUtil.class);
	
	/**
	 * return the current cyberdam user by getting it from te acegi security context
	 * if the principal is not a User object return null. If the context or 
	 * authentication objecs in the SecurityContextHolder are null this will also
	 * return null. (this happens mostly when the server has been reset, or if the session
	 * timed out)
	 */
	static public User getCurrentUser() {
		try {
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof User) {
				return (User) principal;
			} else {
				return null;
			}
		} catch (NullPointerException npe) {
			// log and just return null
			logger.warn("NPE while trying to get the user object from the security context", npe);
			return null;
		}
	}

}
