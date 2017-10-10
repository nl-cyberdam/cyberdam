package nl.cyberdam.multilanguage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.Query;
import org.hibernate.Session;

@Entity
@Table(name = "languagepack")
@NamedQueries ( {
    @NamedQuery(name = LanguagePack.QUERY_NAME, query = "SELECT l FROM LanguagePack l"),
    @NamedQuery(name = LanguagePack.LOCALEQUERY_NAME, query = "SELECT l FROM LanguagePack l where l.locale = :locale"),
    @NamedQuery(name = LanguagePack.DELETEQUERY_NAME, query = "DELETE FROM LanguagePack l where l.locale = :locale")
} )    
@XmlType(name = "languagePack", propOrder = {
	"name",
    "locale",
    "messages"
})
@XmlRootElement
public class LanguagePack implements java.io.Serializable {
    public static final String        QUERY_NAME = "findAllLanguagePack";
    public static final String        LOCALEQUERY_NAME = "findLocale";
    public static final String        DELETEQUERY_NAME = "deleteLocale";
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long                      id;
    //@NotEmpty(message = "notempty")
    @Column(unique = true)
    private String                    locale;
    private String                    name;
    @Transient
    private String                    version    = "2.3";
    @OneToMany(mappedBy = "languagePack", cascade = CascadeType.ALL)
    private Set<MultiLanguageMessage> messages   = new HashSet<MultiLanguageMessage>();
    
    
    // Only validate if locale not empty
    @Transient
    @org.hibernate.validator.AssertTrue(message="notcorrect")
    public boolean isLocaleLength() {
        if (locale == null || "".equals(locale) ) {
            return true;  
        } else {
            return (locale != null && locale.length() == 2);
        }
    }
    
    @Transient
    @org.hibernate.validator.AssertTrue(message="notempty")
    public boolean isLocaleValue() {
        return (locale != null && !"".equals(locale));
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @XmlTransient
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Set<MultiLanguageMessage> getMessages() {
        return messages;
    }
    
    public void setMessages(Set<MultiLanguageMessage> messages) {
        this.messages = messages;
    }
    
    public String getLocale() {
        return locale;
    }
    
    public void setLocale(String locale) {
        this.locale = locale;
    }
    
    @XmlAttribute
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    @Transient
    public static List<LanguagePack> findAll(Session session) {
        ArrayList<LanguagePack> retV = new ArrayList<LanguagePack>();
        if (session != null) {
            Query q = session.getNamedQuery(QUERY_NAME);
            Iterator<LanguagePack> iter = q.list().iterator();
            while (iter.hasNext()) {
                retV.add(iter.next());
            }
        }
        return retV;
    }
    
    @Transient
    public static List<String> findAllLocale(Session session) {
        ArrayList<String> returnValue = new ArrayList<String>();
        List<LanguagePack> list = findAll(session);
        for (LanguagePack languagePack : list) {
            returnValue.add(languagePack.getLocale());
        }
        return returnValue;
    }
    
    @SuppressWarnings("unchecked")
    @Transient
    public static LanguagePack findLocale(Session session, String locale) {
        LanguagePack returnValue = null;
        if(session != null) {
            Query q = session.getNamedQuery(LOCALEQUERY_NAME);
            q.setParameter("locale", locale);
            List l = q.list();
            if (l.size() > 0) {
                returnValue = (LanguagePack)l.get(0);
            }      
        }
        return returnValue;
    }

}
