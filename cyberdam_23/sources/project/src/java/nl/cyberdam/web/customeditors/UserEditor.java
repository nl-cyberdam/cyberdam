package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.User;
import nl.cyberdam.service.UserManager;

/**
 *
 */
public class UserEditor extends PropertyEditorSupport {
    private UserManager userManager;

    public UserEditor(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof User) {
            return ((User) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(userManager.loadUser(new Long(text)));
        } else {
            setValue(null);
        }
    }
}
