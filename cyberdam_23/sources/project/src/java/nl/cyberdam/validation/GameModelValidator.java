package nl.cyberdam.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import nl.cyberdam.domain.GameModel;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class GameModelValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return GameModel.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		GameModel model = (GameModel) target;
		Pattern p = Pattern.compile("\\s*");
		Matcher m = p.matcher(model.getName());
		if (m.matches()) {
			errors.reject("empty.gamemodel");
		}
		m = p.matcher(model.getCaption());
		if (m.matches()) {
			errors.reject("caption.empty");
		}
	}
}
