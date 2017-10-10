package nl.cyberdam.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.persistence.JoinTable;

/**
 * represents a participant in a game session - it can be associated with multiple users
 */
@Entity(name="participant")
public class Participant implements Serializable {

    private Long id;
    private RoleToPlaygroundMapping roleAndPlayground;
    private Set<User> users = new HashSet<User>();
    private MessageBox inbox = new MessageBox();
    private MessageBox trash = new MessageBox();
    private MessageBox outbox = new MessageBox();
    private MessageBox outtrash = new MessageBox();
    private GameSession gameSession;
    private boolean notifyNewStepOfPlay = false;
    private Set<Activity> completedActivities = new HashSet<Activity>();
    private SessionResourceBox sessionResourceBox = new SessionResourceBox();
    private SessionResourceBox sessionResourceTrash = new SessionResourceBox();
    private String value1;
    private String value2;
    private String value3;
    private String value4;
    private String value5;
    private List<SessionResource> sessionResources;
    private Set<Activity> enabledActivities = new HashSet<Activity>(0);

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "participant_activityavailable", joinColumns = { @JoinColumn(name = "participant_id") }, inverseJoinColumns = { @JoinColumn(name = "activity_id") })
    public Set<Activity> getEnabledActivities() {
    	return this.enabledActivities;
    }
    public void setEnabledActivities(Set<Activity> activities) {
    	this.enabledActivities = activities;
    }
	/**
	 * checks if the activity is still enabled (even after being completed)
	 */
	public boolean checkActivityEnabled(Activity a) {
		return enabledActivities.contains(a);
	}
	public void enableActivity(Activity a) {
		enabledActivities.add (a);
	}
	public void removeEnabledActivity(Activity a) {
		enabledActivities.remove (a);
	}
	

    @OneToMany
    @JoinColumn(name="participant_id", insertable=false, updatable=false)
    public List<SessionResource> getSessionResources() {
        return sessionResources;
    }

    public void setSessionResources(List<SessionResource> sessionResources) {
        this.sessionResources = sessionResources;
    }


    /**
     * default constructor
     */
    public Participant() {
    }    

    /**
     * convenience constructor - you only need to add users after this.
     */
    Participant(RoleToPlaygroundMapping r, GameSession gs) {
    	setRoleAndPlayground(r);
    	setGameSession(gs);
    }
    
    /**
     * convenience constructor - adds the user to the list of users.
     */
    Participant(RoleToPlaygroundMapping r, User user, GameSession gs) {
    	setRoleAndPlayground(r);
    	addUser(user);
    	setGameSession(gs);
    }
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
    	return id;
    }
    
    public void setId(Long id) {
    	this.id = id;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public SessionResourceBox getSessionResourceBox() {
		return sessionResourceBox;
	}

	public void setSessionResourceBox(SessionResourceBox sessionResourceBox) {
		this.sessionResourceBox = sessionResourceBox;
	}

	@OneToOne(cascade=CascadeType.ALL)
	public SessionResourceBox getSessionResourceTrash() {
		return sessionResourceTrash;
	}

	public void setSessionResourceTrash(SessionResourceBox sessionResourceTrash) {
		this.sessionResourceTrash = sessionResourceTrash;
	}

	@Column(nullable=false)
	public boolean isNotifyNewStepOfPlay() {
		return notifyNewStepOfPlay;
	}

	public void setNotifyNewStepOfPlay(boolean notifyNewStepOfPlay) {
		this.notifyNewStepOfPlay = notifyNewStepOfPlay;
	}

	// no cascade - users should be saved on their own
    @ManyToMany
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
    
    public void addUser(User u) {
    	getUsers().add(u);
    }

    @ManyToOne
    public RoleToPlaygroundMapping getRoleAndPlayground() {
        return roleAndPlayground;
    }

    public void setRoleAndPlayground(RoleToPlaygroundMapping roleAndPlayground) {
        this.roleAndPlayground = roleAndPlayground;
    }
    
    @OneToOne(cascade=CascadeType.ALL)
    public MessageBox getInbox() {
        return inbox;
    }

    public void setInbox(MessageBox inbox) {
        this.inbox = inbox;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public MessageBox getOutbox() {
        return outbox;
    }

    public void setOutbox(MessageBox outbox) {
        this.outbox = outbox;
    }

    @OneToOne(cascade=CascadeType.ALL)
    public MessageBox getTrash() {
        return trash;
    }

    public void setTrash(MessageBox trash) {
        this.trash = trash;
    }
    
	public void setGameSession(GameSession gameSession) {
		this.gameSession = gameSession;
	}

	@ManyToOne
	public GameSession getGameSession() {
		return gameSession;
	}
	
	/**
	 * @return the activities for this user for the current step - both 
	 *         those that have already been done and the ones that still
	 *         have to be performed.
	 *         If there is no current step null will be returned
	 */
	@Transient
	public List<Activity> getActivitiesForCurrentStep() {
		StepOfPlay currentStep = getGameSession().getCurrentStepOfPlay();
		if (currentStep != null) {
			List<Activity> activitiesSource = currentStep.getActivitiesForRole(getRoleAndPlayground().getRole());
			List<Activity> activities = new LinkedList<Activity> ();
			for (Activity activity : activitiesSource)
			{
				activities.add (activity);
			}
			return activities;
		} else {
			return null;
		}
	}

	@Column(length=65535)
	@Lob
	public String getValue1() {
	    return value1;
	}

	public void setValue1(String value1) {
	    this.value1 = value1;
	}

	@Column(length=65535)
	@Lob
	public String getValue2() {
	    return value2;
	}

	public void setValue2(String value2) {
	    this.value2 = value2;
	}

	@Column(length=65535)
	@Lob
	public String getValue3() {
	    return value3;
	}

	public void setValue3(String value3) {
	    this.value3 = value3;
	}

	@Column(length=65535)
	@Lob
	public String getValue4() {
	    return value4;
	}

	public void setValue4(String value4) {
	    this.value4 = value4;
	}

	@Column(length=65535)
	@Lob
	public String getValue5() {
	    return value5;
	}

	
	public void setValue5(String value5) {
	    this.value5 = value5;
	}

	@ManyToMany(fetch=FetchType.EAGER)
	public Set<Activity> getCompletedActivities() {
		return completedActivities;
	}

	public void setCompletedActivities(Set<Activity> completedActivities) {
		this.completedActivities = completedActivities;
	}
	
	/**
	 * checks if the activity has already been completed
	 */
	public boolean checkActivityCompleted(Activity a) {
		return completedActivities.contains(a);
	}
	
	/**
	 * puts the activity in the 'completed' list
	 */
	public void setActivityCompleted(Activity a) {
		getCompletedActivities().add(a);
	}

	/**
	 * adds an activity to the list of completed activities.
	 * 
	 * @param activity
	 */
	public void addCompletedActivity(Activity activity) {
		getCompletedActivities().add(activity);
		
	}

	@OneToOne(cascade=CascadeType.ALL)
	public MessageBox getOuttrash() {
		return outtrash;
	}

	public void setOuttrash(MessageBox outtrash) {
		this.outtrash = outtrash;
	}

	void resetActivitiesForCurrentStep() {
		StepOfPlay currentStep = getGameSession().getCurrentStepOfPlay();
		if (currentStep != null) {
			List<Activity> activities = currentStep.getActivitiesForRole(getRoleAndPlayground().getRole());
			getCompletedActivities().removeAll(activities);
			getEnabledActivities().removeAll(activities);
		}
	}
	
	
}
