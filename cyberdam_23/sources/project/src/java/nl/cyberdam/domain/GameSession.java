package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import nl.cyberdam.service.ScriptManager;
import nl.cyberdam.util.GameUtil;

/**
 * Control using initialize(), markready(), start(), 
 *
 */
@Entity
@Table(name = "gamesession")
public class GameSession extends BaseObject implements java.io.Serializable {

 /*
 In preparation (session has a name, a manifest and a game master) 
 Ready-to-start (session has participants allocated to roles) 
 Running (session is running) 
 Finished (session is finished in normal way) 
 Aborted (session is finished in abnormal way) 
 Cancelled (session is cancelled before it was running) 
 */
   
    private Long id;
    private String name;
    private SessionStatus status;
	private User creator;
	private User lastModifier;
	private Date created;
	private Date lastModified;
    private User owner;
    private Date currentStatusStarted;
    private Date runningStarted;
    private Date sessionStopped;
    private StepOfPlay currentStepOfPlay;
    private GameManifest manifest;
    private List<Participant> participants;
    private List<VariableSessionValue> sessionVariableValues;
	private List<SessionReportLogItem> sessionReportLogItems;
	private List<SessionPlaygroundObject> sessionPlaygroundObjects;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="gamesession_id", insertable=false, updatable=false)
	public List<SessionPlaygroundObject> getSessionPlaygroundObjects() {
		return sessionPlaygroundObjects;
	}

	public void setSessionPlaygroundObjects(
			List<SessionPlaygroundObject> sessionPlaygroundObjects) {
		this.sessionPlaygroundObjects = sessionPlaygroundObjects;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="sessionId", insertable=false, updatable=false)
	public List<SessionReportLogItem> getSessionReportLogItems() {
		return sessionReportLogItems;
	}

	public void setSessionReportLogItems(
		List<SessionReportLogItem> sessionReportLogItems) {
		this.sessionReportLogItems = sessionReportLogItems;
	}

    /** Creates a new instance of GameSession */
    public GameSession() {
    }

    @OneToMany(cascade = CascadeType.ALL)
    public List<Participant> getParticipants() {
        return participants;
    }

    /**
     * if one of the participants doesn't have an associated user the owner of the session will
     * be put in that place.
     */
    public void setParticipants(List<Participant> rolesToParticipants) {
    	// check users
    	for (Participant p: rolesToParticipants) {
    		// if there is no user set it to the owner of the game
    		if (p.getUsers().isEmpty()) {
    			p.getUsers().add(getOwner());
    		}
    	}
        this.participants = rolesToParticipants;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="gamesession_id", insertable=false, updatable=false)
    public List<VariableSessionValue> getSessionVariableValues() {
        return sessionVariableValues;
    }

    public void setSessionVariableValues(List<VariableSessionValue> sessionVariableValues) {
        this.sessionVariableValues = sessionVariableValues;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getRunningStarted() {
        return runningStarted;
    }

    public void setRunningStarted(Date runningStarted) {
        this.runningStarted = runningStarted;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getSessionStopped() {
        return sessionStopped;
    }

    public void setSessionStopped(Date sessionStopped) {
        this.sessionStopped = sessionStopped;
    }
    
    @ManyToOne
    public GameManifest getManifest() {
        return manifest;
    }

    /**
     * if a manifest other than the current one is set the list of role to participant mappings
     * should be initialized - see initializeParticipantMapping().
     * 
     * @param manifest
     */
    public void setManifest(GameManifest manifest) {
        this.manifest = manifest;
    }
    
    /**
     * this method should be executed once after setting the manifest! it initializes the gamesession
     * <ul>
     * <li>initializes role to participant mapping</li>
     * <li>set status to Status.IN_PREPARATION</li>
     * <li>set currentStatusStarted to the current time</li>
     * </ul>
     */
    public void initialize() {
    	// XXX do we need cleanup of old mappings?
    	
        // update mapping
		List<Participant> mappingList = getParticipants();
		// remove old elements
		mappingList = new ArrayList<Participant>();
		// add new elements
		for (RoleToPlaygroundMapping r : manifest.getRolesToPlaygroundObjects()) {
			mappingList.add(new Participant(r, getOwner(), this));
		}
		setParticipants(mappingList);
		
		setStatus(SessionStatus.IN_PREPARATION);
		setCurrentStatusStarted(new Date());
		setCurrentStepOfPlay(getManifest().getGameModel().getInitialStepOfPlay());
    }
    
    /**
     * marking the game as ready is only allowed in status IN_PREPARATION
     */
    public void markready() {
    	if (SessionStatus.IN_PREPARATION.equals(getStatus())) {
    		setStatus(SessionStatus.READY_TO_START);
    		setCurrentStatusStarted(new Date());
    	}
    }
    
    /**
     * starting is only allowed if current status is READY_TO_START
     */
    public void start(ScriptManager scriptManager) {
    	if (SessionStatus.READY_TO_START.equals(getStatus())) {
    		String script = getManifest().getGameModel().getScript();
    		if (script != null)
    		{
    			scriptManager.RunScript(script, null, this);
    		}
    		script = getCurrentStepOfPlay().getScript();
			if (script != null)
			{
    			scriptManager.RunScript(script, null, this);
			}
    		setStatus(SessionStatus.RUNNING);
    		Date now = new Date();
    		setCurrentStatusStarted(now);
    		setRunningStarted(now);
    	}
    }
    
    public void finish() {
    	setStatus(SessionStatus.FINISHED);
    	setSessionStopped(new Date());
    }
    
    public void cancel() {
    	setStatus(SessionStatus.CANCELLED);
    }
    
    public void abort() {
    	setStatus(SessionStatus.ABORTED);
    	setSessionStopped(new Date());
    }

    public void preparation() {
    	setStatus(SessionStatus.IN_PREPARATION);
    }
    
    public void delete() {
        setStatus(SessionStatus.DELETED);
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCurrentStatusStarted() {
        return currentStatusStarted;
    }

    private void setCurrentStatusStarted(Date currentStatusStarted) {
        this.currentStatusStarted = currentStatusStarted;
    }

    @OneToOne
    public StepOfPlay getCurrentStepOfPlay() {
        return currentStepOfPlay;
    }

    public void setCurrentStepOfPlay(StepOfPlay currentStepOfPlay) {
        this.currentStepOfPlay = currentStepOfPlay;
    }

    public SessionStatus getStatus() {
        return status;
    }

    private void setStatus(SessionStatus status) {
        this.status = status;
    }
    
    @ManyToOne
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * return a list of Participant for a list of Role.
     * 
     * @param recipients
     * @return
     */
	public List<Participant> getRolesToParticipantsForRoles(
			List<Role> recipients) {
		List<Participant> r2pRecipients = new ArrayList<Participant>();
		
		// go through list.
		for(Participant participant: getParticipants()) {
			if (recipients.contains(participant.getRoleAndPlayground().getRole())) {
				r2pRecipients.add(participant);
			}
		}
		
		return r2pRecipients;
	}

	/**
	 * this is the public version of setCurrentStepOfPlay()
	 * if the next step does not have any activities the game will be marked 'finished'
	 * all activities that participants should perform in the new step will be marked 'unfinished' 
	 * 
	 * @param nextStep
	 */
	public void gotoStep(StepOfPlay nextStep) {
		setCurrentStepOfPlay(nextStep);
		for (Participant p: getParticipants()) {
			p.resetActivitiesForCurrentStep();
		}
		if (nextStep.getRoleActivities().isEmpty()) {
			finish();
		}
	}
	
	/**
	 * Number of users participating in this session - this is something different than the number
	 * of participants, it can be lower or higher.
	 */
	@Transient
	public int getTotalUsers() {
		Set<User> users = new HashSet<User>();
		for(Participant p: getParticipants()) {
			users.addAll(p.getUsers());
		}
		return users.size();
	}
	
	/**
	 * returns whether the current user may edit this game session 
	 */
    @Transient
	public boolean isEditable() {
		User u = GameUtil.getCurrentUser();
		if (u.equals(getOwner())) {
			return true;
		}
		if (u.getGameAuthorities().isSystemAdministrator() || u.getGameAuthorities().isLmsAdministrator() || u.getGameAuthorities().isVleAdministrator()) {
			return true;
		}
		return false;
	}

    @ManyToOne
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@ManyToOne
	public User getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(javax.persistence.TemporalType.TIMESTAMP)
	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	/**
	 * @return a new GameSession owned by the new owner, with status IN_PREPARATION
	 */
	public GameSession copy (User newOwner) {
		GameSession copy = new GameSession();
		copy.setName(name);
		copy.setCreated(created);
		copy.setCreator(creator);
		
		copy.setOwner(newOwner);
		copy.setLastModifier(newOwner);
		copy.setLastModified(new Date());
		
		copy.setStatus(SessionStatus.IN_PREPARATION);
		//setCurrentStatusStarted(new Date());
		copy.setCurrentStatusStarted(new Date());
		
		copy.setManifest(manifest);
		
		// create mapping
		List<Participant> mappingList = new ArrayList<Participant>();
		// copy participants
		for (Participant p : getParticipants()) {
			Participant pCopy = new Participant(p.getRoleAndPlayground(), copy);
			pCopy.setValue1(p.getValue1());
			pCopy.setValue2(p.getValue2());
			pCopy.setValue3(p.getValue3());
			pCopy.setValue4(p.getValue4());
			pCopy.setValue5(p.getValue5());
			// add users
			for (User u : p.getUsers()) {
				pCopy.addUser(u);
			}
			mappingList.add(pCopy);
		}
		copy.setParticipants(mappingList);
		
		//setCurrentStepOfPlay(manifest.getGameModel().getInitialStepOfPlay());
		copy.setCurrentStepOfPlay(getManifest().getGameModel().getInitialStepOfPlay());
		
		return copy;
	}
	
	/**
	 * @return the number of activities for all the participants together
	 */
	@Transient
	public int getNrOfActivitiesForCurrentStep() {
		// add all activities of all participants
		int count = 0;
		StepOfPlay currentStep = getCurrentStepOfPlay();
		if (currentStep != null) {
			for (Participant p: getParticipants()) {
				count += currentStep.getActivitiesForRole(p.getRoleAndPlayground().getRole()).size();
			}
		} else {
			return 0;
		}
		return count;
	}
	
	/**
	 * @return the number of activities that have been completed by all the participants together
	 */
	@Transient
	public int getNrOfCompletedActivities() {
		int count = 0;
		for (Participant p: getParticipants()) {
			count += p.getCompletedActivities().size();
		}
		return count;
	}
}
