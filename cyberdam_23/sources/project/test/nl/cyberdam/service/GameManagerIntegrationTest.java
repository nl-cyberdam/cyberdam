/*
 * see: http://unitils.sourceforge.net/spring_article.html
 * http://lijinjoseji.wordpress.com/2007/03/26/test-your-spring-hibernate-applications-using-unitils/
 *
 */
package nl.cyberdam.service;

import java.util.ArrayList;
import java.util.List;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.FileUploadActivity;
import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.NextStepOfPlay;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.RoleToPlaygroundMapping;
import nl.cyberdam.domain.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.unitils.UnitilsJUnit3;
import org.unitils.hibernate.HibernateUnitils;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.unitils.spring.annotation.SpringBean;

/**
 *
 */
@SpringApplicationContext({"file:web/WEB-INF/applicationContext.xml","file:web/WEB-INF/applicationContext-acegi-security.xml",  "classpath:test-config.xml"})
public class GameManagerIntegrationTest extends UnitilsJUnit3 {
	
	Log logger = LogFactory.getLog(getClass());

    @SpringBean("gameManager")
    private GameManager gameManager;
    @SpringBean("userManager")
    private UserManager userManager;
    @SpringBean("sessionFactory")
    private SessionFactory sessionFactory;
    
    // 
    Session session;

    @Override
    protected void setUp() throws Exception {

//        // the following is necessary for lazy loading 
//        // sf = (SessionFactory) ctx.getBean("sessionFactory");
//        // open and bind the session for this test thread. 
//        Session s = sessionFactory.openSession();
//        TransactionSynchronizationManager.bindResource(sf, new SessionHolder(s));
//
//    // setup code here 
        session = SessionFactoryUtils.getSession(sessionFactory, true);
        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));

    }

    @Override
    protected void tearDown() throws Exception {
        //        // unbind and close the session. 
//        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sf);
//        Session s = holder.getSession();
//        s.flush();
//        TransactionSynchronizationManager.unbindResource(sf);
//        SessionFactoryUtils.closeSessionIfNecessary(s, sf);
//    // teardown code here 
        SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
        Session s = holder.getSession();
        s.flush();
        TransactionSynchronizationManager.unbindResource(sessionFactory);
        SessionFactoryUtils.closeSession(s);

    }

    public void testSave() {
        GameModel gm = new GameModel();
        gm.setName("123test");
        // List<Person> result = phonebook.searchByLastName("Doe"));        
        // ReflectionAssert.assertPropertyLenEquals("firstName", Arrays.asList("John", "Jane"), result);    
        gameManager.save(gm);

        // now retrieve
        List<GameModel> gameModels = gameManager.findAll();

        for (GameModel g : gameModels) {
            if ("123test".equals(g.getName())) {
                // success
                return;
            }
        }
        fail("The inserted game was not found using findAll()");
    }

    // @DataSet

    public void testSaveWithActivity() {

        // see: http://forum.hibernate.org/viewtopic.php?t=929167
        GameModel gm = new GameModel();
        gm.setName("123test");
        Activity a = new FileUploadActivity();
        a.setName("testActivity");
        gm.addActivity(a);

        gameManager.save(gm);

        // now retrieve
        List<GameModel> gameModels = gameManager.findAll();

        for (GameModel g : gameModels) {
            if ("123test".equals(g.getName())) {
                // check if activity is present
                List<Activity> activities = g.getActivities();
                for (Activity a2 : activities) {
                    if ("testActivity".equals(a2.getName())) {
                        return;
                    }
                }
            }
        }
        fail("The inserted game was not found using findAll()");
    }
    
    public void testCreateUser() {
    	// create user
        User u = new User();
        u.setUsername("test1234");
        u.setEmail("dummy@example.com");
        userManager.saveUser(u);
    }

//    public void testCreateUserTooLongName() {
//    	// create user
//        User u = new User();
//        // this is more than 12 characters
//        u.setLoginName("testtest123465789");
//        u.setEmail("dummy@example.com");
//        try {
//        	userManager.saveUser(u);
//        	fail("Expected Exception for too long username");
//        } finally {
//        	// all's well?
//        }
//    }
    
    public void testMappingToDatabase() {
        HibernateUnitils.assertMappingWithDatabaseConsistent();
    }
    

    
    public void testFindAllGameSessions() {
        
        // insert 2 sessions
        GameSession g1 = new GameSession();
        gameManager.save(g1);
        GameSession g2 = new GameSession();
        gameManager.save(g2);
        
        List<GameSession> gameSessions = gameManager.findAllGameSessions();
        assertTrue("Expected 2 sessions, got: " + gameSessions.size(), gameSessions.size() == 2);
        
    }
    
    
    public void testDeliverMessage() {
    	
    	 // create users
        User u1 = new User();
        u1.setUsername("testuser1");
        u1.setEmail("dummmy1@example.com");
        userManager.saveUser(u1);
        
        User u2 = new User();
        u2.setUsername("testuser2");
        u2.setEmail("dummy2@example.com");
        userManager.saveUser(u2);
        
        User u3 = new User();
        u3.setUsername("testuser3");
        u3.setEmail("dummy3@example.com");
        userManager.saveUser(u3);
        
        // create gamemodel with roles
        GameModel gm = new GameModel();
        Role r1 = new Role();
        gm.addRole(r1);
        Role r2 = new Role();
        gm.addRole(r2);
        Role r3 = new Role();
        gm.addRole(r3);
        
        gameManager.save(gm);
        
        // create playgroundobject
        PlaygroundObject po1 = new PlaygroundObject();
        PlaygroundObject po2 = new PlaygroundObject();
        PlaygroundObject po3 = new PlaygroundObject();
        gameManager.save(po1);
        gameManager.save(po2);
        gameManager.save(po3);
        
        // create manifest
        GameManifest gma = new GameManifest();
        // set gamemodel
        gma.setGameModel(gm);
        // create playgroundmapping for roles
        List<RoleToPlaygroundMapping> r2pg = gma.getRolesToPlaygroundObjects();
        assertTrue(r2pg.size() == 3);
        r2pg.get(0).setPlaygroundObject(po1);
        r2pg.get(1).setPlaygroundObject(po2);
        r2pg.get(2).setPlaygroundObject(po3);
        gameManager.save(gma);
        
        // insert a session
        GameSession gs = new GameSession();
        gs.setManifest(gma);
        gs.initialize();
        // link roles to users
        List<Participant> r2pm = gs.getParticipants();
        assertTrue(r2pm.size() == 3);
        r2pm.get(0).addUser(u1);
        r2pm.get(1).addUser(u2);
        r2pm.get(2).addUser(u3);
        gameManager.save(gs);
        
        // get id's for the r2pm's
        Long r2pmId0 = r2pm.get(0).getId();
        Long r2pmId1 = r2pm.get(1).getId();
        Long r2pmId2 = r2pm.get(2).getId();
        
        logger.debug("ids: " + r2pmId0 + " " + r2pmId1 + " " + r2pmId2);
        
        //List<GameSession> gameSessions = gameManager.findAllGameSessionsForUser(u1);
        //assertTrue(gameSessions.size() == 1);
    	
        // create message
        Message m = new Message();
        m.setSubject("testmessage");
        m.setBody("testbody");
        m.setSender(r2pm.get(0));
        List<Participant> recipients = new ArrayList<Participant>();
        recipients.add(r2pm.get(1));
        recipients.add(r2pm.get(2));
        m.setRecipients(recipients);
        
        // deliver message
        gameManager.deliverMessage(m);
        
        // check in current session
        assertTrue(r2pm.get(0).getOutbox().getMessages().size() == 1);
        assertTrue(r2pm.get(1).getInbox().getMessages().size() == 1);
        assertTrue(r2pm.get(2).getInbox().getMessages().size() == 1);
        
        
        // close old session and create new one?
        //session.flush();
//        SessionFactoryUtils.closeSession(session);
//        TransactionSynchronizationManager.unbindResource(sessionFactory);
//        
//        session = SessionFactoryUtils.getSession(sessionFactory, true);
//        TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
        
        // check recipients and sender
        Participant r2pmFromDb0 = gameManager.loadParticipant(r2pmId0);
        Participant r2pmFromDb1 = gameManager.loadParticipant(r2pmId1);
        Participant r2pmFromDb2 = gameManager.loadParticipant(r2pmId2);
        
        // XXX dit moet weer aangezet
         assertTrue(r2pmFromDb0.getOutbox().getMessages().size() == 1);
         assertTrue(r2pmFromDb1.getInbox().getMessages().size() == 1);
         assertTrue(r2pmFromDb2.getInbox().getMessages().size() == 1);
    }
    
    public void testDeliverTwoMessages() {
    	
    	
   	 // create users
       User u1 = new User();
       u1.setUsername("testuser4");
       u1.setEmail("dummy1@example.com");
       userManager.saveUser(u1);
       
       User u2 = new User();
       u2.setUsername("testuser5");
       u2.setEmail("dummy2@example.com");
       userManager.saveUser(u2);
       
       User u3 = new User();
       u3.setUsername("testuser6");
       u3.setEmail("dummy3@example.com");
       userManager.saveUser(u3);
       
       // create gamemodel with roles
       GameModel gm = new GameModel();
       Role r1 = new Role();
       gm.addRole(r1);
       Role r2 = new Role();
       gm.addRole(r2);
       Role r3 = new Role();
       gm.addRole(r3);
       
       
       gameManager.save(gm);
       
       // create playgroundobject
       PlaygroundObject po1 = new PlaygroundObject();
       PlaygroundObject po2 = new PlaygroundObject();
       PlaygroundObject po3 = new PlaygroundObject();
       gameManager.save(po1);
       gameManager.save(po2);
       gameManager.save(po3);
       
       // create manifest
       GameManifest gma = new GameManifest();
       // set gamemodel
       gma.setGameModel(gm);
       // create playgroundmapping for roles
       List<RoleToPlaygroundMapping> r2pg = gma.getRolesToPlaygroundObjects();
       assertTrue(r2pg.size() == 3);
       r2pg.get(0).setPlaygroundObject(po1);
       r2pg.get(1).setPlaygroundObject(po2);
       r2pg.get(2).setPlaygroundObject(po3);
       gameManager.save(gma);
       
       // insert a session
       GameSession gs = new GameSession();
       gs.setManifest(gma);
       gs.initialize();
       // link roles to users
       List<Participant> r2pm = gs.getParticipants();
       assertTrue(r2pm.size() == 3);
       r2pm.get(0).addUser(u1);
       r2pm.get(1).addUser(u2);
       r2pm.get(2).addUser(u3);
       gameManager.save(gs);
       
       //List<GameSession> gameSessions = gameManager.findAllGameSessionsForUser(u1);
       //assertTrue(gameSessions.size() == 1);
   	
       // create message
       Message m = new Message();
       m.setSubject("testmessage1");
       m.setBody("testbody");
       m.setSender(r2pm.get(0));
       List<Participant> recipients = new ArrayList<Participant>();
       recipients.add(r2pm.get(1));
       recipients.add(r2pm.get(2));
       m.setRecipients(recipients);
       
       // deliver first message
       logger.debug("delivering message 1");
       // gameManager.save(m);
       gameManager.deliverMessage(m);
       
       Message m2 = new Message();
       m2.setSubject("testmessage2");
       m2.setBody("testbody");
       m2.setSender(r2pm.get(0));
       List<Participant> recipients2 = new ArrayList<Participant>();
       recipients2.add(r2pm.get(1));
       recipients2.add(r2pm.get(2));
       m2.setRecipients(recipients2);
       
       // deliver second message
       logger.debug("delivering message 2");
       // gameManager.save(m2);
       gameManager.deliverMessage(m2);
       
       // check recipients and sender (without persistence)
       assertTrue(r2pm.get(0).getOutbox().getMessages().size() == 2);
       assertTrue(r2pm.get(1).getInbox().getMessages().size() == 2);
       assertTrue(r2pm.get(2).getInbox().getMessages().size() == 2);
   	
       
   }
   
   public void testCreateMessage() {
	   Message m = new Message();
       m.setSubject("testmessage");
       m.setBody("testbody");
       gameManager.save(m);
   }
   
   public void testCreateMessages() {
	   Message m = new Message();
       m.setSubject("testmessage");
       m.setBody("testbody");
       gameManager.save(m);
       
       Message m2 = new Message();
       m2.setSubject("testmessage");
       m2.setBody("testbody");
       gameManager.save(m2);
   }
   
   public void testProgressActivity() {
	   
	   // create gamemodel with roles
       GameModel gm = new GameModel();
       
       ProgressActivity a = new ProgressActivity();
       a.setName("nieuwe progress activity");
       
       // add 15 progress options
		for (int i = 0; i < 15; i++) {
			a.getNextStepOfPlayOptions().add(new NextStepOfPlay());
		}
       
       gm.addActivity(a);
       gameManager.save(gm);
       session.flush();
   }
}
