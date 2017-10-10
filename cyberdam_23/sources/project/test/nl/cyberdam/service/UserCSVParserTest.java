package nl.cyberdam.service;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import junit.framework.TestCase;
import nl.cyberdam.tools.UserImporter;

/**
 * Test the csv parsing and user creation routine - it uses a mock userManager object so this
 * test will not be able to test the correct behaviour for duplicate user names.
 */
public class UserCSVParserTest extends TestCase {
	
	private UserManager mockUserManager;
	@Autowired // XXX wont work - this is not yet a spring test!
	private UserImporter userImporter;
	
	protected void setUp() {
		// http://www.easymock.org/EasyMock2_2_Documentation.html
        mockUserManager = createMock(UserManager.class); // 1
    }
	
	public void testBasics() {
		
		// add newline for easy comparison with results!
		String testData = "Dummy,User,dummyuser,,,,,,,,,,dummy@example.com\n";
		String expectResultData = "Dummy,User,dummyuser,,,,,,,,,,dummy@example.com,OK\n";
		
		// 'train' mock
		mockUserManager.addUser("Dummy", "User", "dummyuser", false, false, false, false, false, false, false, null, new Locale("nl"), "dummy@example.com", "pass");
		
		// mock replay
		replay(mockUserManager);
		
		Writer results = new StringWriter();
		try {
			userImporter.addUsers(new StringReader(testData), results, mockUserManager, null, UserImporter.WITH_ADMIN_FLAGS);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		assertEquals("input should be like output with OK appended", expectResultData, results.toString());
		verify(mockUserManager);
	}
	
	public void testMultiline() {
		
		// add newline for easy comparison with results!
		String testData = "Dummy,User,dummyuser,,,,,,,,,,dummy@example.com\nDummy,User,dummyuser2,,y,,y,,,,,en,dummy@example.com\n";
		String expectResultData = "Dummy,User,dummyuser,,,,,,,,,,dummy@example.com,OK\nDummy,User,dummyuser2,,y,,y,,,,,en,dummy@example.com,OK\n";

		// 'train' mock
		mockUserManager.addUser("Dummy", "User", "dummyuser", false, false, false, false, false, false, false, null, new Locale("nl"), "dummy@example.com", "pass");
		mockUserManager.addUser("Dummy", "User", "dummyuser2", false, true, false, true, false, false, false, null, new Locale("en"), "dummy@example.com", "pass");
		
		// mock replay
		replay(mockUserManager);
		
		Writer results = new StringWriter();
		try {
			userImporter.addUsers(new StringReader(testData), results, mockUserManager, null, UserImporter.WITH_ADMIN_FLAGS);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		assertEquals("input should be like output with OK appended on every line", expectResultData, results.toString());
		verify(mockUserManager);
	}
	
	public void testTooManyFields() {
		
		// mock replay - no methods should be called
		replay(mockUserManager);
		
		// add newline for easy comparison with results!
		String testData = "Dummy,User,dummyuser,,,,,,,,,,dummy@example.com,\n";
		Writer results = new StringWriter();
		try {
			userImporter.addUsers(new StringReader(testData), results, mockUserManager, null, UserImporter.WITH_ADMIN_FLAGS);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		assertNotSame("input should not be like output for users without errors", testData, results.toString());
		verify(mockUserManager);
	}
	
	public void testNotEnoughFields() {
		
		// mock replay - no methods should be called
		replay(mockUserManager);
		
		// add newline for easy comparison with results!
		String testData = "Dummy,User,dummyuser,,,,,,,,,dummy@example.com\n";
		Writer results = new StringWriter();
		try {
			userImporter.addUsers(new StringReader(testData), results, mockUserManager, null, UserImporter.WITH_ADMIN_FLAGS);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		assertNotSame("input should not be like output for users without errors", testData, results.toString());
		verify(mockUserManager);
	}
}
