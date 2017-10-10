package nl.cyberdam.validation;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.RoleToPlaygroundMapping;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class GameManifestValidator implements Validator {

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return GameManifest.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		GameManifest manifest = (GameManifest) target;
		if (manifest != null && manifest.getRolesToPlaygroundObjects() != null) {
			for (RoleToPlaygroundMapping rtpm : manifest.getRolesToPlaygroundObjects()) {
				if (rtpm.getPlaygroundObject() == null && rtpm.getPlayground() != null && !rtpm.getPlayground().isHttpLink()) {
					errors.reject("playgroundlink.not.http");
				}
			}
		}
	}
}
