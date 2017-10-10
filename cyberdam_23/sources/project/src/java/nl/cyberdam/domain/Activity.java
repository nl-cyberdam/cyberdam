package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activity", propOrder = {
	"id",	
    "attachments",
    "instructions",
    "name",
    "roleActivities",
    "disabledScript",
    "script"
})
@XmlSeeAlso({ 
	MessageActivity.class,
    ProgressActivity.class,
    FileUploadActivity.class,
    EventActivity.class,
    FormActivity.class
})
@Entity
@Table(name="activity")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name="actiontype",
    discriminatorType=DiscriminatorType.STRING
)
@DiscriminatorValue("action")
public abstract class Activity extends BaseObject{
    private String name = "";
    private String instructions = "";
    private String script;
    private String disabledScript;
    @XmlTransient
    private GameModel gameModel;
    private Long id;
    @XmlElement(type = Resource.class)
    private List<Resource> attachments = new ArrayList<Resource>();
    @XmlTransient
    private Long listindex;
    
    @XmlElement(type =RoleActivity.class)
    private Set<RoleActivity> roleActivities;
    
    public Activity() {
    }

    @Column(nullable = false, updatable = false, insertable = false)
	public Long getListindex() {
		return listindex;
	}

	public void setListindex(Long listindex) {
		this.listindex = listindex;
	}

    @ManyToMany
    @org.hibernate.annotations.IndexColumn(name = "listindex")
    public List<Resource> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Resource> attachments) {
        this.attachments = attachments;
    }

	@Transient
	public List<Resource> getSortedAttachments() {
		Collections.sort(this.attachments, new Comparator<Resource>(){
			public int compare(Resource r1, Resource r2) {
				return r1.getName().compareTo(r2.getName());
			}
		});
		return this.attachments;
	}

	/* Note that the following methods should not be necessary. Instead custom collection editors should
	 * be used to translate objects to ids. BUT they don't work properly: the collection editor can't 
	 * handle collections: a collection is received but needs to be translated to string for some 
	 * reason instead of being translated to a target collection and <form:select> and <form:option> 
	 * clashes: the value attribute stays empty and you need to see for yourself if an item is 
	 * selected or not in the jsp.
	 * The non-collection property editor does not work either because they leave null ref when no items 
	 * are selected and again you need to see for yourself if an item is selected or not in the jsp.
	 * Maybe future releases of spring have bugfixes for these problems */
    @Transient
    public List<String> getAttachmentIds() {
    	List<String> lr = new ArrayList<String>();
    	for (Resource r: attachments) {
    		lr.add(r.getId().toString());
    	}
    	return lr;
    }

    public void setAttachmentIds(List<String> attachmentIds) {
    	this.attachments.clear();
    	if (attachmentIds != null) {
    		for (String id: attachmentIds) {
    			this.attachments.add(this.getGameModel().getResourceById(new Long(id)));
    		}
    	}
    }

    @Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
    @Lob
    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name="gameModel_id", nullable = false, insertable=false, updatable=false)
    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    
    @OneToMany(cascade = CascadeType.ALL)  
	@JoinColumn(name="activity_id", insertable=false, updatable=false)
	public Set<RoleActivity> getRoleActivities() {
		return roleActivities;
	}

	public void setRoleActivities(Set<RoleActivity> roleActivities) {
		this.roleActivities = roleActivities;
	}

	@Column(length=65535)
	@Lob
	public String getScript() {
	    return script;
	}

	public void setScript(String script) {
	    this.script = script;
	}

	@Column(length=65535)
	@Lob
	public String getDisabledScript() {
	    return disabledScript;
	}

	public void setDisabledScript(String script) {
	    this.disabledScript = script;
	}

	@Column(length=65535)
	@Lob

    /**
     * returns type of activity as a string 
     */
    @Transient
    public String getType() {
    	
    	if (this instanceof nl.cyberdam.domain.ProgressActivity) {
			return "progress";
		} else if (this instanceof nl.cyberdam.domain.MessageActivity) {
			 return "message";
		} else if (this instanceof nl.cyberdam.domain.FileUploadActivity) {
			return "fileupload";
		} else if (this instanceof nl.cyberdam.domain.FormActivity) {
			return "form";
		} else if (this instanceof nl.cyberdam.domain.EventActivity) {
			return "event";
		} else {
			return "unknown";
		}
    }

    /**
     * to be implemented in subclasses
     * @return a deep clone of an activity, excluding resources - those should be handled on a GameModel level
     */
	public abstract Activity copy();
}
