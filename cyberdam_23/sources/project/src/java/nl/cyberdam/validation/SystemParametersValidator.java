package nl.cyberdam.validation;

import nl.cyberdam.domain.SystemParameters;

import org.hibernate.validator.InvalidValue;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SystemParametersValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return SystemParameters.class.isAssignableFrom(clazz);
	}
	
	public void validate(Object target, Errors errors) {
		SystemParameters params = (SystemParameters) target;
        InvalidValue[] invalids = AnnotationValidator.getInvalidValues(params);
        for (InvalidValue invalidValue : invalids) {
            errors.rejectValue(invalidValue.getPropertyPath(),
            		invalidValue.getMessage(), invalidValue.getMessage()); // pass in message as second argument too
            															   // to prevent 'null' appearing like 'null.command.username'
        }
	}
}

