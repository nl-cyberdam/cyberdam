package nl.cyberdam.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.SessionStatus;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.service.UserManager;
import nl.cyberdam.web.customeditors.UserEditor;

import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.mvc.CancellableFormController;

public class GameSessionControlController extends CancellableFormController {

    private GameManager gameManager;
    private UserManager userManager;
	private ScriptManager scriptManager;
    
    public GameManager getGameManager() {
        return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    @Override
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws Exception {
        // gets gameModels by id
        binder.registerCustomEditor(User.class, "participants.users", new UserEditor(userManager));
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> referenceData = new HashMap<String, Object>();
        // get all users
        Collection<User> users = getUserManager().findAll();
        referenceData.put("users", users);
        
        return referenceData;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {

        Long gameSessionId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
        return getGameManager().loadGameSession(gameSessionId);
    }
    
    @Override
    protected ModelAndView onCancel(HttpServletRequest request,
    		HttpServletResponse response, Object command) throws Exception {
        return new ModelAndView(new RedirectView("gamemaster.htm?forceRefresh=true", true));
    }
    /**
     * check for state change requests
     */
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
    		HttpServletResponse response, Object command, BindException errors)
    		throws Exception {

    	boolean reload = false;
    	ModelAndView returnValue;
    	Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
    	String state = ServletRequestUtils.getStringParameter(request, "state");
    	GameSession ses = (GameSession) command;

    	if (state != null && state.length() > 0) {
    		reload = true;
    		if (state.equals("preparing") && ses.getStatus() != SessionStatus.IN_PREPARATION) {
    			ses.preparation();
    			getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "status to preparation", ses.getName());
    		} else if (state.equals("makeready") && ses.getStatus() != SessionStatus.READY_TO_START) {
    			ses.markready();
    			getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "status to ready", ses.getName());
    		} else if (state.equals("canceling") && ses.getStatus() != SessionStatus.CANCELLED) {
    			ses.cancel();
    			getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "status to cancel", ses.getName());
    		} else if (state.equals("starting") && ses.getStatus() != SessionStatus.RUNNING) {
    			ses.start(scriptManager);
    			getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "status to running", ses.getName());
    		} else if (state.equals("aborting") && ses.getStatus() != SessionStatus.ABORTED) {
    			ses.abort();
    			getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "status to abort", ses.getName());
    		}
    	}

    	getGameManager().save(ses);
    	if (reload) {
    		Map<String, Object> m = new HashMap<String, Object>();
    		m.put("command", command);
    		m.put("id", id);
    		returnValue = new ModelAndView((String)null, m);
    	} else {
    		returnValue = super.onSubmit(request, response, command, errors);
    	}
    	return returnValue;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

	public void setScriptManager(ScriptManager scriptManager) {
		this.scriptManager = scriptManager;
	}

	public ScriptManager getScriptManager() {
		return scriptManager;
	}
}
