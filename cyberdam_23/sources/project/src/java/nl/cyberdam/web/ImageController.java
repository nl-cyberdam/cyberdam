package nl.cyberdam.web;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Resource;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.service.GameManager;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class ImageController extends AbstractController {

    private GameManager gameManager;

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        Long resourceId = ServletRequestUtils.getLongParameter(request, "resourceId");
        Long sessionResourceId = ServletRequestUtils.getLongParameter(request, "sessionResourceId");
        String contentType;
        int fileSize;
        String fileName;
        byte content[];
        
        if (resourceId != null) {
        	Resource r = gameManager.loadResource(resourceId);
        	contentType = r.getContentType();
        	fileSize = r.getFileSize();
        	fileName = r.getFileName();
        	content = r.getContent();
        } else if (sessionResourceId != null) {
        	SessionResource r = gameManager.loadSessionResource(sessionResourceId);
        	contentType = r.getContentType();
        	fileSize = r.getFileSize();
        	fileName = r.getName();
        	content = r.getContent();
        } else {
        	response.sendError(HttpServletResponse.SC_BAD_REQUEST, "expecting resourceId or sessionResourceId parameter");
        	return null;
        }
    	response.setContentType(contentType);
    	response.setContentLength(fileSize);
    	//      To open up in the browser: "inline; filename=myFile.pdf"
    	//      To download: "attachment; filename=myFile.pdf"
    	response.setHeader("Content-disposition", "inline; filename=\"" + fileName + "\"");
        
    	// XXX this is probably not the fastest way, later research possible improvements
    	OutputStream out = response.getOutputStream();
    	out.write(content);
    	out.flush();
    	
        return null;
    }
}
