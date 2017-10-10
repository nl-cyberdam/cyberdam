package nl.cyberdam.multilanguage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 */
@Entity
@Table(name="multilanguagemessage", uniqueConstraints = {@UniqueConstraint(columnNames={"code", "languagePack"})})
public class MultiLanguageMessage implements java.io.Serializable {
    
    // id for hibernate
    private Long id;
    // the 'key' of the message
    private String code;
    // a 2/3? letter acronym as used by locale
    // private String language;
    @XmlTransient
    private LanguagePack languagePack;
    // the actual message
    private String message;
    // matches the name of i18n files if they are put on the file system or class path
    private String basename;

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    @XmlTransient
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
    @Lob
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    /**
     * this property is retrieved from the associated languagepack 
     */
    @Transient
    public String getLanguage() {
        return getLanguagePack().getLocale();
    }

    @ManyToOne
    @JoinColumn(name = "languagePack", nullable = false)
    @XmlTransient
	public LanguagePack getLanguagePack() {
		return languagePack;
	}

	public void setLanguagePack(LanguagePack languagePack) {
		this.languagePack = languagePack;
	}

}
