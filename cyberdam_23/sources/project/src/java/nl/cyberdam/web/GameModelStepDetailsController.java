package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.service.GameManager;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

public class GameModelStepDetailsController extends CancellableFormController {

	private GameManager gameManager;
	private GameModel gameModel;
	public static final String GAMEMODEL_ATTR = GameModelDetailController.GAMEMODEL_ATTR;
	public static final String STEPOFPLAY_ATTR = GameModelDetailController.STEPOFPLAY_ATTR;

	@Override
	protected Object formBackingObject(HttpServletRequest request)
	{
		gameModel = (GameModel) request.getSession(true).getAttribute(GAMEMODEL_ATTR);
		return request.getSession(true).getAttribute(STEPOFPLAY_ATTR);
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	{
		getGameManager().save(gameModel);
		return new ModelAndView(getSuccessView(), "id", gameModel.getId());
	}

	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		return new ModelAndView(getCancelView(), "id", gameModel.getId());
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public GameManager getGameManager() {
		return gameManager;
	}
}


