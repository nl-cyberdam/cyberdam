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
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resource", propOrder = {
	"id",
    "comment",
    "contentType",
    "fileName",
    "fileSize",
    "name"
})
/**
 *
 */
@Entity
@Table(name="resource")
public class Resource extends BaseObject{

	private Long id;
    private String name;
    private String fileName;
    private String comment;
    @XmlTransient
    private byte[] content;
    private String contentType;
    private int fileSize;
    @XmlTransient
    private Date lastModified;
    @XmlTransient
    private User lastModifier;
    @XmlTransient
    private Date created;
    @XmlTransient
    private User creator;
    
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @ManyToOne
    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @ManyToOne
    public User getLastModifier() {
        return lastModifier;
    }

    public void setLastModifier(User lastModifier) {
        this.lastModifier = lastModifier;
    }

    @Temporal(TemporalType.TIMESTAMP)
    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }
    
    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String originalFileName) {
        this.fileName = originalFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    // XXX I don't know whether the LAZY actually does something in this case.
    @Lob @Basic(fetch=FetchType.LAZY)
    // @Column(columnDefinition="LONGBLOB") results in erors in the tests because hsqldb cannot handle it
    @Column(length=5242880) /* same size as specified in upload controller */
    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Resource copy(User newOwner) {
		Resource copy = new Resource();
		copy.setName(getName());
		copy.setComment(comment);
		copy.setContent(content);
		copy.setContentType(contentType);
		copy.setFileName(fileName);
		copy.setFileSize(fileSize);
		copy.setCreated(new Date());
		copy.setCreator(newOwner);
		copy.setLastModified(new Date());
		copy.setLastModifier(newOwner);
		return copy;
	}
}
