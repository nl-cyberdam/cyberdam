package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.Resource;
import nl.cyberdam.service.GameManager;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 * 
 */
public class GameModelResourceEditController extends CancellableFormController {

	private GameManager gameManager;
	public static final String GAMEMODEL_ATTR = GameModelDetailController.GAMEMODEL_ATTR;

	@Override
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		Long resourceId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
		return gameManager.loadResource(resourceId);
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		GameModel gameModel = (GameModel) request.getSession(true).getAttribute(GAMEMODEL_ATTR);
		Resource r = (Resource) command;
		try {
			gameManager.save(r);
		} catch (DataIntegrityViolationException dive) {
			errors.reject(dive.getMessage());
			return showForm(request, response, errors);
		}

		return new ModelAndView(getSuccessView(), "id", gameModel.getId());
	}

	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		GameModel gameModel = (GameModel) request.getSession(true).getAttribute(GAMEMODEL_ATTR);
		return new ModelAndView(getCancelView(), "id", gameModel.getId());
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
