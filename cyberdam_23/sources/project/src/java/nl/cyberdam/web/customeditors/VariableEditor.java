package nl.cyberdam.web.customeditors;

import java.beans.PropertyEditorSupport;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.service.GameManager;

public class VariableEditor extends PropertyEditorSupport {
	private GameManager gameManager;
	private GameModel gameModel;

	public VariableEditor(GameManager gameManager, GameModel gameModel) {
		this.gameManager = gameManager;
		this.gameModel = gameModel;
	}

	@Override
	public String getAsText() {
		if (this.getValue() != null && this.getValue() instanceof Variable) {
			return ((Variable)this.getValue()).getId().toString();
		}
		return this.getValue() == null ? "" : this.getValue().toString();
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text != null && !text.equals("")) {
			setValue(this.getVariableById(new Long(text)));
		} else {
			setValue(null);
		}
	}

	public Variable getVariableById(Long variableId) {
		for (Variable v: gameManager.findSystemVariables()) {
			if (v.getId().equals(variableId)) return v;
		}
		for (Variable v: gameModel.getVariables()) {
			if (v.getId().equals(variableId)) return v;
		}
		for (Role r: gameModel.getRoles()) {
			for (Variable v: r.getVariables()) {
				if (v.getId().equals(variableId)) return v;
			}
		}
		throw new RuntimeException("variable not found");
	}
}
