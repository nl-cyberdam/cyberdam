package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.web.util.MenuUtil;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;

/**
 *
 */
public class SystemAdministrationController extends UrlFilenameViewController {
    
    /** Creates a new instance of SystemAdministrationController */
	MenuUtil menuUtil;
	
    public SystemAdministrationController() {
    }
    
    public void setMenuUtil(MenuUtil menuUtil) {
		this.menuUtil = menuUtil;
	}
    
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) {
		menuUtil.generateMenu(request, MenuUtil.SYSTEM_MENU);
		return super.handleRequestInternal(request, response);
	}
    
}
