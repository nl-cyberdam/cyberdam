package nl.cyberdam.service;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

/**
 * this is not a real unit test - it just depends on 'large numbers' to simply
 * test if the passwords are of the right size and are not generated twice.
 */
public class SimplePasswordGeneratorTest extends TestCase {

	public void testPasswords() {
		PasswordGenerator generator = new SimplePasswordGenerator();
		String pass;
		Set<String> generatedPasswords = new HashSet<String>();
		for (int i = 0; i < 1000000000; i++) {
			pass = generator.getPassword();
			// System.out.println(pass);
			assertTrue("password size should range from 8 to 10: '" + pass + "'", (pass.length() >= 8 && pass.length() <= 10));
			assertTrue("same password generated twice: '" + pass + "'", !generatedPasswords.contains(pass));
			generatedPasswords.add(pass);
		}
	}

}
