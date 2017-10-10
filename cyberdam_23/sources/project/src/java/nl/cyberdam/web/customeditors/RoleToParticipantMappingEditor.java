/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.Participant;
import nl.cyberdam.service.GameManager;

/**
 *
 */
public class RoleToParticipantMappingEditor extends PropertyEditorSupport {

    private GameManager gameManager;

    public RoleToParticipantMappingEditor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof Participant) {
            return ((Participant) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameManager.loadParticipant(new Long(text)));
        } else {
            setValue(null);
        }
    }

}
