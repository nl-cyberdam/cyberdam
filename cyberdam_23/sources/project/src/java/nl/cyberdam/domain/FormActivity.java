package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "formActivity")
@Entity
@DiscriminatorValue("FormActivity")
public class FormActivity extends Activity implements java.io.Serializable {

	@XmlElement(type = ActivityVariable.class)
	private List <ActivityVariable> activityVariables = new ArrayList<ActivityVariable>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="activity_id")
	public List<ActivityVariable> getActivityVariables() {
		return activityVariables;
	}

	public void setActivityVariables(List<ActivityVariable> activityVariables) {
		this.activityVariables = activityVariables;
	}

	@Override
	public FormActivity copy() {
		FormActivity copy = new FormActivity();
		copy.setName(getName());
		copy.setInstructions(getInstructions());
		copy.setScript(this.getScript());
		copy.setDisabledScript(this.getDisabledScript());
		return copy;
	}

	public void removeActivityVariableByIndex(int activityIndex) {
		activityVariables.remove(activityIndex);
	}

}
