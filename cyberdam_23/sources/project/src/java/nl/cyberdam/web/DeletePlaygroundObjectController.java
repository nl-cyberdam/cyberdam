package nl.cyberdam.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.PlaygroundObject;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class DeletePlaygroundObjectController extends AbstractGameController {
	
	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		PlaygroundObject playgroundObject = getGameManager().loadPlaygroundObject(id);
		Map<String, Object> m = new HashMap<String, Object>();
		Long playgroundId = playgroundObject.getPlayground().getId();
		// check if the object is in use by a GameManifest
		List<GameManifest> gameManifests = getGameManager().findAllManifests(playgroundObject);
//		if (gameManifests.size() > 0) {
//			Locale userLocale = GameUtil.getCurrentUser().getLocale();
//			m.put("error", messageSource.getMessage("playgroundobject.inuse", null, userLocale));
//		}
		
		// on POST
		if (METHOD_POST.equals(request.getMethod())) {
			
			if(WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
				String name = playgroundObject.getName();
				playgroundObject.getPlayground().removePlaygroundObject(playgroundObject);
				getGameManager().delete(playgroundObject);
				getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS, "delete PlaygroundObject", name);
			}

			// redirect to playground author page
			return new ModelAndView(new RedirectView("playgrounddetail.htm?forceRefresh=true#playgroundobjects_section", true),"id",playgroundId);
		}
		
		m.put("playgroundobject", playgroundObject);
		m.put("gameManifests", gameManifests);
		
		return new ModelAndView("deleteplaygroundobject", m);
	}
}
