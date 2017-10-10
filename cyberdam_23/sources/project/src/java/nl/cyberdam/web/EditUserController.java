package nl.cyberdam.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Group;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.UserStatus;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.web.customeditors.GroupEditor;

import org.hibernate.SessionFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class EditUserController extends AbstractUserController {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /** Creates a new instance of EditUserController */
    public EditUserController() {
        setCommandClass(EditUserCommand.class);
        setSessionForm(true); // Store the command object in session instead of instantiating it in both edit and save
    }
    
    
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        binder.registerCustomEditor(Group.class, "user.groups", new GroupEditor(getUserManager()));
    }
    

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	Long id = ServletRequestUtils.getLongParameter(request, "id");   	
    	if (id != null) {
    		return new EditUserCommand(getUserManager().loadUser(id));
    	} else {
    		User u = new User();
    		Locale defaultLocale = new Locale(gameManager.loadSystemParameters().getDefaultLanguageCode());
    		u.setLocale(defaultLocale);
    		SystemParameters systemParams = gameManager.loadSystemParameters();
    		u.setNotifyNewMessages(systemParams.isVleMessage());
    		u.setDefaultNotifyNewStepOfPlay(systemParams.isVleStep());
    		return new EditUserCommand(u);
    	}
    }
    
    /**
     * checks if the user has the right permissions
     */
    @Override
    protected ModelAndView showForm(HttpServletRequest request,
    		HttpServletResponse response, BindException errors)
    		throws Exception {
    	Long id = ServletRequestUtils.getLongParameter(request, "id");
    	User currentUser = GameUtil.getCurrentUser();
    	boolean userIsAdmin = currentUser.getGameAuthorities().isSystemAdministrator() || currentUser.getGameAuthorities().isUserAdministrator();
    	if (id != null && id.equals(currentUser.getId()) || userIsAdmin) {
    		return super.showForm(request, response, errors);
    	} else {
    		logger.info("illegal attempt to visit the edituserpage");
    		return new ModelAndView(new RedirectView("index.htm", true));
    	}
    }

	public Map<String, String> getGroupList() {
		Map<String, String> retV = new HashMap<String, String>();
		Collection<Group> coll = getUserManager().findallGroups();
		if (coll !=null) {
			Iterator<Group> iterator = coll.iterator();
			while (iterator.hasNext()) {
				Group group = iterator.next();
				retV.put(group.getId().toString(), group.getName());		
			}
		}
		return retV;
	}
    
    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        // return super.referenceData(request);
        Map<String, Object> m = new HashMap<String, Object>();
        m.put("languages", messageSource.supportedLanguages());
        m.put("statusoptions",  UserStatus.values());
        m.put("grouplist", getUserManager().findallGroups().toArray());
        return m;
    }

    
    @Override
    protected ModelAndView onSubmit(
			HttpServletRequest request,	HttpServletResponse response, Object command,	BindException errors)
			throws Exception {
    	EditUserCommand editUserCommand = (EditUserCommand) command;
    	User u = editUserCommand.getUser();
    	boolean newUser = false;
    	if (u.getId() == null) {
    		getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM, "add user", u.getUsername());
    		newUser = true;
    	} else {
    		getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM, "edit user", u.getUsername());
    	}
    	// the validator should catch errors in the password size
    	boolean passwordChange = false;
    	if (editUserCommand.getPassword() != null && editUserCommand.getPassword().length() > 0) {
    		// encode password
    		u.setPassword(passwordEncoder.encodePassword(editUserCommand.getPassword(), null));
    		passwordChange = true;
    	}
    	try {
    		getUserManager().saveUser(u);
    		// _now_ send the email about the password change or new user
    		if (newUser) {
    			SimpleMailMessage welcomeMessage = new SimpleMailMessage();
    			Locale userLocale = u.getLocale();
    			String emailSubject = messageSource.getMessage("email.newuser.subject", null, userLocale);
    			Object[] bodyOptions = {u.getUsername(), editUserCommand.getPassword()};
    			String emailBody = messageSource.getMessage("email.newuser.body", bodyOptions, userLocale);        	
    			welcomeMessage.setTo(u.getEmail());
    			welcomeMessage.setSubject(emailSubject);
    			welcomeMessage.setText(emailBody);
    			welcomeMessage.setFrom(gameManager.loadSystemParameters().getEmail());
    			mailService.send(welcomeMessage);
    		} else if (passwordChange) {
    			// log
    			gameManager.getGameLogService().addLog(LogEntry.Module.SYSTEM, "password change", u.getUsername());
    			// send email
    			SimpleMailMessage passwordNotification = new SimpleMailMessage();
    			Locale userLocale = u.getLocale();
    			String emailSubject = messageSource.getMessage("email.newpassword.subject", null, userLocale);
    			Object[] bodyOptions = {u.getUsername(), editUserCommand.getPassword()};
    			String emailBody = messageSource.getMessage("email.newpassword.body", bodyOptions, userLocale);        	
    			passwordNotification.setTo(u.getEmail());
    			passwordNotification.setSubject(emailSubject);
    			passwordNotification.setText(emailBody);
    			passwordNotification.setFrom(gameManager.loadSystemParameters().getEmail());
    			mailService.send(passwordNotification);
    		}
    	} catch (org.springframework.dao.DataIntegrityViolationException dive) {
    		errors.setNestedPath("user");
    		errors.rejectValue("username", "alreadytaken");
    		errors.setNestedPath("");
    		return showForm(request, response, errors);
    	}
    	if (acegiCache != null) {
    		logger.debug("clearing acegi user cache for user");
    		acegiCache.removeUserFromCache(u.getUsername());
        }
    	return new ModelAndView(getSuccessView());
    }
    
}
