package nl.cyberdam.web;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class DeleteGameManifestController extends AbstractGameController {
	
	private MessageSource messageSource;

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		GameManifest gm = getGameManager().loadGameManifest(id);
		Map<String, Object> m = new HashMap<String, Object>();
		
		// check if the model is used by a manifest - if that is the case this model cannot be deleted.
		List<GameSession> sessions = getGameManager().findGameSessionsForManifest(gm);
		if (sessions.size() > 0) {
			Locale userLocale = GameUtil.getCurrentUser().getLocale();
			m.put("error", messageSource.getMessage("manifest.inuse", null, userLocale));
		}
		
		// on POST
		if (METHOD_POST.equals(request.getMethod())) {
			
			if(WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
				String manifestName = gm.getName();
				getGameManager().delete(gm);
				getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS, "delete GameManifest", manifestName);
			}

			// redirect to gameauthor page
			return new ModelAndView(new RedirectView("gamemanifestcomposer.htm?forceRefresh=true", true));
		}
		
		m.put("gamemanifest", gm);
		
		return new ModelAndView("deletegamemanifest", m);
	}

}
