package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import nl.cyberdam.util.GameUtil;

import org.hibernate.Hibernate;

/**
 *
 */
@Entity
@Table(name = "gamemanifest")
public class GameManifest implements java.io.Serializable {
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="gameManifest_id")
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    private List<RoleToPlaygroundMapping> rolesToPlaygroundObjects = new ArrayList<RoleToPlaygroundMapping>();
    @ManyToOne
    private GameModel gameModel;
    @ManyToOne
	private User creator;
    @ManyToOne
	private User lastModifier;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
	private Date created;
    @ManyToOne
    private User owner;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(nullable = false)
    private Status status = Status.UNDER_CONSTRUCTION;
    @OneToMany
    @JoinColumn(name="manifest_id")
    private List<GameSession> gameSessions = new ArrayList<GameSession>();

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
    /** Creates a new instance of GameManifest */
    public GameManifest() {
    }

    
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

    public List<RoleToPlaygroundMapping> getRolesToPlaygroundObjects() {
        return rolesToPlaygroundObjects;
    }

    public void setRolesToPlaygroundObjects(List<RoleToPlaygroundMapping> rolesToPlaygroundObjects) {
        this.rolesToPlaygroundObjects = rolesToPlaygroundObjects;
    }

    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
    	this.gameModel = gameModel;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
    	// XXX this is not in the spec but I suppose it's good style to have it anyway
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
	 * returns whether the current user may edit this game manifest 
	 */
    @Transient
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
     * returns whether the current user is allowed to change the status of this manifest
     */
    @Transient
    public boolean isStatusEditable() {
    	if ((this.status == Status.PUBLIC || this.status == Status.OBSOLETE) &&
    			! (GameUtil.getCurrentUser().getGameAuthorities().isLcmsAdministrator() || GameUtil.getCurrentUser().getGameAuthorities().isSystemAdministrator()) ) {
    		return false;
    	} else {
    		return true;
    	}
    }

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
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
	
	public List<GameSession> getGameSessions() {
		return gameSessions;
	}

	public void setGameSessions(List<GameSession> gameSessions) {
		this.gameSessions = gameSessions;
	}

	/**
	 * @return a copy of the manifest, owned by the new owner, with status UNDER_CONSTRUCTION
	 */
	public GameManifest copy(User newOwner) {
		GameManifest copy = new GameManifest();
		copy.setName(name);
		copy.setCreated(created);
		copy.setCreator(creator);
		
		copy.setOwner(newOwner);
		copy.setLastModifier(newOwner);
		copy.setLastModified(new Date());
		
		copy.setStatus(Status.UNDER_CONSTRUCTION);
		
		// setGameModel also sets the rolestoplaygroundobjects array, but we'll 
		// reset that one, to make it a real copy of the current manifest
		copy.setGameModel(getGameModel());
		
		// copy the links between roles and playground objects.
		List<RoleToPlaygroundMapping> mappingList = new ArrayList<RoleToPlaygroundMapping>();
        // add new elements
        for( RoleToPlaygroundMapping rtp: getRolesToPlaygroundObjects()) {
        	RoleToPlaygroundMapping rtpCopy = new RoleToPlaygroundMapping(copy, rtp.getRole(), rtp.getPlaygroundObject(), rtp.getPlayground());
            mappingList.add(rtpCopy);
        }
        copy.setRolesToPlaygroundObjects(mappingList);
		
		return copy;
	}
	
	public void initialize() {
		Hibernate.initialize(this.getGameSessions());
	}
}
