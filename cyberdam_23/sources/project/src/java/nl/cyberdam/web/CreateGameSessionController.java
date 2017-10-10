package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.web.customeditors.GameManifestEditor;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Create a new game session from a game manifest and a list of users
 */
public class CreateGameSessionController extends CancellableFormController {

    private GameManager gameManager;

    /** Creates a new instance of EditGameModelController */
    public CreateGameSessionController() {
        setCommandClass(GameSession.class);
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        // gets gameModels by id
        binder.registerCustomEditor(GameManifest.class, "manifest", new GameManifestEditor(gameManager));
    }

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors) throws Exception {
		String action = ServletRequestUtils.getStringParameter(request, "action");
		GameSession gameSession = (GameSession) command;
		if ("".equals(action)) {
			if (gameSession.getManifest() == null) {
				errors.reject("manifestnotselected");
			}
		}
		if (gameSession.getName() == null || gameSession.getName().length() == 0) {
			errors.reject("empty.gamesession");
		}
	}

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, org.springframework.validation.BindException errors) throws Exception {
		String action = ServletRequestUtils.getStringParameter(request, "action");
		GameSession gameSession = (GameSession) command;
		if (!"".equals(action)) {
			if (action.equals("changeGameManifest")) {
			}
			return showForm(request, response, errors);
		}

        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // set owner and status
        gameSession.setOwner(currentUser);
        gameSession.setCreator(currentUser);
        gameSession.setLastModifier(currentUser);
        // gameSession.setStatus(GameSession.Status.IN_PREPARATION);
        // gameSession.setCurrentStatusStarted(new Date());
        // gameSession.setCurrentStepOfPlay(gameSession.getManifest().getGameModel().getInitialStepOfPlay());
        // initialize mapping
        // gameSession.initializeParticipantMapping();
        gameSession.initialize();

        gameManager.save(gameSession);
        return new ModelAndView(new RedirectView("gamemaster.htm?forceRefresh=true", true));
    }
}