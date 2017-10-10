package nl.cyberdam.web;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Status;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.web.customeditors.PlaygroundEditor;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 * 
 */
public class PlaygroundObjectEditorController extends CancellableFormController {

	private GameManager gameManager;

	public PlaygroundObjectEditorController() {
		setCommandClass(PlaygroundObject.class);
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	protected void initBinder(HttpServletRequest request,
			ServletRequestDataBinder binder) throws Exception {

		binder.registerCustomEditor(Playground.class, "playground",
				new PlaygroundEditor(gameManager));
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		Long playgroundObjectId = ServletRequestUtils.getLongParameter(request,
				"id");
		PlaygroundObject p;

		if (playgroundObjectId != null) {
			p = gameManager.loadPlaygroundObject(playgroundObjectId);
		} else {
			p = new PlaygroundObject();
			Long playgroundId = new Long(ServletRequestUtils.getRequiredLongParameter(request,
					"playgroundId"));
			Playground playground = gameManager.loadPlayground(playgroundId);

			User currentUser = (User) SecurityContextHolder.getContext()
					.getAuthentication().getPrincipal();
			p.setCreator(currentUser);
			p.setCreated(new Date());
			p.setPlayground(playground);

		}
		return p;
	}

	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request)
			throws Exception {
		Map<String, Object> referenceData = new HashMap<String, Object>();
		referenceData.put("statusOptions", Arrays.asList(
				Status.UNDER_CONSTRUCTION,
				Status.PUBLIC,
				Status.OBSOLETE));
		referenceData.put("categoryOptions", PlaygroundObject.Category.values());
		return referenceData;
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		PlaygroundObject po = (PlaygroundObject) command;
		// set last modifier and last modified
		po.setLastModified(new Date());
		User currentUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		po.setLastModifier(currentUser);
		try {
			gameManager.save(po);
		} catch (DataIntegrityViolationException dive) {
			getValidator().validate(po, errors);
			if (errors.getErrorCount() == 0) {
				errors.rejectValue("uri", "alreadytaken");
			}
			return showForm(request, response, errors);
		}
		return new ModelAndView(getSuccessView(), "id", po
				.getPlayground().getId());
	}

	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		PlaygroundObject po = (PlaygroundObject) command;
		return new ModelAndView(getCancelView(), "id", po
				.getPlayground().getId());
	}
}
