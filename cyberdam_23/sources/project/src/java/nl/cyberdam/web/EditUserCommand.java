package nl.cyberdam.web;

import nl.cyberdam.domain.User;

/**
 * command bean for editing a user - it provides extra
 * fields to set the password - the actual user is nested inside
 * this class.
 */
public class EditUserCommand {
	
	private User user;
	private String password;
	private String confirmPassword;
	
	/**
	 * If there is no user yet simply use new User() to create one.
	 */
	public EditUserCommand(User u) {
		user = u;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
