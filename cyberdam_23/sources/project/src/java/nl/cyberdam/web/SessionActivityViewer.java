package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Message;
import nl.cyberdam.service.GameManager;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class SessionActivityViewer extends AbstractController {

    private GameManager gameManager;

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Long sessionResourceId = ServletRequestUtils.getLongParameter(request, "sessionResourceId");
        Long messageId = ServletRequestUtils.getLongParameter(request, "messageId");
        Long sessionId = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
        GameSession gameSession = gameManager.loadGameSession(sessionId);
        Map<String, Object> data = new HashMap<String, Object>();

        if (sessionResourceId != null) {
            data.put("sessionResourceId", sessionResourceId);
        } else {
        	Message m = gameManager.loadMessage(messageId);
            data.put("message", m);
        }
        data.put("sessionName", gameSession.getName());
        return new ModelAndView("sessionactivityviewer", data);
    }
}
