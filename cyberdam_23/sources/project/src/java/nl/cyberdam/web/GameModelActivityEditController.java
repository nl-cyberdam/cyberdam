package nl.cyberdam.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.ActivityVariable;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.MessageActivity;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.web.customeditors.StepOfPlayEditor;
import nl.cyberdam.web.customeditors.VariableEditor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.CancellableFormController;

public class GameModelActivityEditController extends CancellableFormController {

	private GameManager gameManager;
	private GameModel gameModel;
	public static final String GAMEMODEL_ATTR = GameModelDetailController.GAMEMODEL_ATTR;
	public static final String ACTIVITY_ATTR = GameModelDetailController.ACTIVITY_ATTR;

	@Override
	protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
		throws Exception {
		Activity activity = (Activity) getCommand(request);
		if (activity instanceof ProgressActivity) {
			binder.registerCustomEditor(StepOfPlay.class, "nextStepOfPlayOptions.step", new StepOfPlayEditor(gameModel));
		} else if (activity instanceof FormActivity) {
			binder.registerCustomEditor(Variable.class, "activityVariables.variable", new VariableEditor(gameManager, gameModel));
		}
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
	throws Exception {
		gameModel = (GameModel) request.getSession(true).getAttribute(GAMEMODEL_ATTR);
		return request.getSession(true).getAttribute(ACTIVITY_ATTR);
	}

	@SuppressWarnings("unchecked")
	protected Map referenceData(HttpServletRequest request, Object command, Errors errors) throws Exception {
		Map<String, Object> referenceData = new HashMap<String, Object>();
		Activity activity = (Activity) command;
		if (activity instanceof FormActivity) {
			referenceData.put("variableOptions", this.getVariableOptions((FormActivity)activity));
		}
		return referenceData;
	}

	protected void onBindAndValidate(HttpServletRequest request, Object command, BindException errors)
	throws Exception {
		String action = ServletRequestUtils.getStringParameter(request, "action");
		if ("".equals(action)) {
			Activity activity = (Activity) command;
			Pattern p = Pattern.compile("\\s*");
			Matcher m = p.matcher(activity.getName());
			if (m.matches()) {
				errors.reject("empty.activity");
			}
			if (activity instanceof MessageActivity) {
				MessageActivity ma = (MessageActivity)activity;
				if (ma.getRecipients().size() == 0) {
					errors.reject("messageactivity.without.recipients");
				}
			} else if (activity instanceof FormActivity) {
				FormActivity ma = (FormActivity)activity;
				for (ActivityVariable av: ma.getActivityVariables()) {
					m = p.matcher(av.getCaption());
					if (m.matches()) {
						errors.reject("activityvariable.captionempty");
						break;
					}
					if (av.getVariable() == null) {
						errors.reject("activityvariable.novariableselected");
						break;
					}
				}
			}
		}
	}

	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
	throws Exception {
		Activity activity = (Activity) command;
		if (activity instanceof FormActivity) {
			FormActivity fa = (FormActivity)activity;
			String action = ServletRequestUtils.getRequiredStringParameter(request, "action");
			if (!"".equals(action)) {
				if (action.equals("addActivityVariable")) {
					ActivityVariable av = new ActivityVariable();
					av.setActivity(fa);
					fa.getActivityVariables().add(av);
				} else if (action.equals("deleteActivityVariable")) {
					int index = ServletRequestUtils.getRequiredIntParameter(request, "activityVariableIndex");
					fa.removeActivityVariableByIndex(index);
				} else if (action.equals("updateActivityVariable")) {
				} else {
					errors.reject("Unknown action: " + action);
				}
				Map<String, Object> data = new HashMap<String, Object>();
				data.put("scroll", new Boolean(true));
				return showForm(request, response, errors, data);
			}
		}
		try {
			gameManager.save(gameModel);
		} catch (DataIntegrityViolationException dive) {
			errors.reject(dive.getMessage());
			return showForm(request, response, errors);
		}

		return new ModelAndView(getSuccessView(), "id", gameModel.getId());
	}


	protected ModelAndView onCancel(HttpServletRequest request,
			HttpServletResponse response, Object command) throws Exception {
		return new ModelAndView(getCancelView(), "id", gameModel.getId());
	}

	public class VariableOption implements java.io.Serializable {
		private String name;
		private boolean taken;
		Long id;
		public VariableOption() { }
		public VariableOption(String name, boolean taken, Long id) {
			this.setName(name);
			this.setTaken(taken);
			this.setId(id);
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public boolean getTaken() {
			return taken;
		}
		public void setTaken(boolean taken) {
			this.taken = taken;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
	}

	public List<List<VariableOption>> getVariableOptions(FormActivity formActivity) {
		Map<String, Variable> variables = new TreeMap<String, Variable>();
		// in this order gamemodel variables will override equal system variables and role variables 
		// will override gamemodel variables
		List<Variable> systemVariables = gameManager.findSystemVariables();
		if (systemVariables != null) {
			for (Variable sv: systemVariables) {
				variables.put(sv.getName(), sv);
			}
		}
		for (Variable mv: gameModel.getVariables()) {
			variables.put(mv.getName(), mv);
		}
		for (Role r: gameModel.getRoles()) {
			for (Variable rv: r.getVariables()) {
				// we add the role name to the variable name to distinguish between role vars with
				// the same name. Make sure we remove any system or gamemodel entry with the same name
				// There is still a problem with role vars with the same name and same role name!
				variables.remove(rv.getName());
				String roleName = r.getName();
				variables.put(rv.getName() + " (" + (roleName == null ? "<empty>" : roleName) + ")", rv);
			}
		}
		List<List<VariableOption>> llvo = new ArrayList<List<VariableOption>>();
		for (ActivityVariable activityVariable: formActivity.getActivityVariables()) {
			List<VariableOption> lvo = new ArrayList<VariableOption>();
			for (String s: variables.keySet()) {
				Variable v = variables.get(s);
				boolean taken = false;
				for (ActivityVariable av: formActivity.getActivityVariables()) {
					if (!activityVariable.equals(av) && 
							av.getVariable() != null && 
							av.getVariable().getId().equals(v.getId())) taken = true;
				}
				lvo.add(new VariableOption(s, taken, v.getId()));
			}
			llvo.add(lvo);
		}
		return llvo;
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
