package nl.cyberdam.validation;

import java.util.List;

import nl.cyberdam.domain.VariableSessionValue;
import nl.cyberdam.web.SessionFormActivityController;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class FormActivityValidator implements Validator {
    
    @SuppressWarnings("unchecked")
    public boolean supports(Class clazz) {
        return SessionFormActivityController.ListWrapper.class.isAssignableFrom(clazz);
    }
    
    public void validate(Object target, Errors errors) {
        List<VariableSessionValue> variableValues = ((SessionFormActivityController.ListWrapper) target).getVariableValues();
        List<Boolean> enabledList = ((SessionFormActivityController.ListWrapper) target).getEnabledList();
        for (int valueIndex = 0 ; valueIndex < variableValues.size() ; ++valueIndex) {
        	if (enabledList.get(valueIndex).booleanValue()) {
        		VariableSessionValue variableValue = variableValues.get(valueIndex);
        		
        		if ((variableValue.getValue() == null || variableValue.getValue().length() == 0) && 
        				variableValue.getActivityVariable().getMandatory().booleanValue()) {
        			errors.rejectValue ("variableValues[" + (valueIndex) + "].value", "activityvariable.mandatory", "activityvariable.mandatory");
        		}
        	}
        }
    }
}
