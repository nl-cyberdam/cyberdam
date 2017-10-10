package nl.cyberdam.web;

import nl.cyberdam.domain.Participant;
import nl.cyberdam.service.GameManager;

public class SessionTools
{
	public static String getStatusText (Participant participant, GameManager gameManager) {
		String statusTemplate = participant.getGameSession().getManifest().getGameModel().getStatusTemplate();

		if (statusTemplate == null || statusTemplate.length() == 0) {
			statusTemplate = getDefaultStatusText();
		}
		return gameManager.substituteVariablesInText (participant, statusTemplate);
	}
	public static String getDefaultStatusText () {
		String statusTemplate;
		statusTemplate = "<p>[%system.message.sessionname%]: [%system.session.name%]</p>";
		statusTemplate += "<p>[%system.message.sessionstatus%]: [%system.session.status%]</p>";
		statusTemplate += "<p>[%system.message.you.are%]: [%role.name%] ([%system.playgroundObject.name%][%system.message.separator.playground%][%system.playground.name%])</p>";
		statusTemplate += "<p>[%system.message.currentstepofplay%]: [%system.stepOfPlay.name%]</p>";
		return statusTemplate;
	}
}
