package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class ParameterizableViewWithMenuController extends
		ParameterizableViewController {

	MenuUtil menuUtil;

	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		menuUtil.generateMenu(request, MenuUtil.INDEX_MENU);
		return super.handleRequestInternal(request, response);
	}

	public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
}
