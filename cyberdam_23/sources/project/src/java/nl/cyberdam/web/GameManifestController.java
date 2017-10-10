package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.RoleToPlaygroundMapping;
import nl.cyberdam.domain.Status;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.web.customeditors.GameModelEditor;
import nl.cyberdam.web.customeditors.PlaygroundEditor;
import nl.cyberdam.web.customeditors.PlaygroundObjectEditor;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

public class GameManifestController extends CancellableFormController {

	public static final String GAMEMANIFEST_ATTR = "gameManifest";
	private GameManager gameManager;

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
		throws Exception {
		// gets gameModels by id
		binder.registerCustomEditor(GameModel.class, "gameModel", new GameModelEditor(gameManager));
		binder.registerCustomEditor(PlaygroundObject.class, "rolesToPlaygroundObjects.playgroundObject",
				new PlaygroundObjectEditor(gameManager));
		binder.registerCustomEditor(Playground.class, "rolesToPlaygroundObjects.playground", 
				new PlaygroundEditor(gameManager));
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
		GameManifest gm = (GameManifest)request.getSession(true).getAttribute(GAMEMANIFEST_ATTR);
		if (gm == null) {
			Long gameManifestId = ServletRequestUtils.getLongParameter(request, "id");
			if (gameManifestId != null) {
				gm = gameManager.loadGameManifest(gameManifestId);
				gm.initialize();
			} else {
				gm = new GameManifest();
			}
			request.getSession(true).setAttribute(GAMEMANIFEST_ATTR, gm);
		}
		return gm;
	}

	@Override
	@SuppressWarnings(value = "unchecked")
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map referenceData = new HashMap();
		Status[] status = Status.values();
		Status[] statusWithoutPublic = new Status[status.length-1];
		for (int i=0,j=0; i<status.length; i++) {
			if (status[i] != Status.PUBLIC) {
				statusWithoutPublic[j++] = status[i];
			}
		}
		List<Playground> availablePlaygrounds = gameManager.findAllPublicPlaygrounds();
		referenceData.put("playgroundOptions", availablePlaygrounds);

		GameManifest gameManifest = (GameManifest) getCommand(request);
		if (gameManifest.getGameModel() != null && gameManifest.getGameModel().getStatus() != Status.PUBLIC) {
			referenceData.put("statusOptions", statusWithoutPublic);
		} else {
			referenceData.put("statusOptions", status);
		}

		List<List<PlaygroundObject>> availablePlaygroundObjects = new ArrayList<List<PlaygroundObject>>();
		for (RoleToPlaygroundMapping list : gameManifest.getRolesToPlaygroundObjects()) {
			List<PlaygroundObject> lpo = new ArrayList<PlaygroundObject>();
			if (list.getPlayground() != null) {
				lpo = gameManager.findAllPlaygroundObjectsForUser(GameUtil.getCurrentUser(), 
						list.getPlayground().getId(), null);
			}
			availablePlaygroundObjects.add(lpo);
		}
		referenceData.put("playgroundObjectOptions", availablePlaygroundObjects);
		referenceData.put("manifestInUse", !gameManifest.getGameSessions().isEmpty());

		return referenceData;
	}

	protected void onBind(HttpServletRequest request, Object command) throws Exception {
		GameManifest gameManifest = (GameManifest) command;
		for (RoleToPlaygroundMapping mapping : gameManifest.getRolesToPlaygroundObjects()) {
			if (mapping.getPlayground() == null) {
				mapping.setPlaygroundObject(null);
			}
		}
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
		throws Exception {
		String action = ServletRequestUtils.getStringParameter(request, "action");
		GameManifest manifest = (GameManifest) command;
		if ("".equals(action)) {
			Pattern p = Pattern.compile("\\s*");
			Matcher m = p.matcher(manifest.getName());
			if (m.matches()) {
				errors.reject("empty.manifest");
			}
			if (manifest != null && manifest.getRolesToPlaygroundObjects() != null) {
				for (RoleToPlaygroundMapping rtpm : manifest.getRolesToPlaygroundObjects()) {
					if (rtpm.getPlaygroundObject() == null && rtpm.getPlayground() != null && !rtpm.getPlayground().isHttpLink()) {
						errors.reject("playgroundlink.not.http");
					}
				}
			}
			if (manifest.getGameModel() == null) {
				errors.reject("empty.gamemodel");
			}
		} else if ("changeGameModel".equals(action)) {
			if (!manifest.getGameSessions().isEmpty()) {
				errors.reject("gamemanifest.inuse.by.gamesession");
				manifest.setGameModel(null);
			}
		}
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
		throws Exception {
		String action = ServletRequestUtils.getRequiredStringParameter(request, "action");
		GameManifest gm = (GameManifest) command;
		if (!"".equals(action)) {
			if (action.equals("update")) {
			} else if (action.equals("changeGameModel")) {
				gm.getRolesToPlaygroundObjects().clear();
				for( Role r: gm.getGameModel().getRoles()) {
					gm.getRolesToPlaygroundObjects().add(new RoleToPlaygroundMapping(gm, r));
				}
			}
			gameManager.save(gm);
			return showForm(request, response, errors);
		}
		if (gm.getOwner() == null) {
			User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			gm.setOwner(currentUser);
			gm.setCreator(currentUser);
		}
		try {
			gameManager.save(gm); 
		}
		catch (DataIntegrityViolationException dive) {
			return showForm(request, response, errors);
		}
		return new ModelAndView(getSuccessView());
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
