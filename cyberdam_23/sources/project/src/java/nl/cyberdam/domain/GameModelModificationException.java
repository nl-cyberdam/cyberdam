package nl.cyberdam.domain;

/**
 * This exception is used whenever an illegal change to the contents of
 * a GameModel is attempted. 
 */
public class GameModelModificationException extends RuntimeException {

	public GameModelModificationException(String message) {
		super(message);
	}

}
