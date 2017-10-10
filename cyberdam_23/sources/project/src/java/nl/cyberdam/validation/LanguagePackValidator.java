package nl.cyberdam.validation;

import nl.cyberdam.multilanguage.LanguagePack;

import org.hibernate.validator.InvalidValue;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class LanguagePackValidator implements Validator {
    
    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return LanguagePack.class.isAssignableFrom(clazz);
    }
    
    public void validate(Object target, Errors errors) {
        LanguagePack languagePack = (LanguagePack) target;
        InvalidValue[] invalids = AnnotationValidator
                .getInvalidValues(languagePack);
        for (InvalidValue invalidValue : invalids) {
            errors.rejectValue(invalidValue.getPropertyPath(), invalidValue
                    .getMessage(), invalidValue.getMessage());
        }
    }
    
}
