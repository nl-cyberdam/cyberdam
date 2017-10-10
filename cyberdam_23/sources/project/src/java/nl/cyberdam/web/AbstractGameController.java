package nl.cyberdam.web;

import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.UserManager;

import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Abstract class with reference to a GameManager and UserManager
 */
public abstract class AbstractGameController extends AbstractController {

	// just like in CancellableFormController
	public static String PARAM_CANCEL = "_cancel";
	public static String PARAM_DELETE = "_delete";
	
    GameManager gameManager;
    UserManager userManager;

    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

	public UserManager getUserManager() {
		return userManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
}
