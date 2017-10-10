package nl.cyberdam.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * A description of a next step to progress to - used by ProgressActivity
 * 
 * @see ProgressActivity
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nextStepOfPlay", propOrder = {
	"id",	
    "description",
    "step"
})
@Entity
@Table(name="nextstepofplay")
public class NextStepOfPlay implements Serializable {
	
	private Long id;
	private String description = "";
	private StepOfPlay step;

	public void setId(Long id) {
		this.id = id;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}
	
	public void setStep(StepOfPlay step) {
		this.step = step;
	}
	
	@OneToOne
	public StepOfPlay getStep() {
		return step;
	}
}
