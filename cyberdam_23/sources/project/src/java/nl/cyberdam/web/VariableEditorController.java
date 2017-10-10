package nl.cyberdam.web;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Variable;
import nl.cyberdam.service.GameManager;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;


public class VariableEditorController extends CancellableFormController {
    
    protected static final String ALREADYTAKEN     = "alreadytaken";
    protected static final String LOCALE           = "locale";
    protected static final String VARIABLE_ID 	   = "id";
    private SessionFactory        sessionFactory;
    private GameManager			  gameManager;
    
    public VariableEditorController() {
        setCommandClass(Variable.class);
//        setSessionForm(true); // Store the command object in session instead of instantiating it in both edit and save
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        
        Long variableId = ServletRequestUtils.getLongParameter(request,
                VARIABLE_ID);
        Variable variable;
        if (variableId != null) {
            variable = (Variable) getSessionFactory()
                    .getCurrentSession().load(Variable.class,
                            variableId);
        } else {
            variable = new Variable();
        }
        return variable;
    }
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ModelAndView returnValue = new ModelAndView(getSuccessView());
        Variable variable = (Variable) command;
        getValidator().validate(variable, errors);

        // check uniqueness
    	List<Variable> lv =  getGameManager().findSystemVariables();
    	boolean b = false;
    	for (Variable v: lv) {
    		if (!v.getId().equals(variable.getId()) && v.getName().equals(variable.getName())) b = true;
    	}
    	if (b == true) {
    		errors.reject("variable.alreadytaken");
    	}
        if (errors.hasErrors()) {
            returnValue =  showForm(request, response, errors);
        } else {
            try {
                Pattern p = Pattern.compile("\\s*");
               	Matcher m = p.matcher(variable.getInitialValue());
               	if (m.matches()) {
               		variable.setInitialValue(null);
               	}
                getSessionFactory().getCurrentSession().saveOrUpdate(variable);
                getSessionFactory().getCurrentSession().flush();
            } catch (ConstraintViolationException ce) {
                errors.rejectValue(LOCALE, ALREADYTAKEN);
                returnValue =  showForm(request, response, errors);               
            }
        }
        return returnValue;
    }
    
    protected ModelAndView onCancel(HttpServletRequest request,
            HttpServletResponse response, Object command) throws Exception {
        return new ModelAndView(getCancelView());
    }
    
    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }
    
    // Should be injected
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public GameManager getGameManager() {
		return gameManager;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
