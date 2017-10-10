package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.Participant;

import org.springframework.beans.support.PagedListHolder;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.web.bind.ServletRequestDataBinder;
 

/**
 *
 */
public class MessagesController extends SessionController {
    
    /** Creates a new instance of MessagesController */
    public MessagesController() {
    	viewName = "messages";
    }
    
    // PageHolder for sorting by sentDate
    @Override
    protected Map<String, Object> additionalModelData(HttpServletRequest request,
    		HttpServletResponse arg1, Participant participant) throws Exception {
    	Map<String, Object> modelData = new HashMap<String, Object>();
    	
    	PagedListHolder inboxmessagesListHolder = new PagedListHolder();
    	inboxmessagesListHolder.setSource(new ArrayList<Message>(participant.getInbox().getMessages()));
    	ServletRequestDataBinder inbinder = new ServletRequestDataBinder(inboxmessagesListHolder);
        inbinder.bind(request);
        inboxmessagesListHolder.setSort(new MutableSortDefinition("sentDate", true, false));
        inboxmessagesListHolder.resort();   
        inboxmessagesListHolder.setPageSize(Integer.MAX_VALUE);
        modelData.put("inboxmessagesListHolder", inboxmessagesListHolder);
        
        PagedListHolder outboxmessagesListHolder = new PagedListHolder();
    	outboxmessagesListHolder.setSource(new ArrayList<Message>(participant.getOutbox().getMessages()));
    	ServletRequestDataBinder outbinder = new ServletRequestDataBinder(outboxmessagesListHolder);
        outbinder.bind(request);
        outboxmessagesListHolder.setSort(new MutableSortDefinition("sentDate", true, false));
        outboxmessagesListHolder.resort();  
        outboxmessagesListHolder.setPageSize(Integer.MAX_VALUE);
        modelData.put("outboxmessagesListHolder", outboxmessagesListHolder);
               
    	return modelData;
    }



}
