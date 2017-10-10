package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Activity that enables the user to choose from a list of next steps of play.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "progressActivity", propOrder = {
    "defaultOption",
    "nextStepOfPlayOptions"
})
@Entity
public class ProgressActivity extends Activity implements java.io.Serializable {
    
	private List<NextStepOfPlay> nextStepOfPlayOptions = new ArrayList<NextStepOfPlay>();
    private int defaultOption;
    
    /** 
     * Creates a new instance of ProgressActivity 
     */
    public ProgressActivity() {
    	for (int i = 0; i < 15; i++) {
    		NextStepOfPlay nextStep = new NextStepOfPlay();
    		this.nextStepOfPlayOptions.add(nextStep);
    	}
    }
    
    public int getDefaultOption() {
        return defaultOption;
    }

    public void setDefaultOption(int defaultOption) {
        this.defaultOption = defaultOption;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PROGRESSACTIVITY_ID")
	public List<NextStepOfPlay> getNextStepOfPlayOptions() {
		return nextStepOfPlayOptions;
	}
    
	public void setNextStepOfPlayOptions(List<NextStepOfPlay> nextStepOfPlayOptions) {
		this.nextStepOfPlayOptions = nextStepOfPlayOptions;
	}
	
	@Transient
	public List<NextStepOfPlay> getFilteredNextStepOfPlayOptions() {
		List<NextStepOfPlay> filtered = new ArrayList<NextStepOfPlay>();
		for (NextStepOfPlay s: nextStepOfPlayOptions) {
			if (s.getStep() == null) continue;
			boolean found = false;
//			for (NextStepOfPlay sf: filtered) {
//				if (s.getStep().equals(sf.getStep())) found = true;
//			}
			if (!found) filtered.add(s);
		}
		return filtered;
	}

    /**
     * only name, instructions, defaultoption are copied nextstepsofplay and attachments have to be handled externally
     */
	@Override
	public ProgressActivity copy() {
		ProgressActivity copy = new ProgressActivity();
		copy.setName(getName());
    	copy.setInstructions(getInstructions());
    	copy.setDefaultOption(getDefaultOption());
		copy.setScript(this.getScript());
		copy.setDisabledScript(this.getDisabledScript());
		return copy;
	}
}
