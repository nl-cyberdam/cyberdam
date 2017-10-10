package nl.cyberdam.util;

import nl.cyberdam.domain.User;
import nl.cyberdam.service.LogService;
import nl.cyberdam.service.UserManager;

import org.acegisecurity.event.authentication.AuthenticationSuccessEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * add <bean class="nl.cyberdam.util.UpdateLastLoginListener"/> to the
 * spring bean config to have the last login date of users updated on 
 * every login.
 */
public class UpdateLastLoginListener implements ApplicationListener {

    private Log logger = LogFactory.getLog(getClass());
    private UserManager userManager;
    private LogService logService;
    
    /**
     * UserManager should be injected
     * 
     * @param userManager
     */
    public void setUserManager(UserManager userManager) {
    	this.userManager = userManager;
    }

    //
    // implements ApplicationListener
    //
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent authenticationSuccessEvent = (AuthenticationSuccessEvent) event;
            // update lastLogin field
            User u = (User) authenticationSuccessEvent.getAuthentication().getPrincipal();
            userManager.updateLastLogin(u);
            logService.userLoggedIn(u);
        }
    }

    /**
     * injected 
     */
	public void setLogService(LogService logService) {
		this.logService = logService;
	}
}