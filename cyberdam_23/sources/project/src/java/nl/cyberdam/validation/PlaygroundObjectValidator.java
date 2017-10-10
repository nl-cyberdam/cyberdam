package nl.cyberdam.validation;

import nl.cyberdam.domain.PlaygroundObject;

import org.hibernate.validator.InvalidValue;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class PlaygroundObjectValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return PlaygroundObject.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
        PlaygroundObject po = (PlaygroundObject) target;
        InvalidValue[] invalids = AnnotationValidator.getInvalidValues(po);
        for (PlaygroundObject otherPo :  po.getPlayground().getPlaygroundObjects()) {
        	if (po.getId()!=otherPo.getId() && po.getUri().equals(otherPo.getUri())) {
				errors.reject("uri.notUnique");
			}
		}
        for (InvalidValue invalidValue : invalids) {
            errors.rejectValue(invalidValue.getPropertyPath(),
            		invalidValue.getMessage(), invalidValue.getMessage()); // pass in message as second argument too
        }
		
	}

}
