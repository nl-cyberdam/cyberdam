package nl.cyberdam.domain;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import nl.cyberdam.util.GameUtil;

import org.hibernate.validator.NotEmpty;

/**
 * Playground object - basic data regarding organisations or individuals that
 * will appear on the interactive Cyberdam city map and/or City Directory (old
 * name: Yellow pages) and underlying web sites. It consists of name, category,
 * geographical coordinates, popup text, thumbnail, picture, description, and
 * optional web pages. Owner of a playground object is the user who created or
 * copied it, usually a playground object editor.
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "playground", propOrder = {
	"id",
    "caption",
    "description",
    "link",
    "name",
    "playgroundObjects",
    "status",
    "uriId",
    "version",
    "lastModified",
    "lastModifier"
})

@Entity
@Table(name = "playground")
public class Playground extends BaseObject {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String name;
	
	@Column(unique = true, nullable = false)
	private String uriId;

	private String caption;

	@Column(length = 65535)
	/* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
	@Lob
	private String description;

	private String version;
	
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	private Date lastModified;

	@ManyToOne
	private User lastModifier;

	private String link;

	@Column(nullable = false)
	private Status status = Status.UNDER_CONSTRUCTION;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "playground", fetch = FetchType.EAGER)
	private Set<PlaygroundObject> playgroundObjects = new HashSet<PlaygroundObject>();

	/** Creates a new instance of Playground */
	public Playground() {
	}

	/**
	 * returns unmodifiable set, use special methods to manipulate the contents
	 * of the map.
	 */
	public Set<PlaygroundObject> getPlaygroundObjects() {
		return Collections.unmodifiableSet(playgroundObjects);
	}

	/**
	 * Adds all the playground objects to this Playgrounds collection of playground objects
	 */
	public void addPlaygroundObjects(Set<PlaygroundObject> playgroundObjects) {
		this.playgroundObjects.addAll(playgroundObjects);
	}
	
	public void addPlaygroundObject(PlaygroundObject playgroundObject) {
		this.playgroundObjects.add(playgroundObject);
	}
	
	public void removePlaygroundObject(PlaygroundObject playgroundObject) {
		this.playgroundObjects.remove(playgroundObject);
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
	
	@NotEmpty(message="notempty")
	public String getUriId() {
		return uriId;
	}

	public void setUriId(String uriId) {
		this.uriId = uriId;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Date  getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date  lastModified) {
		this.lastModified = lastModified;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public User getLastModifier() {
		return lastModifier;
	}

	public void setLastModifier(User lastModifier) {
		this.lastModifier = lastModifier;
	}

	@Transient
	public boolean isEditable() {
		User u = GameUtil.getCurrentUser();
		if (u.getGameAuthorities().isSystemAdministrator()
				|| u.getGameAuthorities().isLcmsAdministrator()) {
			return true;
		}
		return false;
	}

	/**
	 * returns whether the current user is allowed to change the status of this
	 * playground object
	 */
	@Transient
	public boolean isStatusEditable() {
		if ((this.status == Status.PUBLIC || this.status == Status.OBSOLETE)
				&& !(GameUtil.getCurrentUser().getGameAuthorities()
						.isLcmsAdministrator() || GameUtil.getCurrentUser()
						.getGameAuthorities().isSystemAdministrator())) { 
			return false;
		} else {
			return true;
		}
	}
	
	public Playground copy(User newOwner) {
		Playground copy = new Playground();
		copy.setName(name);
		copy.setCaption(caption);
	    copy.setDescription(description);
	    copy.setLink(link);
	    copy.setVersion(version);
	    copy.setLastModified(new Date());
	    copy.setLastModifier(newOwner);
	    copy.setUriId(uriId);
	    
	    for (PlaygroundObject playgroundObject: getPlaygroundObjects()) {
			PlaygroundObject newPlaygroundObject = playgroundObject.copy(newOwner);
			// add role to the copy
			Set<PlaygroundObject> set =new HashSet<PlaygroundObject>(copy.getPlaygroundObjects());
			//copy.getPlaygroundObjects().add(newPlaygroundObject);
			set.add(newPlaygroundObject);
			copy.addPlaygroundObjects(set);
			newPlaygroundObject.setPlayground(copy);
		}
		return copy;
	}
	
	public boolean isHttpLink() {
		if (link != null  && link.trim().toLowerCase().startsWith("http:")) {
			return true;
		}
		return false;
	}

}
