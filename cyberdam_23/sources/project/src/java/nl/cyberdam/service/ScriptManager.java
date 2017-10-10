package nl.cyberdam.service;

import java.io.IOException;
import java.util.Map;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.domain.VariableSessionValue;
import nl.cyberdam.util.GameUtil;

import org.mozilla.javascript.Callable;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;

//a collection of tools for handling activities and scripts

class MyFactory extends ContextFactory {
    // Custom Context to store execution time.
    @SuppressWarnings("deprecation")
    private class MyContext extends Context {
	long startTime;
    }

    protected Context makeContext() {
	MyContext cx = new MyContext();
	// Use pure interpreter mode to allow for
	// observeInstructionCount(Context, int) to work
	cx.setOptimizationLevel(-1);
	// Make Rhino runtime to call observeInstructionCount
	// each 10000 bytecode instructions
	cx.setInstructionObserverThreshold(10000);
	cx.setMaximumInterpreterStackDepth(20);
	return cx;
    }

    protected void observeInstructionCount(Context cx, int instructionCount) {
	MyContext mcx = (MyContext) cx;
	long currentTime = System.currentTimeMillis();
	if (currentTime - mcx.startTime > 10 * 1000) {
	    // More then 10 seconds from Context creation time:
	    // it is time to stop the script.
	    // Throw Error instance to ensure that script will never
	    // get control back through catch or finally.
	    throw new Error();
	}
    }

    protected Object doTopCall(Callable callable, Context cx, Scriptable scope, Scriptable thisObj,
	    Object[] args) {
	MyContext mcx = (MyContext) cx;
	mcx.startTime = System.currentTimeMillis();

	return super.doTopCall(callable, cx, scope, thisObj, args);
    }

}

public class ScriptManager {

    private GameManager gameManager;
    private Resource scriptLibrary;
    private String librarySource;
    private MessageSource messageSource;
   
    public void setScriptLibrary(Resource scriptLibrary) throws IOException {
	this.scriptLibrary = scriptLibrary;
	    byte[] source = new byte[10000];
	    int length = 0;
	    length = scriptLibrary.getInputStream().read(source);
	    librarySource = new String (source, 0, length);
    }

    public Resource getScriptLibrary() {
	return scriptLibrary;
    }

    public GameManager getGameManager() {
	return gameManager;
    }

    public void setGameManager(GameManager gameManager) {
	this.gameManager = gameManager;
    }

    public void RunScriptForStep (StepOfPlay step, Participant participant, GameSession gameSession)
    {
    	String script = step.getScript();
    	if (script != null)
    	{
    	    RunScript (script, participant, gameSession);
    	}
    }
    
    public void CompletedActivity (Activity activity, Participant participant, GameSession gameSession)
    {
    	if (participant != null)
    	{
    		gameSession = participant.getGameSession();
    	}
        StepOfPlay currentStep = gameSession.getCurrentStepOfPlay();

        String script = activity.getScript();
    	if (script != null)
    	{
    	    RunScript (script, participant, gameSession, activity);
	        StepOfPlay nextStep = gameSession.getCurrentStepOfPlay(); 
	        if (currentStep != nextStep)
    	    {
        		RunScriptForStep (gameSession.getCurrentStepOfPlay (), participant, gameSession);
			}
        }
    }

    public Object RunScript(String script, Participant participant,
    	    GameSession gameSession)
    {
    	return RunScript (script, participant, gameSession, null);
    }

    public Object RunScript(String script, Participant participant,
	    GameSession gameSession, Activity activity) {
	// Initialize GlobalFactory with custom factory
	if (!(ContextFactory.getGlobal() instanceof MyFactory)) {
	    ContextFactory.initGlobal(new MyFactory());
	}

	if (participant != null)
	{
	    gameSession = participant.getGameSession ();
	}

	Context jsContext = ContextFactory.getGlobal().enterContext();
	Scriptable scope = jsContext.initStandardObjects();
	// Object wrappedObject = Context.javaToJS(progress, scope);
	// ScriptableObject.putProperty(scope, "activity", wrappedObject);
	final Map<String, String> variableValues = getGameManager()
	.variableValuesForSession(participant, gameSession);
	ScriptableObject.putProperty(scope, "variables", Context.javaToJS(variableValues, scope));
	// for (Map.Entry<String, String> entries : variableValues.entrySet()) {
	    // System.out.println("MAP: " + entries.getKey() + "->" + entries.getValue());
	// }
	ScriptableObject.putProperty(scope, "participant", Context.javaToJS(participant, scope));
	ScriptableObject.putProperty(scope, "activity", Context.javaToJS(activity, scope));
	ScriptableObject.putProperty(scope, "session", Context.javaToJS(gameSession, scope));
	ScriptableObject.putProperty(scope, "messageSource", Context.javaToJS(messageSource, scope));
	User u = GameUtil.getCurrentUser();
	ScriptableObject.putProperty(scope, "locale", Context.javaToJS((u != null) ? u.getLocale() : null, scope));

	StepOfPlay step = gameSession.getCurrentStepOfPlay();
	String source = new String(librarySource);
	source = source + script; 
	Object scriptValue = jsContext.evaluateString(scope, source, "script", 1, null);
	if (step != gameSession.getCurrentStepOfPlay()) {
	    getGameManager().save(gameSession);
	}
	for (Activity saveActivity : gameSession.getManifest().getGameModel().getActivities())
	{
		getGameManager().save(saveActivity);
	}

	// final Map<String, String> variableValues =

	// gameManager.variableValuesForSession (participant);
	Scriptable values = (Scriptable) scope.get("variables", scope);
	for (Object path : values.getIds()) {
	    String value = values.get((String) path, scope).toString();
	    // System.out.println ("Value for " + path + " is " + value);
	    for (Variable variable : gameSession.getManifest().getGameModel().getVariables()) {
		if (variable.getName().equals(path)) {
		    VariableSessionValue selectedSessionValue = null;
		    for (VariableSessionValue sessionValue : gameSession.getSessionVariableValues()) {
			if (sessionValue.getVariable() == variable) {
			    selectedSessionValue = sessionValue;
			}
		    }
		    if (selectedSessionValue == null) {
			selectedSessionValue = new VariableSessionValue();
			selectedSessionValue.setVariable (variable);
			selectedSessionValue.setGameSession(gameSession);
			gameSession.getSessionVariableValues().add(selectedSessionValue);
			// participant.getGameSession
			// ().getSessionVariableValues ().add ();
		    }
		    selectedSessionValue.setValue(value);
		    gameManager.save(gameSession);
		}
	    }
	}
	//System.out.println(scriptValue);
	return scriptValue;
    }

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public MessageSource getMessageSource() {
		return messageSource;
	}
}
