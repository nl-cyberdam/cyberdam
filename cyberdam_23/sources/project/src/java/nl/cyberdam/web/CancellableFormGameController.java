package nl.cyberdam.web;

import nl.cyberdam.service.GameManager;

import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 * a cancellable form controller with references to the gameManager
 */
public class CancellableFormGameController extends CancellableFormController {
	
	private GameManager gameManager;

	public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

}
