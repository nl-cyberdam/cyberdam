package nl.cyberdam.service;

import java.util.ArrayList;
import java.util.List;

import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.GameSession;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.RoleToPlaygroundMapping;
import nl.cyberdam.domain.StepOfPlay;
import nl.cyberdam.domain.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;
import static org.testng.AssertJUnit.*;

/**
 * Tests for the service layer.
 */
@ContextConfiguration(locations = {"file:web/WEB-INF/applicationContext.xml","file:web/WEB-INF/applicationContext-acegi-security.xml",  "classpath:test-config.xml"}) 
public class ServicesIntegrationTest extends AbstractTransactionalTestNGSpringContextTests {

//	UserManager userManager = (UserManager) applicationContext.getBean("userManager");
	@Autowired
	UserManager userManager;
//	GameManager gameManager = (GameManager) applicationContext.getBean("gameManager");
	@Autowired
	GameManager gameManager;

	@Test	
	public void aTest() {

		userManager.addUser("ikke", "ikke@test.com", "ikke");
	}
	
	@Test
    public void testDeliverMessage() {

   	   // create users
       User u1 = new User();
       u1.setUsername("testuser1");
       u1.setEmail("dummy1@example.com");
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

       // XXX close old session and create new one?

       // check recipients and sender
       Participant r2pmFromDb0 = gameManager.loadParticipant(r2pmId0);
       Participant r2pmFromDb1 = gameManager.loadParticipant(r2pmId1);
       Participant r2pmFromDb2 = gameManager.loadParticipant(r2pmId2);

        assertTrue(r2pmFromDb0.getOutbox().getMessages().size() == 1);
        assertTrue(r2pmFromDb1.getInbox().getMessages().size() == 1);
        assertTrue(r2pmFromDb2.getInbox().getMessages().size() == 1);
   }
	
	
	
	@Test
    public void testSetCurrentStepOfPlay() {

   	 // create users
       User u1 = new User();
       u1.setUsername("testuser1");
       u1.setEmail("dummy1@example.com");
       userManager.saveUser(u1);
       
       // create gamemodel with role and steps of play
       GameModel gm = new GameModel();
       Role r1 = new Role();
       gm.addRole(r1);
       
       StepOfPlay s1 = new StepOfPlay();
       s1.setName("step1");
       StepOfPlay s2 = new StepOfPlay();
       s2.setName("step2");
       
       gm.addStepOfPlay(s1);
       gm.addStepOfPlay(s2);
       
       gm.setInitialStepOfPlay(s1);
       
       gameManager.save(gm);
       
       // create playgroundobject
       PlaygroundObject po1 = new PlaygroundObject();

       gameManager.save(po1);
  
       // create manifest
       GameManifest gma = new GameManifest();
       // set gamemodel
       gma.setGameModel(gm);
       // create playgroundmapping for roles
       List<RoleToPlaygroundMapping> r2pg = gma.getRolesToPlaygroundObjects();
       r2pg.get(0).setPlaygroundObject(po1);

       gameManager.save(gma);
       
       // create a session
       GameSession gs = new GameSession();
       gs.setManifest(gma);
       gs.initialize();
       // link roles to users
       List<Participant> r2pm = gs.getParticipants();
       r2pm.get(0).addUser(u1);
       
       gameManager.save(gs);
       
       // now assert the current step of play is step1 (XXX shouldn't compare on names...)
       assertTrue("step1 should be first step of play", gs.getCurrentStepOfPlay().getName().equals(s1.getName()));
       
       // set the next step of play
       gs.gotoStep(s2);
       
       assertTrue("step2 should now be the step of play", gs.getCurrentStepOfPlay().getName().equals(s2.getName()));
   }
	
	@Test
    public void testSaveWithRole() {

        // see: http://forum.hibernate.org/viewtopic.php?t=929167
        GameModel gm = new GameModel();
        gm.setName("123test");
        Role r = new Role();
        r.setName("testRole");
        gm.addRole(r);

        gameManager.save(gm);

        // now retrieve
        List<GameModel> gameModels = gameManager.findAll();

        for (GameModel g : gameModels) {
            if ("123test".equals(g.getName())) {
                // check if activity is present
                List<Role> roles = g.getRoles();
                for (Role a2 : roles) {
                    if ("testRole".equals(a2.getName())) {
                        return;
                    }
                }
            }
        }
        fail("The inserted game was not found using findAll()");
    }
	
	@Test
    public void testFindAllGameSessionsForUser() {
        
        // create user
        User u = new User();
        u.setUsername("test123");
        u.setEmail("dummy@example.com");
        userManager.saveUser(u);
        
        // assertTrue("Expected 1 user, got: " + userManager.findAll().size(), userManager.findAll().size() == 1);
        
        // create gamemodel with role
        GameModel gm = new GameModel();
        Role r = new Role();
        gm.addRole(r);
        gameManager.save(gm);
        
        // create playgroundobject
        PlaygroundObject po = new PlaygroundObject();
        gameManager.save(po);
        
        // create manifest
        GameManifest gma = new GameManifest();
        // set gamemodel
        gma.setGameModel(gm);
        // create playgroundmapping for role
        List<RoleToPlaygroundMapping> r2pg = gma.getRolesToPlaygroundObjects();
        assertTrue(r2pg.size() == 1);
        r2pg.get(0).setPlaygroundObject(po);
        gameManager.save(gma);
        
        // insert a session
        GameSession gs = new GameSession();
        gs.setManifest(gma);
        gs.initialize();
        // link role to user
        List<Participant> r2pm = gs.getParticipants();
        assertTrue(r2pm.size() == 1);
        r2pm.get(0).addUser(u);
        gameManager.save(gs);
        
        List<GameSession> allGameSessions = gameManager.findAllGameSessions();
        assertTrue("Expected 1 or more gamesessions, got: " + allGameSessions.size(), allGameSessions.size() >= 1);
        // List<Participant> gameParticipantSessions = gameManager.findParticipantsForUser(u, null);
        //assertTrue("Expected 1 gamesession for the user, got: " + gameParticipantSessions.size(), gameParticipantSessions.size() == 1);
    }
}
