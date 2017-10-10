package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.Participant;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * if 'restore' set to true the operation is reversed
 */
public class SessionDeleteMessageController extends AbstractGameController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long messageId = Long.valueOf(ServletRequestUtils
		.getRequiredLongParameter(request, "id"));
		Long participantId = Long.valueOf(ServletRequestUtils
				.getRequiredLongParameter(request, "participantId"));
		Boolean restore = ServletRequestUtils.getBooleanParameter(request, "restore");
		String box = ServletRequestUtils.getRequiredStringParameter(request, "box");
		Participant participant = getGameManager()
				.loadParticipant(participantId);
		
		Message message = getGameManager().loadMessage(messageId);
		if (restore != null && restore.booleanValue()) {
			if (box.equals("inbox")) {
				participant.getTrash().getMessages().remove(message);
				participant.getInbox().getMessages().add(message);
			} else if (box.equals("outbox")) {
				participant.getOuttrash().getMessages().remove(message);
				participant.getOutbox().getMessages().add(message);
			}
		} else {
			if (box.equals("inbox")) {
				participant.getInbox().getMessages().remove(message);
				participant.getTrash().getMessages().add(message);	
			} else if (box.equals("outbox")) {
				participant.getOutbox().getMessages().remove(message);
				participant.getOuttrash().getMessages().add(message);
			}
		}
		getGameManager().save(participant);
		
		// if message was restored, stay on messagetrash page, otherwise stay on messages page
		if (restore != null && restore.booleanValue()) {
			return new ModelAndView(new RedirectView("messagetrash.htm", true), "participantId", participantId);
		} else {
			return new ModelAndView(new RedirectView("messages.htm", true), "participantId", participantId);
		}
	}
}
