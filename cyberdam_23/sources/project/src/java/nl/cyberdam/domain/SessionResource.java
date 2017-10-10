package nl.cyberdam.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="sessionresource")
public class SessionResource extends BaseObject {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
    private String name;
    @Lob @Basic(fetch=FetchType.LAZY)
    // @Column(columnDefinition="LONGBLOB") results in erors in the tests because hsqldb cannot handle it
    @Column(length=5242880) /* same size as specified in upload controller */
    private byte[] content;
    private String contentType;
    private int fileSize;
    private boolean published;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created;
    @ManyToOne
    private StepOfPlay stepOfPlay;
    @ManyToOne
    private Participant participant;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public StepOfPlay getStepOfPlay() {
		return stepOfPlay;
	}
	public void setStepOfPlay(StepOfPlay stepOfPlay) {
		this.stepOfPlay = stepOfPlay;
	}
	
	public SessionResource copy() {
		SessionResource sr = new SessionResource();
		sr.setContent(content);
		sr.setContentType(contentType);
		sr.setCreated(created);
		sr.setFileSize(fileSize);
		sr.setName(name);
		return sr;
		
	}

	public Participant getParticipant() {
	    return participant;
	}

	public void setParticipant(Participant participant) {
	    this.participant = participant;
	}
	public boolean isPublished() {
	    return published;
	}
	public void setPublished(boolean published) {
	    this.published = published;
	}
}
