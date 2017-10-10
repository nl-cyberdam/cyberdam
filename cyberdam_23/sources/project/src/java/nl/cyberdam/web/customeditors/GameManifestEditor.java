/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.service.GameManager;

/**
 *
 */
public class GameManifestEditor extends PropertyEditorSupport {
    private GameManager gameManager;

    public GameManifestEditor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof GameManifest) {
            return ((GameManifest) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameManager.loadGameManifest(new Long(text)));
        } else {
            setValue(null);
        }
    }
}
