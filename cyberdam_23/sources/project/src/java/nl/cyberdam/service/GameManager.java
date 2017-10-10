package nl.cyberdam.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.EventActivity;
import nl.cyberdam.domain.FileUploadActivity;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.MessageActivity;
import nl.cyberdam.domain.MessageBox;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.Resource;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.RoleActivity;
import nl.cyberdam.domain.RoleToPlaygroundMapping;
import nl.cyberdam.domain.SessionPlaygroundObject;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.Variable;
import nl.cyberdam.domain.VariableSessionValue;
import nl.cyberdam.multilanguage.LanguagePack;

/**
 *
 */
public interface GameManager extends IDefaultService {

    public List<GameModel> findAll();
    public List<GameModel> findAll(String textFilter);
    public List<GameModel> findAllForUser(User u, String textFilter);

    public List<GameSession> findAllGameSessions();
    public List<GameSession> findAllGameSessions(String textFilter);
    
//    public List<GameSession> findAllGameSessionsForUser(User u);

    public List<GameManifest> findAllManifests(String textFilter);

    public List<PlaygroundObject> findAllPlaygroundObjects();
    public List<PlaygroundObject> findAllPlaygroundObjectsForUser(User currentUser);
    public List<PlaygroundObject> findAllPlaygroundObjectsForUser(User currentUser,Long playgroundId, String textFilter);
    public List<PlaygroundObject> findAllPlaygroundObjectsForMap(Playground playground);
	public List<PlaygroundObject> findAllPlaygroundObjectsForDirectory(Playground playground, String filter);

    public List<PlaygroundObject> findAllDirectoryPlaygroundObjects();

    public GameModel load(Long requiredIntParameter);
    
    public void delete(Object o);
    
    public GameManifest loadGameManifest(Long id);

    public PlaygroundObject loadPlaygroundObject(Long playgroundObjectId);

    public Role loadRole(Long roleId);
    
    public LanguagePack loadLanguagePack(Long id);

    public StepOfPlay loadStepOfPlay(Long stepOfPlayId);

	public Variable loadVariable(Long id);

    public RoleToPlaygroundMapping loadRoleToPlaygroundMapping(Long id);
    
    public GameSession loadGameSession(Long id);

    public Resource loadResource(Long id);
    public void save(SessionResource sr);

    public void save(Role r);

	public void save(StepOfPlay s);

	public void save(RoleActivity ra);
    
    public void save(GameManifest gm);

    public void save(GameModel gameModel);

	public void save(Activity activity);

    public void save(Variable v);

    public void save(GameSession gameSession);

    public void save(Message m);
    
    public void save(LanguagePack languagePack);

    public void save(PlaygroundObject playgroundObject);

    public void save(Resource r);
    
    public void save(MessageBox m);
    public void save(SessionPlaygroundObject o);

	public void deliverMessage(Message m);

	public Participant loadParticipant(Long long1);
	
	public List<Participant> findParticipantsForUser(User u, String textFilter);
	
	public List<Variable> findSystemVariables();

	public List<VariableSessionValue> findVariableValuesForSession(GameSession s);

	public EventActivity loadEventActivity(Long activityId);
	public ProgressActivity loadProgressActivity(Long activityId);
	public FileUploadActivity loadFileUploadActivity(Long activityId);
	public MessageActivity loadMessageActivity(Long activityId);
	public FormActivity loadFormActivity(Long activityId);

	public void save(Participant participant);

	public Message loadMessage(Long messageId);
	
	public SystemParameters loadSystemParameters();
	public void save(SystemParameters s);
	
	public List<GameManifest> findManifestsForGameModel(GameModel g);
	public List<GameSession> findGameSessionsForManifest(GameManifest gm);
	public List<GameManifest> findAllManifestsForUser(User currentUser, String textFilter);
	/**
	 * all games that are ready for use (private and owned by the current user, or public)
	 */
	public List<GameModel> findAllReady(User currentUser, String textFilter);
	public List<GameSession> findAllGameSessionsForUser(User currentUser, String textFilter);
	public SessionResource loadSessionResource(Long id);
	public List<GameManifest> findAllManifests(PlaygroundObject playgroundObject);
	public List<GameManifest> findAllManifests(Playground playground);

	public Date startupDate();
	public List<GameManifest> findAllReadyManifests(User currentUser, String textFilter);
	
    public void save(Playground r);
	public Playground loadPlayground(Long playgroundId);
    public List<Playground> findAllPlaygrounds(String textFilter);
    public List<Playground> findAllPublicPlaygrounds();
    public List<Playground> findAllPlaygroundsForUser(User user);
    public Set<Playground> findAllPlaygroundsFormanifest(GameManifest manifest);
    public List<PlaygroundObject> findAllPlaygroundObjects(Long playgroundId, String textFilter);
    
    public List<?> loadPlayground(String uri);
    public Map < String, String > variableValuesForSession (Participant participant, GameSession gameSession);
    public String substituteVariablesInText (Map < String, String > variableValues, String description); // XXX this method can be moved to a helper class/function because it no longer needs the GameManager service
    public String substituteVariablesInText (Participant participant, String description);
}
