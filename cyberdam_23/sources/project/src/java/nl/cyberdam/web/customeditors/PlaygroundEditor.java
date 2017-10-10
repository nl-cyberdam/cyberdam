package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.Playground;
import nl.cyberdam.service.GameManager;

/**
 *
 */
public class PlaygroundEditor extends PropertyEditorSupport {
    private GameManager gameManager;

    public PlaygroundEditor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof Playground) {
            return ((Playground) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameManager.loadPlayground(new Long(text)));
        } else {
            setValue(null);
        }
    }
}
