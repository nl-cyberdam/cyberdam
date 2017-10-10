package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.SessionStatus;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

/**
 * does not actually delete the game session - merely aborts it.
 */
public class DeleteGameSessionController extends AbstractGameController {

	private MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		GameSession gameSession = getGameManager().loadGameSession(id);
		// on POST
		if (METHOD_POST.equals(request.getMethod())) {
			
			if(WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
				String gameSessionName = gameSession.getName();
				
				if (gameSession.getStatus().equals(SessionStatus.IN_PREPARATION) ||
						gameSession.getStatus().equals(SessionStatus.READY_TO_START)) {
					gameSession.cancel();
					getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "cancel gameSession", gameSessionName);
				} else if (gameSession.getStatus().equals(SessionStatus.RUNNING)) {
					gameSession.abort();
					getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "abort gameSession", gameSessionName);
				} else if (gameSession.getStatus().equals(SessionStatus.ABORTED)) {
				    gameSession.delete();
				    getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "delete gameSession", gameSessionName);
				} else {
					getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "failed statechange gameSession", gameSessionName);
				}
				
				getGameManager().delete(gameSession);
			}

			// redirect to gameauthor page
			return new ModelAndView(new RedirectView("gamemaster.htm?forceRefresh=true", true));
		}
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("gamesession", gameSession);
		
		return new ModelAndView("deletegamesession", m);
	}

}
