package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.SessionResource;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * if 'restore' set to true the operation is reversed
 */
public class SessionDeleteFileController extends AbstractGameController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long resourceId = Long.valueOf(ServletRequestUtils
		.getRequiredLongParameter(request, "id"));
		Long participantId = Long.valueOf(ServletRequestUtils
				.getRequiredLongParameter(request, "participantId"));
		Boolean restore = ServletRequestUtils.getBooleanParameter(request, "restore");
		Participant participant = getGameManager()
				.loadParticipant(participantId);
		
		SessionResource file = getGameManager().loadSessionResource(resourceId);
		if (restore != null && restore.booleanValue()) {
			participant.getSessionResourceTrash().getResources().remove(file);
			participant.getSessionResourceBox().getResources().add(file);
		} else {
			participant.getSessionResourceBox().getResources().remove(file);
			participant.getSessionResourceTrash().getResources().add(file);
		}
		getGameManager().save(participant);
		
		return new ModelAndView(new RedirectView("filedirectory.htm", true), "participantId", participantId);
	}

}
