//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-520 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.04.29 at 02:34:27 DU CEST 
//


package nl.cyberdam.domain;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import nl.cyberdam.multilanguage.LanguagePack;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the nl.cyberdam.domain package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _Root_QNAME = new QName("", "root");
    private final static QName _LanguagePack_QNAME = new QName("", "languagePack");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: nl.cyberdam.domain
     * 
     */
    public ObjectFactory() {
    }
    
    /**
     * Create an instance of {@link LanguagePack }
     * 
     */
    public LanguagePack createLanguagePack() {
        return new LanguagePack();
    }

    /**
     * Create an instance of {@link Playground }
     * 
     */
    public Playground createPlayground() {
        return new Playground();
    }

    /**
     * Create an instance of {@link RoleToPlaygroundMapping }
     * 
     */
    public RoleToPlaygroundMapping createRoleToPlaygroundMapping() {
        return new RoleToPlaygroundMapping();
    }

    /**
     * Create an instance of {@link GameSession }
     * 
     */
    public GameSession createGameSession() {
        return new GameSession();
    }

    /**
     * Create an instance of {@link SessionResourceBox }
     * 
     */
    public SessionResourceBox createSessionResourceBox() {
        return new SessionResourceBox();
    }

    /**
     * Create an instance of {@link StepOfPlay }
     * 
     */
    public StepOfPlay createStepOfPlay() {
        return new StepOfPlay();
    }

    /**
     * Create an instance of {@link PlaygroundObject }
     * 
     */
    public PlaygroundObject createPlaygroundObject() {
        return new PlaygroundObject();
    }

    /**
     * Create an instance of {@link FileUploadActivity }
     * 
     */
    public FileUploadActivity createFileUploadActivity() {
        return new FileUploadActivity();
    }

    /**
     * Create an instance of {@link Message }
     * 
     */
    public Message createMessage() {
        return new Message();
    }

    /**
     * Create an instance of {@link LocaleAdapter }
     * 
     */
    public LocaleAdapter createLocaleAdapter() {
        return new LocaleAdapter();
    }

    /**
     * Create an instance of {@link NextStepOfPlay }
     * 
     */
    public NextStepOfPlay createNextStepOfPlay() {
        return new NextStepOfPlay();
    }

    /**
     * Create an instance of {@link GameModel }
     * 
     */
    public GameModel createGameModel() {
        return new GameModel();
    }

    /**
     * Create an instance of {@link GameManifest }
     * 
     */
    public GameManifest createGameManifest() {
        return new GameManifest();
    }

    /**
     * Create an instance of {@link Role }
     * 
     */
    public Role createRole() {
        return new Role();
    }

    /**
     * Create an instance of {@link SystemParameters }
     * 
     */
    public SystemParameters createSystemParameters() {
        return new SystemParameters();
    }

    /**
     * Create an instance of {@link User }
     * 
     */
    public User createUser() {
        return new User();
    }

    /**
     * Create an instance of {@link RoleActivity }
     * 
     */
    public RoleActivity createRoleActivity() {
        return new RoleActivity();
    }

    /**
     * Create an instance of {@link MessageActivity }
     * 
     */
    public MessageActivity createMessageActivity() {
        return new MessageActivity();
    }

    /**
     * Create an instance of {@link Resource }
     * 
     */
    public Resource createResource() {
        return new Resource();
    }

    /**
     * Create an instance of {@link MessageBox }
     * 
     */
    public MessageBox createMessageBox() {
        return new MessageBox();
    }

    /**
     * Create an instance of {@link Authorities }
     * 
     */
    public Authorities createAuthorities() {
        return new Authorities();
    }

    /**
     * Create an instance of {@link LogEntry }
     * 
     */
    public LogEntry createLogEntry() {
        return new LogEntry();
    }

    /**
     * Create an instance of {@link SessionResource }
     * 
     */
    public SessionResource createSessionResource() {
        return new SessionResource();
    }

    /**
     * Create an instance of {@link Root }
     * 
     */
    public Root createRoot() {
        return new Root();
    }

    /**
     * Create an instance of {@link Group }
     * 
     */
    public Group createGroup() {
        return new Group();
    }

    /**
     * Create an instance of {@link Participant }
     * 
     */
    public Participant createParticipant() {
        return new Participant();
    }

    /**
     * Create an instance of {@link ProgressActivity }
     * 
     */
    public ProgressActivity createProgressActivity() {
        return new ProgressActivity();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Root }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "root")
    public JAXBElement<Root> createRoot(Root value) {
        return new JAXBElement<Root>(_Root_QNAME, Root.class, null, value);
    }
    
    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LanguagePack }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "languagePack")
    public JAXBElement<LanguagePack> createLanguagePack(LanguagePack value) {
        return new JAXBElement<LanguagePack>(_LanguagePack_QNAME, LanguagePack.class, null, value);
    }

}
