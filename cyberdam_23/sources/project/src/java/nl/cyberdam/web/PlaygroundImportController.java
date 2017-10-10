package nl.cyberdam.web;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.service.ExchangeManager;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.ExchangeManager.ExchangeBean;
import nl.cyberdam.util.GameUtil;
import nl.cyberdam.util.NoCloseZipInputStream;

import org.hibernate.validator.InvalidStateException;
import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;
import org.xml.sax.SAXException;

public class PlaygroundImportController extends AbstractFormController {

	public static final String FORM_TYPE = "form_type";
	public static final String HASH_MAP_KEY = "hash_map_key";

	public static final Integer UPLOAD_SCREEN = Integer.valueOf(0);
	public static final Integer MODIFY_SCREEN = Integer.valueOf(1);
	public static final Integer RESULT_SCREEN = Integer.valueOf(2);

	// used for getting 'copy of' in multilanguage;
	private MessageSource messageSource;
	private GameManager gameManager;
	private ExchangeManager<Playground> exchangeManager;
	private String swfPath;

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		return new SessionFileUploadBean();
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
		// cast the bean
		SessionFileUploadBean bean = (SessionFileUploadBean) command;

		Map<String, Object> data = new HashMap<String, Object>();
		if (WebUtils.hasSubmitParameter(request, "_cancel")) {
			return new ModelAndView(new RedirectView("playgroundlistpage.htm",
					true));
		} else if (WebUtils.hasSubmitParameter(request, "_upload")) {
			processUpload(request, bean, data);
			return new ModelAndView("playgroundimport", data);

		} else if (WebUtils.hasSubmitParameter(request, "_save")) {
			processSave(request, bean, data);
			return new ModelAndView("playgroundimport", data);

		} else if (WebUtils.hasSubmitParameter(request, "_ok")) {
			return new ModelAndView(new RedirectView(
					"playgroundlistpage.htm?forceRefresh=true", true));
		}

		// check is there were errors - if so: display the upload form again
		if (errors.hasErrors()) {
			return showForm(request, response, errors);
		}

		return new ModelAndView(new RedirectView(
				"playgroundlistpage.htm?forceRefresh=true", true));
	}

	private void putResult(HashMap<String, Integer> counters,
			Map<String, Object> data, Locale userLocale, Playground playground) {
		List<String> list = new ArrayList<String>();
		
		if (playground != null) {
			DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, userLocale);
			//list.add(messageSource.getMessage("playground.name", null, userLocale)+ " : " + playground.getName());
			//list.add(messageSource.getMessage("Uri", null, userLocale)+ " : " + playground.getUriId());
			//list.add(messageSource.getMessage("caption", null, userLocale)+ " : " + playground.getCaption());
			//list.add(messageSource.getMessage("version", null, userLocale)+ " : " + playground.getVersion());
			list.add(messageSource.getMessage("lastmodifiedby", null, userLocale)+ " : " + playground.getLastModifier().getUsername());
			list.add(messageSource.getMessage("lastmodified", null, userLocale)+ " : " + df.format(playground.getLastModified()));
		}
		
		if (counters != null) {
			for (String item : counters.keySet()) {
				String title = messageSource.getMessage(item, null, userLocale);

				list.add(title + " : " + counters.get(item));
			}
			data.put("resultStrings", list);
		}
	}

	private void processUpload(HttpServletRequest request,
			SessionFileUploadBean bean, Map<String, Object> data)
			throws IOException, JAXBException, SAXException {
		MultipartFile file = bean.getFile();

		NoCloseZipInputStream is = new NoCloseZipInputStream(file
				.getInputStream());

		ExchangeBean<Playground> exchangeBean = exchangeManager
				.getExchangeBean();
		exchangeBean.setZipFile(is);
		
		exchangeManager.imp(exchangeBean);
		
		request.getSession().setAttribute(HASH_MAP_KEY, exchangeBean);
		gameManager.getGameLogService().addLog(LogEntry.Module.SYSTEM,
				"fileupload import", file.getName());
		
		savePlayground(null, data, exchangeBean);

	}

	@SuppressWarnings("unchecked")
	private void processSave(HttpServletRequest request,
			SessionFileUploadBean bean, Map<String, Object> data)
			throws IOException, JAXBException {
		ExchangeBean<Playground> exchangeBean = (ExchangeBean<Playground>) request
				.getSession().getAttribute(HASH_MAP_KEY);

		
		savePlayground(bean.getPlaygrounduri(), data, exchangeBean);
	}

	private void savePlayground(String uri,
			Map<String, Object> data, ExchangeBean<Playground> exchangeBean)
			throws IOException, JAXBException {
		HashMap<String, Integer> counters = null;
		if (!exchangeManager.existsExchangeObject(exchangeBean.getExchangeObject(), uri)) {
			
			try {
				counters = exchangeManager.save(exchangeBean, uri, GameUtil.getCurrentUser(), swfPath);
			} catch (InvalidStateException ise) {
				// TODO use logger
				ise.printStackTrace();
				data.put("invalidvalues", ise.getInvalidValues());
			}
			
			if (counters!=null&&counters.get(ExchangeManager.SWF_NOT_SAVED) != null) {
				counters.remove(ExchangeManager.SWF_NOT_SAVED);
				data.put(ExchangeManager.SWF_NOT_SAVED, Boolean.TRUE);
			} else {
				data.put(ExchangeManager.SWF_NOT_SAVED, Boolean.FALSE);
			}
			putResult(counters, data, GameUtil.getCurrentUser().getLocale(), exchangeBean.getExchangeObject());
			data.put("playground", exchangeBean.getExchangeObject());
			data.put(FORM_TYPE, RESULT_SCREEN);
			exchangeBean.getZipFile().realyClose();
			
	} else if (exchangeBean.getExchangeObject() != null) {
		data.put("playgroundUri", exchangeBean.getExchangeObject().getUriId());
		data.put(FORM_TYPE, MODIFY_SCREEN);
	}
	}

	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {

		Map<String, Object> data = new HashMap<String, Object>();
		data.put(FORM_TYPE, UPLOAD_SCREEN);
		String doCancel = ServletRequestUtils.getStringParameter(request,
				"_cancel");
		if (doCancel != null) {
			// redirect
			return new ModelAndView(new RedirectView("playgroundlistpage.htm",
					true));
		}
		if (errors.hasErrors()) { // if we had any errors: add them to the
			data.put("errors", errors.getGlobalErrors());
		}

		return new ModelAndView("playgroundimport", data);
	}

	public void setExchangeManager(ExchangeManager<Playground> exchangeManager) {
		this.exchangeManager = exchangeManager;
	}

	public void setSwfPath(String swfPath) {
		this.swfPath = swfPath;
	}
}
