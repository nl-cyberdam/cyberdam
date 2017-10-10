package nl.cyberdam.domain;

/**
 * Exception used to signal that part of a gamemodel cannot be removed/modified
 * because it is in use. Has the possibility to store a multilanguage key and
 * the name of the other item that is using the one that the exception is about. 
 */
public class ItemInUseException extends GameModelModificationException {

	// multilanguage key
	private String key;
	// the string representation of that what is using the item
	private String usedBy;
	
	public ItemInUseException(String message, String key, String usedBy) {
		super(message);
		this.key = key;
		this.usedBy = usedBy;
	}
	
	public String getMultiLanguageKey() {
		return key;
	}
	
	public String getUsedBy() {
		return usedBy;
	}
}
