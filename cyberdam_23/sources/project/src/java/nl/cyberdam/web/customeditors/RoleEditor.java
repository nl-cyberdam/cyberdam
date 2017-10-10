package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.Role;

/**
 * it does not access objects by id because they are not guaranteed to be stored in the 
 * database already, it uses the position in the list of steps of play instead.
 */
public class RoleEditor extends PropertyEditorSupport {
    private GameModel gameModel;

    public RoleEditor(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof Role) {
            return Integer.toString(gameModel.getRoles().indexOf(this.getValue()));
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameModel.getRoles().get(Integer.parseInt(text)));
        } else {
            setValue(null);
        }
    }
}