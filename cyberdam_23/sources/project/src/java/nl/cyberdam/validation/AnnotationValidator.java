/*
 * AnnotationValidator.java
 *
 * http://www-128.ibm.com/developerworks/java/library/j-hibval.html
 */

package nl.cyberdam.validation;

import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.domain.User;
import nl.cyberdam.multilanguage.LanguagePack;

import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

/**
 * Handles validations based on the Hibernate Annotations Validator framework.
 * 
 * @author Ted Bergeron
 * @version $Id: AnnotationValidator.java,v 1.5 2006/01/20 17:34:09 ted Exp $
 */
public class AnnotationValidator {

	// It is considered a good practice to execute these lines once and
	// cache the validator instances.
	public static final ClassValidator<User> USER_VALIDATOR = new ClassValidator<User>(
			User.class);
	public static final ClassValidator<PlaygroundObject> PLAYGROUNDOBJECT_VALIDATOR = new ClassValidator<PlaygroundObject>(
			PlaygroundObject.class);
	public static final ClassValidator<Playground> PLAYGROUND_VALIDATOR = new ClassValidator<Playground>(
			Playground.class);
	public static final ClassValidator<Message> MESSAGE_VALIDATOR = new ClassValidator<Message>(
			Message.class);
	public static final ClassValidator<SystemParameters> SYSTEMPARAMETERS_VALIDATOR = new ClassValidator<SystemParameters>(
			SystemParameters.class);
	public static final ClassValidator<LanguagePack> LANGUAGEPACK_VALIDATOR = new ClassValidator<LanguagePack>(
	        LanguagePack.class); 

	private static ClassValidator<? extends Object> getValidator(
			Class<? extends Object> clazz) {
		if (User.class.isAssignableFrom(clazz)) {
			return USER_VALIDATOR;
		} else if (PlaygroundObject.class.isAssignableFrom(clazz)) {
			return PLAYGROUNDOBJECT_VALIDATOR;
		} else if (Playground.class.isAssignableFrom(clazz)) {
			return PLAYGROUND_VALIDATOR;
		} else if (Message.class.isAssignableFrom(clazz)) {
			return MESSAGE_VALIDATOR;
		} else if (SystemParameters.class.isAssignableFrom(clazz)) {
			return SYSTEMPARAMETERS_VALIDATOR;
		} else if (LanguagePack.class.isAssignableFrom(clazz)) {
		    return LANGUAGEPACK_VALIDATOR;
		} else {
			throw new IllegalArgumentException("Unsupported class '" + clazz
					+ "' was passed.");
		}
	}

	public static InvalidValue[] getInvalidValues(Object modelObject) {
		String nullProperty = null;
		return getInvalidValues(modelObject, nullProperty);
	}

	public static InvalidValue[] getInvalidValues(Object modelObject,
			String property) {
		Class<? extends Object> clazz = modelObject.getClass();
		ClassValidator validator = getValidator(clazz);

		InvalidValue[] validationMessages;

		if (property == null) {
			validationMessages = validator.getInvalidValues(modelObject);
		} else {
			// only get invalid values for specified property.
			// For example, "city" applies to getCity() method.
			validationMessages = validator.getInvalidValues(modelObject,
					property);
		}
		return validationMessages;
	}
}