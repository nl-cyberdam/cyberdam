package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.User;
import nl.cyberdam.util.GameUtil;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * user is always the current user logged in
 */
public class MySettingsController extends AbstractUserController {

	PasswordEncoder passwordEncoder;

	/** Creates a new instance of EditUserController */
    public MySettingsController() {
        setCommandClass(EditUserCommand.class);
        // setSessionForm(true); // Store the command object in session instead of instantiating it in both edit and save
    }

    /**
     * always return the current user from the security context
     */
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	User currentUser = getUserManager().loadUser(GameUtil.getCurrentUser().getId());
    	return new EditUserCommand(currentUser);
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        // return super.referenceData(request);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("languages", messageSource.supportedLanguages());
        return m;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
    	User currentUser = getUserManager().loadUser(GameUtil.getCurrentUser().getId());
    	String oldPassword = currentUser.getPassword();
    	request.setAttribute("nl.cyberdam.web.oldPassword", oldPassword);
    	return super.handleRequestInternal(request, response);
    }

    @Override
    protected ModelAndView onSubmit(
			HttpServletRequest request,	HttpServletResponse response, Object command,	BindException errors)
			throws Exception {
    	EditUserCommand editUserCommand = (EditUserCommand) command;
    	User u = editUserCommand.getUser();

    	getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM, "edit user", u.getUsername());

    	String password = u.getPassword();
    	if (password != null && password.length() != 0)
    	{
    		u.setPassword(passwordEncoder.encodePassword(password, null));
    	}
    	else
    	{
    		password = (String) request.getAttribute("nl.cyberdam.web.oldPassword");
    		u.setPassword(password);
    	}
    	getUserManager().saveUser(u);

    	if (acegiCache != null) {
    		logger.debug("clearing acegi user cache for user");
    		acegiCache.removeUserFromCache(u.getUsername());
        }
    	return new ModelAndView(getSuccessView());
    }

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
