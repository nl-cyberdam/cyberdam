package nl.cyberdam.validation;

import nl.cyberdam.domain.Playground;

import org.hibernate.validator.InvalidValue;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlaygroundValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return Playground.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
        Playground po = (Playground) target;
        InvalidValue[] invalids = AnnotationValidator.getInvalidValues(po);
        for (InvalidValue invalidValue : invalids) {
            errors.rejectValue(invalidValue.getPropertyPath(),
            		invalidValue.getMessage(), invalidValue.getMessage()); // pass in message as second argument too
        }
		
	}

}