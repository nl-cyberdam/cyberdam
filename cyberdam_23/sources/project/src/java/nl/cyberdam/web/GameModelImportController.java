package nl.cyberdam.web;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.service.ExchangeManager;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.ExchangeManager.ExchangeBean;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.util.NoCloseZipInputStream;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;

public class GameModelImportController extends AbstractFormController {

	public static final String FORM_TYPE = "form_type";

	public static final Integer UPLOAD_SCREEN = Integer.valueOf(0);
	public static final Integer MODIFY_SCREEN = Integer.valueOf(1);
	public static final Integer RESULT_SCREEN = Integer.valueOf(2);

	// used for getting 'copy of' in multilanguage;
	private MessageSource messageSource;
	private GameManager gameManager;
	private ExchangeManager<GameModel> exchangeManager;

	public GameModelImportController() {
		setCommandClass(SessionFileUploadBean.class);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	@Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {

		logger.info("process form submission");
		SessionFileUploadBean bean = (SessionFileUploadBean) command;

		Map<String, Object> data = new HashMap<String, Object>();
		// check for _cancel
		if (WebUtils.hasSubmitParameter(request, "_cancel")) {
			return new ModelAndView(new RedirectView("gameauthor.htm", true));
		} else if (WebUtils.hasSubmitParameter(request, "_upload")) {

			MultipartFile file = bean.getFile();

			NoCloseZipInputStream is = new NoCloseZipInputStream(file
					.getInputStream());
			ExchangeBean<GameModel> exchangeBean = exchangeManager
					.getExchangeBean();

			exchangeBean.setZipFile(is);
			/*HashMap<String, Integer> counters = exchangeManager.imp(
					exchangeBean, GameUtil.getCurrentUser());*/
			exchangeManager.imp(exchangeBean);
			HashMap<String, Integer> counters = exchangeManager.save(exchangeBean, null,  GameUtil.getCurrentUser(), null);

			gameManager.getGameLogService().addLog(LogEntry.Module.SYSTEM,
					"fileupload import", file.getName());
			is.realyClose();
			data.put(FORM_TYPE, RESULT_SCREEN);
			putResult(counters, data, GameUtil.getCurrentUser().getLocale(), exchangeBean.getExchangeObject());

			return new ModelAndView("gamemodelimport", data);
		} else if (WebUtils.hasSubmitParameter(request, "_ok")) {
			return new ModelAndView(new RedirectView(
					"gameauthor.htm?forceRefresh=true", true));
		}

		if (errors.hasErrors()) {
			return showForm(request, response, errors);
		}

		return new ModelAndView(new RedirectView(
				"gameauthor.htm?forceRefresh=true", true));
	}

	private void putResult(HashMap<String, Integer> counters,
			Map<String, Object> data, Locale userLocale, GameModel gm) {
		List<String> list = new ArrayList<String>();
		if (gm != null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, userLocale);
			list.add(messageSource.getMessage("game.name", null, userLocale)+ " : " + gm.getName());
			list.add(messageSource.getMessage("caption", null, userLocale)+ " : " + gm.getCaption());
			list.add(messageSource.getMessage("lastmodifiedby", null, userLocale)+ " : " + gm.getLastModifier().getUsername());
			list.add(messageSource.getMessage("lastmodified", null, userLocale)+ " : " + df.format(gm.getLastModified()));
		}
		if (counters != null) {
			for (String item : counters.keySet()) {
				String title = messageSource.getMessage(item, null, userLocale);

				list.add(title + " : " + counters.get(item));
			}
			data.put("resultStrings", list);
		}
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		String doCancel = ServletRequestUtils.getStringParameter(request,
				"_cancel");
		if (doCancel != null) {
			// redirect
			return new ModelAndView(new RedirectView("gameauthor.htm", true));
		}
		if (errors.hasErrors()) { // if we had any errors: add them to the
			data.put("errors", errors.getGlobalErrors());
		}
		data.put(FORM_TYPE, UPLOAD_SCREEN);
		return new ModelAndView("gamemodelimport", data);
	}

	public void setExchangeManager(ExchangeManager<GameModel> exchangeManager) {
		this.exchangeManager = exchangeManager;
	}

}
