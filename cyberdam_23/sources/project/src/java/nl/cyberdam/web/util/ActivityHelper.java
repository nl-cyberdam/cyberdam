package nl.cyberdam.web.util;

import java.util.HashMap;
import java.util.Map;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.service.ScriptManager;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class ActivityHelper {
	// checks if an activity is still available, returns a model-and-view that
	// will display the error if not, or null if not
	public static ModelAndView RedirectDisabledActivity(Participant participant, Activity activity,
			String redirectView, ScriptManager scriptManager) {
		ModelAndView mav = null;
		final GameSession gameSession = participant.getGameSession();
		boolean available = gameSession.getCurrentStepOfPlay().getActivitiesForRole(
				participant.getRoleAndPlayground().getRole()).contains(activity);
		boolean completed = participant.checkActivityCompleted (activity);
		boolean allowCompleted = participant.checkActivityEnabled(activity);
		boolean enabled = true;
		// don't run the disabled script if any boolean has the wrong value to continue
		if (available && (!completed || allowCompleted)) {
			Object scriptValue = scriptManager.RunScript(activity.getDisabledScript(), participant, participant.getGameSession());
			if (scriptValue != null && scriptValue instanceof Boolean) {
				// the disabledScript confusingly returns TRUE 
				enabled = enabled && ((Boolean) scriptValue).booleanValue();
			}
		}
		if (!enabled || !available || (completed && !allowCompleted)) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put("participantId", participant.getId());
			data.put("activityDisabled", "1");
			data.put("activityEnabled", new Boolean (enabled));
			data.put("activityAvailable", new Boolean (available));
			data.put("activityCompleted", new Boolean (completed));
			data.put("activityAllowCompleted", new Boolean (allowCompleted));
			mav = new ModelAndView(new RedirectView(redirectView, true), data);
		}
		return mav;
	}
}
