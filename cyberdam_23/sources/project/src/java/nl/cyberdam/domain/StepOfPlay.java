package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "stepOfPlay", propOrder = {
	"id",
    "name",
    "script"
})
@Entity
@Table(name="stepofplay")
public class StepOfPlay extends BaseObject{
    private Long id;
    @XmlTransient
    private Long listindex;
    private String name;
    @XmlTransient
    private List<RoleActivity> roleActivities = new ArrayList<RoleActivity>();
    private String script;

    // FetchType.EAGER is needed to make sure the gamemodel gets initialized correctly
    // in gamemodel
    @OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @org.hibernate.annotations.IndexColumn(name = "listindex")
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.DELETE_ORPHAN)
    public List<RoleActivity> getRoleActivities() {
		return roleActivities;
	}
    
    /** Creates a new instance of StateOfPlay */
    public StepOfPlay() {
    }

	public void setRoleActivities(List<RoleActivity> roleActivities) {
		this.roleActivities = roleActivities;
	}
	
	@Transient
	public List<Activity> getActivitiesForRole(Role r) {
		List<Activity> activitiesForRole = new ArrayList<Activity>();
		for (RoleActivity ra: getRoleActivities()) {
			if (ra!=null && r!=null && r.equals(ra.getRole())) {
				activitiesForRole.add(ra.getActivity());
			}
		}
		return activitiesForRole;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(length=65535)
    @Lob
    public String getScript() {
	return script;
    }

    public void setScript (String script) {
	this.script = script;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }
    
    @Column(nullable = false, updatable = false, insertable = false)
    public Long getListindex() {
		return listindex;
	}

	public void setListindex(Long listindex) {
		this.listindex = listindex;
	}

	/**
	 * returns the name of this step of play
	 */
	@Override
	public String toString() {
		return name;
	}

	@Transient
	public void addRoleActivity(RoleActivity roleActivity) {
		for (RoleActivity ra: roleActivities) {
			if (ra.getActivity().getId().equals(roleActivity.getActivity().getId()) && 
					ra.getRole().equals(roleActivity.getRole()))
			{
				throw new ItemInUseException("This activity is already assigned to the step and role", "roleactivity.already.exists", roleActivity.getActivity().getName());
			}
		}
		roleActivities.add(roleActivity);
	}

	@Transient
	public void removeRoleActivity(Role role, Long activityId) {
		for (Iterator<RoleActivity> it = roleActivities.iterator();it.hasNext();) {
			RoleActivity ra = it.next();
			if (ra.getActivity().getId().equals(activityId) &&
				ra.getRole().equals(role)) it.remove();
		}
	}
}
