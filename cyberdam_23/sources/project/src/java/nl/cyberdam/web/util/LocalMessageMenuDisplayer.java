package nl.cyberdam.web.util;

import net.sf.navigator.displayer.ListMenuDisplayer;
import nl.cyberdam.multilanguage.MessageSource;

public class LocalMessageMenuDisplayer extends ListMenuDisplayer {

	MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	public String getMessage(String key) {
		return messageSource.getMessage("nl", key).getMessage();
	}
	
}
