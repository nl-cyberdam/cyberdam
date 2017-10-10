package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;

import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.service.GameManager;

import org.springframework.web.servlet.mvc.CancellableFormController;

public class ModelTemplateEditorController extends CancellableFormController {
	private GameManager gameManager;

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		return getGameManager().loadSystemParameters();
	}
	
	@Override
	protected void doSubmitAction(Object command) throws Exception {	
		SystemParameters parameters = (SystemParameters) command;
		getGameManager().save(parameters);
	}

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
