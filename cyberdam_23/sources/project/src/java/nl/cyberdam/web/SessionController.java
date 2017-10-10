package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.web.util.ActivityHelper;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * provides a participant object
 *
 */
public class SessionController extends AbstractGameController {

    private ScriptManager scriptManager;

    protected String viewName = "session";
	private MenuUtil menuUtil;
	
	/**
	 * get id from the url and provide a participant object, this method calls additionalModelData()
	 * to get extra data for the model.
	 */
	@Override
	final protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long participantId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "participantId"));
		Boolean activityDisabled = ServletRequestUtils.getBooleanParameter (request, "activityDisabled");
		Participant participant = getGameManager().loadParticipant(participantId);
		menuUtil.generateMenu(request, MenuUtil.GAME_MENU,participant.getGameSession().getManifest());
		// Security check, only access for 
		// - users playing this participant
		// - Users that are the owner of the GameSession
		// - the SystemAdministrator
		// - VLE Administrator (added 2008/06/27, bug: 0003020)
		// - LMS Administrator (added 2008/06/27, bug: 0003020)
		User currentUser = GameUtil.getCurrentUser();
		if(! (participant.getUsers().contains(currentUser)
		   || participant.getGameSession().getOwner().equals(currentUser)
		   || currentUser.getGameAuthorities().isSystemAdministrator()
		   || currentUser.getGameAuthorities().isVleAdministrator()
		   || currentUser.getGameAuthorities().isLmsAdministrator())) {
			
			response.sendError(HttpServletResponse.SC_FORBIDDEN, 
			"Geen Toegang"); 
			return null; 
		}
		
		ModelAndView mav = new ModelAndView(viewName);
		
		Map <?, ?> additionalData = additionalModelData(request, response, participant);
		mav.addObject("participant", participant);
		List<Activity> activities = participant.getActivitiesForCurrentStep();
		LinkedList<Activity> enabledActivities = new LinkedList<Activity>(); 
		for (Activity activity : activities)
		{
			ModelAndView redirect = ActivityHelper.RedirectDisabledActivity(participant, activity, "", scriptManager);
			if (redirect == null || (((Boolean) redirect.getModel().get("activityEnabled")).booleanValue () == true && ((Boolean) redirect.getModel().get("activityAvailable")).booleanValue () == true))
			{
				enabledActivities.add(activity);
			}
		}
		mav.addObject("enabledActivities", enabledActivities);
		mav.addObject("activityDisabled", activityDisabled);
		mav.addObject("statusText", SessionTools.getStatusText(participant, gameManager));

		if (additionalData != null) {
			mav.addAllObjects(additionalData);
		}
				
		return mav;
	}
    
	/**
	 * set systemMaxMail systemMaxActivities systemMaxFiles - override to provide additional data
	 */
	protected Map<?, ?> additionalModelData(HttpServletRequest request,
			HttpServletResponse arg1, Participant participant) throws Exception {
		SystemParameters params = gameManager.loadSystemParameters();
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("systemMaxMail", new Integer(params.getVleMail() - 1));
		data.put("systemMaxActivities", new Integer(params.getVleActivities() - 1));
		data.put("systemMaxFiles", new Integer(params.getVleFiles() - 1));

		String headerTemplate = participant.getGameSession().getManifest().getGameModel().getHeaderTemplate();
		if (headerTemplate != null)
		{
		    data.put("headerText", gameManager.substituteVariablesInText (participant, headerTemplate));
		}

		// PageHolder for sorting by sentDate
		PagedListHolder inboxmessagesListHolder = new PagedListHolder();
    	inboxmessagesListHolder.setSource(new ArrayList<Message>(participant.getInbox().getMessages()));
    	ServletRequestDataBinder inbinder = new ServletRequestDataBinder(inboxmessagesListHolder);
        inbinder.bind(request);
        inboxmessagesListHolder.setSort(new MutableSortDefinition("sentDate", true, false));
        inboxmessagesListHolder.resort();        
        data.put("inboxmessagesListHolder", inboxmessagesListHolder);
		data.put("isClassicLayout", participant.getGameSession().getManifest().getGameModel().isSessionClassicLayout());
		return data;
	}

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}

	public void setScriptManager(ScriptManager scriptManager) {
		this.scriptManager = scriptManager;
	}

	public ScriptManager getScriptManager() {
		return scriptManager;
	}
}