package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.GameSession;
import nl.cyberdam.service.GameManager;

/**
 *
 */
public class GameSessionEditor extends PropertyEditorSupport {
    private GameManager gameManager;

    public GameSessionEditor(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof GameSession) {
            return ((GameSession) this.getValue()).getId().toString();
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameManager.loadGameSession(new Long(text)));
        } else {
            setValue(null);
        }
    }
}
