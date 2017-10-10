package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name="messagebox")
public class MessageBox implements java.io.Serializable {
    
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    @ManyToMany(cascade=CascadeType.ALL)
    @JoinColumn(nullable = false)
    private List<Message> messages = new ArrayList<Message>();

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    public void addMessage(Message m) {
        getMessages().add(m);
    }
    
    public void removeMesage(Message m) {
        getMessages().remove(m);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Message getMessage(Long messageId) {
		for(Message m: getMessages()) {
			if(messageId.equals(m.getId())) {
				return m;
			}
		}
		// XXX or throw an exception?
		return null;
	}

}
