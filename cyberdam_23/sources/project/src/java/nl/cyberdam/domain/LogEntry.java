package nl.cyberdam.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class represents the log of an action done by a user at a time.
 * 
 */
@Entity
@Table(name="logentry")
public class LogEntry implements Serializable {

	public enum Module{SYSTEM, LMS, LCMS};
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@Enumerated(EnumType.STRING)
	private Module module;
	private String action;
	private String parameter;
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	// replaces the reference to the user because we don't want
	// users to be not deletable as soon as they are recoreded
	// in a log message
	private String userString;
	
	/**
	 * convenience constructor
	 */
	public LogEntry(Module module, String action, String parameter,
			Date date, User user) {
		this.module = module;
		this.action = action;
		this.parameter = parameter;
		this.date = date;
		this.userString = user.getUsername() + " (" + user.getFirstName() + " " + user.getLastName() + ")";
	}
	
	/**
	 * default constructor.
	 */
	public LogEntry() {
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Module getModule() {
		return module;
	}
	public void setModule(Module module) {
		this.module = module;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getParameter() {
		return parameter;
	}
	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

	public String getUserString() {
		return userString;
	}
}
