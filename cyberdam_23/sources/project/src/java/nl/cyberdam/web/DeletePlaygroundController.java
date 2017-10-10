package nl.cyberdam.web;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class DeletePlaygroundController extends AbstractGameController {
	
	private MessageSource messageSource;
	
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		Playground playground = getGameManager().loadPlayground(id);
		Map<String, Object> m = new HashMap<String, Object>();
		
		// on POST
		if (METHOD_POST.equals(request.getMethod())) {
			
			if(WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
				String name = playground.getName();
				try {
				    getGameManager().delete(playground);
				} catch(Exception e) {
				    m.put("error", "roletoplaygroundmapping.inuse");
				    return new ModelAndView("deleteplayground", m);
				}
				getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS, "delete Playground", name);
			}

			// redirect to playgroundlistpage page
			return new ModelAndView(new RedirectView("playgroundlistpage.htm?forceRefresh=true", true));
		}
		
		m.put("playground", playground);
		m.put("gameManifests", getGameManager().findAllManifests(playground));
		
		List<String> list = new ArrayList<String>();
		if (playground != null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, GameUtil.getCurrentUser().getLocale());
			list.add(messageSource.getMessage("playground.name", null, GameUtil.getCurrentUser().getLocale())+ " : " + playground.getName());
			list.add(messageSource.getMessage("Uri", null, GameUtil.getCurrentUser().getLocale())+ " : " + playground.getUriId());
			list.add(messageSource.getMessage("caption", null, GameUtil.getCurrentUser().getLocale())+ " : " + playground.getCaption());
			list.add(messageSource.getMessage("version", null, GameUtil.getCurrentUser().getLocale())+ " : " + playground.getVersion());
			list.add(messageSource.getMessage("lastmodifiedby", null, GameUtil.getCurrentUser().getLocale())+ " : " + playground.getLastModifier().getUsername());
			list.add(messageSource.getMessage("lastmodified", null, GameUtil.getCurrentUser().getLocale())+ " : " + df.format(playground.getLastModified()));
		}
		m.put("resultStrings", list);
		
		return new ModelAndView("deleteplayground", m);
	}
}
