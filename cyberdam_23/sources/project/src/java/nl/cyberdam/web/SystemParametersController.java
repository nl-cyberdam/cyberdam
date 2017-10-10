package nl.cyberdam.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.service.GameManager;

import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 * Loads and saves the default systemparameters object. 
 */
public class SystemParametersController extends CancellableFormController {

	private GameManager gameManager;
	
	public SystemParametersController() {
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		return getGameManager().loadSystemParameters();
	}
	
	@SuppressWarnings({ "unchecked", "boxing" })
	@Override
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map m = new HashMap();
		// 5,10,15,20,30,40,80
		List options = Arrays.asList(5,10,15,20,30,40,80);
		m.put("defaultRowOptions", options);
		return m;
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

//		return new ModelAndView("systemparameters", "systemparameters", getGameManager().loadSystemParameters());


}
