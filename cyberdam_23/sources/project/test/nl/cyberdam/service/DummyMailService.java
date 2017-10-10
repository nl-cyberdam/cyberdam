package nl.cyberdam.service;

import org.springframework.mail.SimpleMailMessage;

/**
 * This class is a real dummy, it does nothing...
 */
public class DummyMailService implements MailService {

	public void send(SimpleMailMessage m) {
		// do nothing
	}

}
