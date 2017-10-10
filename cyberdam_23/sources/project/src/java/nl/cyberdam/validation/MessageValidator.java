package nl.cyberdam.validation;

import nl.cyberdam.domain.Message;

import org.hibernate.validator.InvalidValue;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class MessageValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return Message.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		Message msg = (Message) target;
        InvalidValue[] invalids = AnnotationValidator.getInvalidValues(msg);
        for (InvalidValue invalidValue : invalids) {
            errors.rejectValue(invalidValue.getPropertyPath(),
            		invalidValue.getMessage(), invalidValue.getMessage()); // pass in message as second argument too
        }
		
	}

}
