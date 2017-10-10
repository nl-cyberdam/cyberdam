package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.service.GameManager;

/**
 *
 */
public class GameModelEditor extends PropertyEditorSupport {
    private GameManager gameManager;

    public GameModelEditor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof GameModel) {
            return ((GameModel) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameManager.load(new Long(text)));
        } else {
            setValue(null);
        }
    }
}
