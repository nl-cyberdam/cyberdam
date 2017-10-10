package nl.cyberdam.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.User;
import nl.cyberdam.service.LogService;

import org.acegisecurity.Authentication;
import org.acegisecurity.ui.logout.LogoutHandler;

public class LogLogoutHandler implements LogoutHandler {
	
	private LogService logService;

	public void logout(HttpServletRequest arg0, HttpServletResponse arg1,
			Authentication arg2) {
		User u = GameUtil.getCurrentUser();
		if (u != null) {
			logService.userLoggedOut(u);
		}
	}

	/**
	 * should be injected
	 */
	public void setLogService(LogService logService) {
		this.logService = logService;
	}

}
