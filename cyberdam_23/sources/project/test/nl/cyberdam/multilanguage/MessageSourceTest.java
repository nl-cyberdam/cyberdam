package nl.cyberdam.multilanguage;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

/**
 *
 */
public class MessageSourceTest extends TestCase {
    
    public MessageSourceTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetMessages() {
//        System.out.println("getMessages");
//        MessageSource instance = new MessageSource();
//        List expResult = null;
//        List result = instance.getMessages();
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    } /* Test of getMessages method, of class MessageSource. */

    public void testGetMessagesByCode() {
//        System.out.println("getMessagesByCode");
//        String code = "";
//        MessageSource instance = new MessageSource();
//        List expResult = null;
//        List result = instance.getMessagesByCode(code);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    } /* Test of getMessagesByCode method, of class MessageSource. */

    public void testGetMessagesForLanguage() {
//        System.out.println("getMessagesForLanguage");
//        String language = "";
//        MessageSource instance = new MessageSource();
//        List expResult = null;
//        List result = instance.getMessagesForLanguage(language);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    } /* Test of getMessagesForLanguage method, of class MessageSource. */

    public void testGetMessage() {
//        System.out.println("getMessage");
//        String language = "";
//        String code = "";
//        MessageSource instance = new MessageSource();
//        Message expResult = null;
//        Message result = instance.getMessage(language, code);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    } /* Test of getMessage method, of class MessageSource. */

    public void testSave() {
//        System.out.println("save");
//        Message message = null;
//        MessageSource instance = new MessageSource();
//        instance.save(message);
//        fail("The test case is a prototype.");
    } /* Test of save method, of class MessageSource. */

    public void testSetSessionFactory() {
//        System.out.println("setSessionFactory");
//        SessionFactory sessionFactory = null;
//        MessageSource instance = new MessageSource();
//        instance.setSessionFactory(sessionFactory);
//        fail("The test case is a prototype.");
    } /* Test of setSessionFactory method, of class MessageSource. */

    public void testResolveCode() {
//        System.out.println("resolveCode");
//        String code = "";
//        Locale locale = null;
//        MessageSource instance = new MessageSource();
//        MessageFormat expResult = null;
//        MessageFormat result = instance.resolveCode(code, locale);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    } /* Test of resolveCode method, of class MessageSource. */

    public void testResolveCodeWithoutArguments() {
//        System.out.println("resolveCodeWithoutArguments");
//        String code = "";
//        Locale locale = null;
//        MessageSource instance = new MessageSource();
//        String expResult = "";
//        String result = instance.resolveCodeWithoutArguments(code, locale);
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    } /* Test of resolveCodeWithoutArguments method, of class MessageSource. */

    public void testSupportedLanguages() {
        System.out.println("supportedLanguages");
        MultiLanguageSource instance = new MessageSource();
        Properties p = new Properties();
        p.setProperty("multilanguage.languages", "en,nl");
        instance.setSystemProperties(p);
        List expResult = Arrays.asList("en","nl");
        List result = instance.supportedLanguages();
        assertEquals(expResult, result);
        // fail("The test case is a prototype.");
    } /* Test of supportedLanguages method, of class MessageSource. */

    public void testGetSystemProperties() {
//        System.out.println("getSystemProperties");
//        MessageSource instance = new MessageSource();
//        Properties expResult = null;
//        Properties result = instance.getSystemProperties();
//        assertEquals(expResult, result);
//        fail("The test case is a prototype.");
    } /* Test of getSystemProperties method, of class MessageSource. */

    public void testSetSystemProperties() {
//        System.out.println("setSystemProperties");
//        Properties systemProperties = null;
//        MessageSource instance = new MessageSource();
//        instance.setSystemProperties(systemProperties);
//        fail("The test case is a prototype.");
    } /* Test of setSystemProperties method, of class MessageSource. */
    
}
