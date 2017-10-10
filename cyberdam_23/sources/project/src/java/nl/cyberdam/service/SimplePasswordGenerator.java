package nl.cyberdam.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * Generates random strings
 */
public class SimplePasswordGenerator implements PasswordGenerator {

	// chars ok for use in a password - removed confusing bits liI1o0
	private static String passwordCharacters = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ23456789*";
	private Random rand;

	public SimplePasswordGenerator() {
		// try to get the SHA1PRNG random generator - that one should be available
		// but otherwise fall back to a simple one
		try {
			rand = SecureRandom.getInstance("SHA1PRNG");
		} catch (NoSuchAlgorithmException e) {
			// simple random with seed based on current time
			rand = new Random();
		}
	}
	
	public String getPassword() {

		// get random password length 8 - 10
		int length = rand.nextInt(3) + 8;
		char[] password = new char[length];

		for (int x = 0; x < length; x++) {
			password[x] = (char) passwordCharacters.charAt(rand.nextInt(passwordCharacters.length()));
		}

		return String.valueOf(password);
	}

}
