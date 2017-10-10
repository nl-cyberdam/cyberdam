package nl.cyberdam.web;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.service.ExchangeManager;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

public class PlaygroundExportController extends AbstractGameController {

	private ExchangeManager<Playground> exchangeManager;
	private String swfPath;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
		Long id = new Long(ServletRequestUtils.getRequiredLongParameter(
				request, "id"));
		final Playground playground = getGameManager().loadPlayground(id);

		Date now = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String s = df.format(now);

		response.setContentType("application/zip");
		response.setHeader("Content-Disposition", "attachment;filename=\""
				+ playground.getUriId() + "_" + s + ".zip\"");
		//exchangeManager.setServletContext(getServletContext());
		exchangeManager.export(playground, response.getOutputStream(), swfPath);

		getGameManager().getGameLogService().addLog(LogEntry.Module.LCMS,
				"export playground", playground.getName());
		response.getOutputStream().flush();
		response.getOutputStream().close();
		logger.debug("playground export called");
		}
		catch(FileNotFoundException f){
			logger.error(f);
				return new ModelAndView(new RedirectView("playgroundlistpage.htm?no_swf_found=0", true));
		}
		return null;

	}

	/**
	 * should be injected
	 */

	public void setExchangeManager(ExchangeManager<Playground> exchangeManager) {
		this.exchangeManager = exchangeManager;
	}
	
	 public void setSwfPath(String swfPath) {
			logger.info("setSwfPath: " + swfPath);
			this.swfPath = swfPath;
	}

}
