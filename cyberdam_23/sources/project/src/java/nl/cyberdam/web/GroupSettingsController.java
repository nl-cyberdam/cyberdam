package nl.cyberdam.web;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import nl.cyberdam.domain.Group;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.UserManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.CancellableFormController;

/**
 *
 */
public class GroupSettingsController extends CancellableFormController {

    private UserManager userManager;

    /** Creates a new instance of EditUserController */
    public GroupSettingsController() {
        setCommandClass(Group.class);
        setSessionForm(true); // Store the command object in session instead of instantiating it in both edit and save
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        
        Long groupId = ServletRequestUtils.getLongParameter(request, "id");
        if (groupId != null) {
            return getUserManager().loadGroup(groupId);
        } else {
            return new Group();
        }
    }

    @Override
    protected Map<String, Object> referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> referenceData = new HashMap<String, Object>();
        // get all users
        Collection<User> users = userManager.findAll();
        referenceData.put("users", users);
        return referenceData;
    }

    @Override
    protected void doSubmitAction(Object command) {
    	Group g = (Group) command;
    	g.setLastModified(new Date());
    	g.setLastModifiedBy(GameUtil.getCurrentUser());
        getUserManager().saveGroup(g);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

}
