package nl.cyberdam.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

/**
 * Show confirmation page and delete game model if 'ok' is pressed.
 */
public class DeleteGameModelController extends AbstractGameController {
	
	// used for getting multilanguage strings
	private MessageSource messageSource;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		GameModel gm = getGameManager().load(id);
		Map<String, Object> m = new HashMap<String, Object>();
		
		// check if the model is used by a manifest - if that is the case this model cannot be deleted.
		List<GameManifest> manifests = getGameManager().findManifestsForGameModel(gm);
		if (manifests.size() > 0) {
			Locale userLocale = GameUtil.getCurrentUser().getLocale();
			m.put("error", messageSource.getMessage("model.inuse", null, userLocale));
		}

		// on POST
		if (METHOD_POST.equals(request.getMethod())) {
			
			if(WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
				String gameName = gm.getName();
				getGameManager().delete(gm);
				getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS, "delete GameModel", gameName);
			}

			// redirect to gameauthor page
			return new ModelAndView(new RedirectView("gameauthor.htm?forceRefresh=true", true));
		}
		
		
		m.put("gamemodel", gm);
		
		return new ModelAndView("deletegamemodel", m);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
