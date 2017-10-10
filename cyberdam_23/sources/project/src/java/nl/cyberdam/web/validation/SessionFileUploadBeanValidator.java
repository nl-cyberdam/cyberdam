package nl.cyberdam.web.validation;

import nl.cyberdam.service.GameManager;
import nl.cyberdam.web.SessionFileUploadBean;
import nl.cyberdam.web.ResourceUploadBean;
import nl.cyberdam.web.FileUploadBean;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SessionFileUploadBeanValidator implements Validator {
	
	GameManager gameManager;

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@SuppressWarnings("unchecked")
	public boolean supports(Class clazz) {
		return SessionFileUploadBean.class.isAssignableFrom(clazz) || ResourceUploadBean.class.isAssignableFrom(clazz);
	}

	public void validate(Object target, Errors errors) {
		FileUploadBean uploadBean = (FileUploadBean) target;
		
		if (uploadBean.getFile() == null || uploadBean.getFile().getSize() == 0 ) {
			errors.reject("no.file.specified");
		} else if (uploadBean.getFile() != null && uploadBean.getFile().getSize() > gameManager.loadSystemParameters().getUploadSizeMaxBytes()) {
			errors.reject("filesize.too.large");
		}
	}

}
