package nl.cyberdam.web;

import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.web.util.PlaygroundObjectXmlConverter;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;

/**
 * returns xml content for the flash map
 */
public class XmlMapController extends AbstractGameController {

	// XXX see more options:
	// http://forum.springframework.org/showthread.php?t=28323

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long participantId = 0;
        String idString = ServletRequestUtils.getStringParameter(request,"participantId");
        if (idString != null && idString.length() != 0)
        {
        	participantId = Long.parseLong(idString);
        }
		Long playgroundId = ServletRequestUtils.getLongParameter(request,
				"playgroundId");
		Playground playground = getGameManager().loadPlayground(playgroundId);
		response.setContentType("text/xml");
		Writer writer = response.getWriter();
		List<PlaygroundObject> playgroundObjects = getGameManager()
				.findAllPlaygroundObjectsForMap(playground);
		PlaygroundObjectXmlConverter.returnXmlString(playgroundObjects, writer,
				request.getContextPath(), participantId);
		writer.close();
		return null; // indicates this controller did all necessary
		// processing
	}

}
