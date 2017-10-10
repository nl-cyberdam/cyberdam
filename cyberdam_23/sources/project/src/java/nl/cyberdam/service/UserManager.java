package nl.cyberdam.service;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import nl.cyberdam.domain.Group;
import nl.cyberdam.domain.User;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetailsService;

/**
 *
 */
public interface UserManager extends UserDetailsService {

    public Collection<Group> findallGroups(String name);
    
    public Collection<Group> findallGroups();

    public Group loadGroup(Long requiredIntParameter);

    public void saveGroup(Group group);
    Collection<User> findAll();

    void addUser(String username, String email, String password);
    
    void saveUser(User u);
    
    User loadUser(Long i);

    List<User> getFilteredUsers(String searchText);
    
    void setPasswordEncoder(PasswordEncoder p);

	void addUser(String givenname, String familyname, String username,
			boolean gameSessionMasterString, boolean gameManifestComposer,
			boolean gameAuthor, boolean playgroundAuthor,
			boolean lcmsAdministrator, boolean lmsAdministrator,
			boolean vleAdministrator, Set<Group> groups, Locale language,
			String email, String password);

	/**
	 * find all users that have not been disabled in some way.
	 */
	public List findAllActiveUsers();

	public void delete(Group g);

	public void delete(User user);
	
	public void updateLastLogin(User user);
}
