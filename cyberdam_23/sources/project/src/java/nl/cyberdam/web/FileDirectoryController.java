package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.SessionResource;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;

/**
 *
 */
public class FileDirectoryController extends SessionController {
    
    /** Creates a new instance of FileDirectoryController */
    public FileDirectoryController() {
        viewName = "filedirectory";
    }

    @Override
    protected Map<String, Object> additionalModelData(HttpServletRequest request,
    		HttpServletResponse arg1, Participant participant) throws Exception {
    	Map<String, Object> modelData = new HashMap<String, Object>();
    	PagedListHolder filesListHolder = new PagedListHolder();
    	filesListHolder.setSource(new ArrayList<SessionResource>(participant.getSessionResourceBox().getSortedResources()));
    	ServletRequestDataBinder binder = new ServletRequestDataBinder(filesListHolder);
        binder.bind(request);
        filesListHolder.resort();
        modelData.put("fileslistholder", filesListHolder);
    	return modelData;
    }
    
    
}
