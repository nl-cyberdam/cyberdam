package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.util.HomePage;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class PrintableSessionReportController extends AbstractController {
    private GameManager             gameManager;
    private SessionReportLogService sessionReportLogService;
    
    public void setSessionReportLogService(
            SessionReportLogService sessionReportLogService) {
        this.sessionReportLogService = sessionReportLogService;
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long gameSessionId = new Long(ServletRequestUtils
                .getRequiredLongParameter(request, "id"));
        GameSession gameSession = gameManager.loadGameSession(gameSessionId);
        List<Playground> playgrounds = new ArrayList<Playground>(gameManager
                .findAllPlaygroundsFormanifest(gameSession.getManifest()));
        Map<String, Object> referenceData = new HashMap<String, Object>();
        referenceData.put("user", GameUtil.getCurrentUser());
        Date date = new Date(System.currentTimeMillis());
        referenceData.put("date", date.toString());
        referenceData.put("home", HomePage.getURL(request));
        referenceData.put("version", SystemParameters.VERSION);
        referenceData.put("playgrounds", playgrounds);
        referenceData.put("gameSession", gameSession);
        referenceData.put("participants", gameSession.getParticipants());
        referenceData.put("sessionReportList", sessionReportLogService
                .findAllForSession(gameSessionId));
        return new ModelAndView("printablesessionreport", referenceData);
    }
    
}
