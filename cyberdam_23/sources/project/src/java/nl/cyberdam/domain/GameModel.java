package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import nl.cyberdam.util.GameUtil;

import org.hibernate.Hibernate;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * GameModel - since references between the objects contained by this model should
 * be kept correct all getXXX() methods that return collections return an unmodifiable
 * collection, and setters are not available.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gameModel", propOrder = {
	"id",	
    "roles",
    "stepsOfPlay",
    "activities",
    "resources",
    "variables",
    "caption",
    "description",
    "lastModifier",
    "lastModified",
    "initialStepOfPlay",
    "name",
    "statusTemplate",
    "headerTemplate",
    "script",
    "sessionClassicLayout"
})
@Entity
@Table(name = "gamemodel")
public class GameModel extends BaseObject implements java.io.Serializable {
    
	public class MyHashMap<K,V> extends HashMap<BaseObject, BaseObject>{
		public BaseObject get(BaseObject key, boolean b) {
			if (key != null) {
			if (!b) {
				return get(key);
			}
			for (BaseObject bo : keySet()) {
				if (bo.getId().equals(key.getId())) {
					return get(bo);
				}
			}
			}
			return null;
		}
	}
	
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String caption;
    @Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
    @Lob
    private String description;
    
    @XmlTransient
    @Column(nullable = false)
    private Status status = Status.UNDER_CONSTRUCTION;
    
    @XmlTransient
    @ManyToOne
    private User creator;
    
    @XmlTransient
    @ManyToOne
    private User owner;
    
    //@XmlTransient
    @ManyToOne
    private User lastModifier;
    
    //@XmlTransient
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModified;
    
    @XmlTransient
    @Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
    private Date created;

    @Column(length=65535)
    @Lob
    private String statusTemplate;

    @Column(length=65535)
    @Lob
    private String headerTemplate;

    @Column(length=65535)
    @Lob
    private String script;

	private boolean sessionClassicLayout = true;

	@XmlTransient
	@OneToMany
	@JoinColumn(name="gameModel_id")
	private List<GameManifest> gameManifests = new ArrayList<GameManifest>();

    // XXX maybe do the reattaching after all?
    // fetchtype.EAGER is needed, so the findAll method will return GameModels with initialized
    // roles, enabling the pager of the gameauthor.htm to work without having to 
    // reattach all retrieved objects to the current hibernate session.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="gameModel_id", nullable = false)
    @org.hibernate.annotations.IndexColumn(name = "listindex")
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @XmlElement(type = Role.class)
    private List<Role> roles = new ArrayList<Role>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="gameModel_id", nullable = false) 
    @org.hibernate.annotations.IndexColumn(name = "listindex")
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @XmlElement(type = StepOfPlay.class)
    private List<StepOfPlay> stepsOfPlay = new ArrayList<StepOfPlay>();

    // @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="gameModel_id", nullable = false)
    @org.hibernate.annotations.IndexColumn(name = "listindex")
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @XmlElement(type = Activity.class)
    private List<Activity> activities = new ArrayList<Activity>();

    @OneToMany(cascade = CascadeType.ALL)
    @org.hibernate.annotations.IndexColumn(name = "listindex")
    @XmlElement(type = Resource.class)
    private List<Resource> resources = new ArrayList<Resource>();
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="gameModel_id") 
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    @XmlElement(type = Variable.class)
    private List<Variable> variables = new ArrayList<Variable>();

    // reference to the initial step of play
    @ManyToOne
    private StepOfPlay initialStepOfPlay;
    
    /** Creates a new instance of GameModel */
    public GameModel() {
    }

    /**
     * returns an unmodifiable list
     */
    public List<Resource> getResources() {
        return Collections.unmodifiableList(resources);
    }

    /**
     * returns an unmodifiable list
     */
    public List<Activity> getActivities() {
        return Collections.unmodifiableList(activities);
    }
    
    /**
     * returns an unmodifiable list
     */
    public List<StepOfPlay> getStepsOfPlay() {
        return Collections.unmodifiableList(stepsOfPlay);
    }

    /**
     * returns an unmodifiable list
     */
    public List<Role> getRoles() {
        return Collections.unmodifiableList(roles);
    }

    /**
     * returns an unmodifiable list
     */
    public List<Variable> getVariables() {
        return Collections.unmodifiableList(variables);
    }
    
    public void setVariables(List<Variable> variables) {
    	this.variables = variables;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusTemplate() {
        return statusTemplate;
    }

    public void setStatusTemplate(String statusTemplate) {
        this.statusTemplate = statusTemplate;
    }

    public String getHeaderTemplate() {
        return headerTemplate;
    }

    public void setHeaderTemplate(String sessionTemplate) {
        this.headerTemplate = sessionTemplate;
    }

    public void addRole(Role r) {
        roles.add(r);
        r.setIndex(roles.indexOf(r));
    }

    /**
     * if the role is in use by a RoleActivity or MessageActivity object a 
     * ItemInUseException will be thrown.  
     */
    public void removeRole(Role r) {
    	// check if the role is in use in a step of play
    	for(StepOfPlay step: stepsOfPlay) {
    		for(RoleActivity ra: step.getRoleActivities()) {
    			// 20090506: getId() only works if the activity has already been persisted, otherwise use 'normal' equals on the Roles
    			if (r.equals(ra.getRole())) {
//    			if (r.getId().equals(ra.getRole().getId())) {
    				throw new ItemInUseException("role is in use in step of play: " + step.getName(), "role.in.use.by", step.getName());
    			}
    		}
    	}
    	// check if the role is in use in messageactivity
    	for(Activity activity: activities) {
    		if (activity instanceof MessageActivity) {
				MessageActivity messageActivity = (MessageActivity) activity;
				if (messageActivity.getRecipients().contains(r)) {
					throw new ItemInUseException("role is in use in message activity: " + messageActivity.getName(), "role.in.use.by", messageActivity.getName());
				}
				if (activity instanceof FormActivity) {
					FormActivity formActivity = (FormActivity) activity;
					for (ActivityVariable activityVariable: formActivity.getActivityVariables()) {
						for (Variable variable: r.getVariables()) {
							if (variable.getId().equals(activityVariable.getVariable().getId())) {
								throw new ItemInUseException("role has a variable that is in use in form activity: " + formActivity.getName(), "rolevariable.in.use.by", formActivity.getName());
							}
						}
					}
				}
			}
		}
		// if all checks succeed remove the role
		for (StepOfPlay s: stepsOfPlay) {
			for (Iterator<RoleActivity> it = s.getRoleActivities().iterator();it.hasNext();) {
				RoleActivity ra = it.next();
				if (ra.getRole().equals(r)) it.remove();
			}
		}
		roles.remove(r);
	}

    public Role getRoleById(Long roleId) {
        for (Role r : roles) {
            if (roleId.equals(r.getId())) {
                return r;
            }
        }
        throw new RuntimeException("role not found");
    }
    
    public void removeRoleByIndex(int roleIndex) {
        Role r = roles.get(roleIndex);
        removeRole(r);
    }

	public void changeRoleOrder(int oldIndex, int newIndex) {
		Role r = roles.get(oldIndex);
		roles.remove(oldIndex);
		if (oldIndex < newIndex) newIndex--;
		roles.add(newIndex, r);
	}

    public void addStepOfPlay(StepOfPlay s) {
        stepsOfPlay.add(s);
    }

    public void addVariable(Variable v) {
        variables.add(v);
    }

    /**
     * throws ItemInUseException if the step if play is being used
     * by a progressactivity
     */
    public void removeStepOfPlay(StepOfPlay s) {
    	// check if the role is in use in messageactivity
    	
    	for(Activity activity: activities) {
    		if (activity instanceof ProgressActivity) {
    			ProgressActivity progressActivity = (ProgressActivity) activity;
    			// check for both version with id (saved / in hibernate) as version with id == null
    			for (NextStepOfPlay nextStep: progressActivity.getNextStepOfPlayOptions()) {    				
    				if ((s.getId() == null && s.equals(nextStep.getStep())) || (s.getId() != null && nextStep.getStep() != null && s.getId().equals(nextStep.getStep().getId()))) {
    					throw new ItemInUseException("step of play is in use by activity: " + progressActivity.getName(), "stepofplay.in.use.by", progressActivity.getName());
    				}
    			}
    		}
    	}
    	  
    	// check if step of play already has activities added to it
    	if (s.getRoleActivities().size() > 0) {
    		throw new ItemInUseException("step of play has activities assigned to it:.", "stepofplay.has.activities", null);
    	}
    	
    	// check if the role is in use in initial step of play
    	StepOfPlay initialStepOfPlay = getInitialStepOfPlay();
    	// check for both versions that have already been saved and versions that have not been saved
    	if (initialStepOfPlay != null && ((initialStepOfPlay.getId() != null && initialStepOfPlay.getId().equals(s.getId())) || (initialStepOfPlay.getId() == null && initialStepOfPlay.equals(s)))) {
    		throw new ItemInUseException("step of play is in use by initial step of play:.", "stepofplay.is.initialstep", null);
    	}
    	stepsOfPlay.remove(s);
    }

    public void removeStepOfPlayByIndex(int index) {
    	StepOfPlay s = stepsOfPlay.get(index);
        removeStepOfPlay(s);
    }
    
	public void changeStepOrder(int oldIndex, int newIndex) {
		StepOfPlay s = stepsOfPlay.get(oldIndex);
		stepsOfPlay.remove(oldIndex);
		if (oldIndex < newIndex) newIndex--;
		stepsOfPlay.add(newIndex, s);
	}

	public void removeVariableById(Long id) {
		Variable variable = null;
		for (Variable v: variables) {
			if (v.getId().equals(id)) variable = v;
		}
		if (variable == null) throw new RuntimeException("variable not found");
		// disallow removal if the variable is in use 
		for (Activity activity: activities) {
			if (activity instanceof FormActivity) {
				FormActivity formActivity = (FormActivity) activity;
				for (ActivityVariable activityVariable : formActivity.getActivityVariables())
				{
					if (variable.getId().equals(activityVariable.getVariable().getId()))
					{
						throw new ItemInUseException("variable is in use in form activity: " + formActivity.getName(), "variable.in.use.by", formActivity.getName());
					}
				}
			}
		}
		variables.remove(variable);
	}

   public Status getStatus() {
        return status;
    }

    /**
     * when status is PUBLIC or OBSOLETE only LCMS Administrator or a System 
     * Administrator can make changes.
     */
    public void setStatus(Status status) {
        assert status != null;
        
        // check if there is actually something changing
        if (this.status != status) {
        	if ((this.status == Status.PUBLIC || this.status == Status.OBSOLETE) &&
        		! (GameUtil.getCurrentUser().getGameAuthorities().isLcmsAdministrator() || GameUtil.getCurrentUser().getGameAuthorities().isSystemAdministrator()) ) {
        		throw new RuntimeException("user is not allowed to change this value");
        	} else {
        		this.status = status;
        	}
        }
    }
    
    /**
     * returns whether the current user is allowed to change the status of this model
     * @return
     */
    public boolean getStatusEditable() {
    	if ((this.status == Status.PUBLIC || this.status == Status.OBSOLETE) &&
    			! (GameUtil.getCurrentUser().getGameAuthorities().isLcmsAdministrator() || GameUtil.getCurrentUser().getGameAuthorities().isSystemAdministrator()) ) {
    		return false;
    	} else {
    		return true;
    	}
    }

	public Resource getResourceById(Long resourceId) {
		for (Resource r : resources) {
			if (resourceId.equals(r.getId())) {
				return r;
			}
		}
		throw new RuntimeException("resource not found");
	}

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    
    public void addActivity(Activity a) {
        activities.add(a);
    }
    
    /**
     * if activity is in use in a step of play a ItemInUseException 
     * will be thrown
     */
    public void removeActivity(Activity a) {
    	// check if the role is in use in a step of play
    	for(StepOfPlay step: stepsOfPlay) {
    		for(RoleActivity ra: step.getRoleActivities()) {
    			// 20090506: getId() only works if the activity has already been persisted, otherwise use 'normal' equals on the Activities
    			if ((a.getId() != null && a.getId().equals(ra.getActivity().getId())) || (a.getId() == null && a.equals(ra.getActivity()))) {
    				throw new ItemInUseException("activity is in use in step of play: " + step.getName(), "activity.in.use.by", step.getName());
    			}
    		}
    	}
    	// if check was ok remove the activity
		activities.remove(a);
    }
    
    public void removeActivityById(Long activityId) {
        for (Activity a : activities) {
            if (activityId.equals(a.getId())) {
                removeActivity(a);
                break;
            }
        }
    }

    public Activity getActivityById(Long activityId) {
        for (Activity a : activities) {
            if (activityId.equals(a.getId())) {
                return a;
            }
        }
        throw new RuntimeException("activity not found");
    }

	public StepOfPlay getInitialStepOfPlay() {
		return initialStepOfPlay;
	}

	public void setInitialStepOfPlay(StepOfPlay initialStepOfPlay) {
		this.initialStepOfPlay = initialStepOfPlay;
	}

	/**
	 * returns whether the current user may edit this gamemodel 
	 */
	public boolean isEditable() {
		User u = GameUtil.getCurrentUser();
		if (u.equals(getOwner())) {
			return true;
		}
		if (u.getGameAuthorities().isSystemAdministrator() || u.getGameAuthorities().isLcmsAdministrator()) {
			return true;
		}
		return false;
	}
	
	/**
	 * returns quite a deep clone of the gamemodel
	 * Not cloned are: 
	 * <ul>
	 * <li> the owner (will be reference to new owner)
	 * <li> status (will always be UNDER_CONSTRUCTION for the clone)
	 * <li> lastModified, lastModifier (will be NOW and the new owner)
	 * </ul>
	 */
	public GameModel copy(User newOwner, boolean isImport) { 
		GameModel copy = new GameModel();
		copy.setName(name);
		copy.setCaption(caption);
		copy.setDescription(description);
		
		// Meta data
//		copy.setUri(getUri());
//		copy.setLanguage(getLanguage());
//		copy.setBackground(getBackground());
//		copy.setKeywords(getKeywords());
//		copy.setContribution(getContribution());
//		copy.setSize(getSize());
		// 
		
		if (isImport) {
			copy.setCreated(new Date());
			copy.setCreator(newOwner);
		} else {
			copy.setCreated(created);
			copy.setCreator(creator);
		}
		
		copy.setOwner(newOwner);
		copy.setLastModifier(newOwner);
		copy.setLastModified(new Date());
		copy.setHeaderTemplate(headerTemplate);
		copy.setStatusTemplate(statusTemplate);
		copy.setScript(getScript());
		
		// lists of references between original and copy
		MyHashMap<Role, Role> old2newRoles = new MyHashMap<Role, Role>();
		MyHashMap<Activity, Activity> old2newActivities = new MyHashMap<Activity, Activity>();
		MyHashMap<Resource, Resource> old2newResources = new MyHashMap<Resource, Resource>();
		MyHashMap<StepOfPlay, StepOfPlay> old2newStepsOfPlay = new MyHashMap<StepOfPlay, StepOfPlay>();
		MyHashMap<Variable, Variable> old2newVariable = new MyHashMap<Variable, Variable>();

		for (Role role: getRoles()) {
			Role newRole = role.copy();

			old2newRoles.put(role, newRole);
			
			for (Variable variable: role.getVariables()) {
				Variable newVariable = variable.copy();
				old2newVariable.put(variable, newVariable);
				newRole.addVariable(newVariable);
			}
			// add role to the copy
			copy.addRole(newRole);
		}
		
		for (Resource resource: getResources()) {
			Resource newResource = resource.copy(newOwner);
			
			old2newResources.put(resource, newResource);
			
			copy.addResource(newResource);
		}
		
		// create mapping of old to new steps
		for (StepOfPlay step : getStepsOfPlay()) {
			StepOfPlay copiedStep = new StepOfPlay();
			old2newStepsOfPlay.put(step, copiedStep);
		}
		
		for (Variable variable: getVariables()) {
			Variable newVariable = variable.copy();
			old2newVariable.put(variable, newVariable);
			copy.addVariable(newVariable);
		}

		for (Activity activity: getActivities()) {
			// copy handles all but resources
			Activity newActivity;

			// 3 special cases for the different types of activities
			if (activity instanceof FileUploadActivity) {
				FileUploadActivity newFileUploadActivity = (FileUploadActivity) activity.copy();
				newActivity = newFileUploadActivity;
				
			} else if (activity instanceof MessageActivity) {
				MessageActivity messageActivity = (MessageActivity) activity;
				MessageActivity newMessageActivity = (MessageActivity) activity.copy();
				
				for(Role recipient: messageActivity.getRecipients()) {
					// get matching role in the new model
						Role newRecipient = (Role)old2newRoles.get(recipient, isImport);
					newMessageActivity.getRecipients().add(newRecipient);
				}
				newActivity = newMessageActivity;
				
			} else if (activity instanceof ProgressActivity) {
				ProgressActivity progressActivity = (ProgressActivity) activity;
				ProgressActivity newProgressActivity = (ProgressActivity) activity.copy();
				// clear default options since we add new ones
				newProgressActivity.getNextStepOfPlayOptions().clear();
				
				for(NextStepOfPlay nextStep: progressActivity.getNextStepOfPlayOptions()) {
					NextStepOfPlay newNextStep = new NextStepOfPlay();
					newNextStep.setDescription(nextStep.getDescription());
					// set reference to next step
					StepOfPlay s = (StepOfPlay)old2newStepsOfPlay.get(nextStep.getStep(), isImport);
					newNextStep.setStep(s);
					
					newProgressActivity.getNextStepOfPlayOptions().add(newNextStep);
				}
				
				newActivity = newProgressActivity;
			} else if (activity instanceof FormActivity) {
				FormActivity formActivity = (FormActivity) activity;
				FormActivity newFormActivity = formActivity.copy();

				for(ActivityVariable activityVariable: formActivity.getActivityVariables()) {
					ActivityVariable newActivityVariable = activityVariable.copy();
					// set reference to next step
					Variable v = (Variable)old2newVariable.get(activityVariable.getVariable(), isImport);
					if (v == null) {
						// this must be a systemvar: not changed
						v = activityVariable.getVariable();
					}
					newActivityVariable.setVariable(v);

					newFormActivity.getActivityVariables().add(newActivityVariable);
				}

				newActivity = newFormActivity;
			} else if (activity instanceof EventActivity) {
				EventActivity newEventActivity = (EventActivity) activity.copy();
				newActivity = newEventActivity;
				
			} else {
				// if the activity is of any other kind we throw an exception since it should not be here
				throw new RuntimeException("Unexpected type of activity: " + activity.getClass().toString());
			}
			
			// set the correct references to resources
			for (Resource resource: activity.getAttachments()) {
				// find the new resource that corresponds to the old resource
				Resource r = (Resource)old2newResources.get(resource, isImport);
				newActivity.getAttachments().add(r);
			}
			
			
			old2newActivities.put(activity, newActivity);

			// add activity to the copy
			copy.addActivity(newActivity);
		}
		
		for (StepOfPlay step : getStepsOfPlay()) {
			
			// StepOfPlay copiedStep = new StepOfPlay();
			StepOfPlay copiedStep = (StepOfPlay)old2newStepsOfPlay.get(step);
			
			copiedStep.setName(step.getName());
			copiedStep.setScript(step.getScript());
			
			// find the roles / activities the step is referring to
			for (RoleActivity roleActivity : step.getRoleActivities()) {
				// get the references to the right role and activity
				Role r = (Role)old2newRoles.get(roleActivity.getRole(), isImport);
				Activity a = (Activity)old2newActivities.get(roleActivity.getActivity(), isImport);
				
				RoleActivity copiedRoleActivity = new RoleActivity();
				copiedRoleActivity.setRole(r);
				copiedRoleActivity.setActivity(a);
				
				copiedStep.getRoleActivities().add(copiedRoleActivity);
			}
			
			copy.addStepOfPlay(copiedStep);
			
			if (isImport) {
				if (getInitialStepOfPlay()!=null&&step.getId().equals(getInitialStepOfPlay().getId())) {
						copy.setInitialStepOfPlay(copiedStep);
				}
			} else {
			// copy the initialStepOfPlay
			if (step.equals(getInitialStepOfPlay())) {
				copy.setInitialStepOfPlay(copiedStep);
			}
			}
		}
		
		return copy;
	}
	
	public void addCopyOfActivity(Activity a, String newActivityName) {
		Activity newActivity;
		if (a instanceof FileUploadActivity) {
			FileUploadActivity newFileActivity = (FileUploadActivity) a.copy();
			newActivity = newFileActivity;
		} else if (a instanceof MessageActivity) {
			MessageActivity ma = (MessageActivity) a;
			MessageActivity newMessageActivity = ma.copy();
			for (Role role: ma.getRecipients()) {
				newMessageActivity.getRecipients().add(role);
			}
			newActivity = newMessageActivity;
		} else if (a instanceof ProgressActivity) {
			ProgressActivity pa = (ProgressActivity) a;
			ProgressActivity newProgressActivity = pa.copy();
			// clear default options since we add new ones
			newProgressActivity.getNextStepOfPlayOptions().clear();
			for (NextStepOfPlay nextStep: pa.getNextStepOfPlayOptions()) {
				NextStepOfPlay newNextStep = new NextStepOfPlay();
				newNextStep.setDescription(nextStep.getDescription());
				newNextStep.setStep(nextStep.getStep());
				newProgressActivity.getNextStepOfPlayOptions().add(newNextStep);
			}
			newActivity = newProgressActivity;
		} else if (a instanceof FormActivity) {
			FormActivity formActivity = (FormActivity) a;
			FormActivity newFormActivity = formActivity.copy();
			for(ActivityVariable activityVariable: formActivity.getActivityVariables()) {
				ActivityVariable newActivityVariable = activityVariable.copy();
				newActivityVariable.setVariable(activityVariable.getVariable());
				newFormActivity.getActivityVariables().add(newActivityVariable);
			}
			newActivity = newFormActivity;
		} else if (a instanceof EventActivity) {
			EventActivity newEventActivity = (EventActivity) a.copy();
			newActivity = newEventActivity;
		} else {
			throw new RuntimeException("unexpected activity type: " + a.getClass());
		}
		for (Resource resource: a.getAttachments()) {
			newActivity.getAttachments().add(resource);
		}
		newActivity.setName(newActivityName);
		addActivity(newActivity);
	}

	/**
	 * delegates to copy(user, false)
	 * 
	 * @param newOwner
	 * @return
	 */
	public GameModel copy(User newOwner) {
		return copy(newOwner, false);
	}
	
	public void addResource(Resource r) {
		resources.add(r);
	}

	/**
	 * an ItemInUseException will be thrown if the resource is used anywhere in the
	 * GameModel. 
	 */
	public void removeResource(Resource r) {
		// check if resource is in use
		for(Activity activity: activities) {
			if (activity.getAttachments() != null && activity.getAttachments().contains(r)) {
				throw new ItemInUseException("Resource is in use by activity: " + activity.getName(), "resource.in.use.by", activity.getName());
			}
		}
		resources.remove(r);
	}

	public void removeResourceById(Long id) {
		Resource resource = null;
		for (Resource r: resources) {
			if (r.getId().equals(id)) resource = r;
		}
		removeResource(resource);
	}

	public void initialize() {
		Hibernate.initialize(this);
		Hibernate.initialize(this.getStepsOfPlay());
		Hibernate.initialize(this.getRoles());
		Hibernate.initialize(this.getActivities());
		Hibernate.initialize(this.getResources());
		Hibernate.initialize(this.getVariables());
		Hibernate.initialize(this.getGameManifests());
		// initialize all collections that are linked from the activities
		for(Activity activity: this.getActivities()) {
			Hibernate.initialize(activity.getAttachments());
			if (activity instanceof MessageActivity) {
				Hibernate.initialize(((MessageActivity) activity).getRecipients());
			}
			if (activity instanceof FormActivity) {
				Hibernate.initialize(((FormActivity) activity).getActivityVariables());
			}
			if (activity instanceof ProgressActivity) {
				Hibernate.initialize(((ProgressActivity) activity).getNextStepOfPlayOptions());
			}
		}
		for(StepOfPlay step: this.getStepsOfPlay()) {
			Hibernate.initialize(step.getRoleActivities());
		}
		for(Resource resource: this.getResources()) {
			Hibernate.initialize(resource);
		}
		for(Variable variable: this.getVariables()) {
			Hibernate.initialize(variable);
		}
		for(Role role: this.getRoles()) {
			Hibernate.initialize(role.getVariables());
			for(Variable variable: role.getVariables()) {
				Hibernate.initialize(variable);
			}
		}
	}

	public String escapeDoubleQuoteAndSlash(String str) {
		int length = str.length();
		StringBuffer buffer = new StringBuffer(length * 2);
		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if (ch == '"' || ch == '\\') {
				buffer.append('\\');
			}
			buffer.append(ch);
		}
		return buffer.toString();
	}

	public String escapeQuote(String str) {
		int length = str.length();
		StringBuffer buffer = new StringBuffer(length * 2);
		for (int i = 0; i < length; i++) {
			char ch = str.charAt(i);
			if (ch == '\'') {
				buffer.append('\\');
			}
			buffer.append(ch);
		}
		return buffer.toString();
	}

	public JSONObject mapToGridData() {
		JSONObject json = new JSONObject();
		try {
			JSONArray steps = new JSONArray();
			for (StepOfPlay s : this.getStepsOfPlay()) {
				JSONObject step = new JSONObject();
				step.put("name", this.escapeDoubleQuoteAndSlash(s.getName()));
				JSONArray activitiesarray = new JSONArray();
				for (Role r : this.getRoles()) {
					JSONArray activities = new JSONArray();
					for (RoleActivity ra : s.getRoleActivities()) {
						if (ra.getRole().equals(r)) {
							JSONObject activity = new JSONObject();
							activity.put("id", ra.getActivity().getId());
							activity.put("name", this.escapeDoubleQuoteAndSlash(ra.getActivity().getName()));
							activities.put(activity);
						}
					}
					activitiesarray.put(activities);
				}
				step.put("activitiesarray", activitiesarray);
				steps.put(step);
			}
			json.put("steps",steps);
		} catch (JSONException e) {
		}
		return json;
	}

	public void mapFromGridData(JSONObject json) throws JSONException {
		JSONArray steps = json.getJSONArray("steps");
		for (int stepIndex = 0;stepIndex < steps.length();stepIndex++) {
			StepOfPlay s = this.getStepsOfPlay().get(stepIndex);
			JSONObject step = steps.getJSONObject(stepIndex);
			s.setName(step.getString("name"));
			s.getRoleActivities().clear();
			JSONArray activitiesarray = step.getJSONArray("activitiesarray");
			for (int roleIndex = 0; roleIndex < activitiesarray.length();roleIndex++) {
				Role r = this.getRoles().get(roleIndex);
				JSONArray activities = activitiesarray.getJSONArray(roleIndex);
				for (int activityIndex = 0; activityIndex < activities.length();activityIndex++) {
					Long activityId = new Long(activities.getJSONObject(activityIndex).getLong("id"));
					Activity activity = this.getActivityById(activityId);
					RoleActivity ra = new RoleActivity(r, activity);
					s.getRoleActivities().add(ra);
				}
			}
		}
	}

	public User getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public void setStepsOfPlay(List<StepOfPlay> stepsOfPlay) {
		this.stepsOfPlay = stepsOfPlay;
	}

	public void setScript(String script) {
		this.script = script;
	}

	public String getScript() {
		return script;
	}

	public boolean isSessionClassicLayout() {
		return sessionClassicLayout;
	}

	public void setSessionClassicLayout(boolean sessionClassicLayout) {
		this.sessionClassicLayout = sessionClassicLayout;
	}
	public List<GameManifest> getGameManifests() {
		return gameManifests;
	}

	public void setGameManifests(List<GameManifest> gameManifests) {
		this.gameManifests = gameManifests;
	}
}
