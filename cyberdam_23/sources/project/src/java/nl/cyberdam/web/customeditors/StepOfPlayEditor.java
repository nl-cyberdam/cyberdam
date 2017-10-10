package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.StepOfPlay;

/**
 * it does not access objects by id because they are not guaranteed to be stored in the 
 * database already, it uses the position in the list of steps of play instead.
 */
public class StepOfPlayEditor extends PropertyEditorSupport {
    private GameModel gameModel;

    public StepOfPlayEditor(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public String getAsText() {
        if (this.getValue() != null && this.getValue() instanceof StepOfPlay) {
            return Integer.toString(gameModel.getStepsOfPlay().indexOf(this.getValue()));
        }
        return this.getValue() == null ? "" : this.getValue().toString();
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text != null && !text.equals("")) {
            setValue(gameModel.getStepsOfPlay().get(Integer.parseInt(text)));
        } else {
            setValue(null);
        }
    }
}