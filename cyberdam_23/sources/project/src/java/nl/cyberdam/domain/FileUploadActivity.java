package nl.cyberdam.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Currently this is more or less a 'tagging' class - nearly all needed functionality
 * is in the Activity superclass.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fileUploadActivity")
@Entity
@DiscriminatorValue("fileuploadaction")
public class FileUploadActivity extends Activity implements java.io.Serializable {
    
    /** Creates a new instance of FileUploadActivity */
    public FileUploadActivity() {
    }
    
    /**
     * only name, instructions are copied, attachments have to be handled externally
     */
    @Override
    public FileUploadActivity copy() {
    	FileUploadActivity copy = new FileUploadActivity();
    	copy.setName(getName());
    	copy.setInstructions(getInstructions());
		copy.setScript(this.getScript());
		copy.setDisabledScript(this.getDisabledScript());
    	return copy;
    }
}
