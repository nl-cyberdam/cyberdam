package nl.cyberdam.util;

import nl.cyberdam.domain.SystemParameters;

/**
 * Utility to print the version as specified in SystemParameters.VERSION to the command line. 
 */
public class Version {
	public static void main(String[] args) {
		System.out.println("Cyberdam version: '" + SystemParameters.VERSION + "'");
	}
}
