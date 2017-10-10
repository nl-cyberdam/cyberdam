package nl.cyberdam.service;

import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

/**
 * send email using a TaskExcutor 
 */
public class MailServiceImpl implements MailService {

	private MailSender mailSender;
	private TaskExecutor taskExecutor;
	
	/**
	 * should be injected before calling deliverMessage() because the mailSender is used
	 * to send email notifications of new messages in the game session.
	 */
    public void setMailSender(MailSender mailSender) {
		this.mailSender = mailSender;
	}

    /**
     * should be injected 
     */
    public void setTaskExecutor(TaskExecutor taskExecutor) {
    	this.taskExecutor = taskExecutor;
    }

    /**
     * queue a message using the taskexecutor
     */
	public void send(SimpleMailMessage m) {
		// mailSender.send(m);
		MailSendTask task = new MailSendTask(m);
		taskExecutor.execute(task);
	}
	
	/**
	 * simple wrapper class to put the email in a taskexecutor 
	 */
	class MailSendTask implements Runnable {
		
		private SimpleMailMessage message;
		
		public MailSendTask(SimpleMailMessage message) {
			this.message = message;
		}

		public void run() {
			mailSender.send(message);
		}
	}
}
