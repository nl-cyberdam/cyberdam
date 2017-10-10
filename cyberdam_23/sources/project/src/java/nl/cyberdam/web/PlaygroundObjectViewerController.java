package nl.cyberdam.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.web.util.KeyHolder;

import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class PlaygroundObjectViewerController extends PlaygroundObjectEditorController {
	
	/*
	 * Adding extra property for viewer mode (viewerMode = true).
	 */
	@Override
	protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> retV = super.referenceData(request);
		retV.put(KeyHolder.getViewerModeKey(), new Boolean(true));
		return retV;
	}

	
	/*
	 * In viewer mode the submit is not allowed!
	 * Simple call onCancel(...)
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
			return onCancel(request, response, command);
	}
}
