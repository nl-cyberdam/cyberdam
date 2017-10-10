package nl.cyberdam.web;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.ActivityVariable;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

public class VariableDeleteController extends AbstractGameController {

	private MessageSource messageSource;
	private String successRedirect;
	private String cancelRedirect;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
		Variable variable = getGameManager().loadVariable(id);
		ModelAndView returnValue = null;
		if (WebUtils.hasSubmitParameter(request, PARAM_DELETE)) {
			for (ActivityVariable activityVariable : variable.getActivityVariables()) {
				FormActivity formActivity = activityVariable.getActivity();
				Locale userLocale = GameUtil.getCurrentUser().getLocale();
				String error = messageSource.getMessage("variable.in.use.by", null, userLocale );
				Map<String, Object> m = new HashMap<String, Object>();
				m.put("variable", variable);
				m.put("iiuerror", MessageFormat.format(error, formActivity.getName()));
				return new ModelAndView("variabledelete", m);
			}
			String variableName = variable.getName();
			getGameManager().delete(variable);
			getGameManager().getGameLogService().addLog(LogEntry.Module.LMS,
					"delete Variable", variableName);
			// Deleted
			returnValue = new ModelAndView(successRedirect);
		} else if (WebUtils.hasSubmitParameter(request, PARAM_CANCEL)) {
			// Canceled
			returnValue = new ModelAndView(cancelRedirect);
		} else {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("variable", variable);
			returnValue = new ModelAndView("variabledelete", m);
		}

		return returnValue;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setSuccessRedirect(String successRedirect) {
		this.successRedirect = successRedirect;
	}

	public void setCancelRedirect(String cancelRedirect) {
		this.cancelRedirect = cancelRedirect;
	}
}