package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for viewing a message sent during a session.
 */
public class MessageController extends AbstractGameController {

	/** Creates a new instance of MessageController */
	private MenuUtil menuUtil;
	public MessageController() {
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long participantId = new Long(ServletRequestUtils.getRequiredLongParameter(request,
				"participantId"));
		Long messageId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
		String box = ServletRequestUtils.getStringParameter(request, "box");
		Participant participant = getGameManager().loadParticipant(
				participantId);
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU,participant.getGameSession().getManifest());
		Message message = null;
		Map<String, Object> data = new HashMap<String, Object>();
		if (box == null || box.equals("inbox")) {
			message = participant.getInbox().getMessage(messageId);
			data.put("incoming", Boolean.TRUE);
		} else if (box.equals("outbox")) {
			message = participant.getOutbox().getMessage(messageId);
			data.put("incoming", Boolean.FALSE);
		} else if (box.equals("trash")) {
			message = participant.getTrash().getMessage(messageId);
			data.put("incoming", Boolean.TRUE);
		} else if (box.equals("outtrash")) {
			message = participant.getOuttrash().getMessage(messageId);
			data.put("incoming", Boolean.FALSE);
		}

		data.put("participant", participant);
		data.put("statusText", SessionTools.getStatusText(participant, gameManager));

		if (message != null) {
			data.put("message", message);
		}
		return new ModelAndView("message", data);
	}

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
}
