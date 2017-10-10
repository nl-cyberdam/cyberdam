package nl.cyberdam.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import nl.cyberdam.util.GameUtil;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.NotEmpty;
import org.hibernate.validator.NotNull;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "playgroundObject", propOrder = {
	"id",
    "address",
    "caption",
    "category",
    "description",
    "inDirectory",
    "name",
    "onMap",
    "picture",
    "thumbnail",
    "uri",
    "url",
    "status",
    "x",
    "y"
})
/**
 * Representation of a playground object - also contains hibernate annotations.
 */
@Entity
@Table(name="playgroundobject")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PlaygroundObject extends BaseObject implements java.io.Serializable {

	public enum Category {TBD, GOVERNMENT_BODY, UTILITY, ENTERPRISE, ASSOCIATION, PRIVATE_HOUSEHOLD, PHOTO_SHOT};
	
	private Long id;
	// private Point location;

	private Integer x;
	private Integer y;
	private String name;
	private String uri;
	private String address;
	private String caption;
	private String url;
	
	
	@XmlTransient
	private User creator;
	@XmlTransient
	private User lastModifier;
	@XmlTransient
	private Date created;
	@XmlTransient
	private Date lastModified;
	@XmlTransient
	private User owner;
	@XmlTransient
	private Playground playground;
	
	private Status status;
	private Category category;
	private String description;
	private boolean onMap = false;
	private boolean inDirectory = false;
	// thumbnail is used in popup window on the city map
	private Resource thumbnail;
	// picture is used on the playground object page
	private Resource picture;
	@XmlTransient
	private List<SessionPlaygroundObject> sessionPlaygroundObjects;
    
    /** Creates a new instance of PlaygroundObject */
    public PlaygroundObject() {
        
    }
    
    /**
     * convenience constructor
     */
    public PlaygroundObject(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean isOnMap() {
        return onMap;
    }

    public void setOnMap(boolean onMap) {
        this.onMap = onMap;
    }

    public boolean isInDirectory() {
        return inDirectory;
    }

    public void setInDirectory(boolean inDirectory) {
        this.inDirectory = inDirectory;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    @OneToOne(cascade = CascadeType.ALL)
    public Resource getPicture() {
        return picture;
    }

    public void setPicture(Resource picture) {
        this.picture = picture;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Resource getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Resource thumbnail) {
        this.thumbnail = thumbnail;
    }

    @NotEmpty(message="notempty")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @NotNull(message="notnull")
    public Integer getX() {
	    return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    @NotNull(message="notnull")
    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    // @NotNull(message="notnull")
    // @Min(value=1, message="required")
    // @NotEmpty(message="notempty")
    @NotEmpty(message="notempty")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id  @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    /**
     * only hibernate is allowed to set the id
     */
    protected void setId(Long id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @ManyToOne
    @JoinColumn(name="playground_fk")
    public Playground getPlayground() {
        return playground;
    }

    public void setPlayground(Playground playground) {
        this.playground = playground;
    }

    // @NotEmpty(message="notempty")
    //@Column(unique = true)   
    @NotEmpty(message="notempty")
    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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

    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @NotEmpty(message="notempty")
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name="playgroundobject_id", insertable=false, updatable=false)
    public List<SessionPlaygroundObject> getSessionPlaygroundObjects() {
        return sessionPlaygroundObjects;
    }

    public void setSessionPlaygroundObjects(List<SessionPlaygroundObject> sessionPlaygroundObjects) {
        this.sessionPlaygroundObjects = sessionPlaygroundObjects;
    }

	/**
	 * returns whether the current user may edit this playground object 
	 */
    @Transient
	public boolean isEditable() {
		User u = GameUtil.getCurrentUser();
		if (u.equals(getCreator())) {
			return true;
		}
		if (u.getGameAuthorities().isSystemAdministrator() || u.getGameAuthorities().isLcmsAdministrator()) {
			return true;
		}
		return false;
	}
    
    /**
     * returns whether the current user is allowed to change the status of this playground object
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

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
    @Lob
	public String getDescription() {
		return description;
	}

	@ManyToOne
	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	//@NotEmpty(message="notempty")
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public PlaygroundObject copy(User newOwner) {
		PlaygroundObject copy = new PlaygroundObject();
		copy.setName(name);
		copy.setCaption(caption);
	    copy.setDescription(description);
	    copy.setAddress(address);
	    copy.setCategory(category);
	    copy.setInDirectory(inDirectory);
	    copy.setOnMap(onMap);
	    copy.setUri(uri);
	    copy.setUrl(url);
	    copy.setX(x);
	    copy.setY(y);
	    copy.setStatus(status);
	    copy.setCreated(new Date());
	    copy.setCreator(newOwner);
	    copy.setLastModified(new Date());
	    copy.setLastModifier(newOwner);
	    copy.setOwner(newOwner);
	    if (thumbnail != null) {	
	    	copy.setThumbnail(thumbnail.copy(newOwner));
	    }
	    if (picture != null) {		   	
	    	copy.setPicture(picture.copy(newOwner));
	    }	
	   
		return copy;
	}
	
	@javax.xml.bind.annotation.XmlTransient
	@javax.persistence.Transient
	@org.hibernate.validator.AssertTrue(message="failed")
	public boolean isCoordinatesOrURLFilled() {
	    boolean returnValue = false;
	    if (getUrl() != null && getUrl() != "") {
	        // URL filled
	        returnValue = true;
	        if (getX() == null && getY() == null) {
	            Integer zero = new Integer(0);
	            setX(zero);
	            setY(zero);
	        }
	    } else if (getX() != null && getY() != null) {
	        // X, Y filled
	        returnValue = true;
	    }
	    return returnValue;
	}
}
