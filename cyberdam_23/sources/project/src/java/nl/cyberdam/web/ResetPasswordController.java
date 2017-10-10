package nl.cyberdam.web;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.MailService;
import nl.cyberdam.service.PasswordGenerator;
import nl.cyberdam.service.UserManager;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.springframework.context.MessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class ResetPasswordController extends AbstractController {

	private UserManager userManager;
	private MailService mailService;
	private GameManager gameManager;
	private PasswordGenerator passwordGenerator;
	private PasswordEncoder passwordEncoder;
	private MessageSource messageSource;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		ModelAndView mav = new ModelAndView("index");

		String userName = ServletRequestUtils.getStringParameter(request, "j_username");
		if (userName == null || userName.length() == 0) {
			mav.addObject("resetMissing", new Boolean(true));
		} else {
			for (Object userObject : userManager.findAllActiveUsers()) {
				User user = (User) userObject;
				if (userName.equals(user.getUsername())) {
					String password = passwordGenerator.getPassword();
					user.setPassword(passwordEncoder.encodePassword(password, null));
					userManager.saveUser(user);
					SimpleMailMessage message = new SimpleMailMessage();
					message.setTo(user.getEmail());
					Locale userLocale = user.getLocale();
					String subject = messageSource.getMessage(
							"email.reset.subject", null, userLocale);
					message.setSubject(subject);
					Object[] bodyOptions = { user.getUsername(), password };
					String body = messageSource.getMessage(
							"email.reset.body", bodyOptions, userLocale);
					message.setText(body);
					message.setFrom(gameManager.loadSystemParameters().getEmail());
					mailService.send(message);
				}
			}
			mav.addObject("resetDone", new Boolean(true));
		}

		return mav;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	public PasswordGenerator getPasswordGenerator() {
		return passwordGenerator;
	}

	public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
		this.passwordGenerator = passwordGenerator;
	}

	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
