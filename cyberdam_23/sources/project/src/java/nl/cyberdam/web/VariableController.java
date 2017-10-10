package nl.cyberdam.web;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.support.PagedListSourceProvider;

public class VariableController extends GameListController {
	
    /** Creates a new instance of VariablesController */
    public VariableController() {
    	super("variables", "variable");
    	setListSourceProvider(new VariableListProvider());
    }
    
    private class VariableListProvider implements PagedListSourceProvider, Serializable {

        public List loadList(Locale loc, Object filter) {
          	return gameManager.findSystemVariables();
        }
    }
}
