package nl.cyberdam.validation;

import java.util.regex.*;

import nl.cyberdam.domain.Variable;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VariableValidator implements Validator {
    
	
    @SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
        return Variable.class.isAssignableFrom(clazz);
    }
    
    public void validate(Object target, Errors errors) {
        Variable variable = (Variable) target;
        Pattern p = Pattern.compile("[a-zA-Z_$][\\w$]{0,254}");
       	Matcher m = p.matcher(variable.getName());
       	if (!m.matches()) {
       		errors.reject("variable.error.syntax");
        }
    }
    
}
