package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.Status;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 * 
 */
public class GameModelMetaDataController extends CancellableFormController {

	private GameManager gameManager;
	public static final String GAMEMODEL_ATTR = GameModelDetailController.GAMEMODEL_ATTR;

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		return request.getSession(true).getAttribute(GAMEMODEL_ATTR);
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request)
			throws Exception {
		Map<String, Object> referenceData = new HashMap<String, Object>();
		referenceData.put("statusOptions", Status.values());
		return referenceData;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
		GameModel gm = (GameModel) command;
		String toDefault = ServletRequestUtils.getStringParameter(request, "_setDefault");
		if (toDefault != null) {
			gm.setStatusTemplate(SessionTools.getDefaultStatusText());
			return showForm(request, response, errors);
		}
		// set last modifier
		User currentUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		gm.setLastModifier(currentUser);
		try {
			gameManager.save(gm);
		} catch (DataIntegrityViolationException dive) {
			errors.reject(dive.getMessage());
			return showForm(request, response, errors);
		}
		return new ModelAndView(getSuccessView(), "id", gm.getId());
	}

	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		GameModel gm = (GameModel) command;
		return new ModelAndView(getCancelView(), "id", gm.getId());
	}
}
