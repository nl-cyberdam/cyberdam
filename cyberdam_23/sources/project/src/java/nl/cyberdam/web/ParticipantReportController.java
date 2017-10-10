package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Role;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.service.SessionReportLogService;
import nl.cyberdam.web.util.MenuUtil;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.support.RequestContextUtils;

public class ParticipantReportController extends AbstractController {

	private static String LIST_ATTR = "participantreportlist";
	private static String PARTICIPANTID_ATTR = "participantId";
	protected GameManager gameManager;
	private MenuUtil menuUtil;
	protected SessionReportLogService sessionReportLogService;

	@SuppressWarnings("unchecked")
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long participantId = new Long(ServletRequestUtils.getRequiredLongParameter(request,
		"participantId"));
		Participant participant = gameManager.loadParticipant(participantId);
		RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request.getSession(
				true).getAttribute(LIST_ATTR);
		Long p = (Long) request.getSession(true).getAttribute(PARTICIPANTID_ATTR);

		if (listHolder == null || !p.equals(participantId)) {
			logger.debug("creating new listHolder object: " + participantId);
			listHolder = new RefreshablePagedListHolder();
			listHolder.setPageSize(gameManager.loadSystemParameters().getDefaultRows());
			listHolder.setSourceProvider(new SessionReportListProvider(participant.getGameSession().getId(),
					participant.getRoleAndPlayground().getRole()));
			request.getSession(true).setAttribute(LIST_ATTR, listHolder);
			request.getSession(true).setAttribute(PARTICIPANTID_ATTR, participantId);
			listHolder.refresh(true);
		}

		ServletRequestDataBinder binder = new ServletRequestDataBinder(listHolder, LIST_ATTR);
		binder.bind(request);

		listHolder.setLocale(RequestContextUtils.getLocale(request));
		listHolder.refresh(true);

		menuUtil.generateMenu(request, MenuUtil.GAME_MENU, participant.getGameSession()
				.getManifest());

		Map<String, Object> model = (Map<String, Object>) binder.getBindingResult().getModel();
		model.put("participant", participant);
		model.put("statusText", SessionTools.getStatusText(participant, gameManager));
		
		return new ModelAndView("participantreport", model);
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void setSessionReportLogService(
			SessionReportLogService sessionReportLogService) {
		this.sessionReportLogService = sessionReportLogService;
	}

	private class SessionReportListProvider implements PagedListSourceProvider, Serializable {

		Long sessionId;
		Role role;

		public SessionReportListProvider(Long sessionId, Role role) {
			super();
			this.sessionId = sessionId;
			this.role = role;
		}

		@SuppressWarnings("unchecked")
		public List loadList(Locale arg0, Object arg1) {
			return sessionReportLogService.findAllForSessionAndRole(sessionId, role);
		}
	}

	public MenuUtil getMenuUtil() {
		return menuUtil;
	}

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
}
