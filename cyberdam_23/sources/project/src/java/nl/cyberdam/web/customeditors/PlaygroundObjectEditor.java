package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.service.GameManager;

/**
 *
 */
public class PlaygroundObjectEditor extends PropertyEditorSupport {
    private GameManager gameManager;

    public PlaygroundObjectEditor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof PlaygroundObject) {
            return ((PlaygroundObject) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameManager.loadPlaygroundObject(new Long(text)));
        } else {
            setValue(null);
        }
    }
}
