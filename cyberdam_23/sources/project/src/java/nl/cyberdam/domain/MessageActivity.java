package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "messageActivity", propOrder = {
    "defaultMessageText",
    "recipients"
})
@Entity
@DiscriminatorValue("messageaction")
public class MessageActivity extends Activity implements java.io.Serializable {
    
    private String defaultMessageText;
    @XmlElement(type = Role.class)
    private List<Role> recipients = new ArrayList<Role>();
    
    /** Creates a new instance of MessageActivity */
    public MessageActivity() {
    }

    @Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
    @Lob
    public String getDefaultMessageText() {
        return defaultMessageText;
    }

    public void setDefaultMessageText(String defaultMessageText) {
        this.defaultMessageText = defaultMessageText;
    }

    @ManyToMany
    public List<Role> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Role> recipients) {
        this.recipients = recipients;
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
	public List<String> getRecipientIds() {
		List<String> lr = new ArrayList<String>();
		for (Role r: recipients) {
			lr.add(r.getId().toString());
		}
		return lr;
	}

	public void setRecipientIds(List<String> recipientIds) {
		this.recipients.clear();
		if (recipientIds != null) {
			for (String id: recipientIds) {
				this.recipients.add(this.getGameModel().getRoleById(new Long(id)));
			}
		}
	}

    /**
     * name, instructions and defaultmessage are copied, recipients and attachments are not handled here
     */
	@Override
	public MessageActivity copy() {
		MessageActivity copy = new MessageActivity();
		copy.setName(getName());
    	copy.setInstructions(getInstructions());
    	copy.setDefaultMessageText(getDefaultMessageText());
		copy.setScript(this.getScript());
		copy.setDisabledScript(this.getDisabledScript());
    	// XXX copy attachments
		return copy;
	}
    
}
