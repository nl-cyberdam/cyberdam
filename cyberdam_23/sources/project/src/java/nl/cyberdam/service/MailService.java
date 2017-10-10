package nl.cyberdam.service;

import org.springframework.mail.SimpleMailMessage;

/**
 * deliver the message - could be queued
 */
public interface MailService {
	
	public void send(SimpleMailMessage m);

}
