/*
 * AddUserController.java
 *
 */

package nl.cyberdam.web;

import nl.cyberdam.domain.User;
import nl.cyberdam.service.UserManager;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 *
 */
public class AddUserController extends CancellableFormController {
    
    private UserManager userManager;
    // this should be set using the spring container
    private PasswordEncoder passwordEncoder;
    
    /** Creates a new instance of AddUserController */
    public AddUserController() {
        setCommandClass(User.class);
    }
    
    protected void doSubmitAction(Object command) {
        User u = (User) command;
        // encode password before saving the object
        u.setPassword(passwordEncoder.encodePassword(u.getPassword(),null));
        getUserManager().saveUser(u);
    }
    
    public UserManager getUserManager() {
        return userManager;
    }
    
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
}

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
    
}
