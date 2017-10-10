package nl.cyberdam.web.validation;

import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.domain.User;
import nl.cyberdam.validation.UserValidator;
import nl.cyberdam.web.EditUserCommand;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class EditUserCommandValidator implements Validator {
	
	@SuppressWarnings("unchecked")
	public boolean supports(Class c) {
		return EditUserCommand.class.isAssignableFrom(c);
	}

	public void validate(Object command, Errors errors) {
		EditUserCommand editUserCommand = (EditUserCommand) command;
		if (editUserCommand.getPassword() != null && editUserCommand.getPassword().length() > 0) {
			
			// check size
			if (editUserCommand.getPassword().length() < SystemParameters.MIN_PASSWORD_LENGTH) {
				errors.reject("password.min.size", new Object[] {new Integer(SystemParameters.MIN_PASSWORD_LENGTH)} , "");
			} else if (!editUserCommand.getPassword().equals(editUserCommand.getConfirmPassword())) {
				errors.reject("password.does.not.match");
			}
		}
		
		// validate the User object
		User u = editUserCommand.getUser();
		UserValidator userValidator = new UserValidator();
		errors.setNestedPath("user");
		userValidator.validate(u, errors);
		errors.setNestedPath("");
	}

}
