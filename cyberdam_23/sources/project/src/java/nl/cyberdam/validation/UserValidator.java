package nl.cyberdam.validation;

import nl.cyberdam.domain.User;

import org.hibernate.validator.InvalidValue;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Validator for a User object
 */
public class UserValidator implements Validator {
    
    /** Creates a new instance of UserValidator */
    public UserValidator() {
    }
    
    @SuppressWarnings("unchecked")
	public boolean supports(Class aClass) {
        return User.class.isAssignableFrom(aClass);
    }
    
    /**
     * all validation logic is done using annotation of the User class itself.
     */
    public void validate(Object object, Errors errors) {
        User user = (User) object;
        InvalidValue[] invalids = AnnotationValidator.getInvalidValues(user);
        for (InvalidValue invalidValue : invalids) {
            errors.rejectValue(invalidValue.getPropertyPath(),
            		invalidValue.getMessage(), invalidValue.getMessage()); // pass in message as second argument too
            															   // to prevent 'null' appearing like 'null.command.username'
        }
    }
    
}
