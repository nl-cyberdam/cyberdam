package nl.cyberdam.web;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Status;
import nl.cyberdam.domain.User;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.validation.AnnotationValidator;

import org.hibernate.validator.InvalidValue;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class CopyPlaygroundObjectController extends AbstractGameController {

	// used for getting 'copy of' in multilanguage;
	private MessageSource messageSource;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
			  
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		PlaygroundObject original = getGameManager().loadPlaygroundObject(id);
		PlaygroundObject copy = new PlaygroundObject();
		
		// make a copy of the playground object
		
		User currentUser = GameUtil.getCurrentUser();
    	String copyOf = messageSource.getMessage("copy.of", null, currentUser.getLocale());
		
		copy.setName(copyOf + " " + original.getName());
		copy.setCaption(original.getCaption());
		copy.setAddress(original.getAddress());
		copy.setCategory(original.getCategory());
		if (original.getThumbnail() != null) { 
			copy.setThumbnail(original.getThumbnail().copy(GameUtil.getCurrentUser()));
		}
		if (original.getPicture() != null) { 
			copy.setPicture(original.getPicture().copy(GameUtil.getCurrentUser()));
		}
		copy.setX(original.getX());
		copy.setY(original.getY());
		copy.setCreator(GameUtil.getCurrentUser());
		copy.setLastModifier(GameUtil.getCurrentUser());
		Date now = new Date();
		copy.setCreated(now);
		copy.setLastModified(now);
		copy.setOnMap(original.isOnMap());
		copy.setInDirectory(original.isInDirectory());
		copy.setStatus(Status.UNDER_CONSTRUCTION);
		copy.setUrl(original.getUrl());
		copy.setUri(original.getUri()+" "+copyOf+" "+now); 
		copy.setDescription(original.getDescription());
		copy.setPlayground(original.getPlayground());
		
	    List<String> err = new ArrayList<String>();
	    InvalidValue[] invalids = AnnotationValidator.getInvalidValues(original);
        for (InvalidValue invalidValue : invalids) {
            err.add(invalidValue.getMessage() + ".command." + invalidValue.getPropertyPath());
        }
        if (!err.isEmpty()) {
            request.getSession(true).setAttribute("err", err);
            request.getSession(true).setAttribute("errId", original.getId());
            return new ModelAndView(new RedirectView("playgrounddetail.htm?forceRefresh=true#playgroundobjects_section", true),"id",original.getPlayground().getId());
        }
		
		getGameManager().save(copy);
		getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS, "copy playgroundobject", original.getName());
		
		// redirect to gameauthor page
		return new ModelAndView(new RedirectView("playgrounddetail.htm?forceRefresh=true#playgroundobjects_section", true),"id",original.getPlayground().getId());
	}
	
	/**
	 * should be injected
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
