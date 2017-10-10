package nl.cyberdam.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "activityVariable", propOrder = {
		"id",
		"caption",
		"mandatory",
		"variable"
})
@Entity
@Table(name="activityvariable")
public class ActivityVariable  extends BaseObject implements java.lang.Comparable<ActivityVariable>{

	private Long id;
	private String caption;
	private Boolean mandatory;
	@XmlTransient
	private FormActivity formActivity;
	private Variable variable;

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public Boolean getMandatory() {
		return mandatory;
	}

	public void setMandatory(Boolean mandatory) {
		this.mandatory = mandatory;
	}

	@ManyToOne
	@JoinColumn(name="activity_id")
	public FormActivity getActivity() {
		return formActivity;
	}

	public void setActivity(FormActivity activity) {
		this.formActivity = activity;
	}

	@ManyToOne
	@JoinColumn(name="variable_id")
	public Variable getVariable() {
		return variable;
	}

	public void setVariable(Variable variable) {
		this.variable = variable;
	}

	public ActivityVariable copy() {
		ActivityVariable copy = new ActivityVariable();
		copy.setCaption(getCaption());
		copy.setMandatory(getMandatory());
		return copy;
	}

	public int compareTo(ActivityVariable av) {
		return this.getCaption().compareTo(av.getCaption());
	}
}
