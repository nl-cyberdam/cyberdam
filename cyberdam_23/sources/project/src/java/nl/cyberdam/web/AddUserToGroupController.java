package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Group;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.UserManager;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

/**
 * adds user with id 'userId' to group with id 'groupId' and redirects
 * to the groupsettings page for the specified group
 * 
 */
public class AddUserToGroupController extends AbstractController {

    private UserManager userManager;

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse arg1) throws Exception {
        Long groupId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(request, "groupId"));
        Long userId = Long.valueOf(ServletRequestUtils.getRequiredLongParameter(request, "userId"));
        Group g = userManager.loadGroup(groupId);
        User u = userManager.loadUser(userId);
        g.addMember(u);
        
        // store the updated group
        userManager.saveGroup(g);
        return new ModelAndView(new RedirectView("groupsettings.htm", true), "id", groupId);
    }
}
