package nl.cyberdam.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;

import org.hibernate.validator.NotEmpty;

/**
 *
 */
@Entity
@Table(name="message")
public class Message implements Serializable {

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@ManyToOne
	private Participant sender;
	@NotEmpty(message="notempty")
	@ManyToMany
	private List<Participant> recipients = new ArrayList<Participant>();
	@NotEmpty(message="notempty")
	private String subject;
	@Temporal(value = javax.persistence.TemporalType.TIMESTAMP)
	@Column(unique = false, nullable = false)
	private Date sentDate; 
	@NotEmpty(message="notempty")
	@Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
    @Lob
	private String body;
	
	@ManyToMany  
    private List<SessionResource> attachments;
	@ManyToOne
    private StepOfPlay stepOfPlay;

    /** Creates a new instance of Message */
    public Message() {
    }

    public Long getId() {
    	return id;
    }

    public List<SessionResource> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<SessionResource> attachments) {
        this.attachments = attachments;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<Participant> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<Participant> recipients) {
        this.recipients = recipients;
    }

    public Participant getSender() {
        return sender;
    }

    public void setSender(Participant sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }


	public void setStepOfPlay(StepOfPlay stepOfPlay) {
		this.stepOfPlay = stepOfPlay;
	}

	public StepOfPlay getStepOfPlay() {
		return stepOfPlay;
	}

	public void setSentDate(Date sentDate) {
		this.sentDate = sentDate;
	}

	public Date getSentDate() {
		return sentDate;
	}
}