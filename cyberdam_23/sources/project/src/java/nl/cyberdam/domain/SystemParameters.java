package nl.cyberdam.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Email;
import org.hibernate.validator.NotNull;

/**
 * Class that wraps the 'system parameters' that can be set for the game. Usually
 * there will be only one instance of this class around, but for simple storage
 * it is modeled identically to other classes in the system.
 *
 */
@Entity
@Table(name="systemparameters")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SystemParameters {
	
	@Id
	private Long id = new Long(1);
	private int croninterval = 10;
	@Email(message="email")
	private String email = "system@cyberdam.nl";
	private String defaultLanguageCode = "en";
	private int defaultRows = 20;
	private int vleActivities = 5;
	private int vleFiles = 5;
	private int vleMail = 5;
	private boolean vleMessage = true;
	private boolean vleStep = true;
	@NotNull(message="notnull")
	private long uploadSizeMaxBytes = 5242880;
	
	/**
	 * return the version of the system as a string (to display on a page)
	 * This is a static function, no database connection is needed to get the version,
	 * just use nl.cyberdam.domain.SystemParameters.VERSION
	 */
	public static String VERSION = "2.3 build 20110207";
	public static String COPYRIGHT = "Stichting RechtenOnline";
	public static int MIN_PASSWORD_LENGTH = 6;
	public static String XMLSCHEMA = "cyberdam.xsd";

	public int getCroninterval() {
		return croninterval;
	}
	public void setCroninterval(int croninterval) {
		this.croninterval = croninterval;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDefaultLanguageCode() {
		return defaultLanguageCode;
	}
	public void setDefaultLanguageCode(String defaultLanguageCode) {
		this.defaultLanguageCode = defaultLanguageCode;
	}
	public int getDefaultRows() {
		return defaultRows;
	}
	public void setDefaultRows(int defaultRows) {
		this.defaultRows = defaultRows;
	}
	public int getVleActivities() {
		return vleActivities;
	}
	
	/**
	 * resets to 1 for values < 1
	 */
	public void setVleActivities(int vleActivities) {
		if (vleActivities < 1) {
			vleActivities = 1;
		}
		this.vleActivities = vleActivities;
	}
	public int getVleFiles() {
		return vleFiles;
	}
	/**
	 * resets to 1 for values < 1
	 */
	public void setVleFiles(int vleFiles) {
		if (vleFiles < 1) {
			vleFiles = 1;
		}
		this.vleFiles = vleFiles;
	}
	public int getVleMail() {
		return vleMail;
	}
	
	/**
	 * resets to 1 for values < 1
	 */
	public void setVleMail(int vleMail) {
		if (vleMail < 1) {
			vleMail = 1;
		}
		this.vleMail = vleMail;
	}
	
	/**
	 * whether or not to send email on messages by default
	 */
	public boolean isVleMessage() {
		return vleMessage;
	}
	public void setVleMessage(boolean vleMessage) {
		this.vleMessage = vleMessage;
	}
	
	/**
	 * whether or not to send email on new step by default
	 */
	public boolean isVleStep() {
		return vleStep;
	}
	public void setVleStep(boolean vleStep) {
		this.vleStep = vleStep;
	}
	public Long getId() {
		return id;
	}
	public long getUploadSizeMaxBytes() {
		return uploadSizeMaxBytes;
	}
	public void setUploadSizeMaxBytes(long uploadSizeMaxBytes) {
		this.uploadSizeMaxBytes = uploadSizeMaxBytes;
	}	
}
