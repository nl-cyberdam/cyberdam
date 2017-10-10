package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.User;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class CopyGameManifestController extends AbstractGameController {
	
	// used for getting 'copy of' in multilanguage;
	private MessageSource messageSource;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));
		// make a copy of the game manifest
		GameManifest original = getGameManager().loadGameManifest(id);
		User currentUser = GameUtil.getCurrentUser();
		GameManifest copy = original.copy(currentUser);
		
		// default copy behavior is to have the same name, so replace it with
		// a name with 'copy of:' prepended.
    	String copyOf = messageSource.getMessage("copy.of", null, currentUser.getLocale());
		copy.setName(copyOf + ": " + original.getName());
		
		getGameManager().save(copy);
		getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS, "copy gamemanifest", original.getName());
		
		// redirect to gameauthor page
		return new ModelAndView(new RedirectView("gamemanifestcomposer.htm?forceRefresh=true", true));
	}

	/**
	 * should be injected 
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
