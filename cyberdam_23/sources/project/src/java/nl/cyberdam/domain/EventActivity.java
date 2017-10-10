package nl.cyberdam.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

// EventActivity adds no properties to the default activity

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "eventActivity")
@Entity
@DiscriminatorValue("EventActivity")
public class EventActivity extends Activity implements java.io.Serializable {

    public EventActivity() {
    }

    @Override
    public EventActivity copy() {
	EventActivity copy = new EventActivity ();
	copy.setName (getName ());
	copy.setInstructions(getInstructions());
	copy.setScript(this.getScript());
	copy.setDisabledScript(this.getDisabledScript());
	return copy;
    }
}
