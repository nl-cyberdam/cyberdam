package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.SessionReportLogService;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;

public class SessionReportController extends AbstractController {
    
    private static String             LIST_ATTR   = "sessionreportlist";
    private static String             LISTID_ATTR = "sessionId";
    private static String             LISTNAME_ATTR = "sessionName";
    protected GameManager             gameManager;
    protected SessionReportLogService sessionReportLogService;
    
    public void setSessionReportLogService(
            SessionReportLogService sessionReportLogService) {
        this.sessionReportLogService = sessionReportLogService;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        Long sessionId = new Long(ServletRequestUtils.getRequiredLongParameter(
                request, "id"));
        GameSession gameSession = gameManager.loadGameSession(sessionId);
        RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request
                .getSession(true).getAttribute(LIST_ATTR);
        Long s = (Long) request.getSession(true).getAttribute(LISTID_ATTR);
        
        if (listHolder == null || !s.equals(sessionId)) {
            logger.debug("creating new listHolder object: " + sessionId);
            listHolder = new RefreshablePagedListHolder();
            listHolder.setPageSize(gameManager.loadSystemParameters()
                    .getDefaultRows());
            listHolder.setSourceProvider(new SessionReportListProvider(
                    sessionId));
            request.getSession(true).setAttribute(LIST_ATTR, listHolder);
            request.getSession(true).setAttribute(LISTID_ATTR, sessionId);
            request.getSession(true).setAttribute(LISTNAME_ATTR, gameSession.getName());
        }
        
        ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder);
        binder.bind(request);
        
        listHolder.setLocale(RequestContextUtils.getLocale(request));
        listHolder.refresh(true);
        
        return new ModelAndView("sessionreport", binder.getBindingResult()
                .getModel());
    }
    
    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    
    private class SessionReportListProvider implements PagedListSourceProvider,
            Serializable {
        
        Long sessionId;
        
        public SessionReportListProvider(Long sessionId) {
            super();
            this.sessionId = sessionId;
        }
        
        @SuppressWarnings("unchecked")
        public List loadList(Locale arg0, Object arg1) {
            return sessionReportLogService.findAllForSession(sessionId);
        }
    }
}