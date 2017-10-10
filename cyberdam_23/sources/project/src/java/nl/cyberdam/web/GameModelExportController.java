package nl.cyberdam.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Status;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.ExchangeManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

public class GameModelExportController extends AbstractGameController {

	// used for getting 'copy of' in multilanguage;
	private ExchangeManager<GameModel> exchangeManager;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(
				request, "id"));
		final GameModel gameModel = getGameManager().load(id);
		
		// check if gamemodel is allowed to be exported
		User currentUser = GameUtil.getCurrentUser();
		if (currentUser.getGameAuthorities().isSystemAdministrator() || 
				currentUser.getGameAuthorities().isLcmsAdministrator()) {
			// all downloads are ok
		} else if (currentUser.getGameAuthorities().isGameAuthor()) {
			if (! (gameModel.getStatus().equals(Status.PUBLIC) 
				   || gameModel.getOwner().equals(currentUser))) {
				// YOU ARE NOT ALLOWED TO EXPORT THIS GAME!
				response.sendError(HttpServletResponse.SC_FORBIDDEN, 
							"Het is niet toegestaan dit gamemodel te exporteren");
				return null;
			}
		} else {
			// YOU ARE NOT ALLOWED TO EXPORT THIS GAME!
			response.sendError(HttpServletResponse.SC_FORBIDDEN, 
					"Het is niet toegestaan dit gamemodel te exporteren");
			return null;
		}
		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(now);

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ gameModel.getName() + "_" + s + ".zip\"");
		//exchangeManager.setServletContext(getServletContext());
		exchangeManager.export(gameModel, response.getOutputStream(), null);

		getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS,
				"export gameModel", gameModel.getName());
		response.getOutputStream().flush();
		logger.debug("gamemodel export called");
		return null;
	}

	/**
	 * should be injected
	 */
	public void setExchangeManager(ExchangeManager<GameModel> exchangeManager) {
		this.exchangeManager = exchangeManager;
	}
}
