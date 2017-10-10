package nl.cyberdam.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
/**
 *
 */
@Entity
@Table(name="cybgroup")
public class Group extends BaseObject implements java.io.Serializable {

	
	public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @ManyToMany
    public Set<User> getMembers() {
        return members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }
    
    private String name;
    private String description;
    private Date lastModified;
    private User lastModifiedBy;
    private Set<User> members;
    private Long id;
    private String category;
	
    
    /** Creates a new instance of Group */
    public Group() {
        setMembers(new HashSet<User>());
    }
    
    public void addMember(User u) {
        getMembers().add(u);
    }
    
    public void removeMember(User u) {
        getMembers().remove(u);
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    

    @Temporal(javax.persistence.TemporalType.DATE)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    @ManyToOne
    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
    @Transient
    @Override
    public String toString() {
    	return getName();
    }
    
    @Transient
    public int getNumberOfMembers() {
    	return (members == null)? 0 : members.size();
    }
 

}
