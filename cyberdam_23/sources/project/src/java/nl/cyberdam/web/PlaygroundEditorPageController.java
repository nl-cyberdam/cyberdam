package nl.cyberdam.web;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Playground;
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
public class PlaygroundEditorPageController extends CancellableFormController {

    private GameManager gameManager;
    private String swfPath;

    public PlaygroundEditorPageController() {
        setCommandClass(Playground.class);
    }
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        Long playgroundId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
        Playground playground = gameManager.loadPlayground(playgroundId);
        return playground;
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> referenceData = new HashMap<String, Object>();
        referenceData.put("statusOptions", Arrays.asList(Status.UNDER_CONSTRUCTION, Status.PUBLIC, Status.OBSOLETE));
        return referenceData;
    }

    protected ModelAndView onSubmit(
			HttpServletRequest request,	HttpServletResponse response, Object command, BindException errors)
			throws Exception {
        Playground playground = (Playground) command;
        // set last modifier and last modified
        playground.setLastModified(new Date());
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        playground.setLastModifier(currentUser);
        try {
        	if (playground.getLink() != null && !playground.isHttpLink()) {
        		if (!(new File(getServletContext().getRealPath(swfPath), playground.getLink()).exists()) && !(new File(getServletContext().getRealPath("images"), playground.getLink()).exists())) {
        			errors.rejectValue("link", "no_swf_found");
        			return showForm(request, response, errors);
        		}
        	}
        	gameManager.save(playground);
        } catch (DataIntegrityViolationException dive) {
        	errors.rejectValue("uriId", "alreadytaken");
        	return showForm(request, response, errors);
        }
        return new ModelAndView(getSuccessView(), "id", playground.getId());
    }

    protected ModelAndView onCancel(Object command) throws Exception {
    	Playground playground = (Playground) command;
    	return new ModelAndView(getCancelView(), "id", playground.getId());
    }

    public void setSwfPath(String swfPath) {
		this.swfPath = swfPath;
	}

}
