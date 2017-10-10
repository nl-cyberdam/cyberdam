/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.Group;
import nl.cyberdam.service.UserManager;

/**
 *
 */
public class GroupEditor extends PropertyEditorSupport {
    private UserManager userManager;

    public GroupEditor(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof Group) {
            return ((Group) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(userManager.loadGroup(new Long(text)));
        } else {
            setValue(null);
        }
    }
}
