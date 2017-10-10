package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.Resource;

/**
 * it does not access objects by id because they are not guaranteed to be stored in the 
 * database already, it uses the position in the list of resources instead.
 *
 */
public class ResourceEditor extends PropertyEditorSupport {
    private GameModel gameModel;

    public ResourceEditor(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof Resource) {
            return Integer.toString(gameModel.getResources().indexOf(this.getValue()));
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameModel.getResources().get(new Integer(text).intValue()));
        } else {
            setValue(null);
        }
    }
}
