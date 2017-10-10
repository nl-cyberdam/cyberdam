package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Group;
import nl.cyberdam.domain.LogEntry;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class DeleteGroupController extends AbstractGameController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		Group group = getUserManager().loadGroup(id);

		// on POST
		if (METHOD_POST.equals(request.getMethod())) {
			
			if(WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
				String groupName = group.getName();
				getUserManager().delete(group);
				getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "delete Group", groupName);
			}

			// redirect to groupadministration page
			return new ModelAndView(new RedirectView("groupadministration.htm?forceRefresh=true", true));
		}
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("group", group);
		
		return new ModelAndView("deletegroup", m);
	}

}
