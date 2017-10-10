package nl.cyberdam.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.UserStatus;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class DeleteUserController extends AbstractGameController {

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"id"));

		User user = getUserManager().loadUser(id);

		// on POST
		if (METHOD_POST.equals(request.getMethod())) {
			
			if(WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
				String userName = user.getUsername();
				// getUserManager().delete(user);
				user.setStatus(UserStatus.DISABLED);
				getGameManager().getGameLogService().addLog(LogEntry.Module.LMS, "delete User", userName);
			}

			// redirect to user administration page
			return new ModelAndView(new RedirectView("useradministration.htm?forceRefresh=true", true));
		}
		
		Map<String, Object> m = new HashMap<String, Object>();
		m.put("user", user);
		
		return new ModelAndView("deleteuser", m);
	}

}
