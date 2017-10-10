package nl.cyberdam.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 * Association between a Role and an Activity. A list of these is maintained
 * by a StepOfPlay to store which roles should do which activity in which state.
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "roleActivity", propOrder = {
	"id",
    "role",
    "stepOfPlay"
})
@Entity
@Table(name="roleactivity")
public class RoleActivity implements Serializable {
	
	private Long id;
	private Role role;
	@XmlTransient
	private Activity activity;
	@XmlElement(type =StepOfPlay.class)
	public StepOfPlay stepOfPlay;
	
	/**
	 * default constructor
	 */
	public RoleActivity() {
		// empty
	}
	/**
	 * convenience constructor
	 * 
	 * @param r a role
	 * @param a an activity
	 */
	public RoleActivity(Role r, Activity a) {
		setRole(r);
		setActivity(a);
	}
	
	@ManyToOne
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	@ManyToOne
	public Activity getActivity() {
		return activity;
	}
	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Transient
    public StepOfPlay getStepOfPlay() {
       return stepOfPlay;
    }
	public void setStepOfPlay(StepOfPlay stepOfPlay) {
		this.stepOfPlay = stepOfPlay;
	}
}
