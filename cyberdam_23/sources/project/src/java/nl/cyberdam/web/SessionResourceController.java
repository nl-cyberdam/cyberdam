package nl.cyberdam.web;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.service.GameManager;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class SessionResourceController extends AbstractController {

    private GameManager gameManager;

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Long resourceId = ServletRequestUtils.getLongParameter(request, "id");
        
        SessionResource r = gameManager.loadSessionResource(resourceId);
        
        response.setContentType(r.getContentType());
        response.setContentLength(r.getFileSize());
//      To open up in the browser: "inline; filename=myFile.pdf"
//      To download: "attachment; filename=myFile.pdf"
        response.setHeader("Content-disposition", "attachment; filename=\"" + r.getName() + "\"");

        // XXX dit is waarschijnlijk niet de snelste manier
        OutputStream out = response.getOutputStream();
        out.write(r.getContent());
        out.flush();
        
        return null;
    }

}
