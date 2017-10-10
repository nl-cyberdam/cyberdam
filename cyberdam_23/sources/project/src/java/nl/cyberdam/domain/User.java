package nl.cyberdam.domain;

import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Email;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

/**
 * Users of the system.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "user", propOrder = {
	"username"
})
@Entity
@Table(name = "cybuser")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseObject implements UserDetails, java.io.Serializable, HasUsername, Comparable<User> {

	public static final int MAX_USERNAME = 12;
	public static final int MAX_FIRST_NAME = 50;
	public static final int MAX_LAST_NAME = 50;
	
	@XmlTransient
	private Long id;
	private String username = new String();
	@XmlTransient
	private String firstName;
	@XmlTransient
	private String lastName;
	@XmlTransient
	private String email;
	@XmlTransient
	private Locale locale;
	@XmlTransient
	private String themeName;
	@XmlTransient
	private Date lastLogin;
	@XmlTransient
	private Set<Group> groups;
	@XmlTransient
	private String password;
	@XmlTransient
	private UserStatus status = UserStatus.DISABLED;
	@XmlTransient
	private Authorities gameAuthorities = new Authorities();
	@XmlTransient
	private boolean NotifyNewMessages = false;
	@XmlTransient
	private boolean defaultNotifyNewStepOfPlay = false;

	/** Creates a new instance of User */
	public User() {
		status = UserStatus.ENABLED;
	}

	//@XmlJavaTypeAdapter(LocaleAdapter.class)
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public String getThemeName() {
		return themeName;
	}

	public void setThemeName(String themeName) {
		this.themeName = themeName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	/**
	 * only hibernate is allowed to set the id
	 */
	protected void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Date lastLogin) {
		this.lastLogin = lastLogin;
	}

	@NotEmpty(message = "notempty")
	@Email(message = "email")
	@Column(nullable = false)
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name = "cybgroup_cybuser", 
			joinColumns = { @JoinColumn(name = "members_id", unique = true) }, 
			inverseJoinColumns = { @JoinColumn(name = "cybgroup_id") }
	)
	public Set<Group> getGroups() {
		return groups;
	}
	
	@Transient
	public String getGroupsAsCSV() {
		StringBuffer retV = new StringBuffer("");
		Set<Group> groups = getGroups();
		Iterator<Group> iter = groups.iterator();
		while(iter.hasNext()) {
			if (!"".equals(retV.toString())) {
				retV.append(", ");
			}
			retV.append(iter.next().toString());
		}
		return retV.toString();
	}

	@Transient
	public String getFullName() {
		if (this.getFirstName().equals("")) return this.getLastName();
		return this.getFirstName() + " " + this.getLastName();
	}
	
	public void setGroups(Set<Group> groups) {
		this.groups = groups;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	/**
	 * implements acegi UserDetails interface
	 */
	@Transient
	public GrantedAuthority[] getAuthorities() {
		return gameAuthorities.getAuthorities();
	}
	
	@Transient 
	private boolean isAuthority(Authority authority) {
		boolean retV = false;
		GrantedAuthority[] a = gameAuthorities.getAuthorities();
		for (int i=0; i<a.length; i++) {
			if (a[i] == authority) {
				retV = true;
				break;
			}
		}
		return retV;
	}
	
	@Transient
	public boolean isUserAdministrator() {
		return isAuthority(Authority.ROLE_USERADMINISTRATOR);
	}
	
	@Transient
	public boolean isSystemAdministrator() {
		return isAuthority(Authority.ROLE_SYSTEMADMINISTRATOR);
	}

	/**
	 * implements acegi UserDetails interface
	 */
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * implements acegi UserDetails interface
	 */
	@NotEmpty(message = "notempty")
	@Length(min = 1, max = MAX_USERNAME, message = "length")
	// @Max(value = MAX_USERNAME, message="toolong")
	@Column(unique = true)
	public String getUsername() {
		return username;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	/**
	 * implements acegi UserDetails interface, accounts do not expire, so this
	 * will always return true
	 */
	public boolean isAccountNonExpired() {
		return true;
	}

	/**
	 * empty implementation
	 */
	public void setAccountNonExpired(boolean b) {

	}

	/**
	 * implements acegi UserDetails interface - returns false if status is
	 * Status.DISABLED
	 */
	public boolean isAccountNonLocked() {
		if (status.equals(UserStatus.DISABLED)) {
			return false;
		}
		return true;
	}

	/**
	 * XXX empty implementation
	 */
	public void setAccountNonLocked(boolean nonlocked) {
		// should we implement this?
	}

	/**
	 * implements acegi UserDetails interface, credentials never expire, always
	 * returns true.
	 */
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/**
	 * empty implementation
	 */
	public void setCredentialsNonExpired(boolean b) {

	}

	/**
	 * implements acegi UserDetails interface
	 */
	// (setting is based on already persistent field 'status')
	@Transient
	public boolean isEnabled() {
		return UserStatus.ENABLED.equals(status);
	}

	public void setEnabled(boolean enabled) {
		if (enabled) {
			status = UserStatus.ENABLED;
		} else {
			status = UserStatus.DISABLED;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof HasUsername) {
			HasUsername other = (HasUsername) o;
			return other.getUsername().equals(this.getUsername());
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return getUsername().hashCode();
	}

	public boolean isNotifyNewMessages() {
		return NotifyNewMessages;
	}

	public void setNotifyNewMessages(boolean NotifyNewMessages) {
		this.NotifyNewMessages = NotifyNewMessages;
	}

	public boolean isDefaultNotifyNewStepOfPlay() {
		return defaultNotifyNewStepOfPlay;
	}

	public void setDefaultNotifyNewStepOfPlay(boolean defaultNotifyNewStepOfPlay) {
		this.defaultNotifyNewStepOfPlay = defaultNotifyNewStepOfPlay;
	}

	@Embedded
	public Authorities getGameAuthorities() {
		return gameAuthorities;
	}

	/*
	 * private setter for Hibernate
	 */
	@SuppressWarnings("unused")
	private void setGameAuthorities(Authorities a) {
		gameAuthorities = a;
	}

	/**
	 * compares users based on username
	 */
	public int compareTo(User other) {
		// XXX check if comparing with null values is correctly implemented
		if (getUsername() != null) {
			return getUsername().compareTo(other.getUsername());
		} else {
			if (other.getUsername() != null) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
