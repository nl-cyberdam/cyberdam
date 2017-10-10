package nl.cyberdam.service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import nl.cyberdam.domain.Group;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.UserStatus;

import org.acegisecurity.providers.encoding.PasswordEncoder;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.hibernate.Query;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 */
@Repository
@Transactional
public class UserManagerImpl extends DefaultService implements UserManager {

    private PasswordEncoder passwordEncoder;

    /** Creates a new instance of UserManagerImpl */
    public UserManagerImpl() {
    }

    /**
     * create user with the minimum of required properties.
     */
    public void addUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encodePassword(password, null));
        sessionFactory.getCurrentSession().save(user);
    }
    
    /**
     * Create user with all possible fields
     * 
     * @param givenname
     * @param familyname
     * @param username
     * @param gameSessionMasterString
     * @param gameManifestComposer
     * @param gameAuthor
     * @param playgroundAuthor
     * @param lcmsAdministrator
     * @param lmsAdministrator
     * @param vleAdministrator
     * @param groups
     * @param language
     * @param email
     * @param password
     */
    public void addUser(String givenname, 
    		            String familyname, 
    		            String username, 
    		            boolean gameSessionMaster,
    		            boolean gameManifestComposer,
    		            boolean gameAuthor,
    		            boolean playgroundAuthor,
    		            boolean lcmsAdministrator,
    		            boolean lmsAdministrator,
    		            boolean vleAdministrator,
    		            Set<Group> groups, 
    		            Locale language,
    		            String email,
    		            String password) {
    	User u = new User();
    	u.setFirstName(givenname);
    	u.setLastName(familyname);
    	u.setUsername(username);
    	u.getGameAuthorities().setGameSessionMaster(gameSessionMaster);
    	u.getGameAuthorities().setGameManifestComposer(gameManifestComposer);
    	u.getGameAuthorities().setGameAuthor(gameAuthor);
    	u.getGameAuthorities().setPlaygroundAuthor(playgroundAuthor);
    	u.getGameAuthorities().setLcmsAdministrator(lcmsAdministrator);
    	u.getGameAuthorities().setLmsAdministrator(lmsAdministrator);
    	u.getGameAuthorities().setVleAdministrator(vleAdministrator);
    	u.setGroups(groups);
    	u.setLocale(language);
    	u.setEmail(email);
    	u.setPassword(passwordEncoder.encodePassword(password, null));
    	addUser(u);
    }
    
    public void addUser(User u) {
    	sessionFactory.getCurrentSession().save(u);
    }

    public void saveUser(User u) {
        sessionFactory.getCurrentSession().saveOrUpdate(u);
    }

    @SuppressWarnings(value = "unchecked")
    public Collection<User> findAll() {
    	Query q = sessionFactory.getCurrentSession().createQuery("from User u");
    	// XXX test setCacheable()
    	q.setCacheable(true);
        return (List<User>) q.list();
    }
    
    @SuppressWarnings("unchecked")
	public List<User> findAllActiveUsers() {
    	Query q = sessionFactory.getCurrentSession().createQuery("from User u where u.status is :status");
    	q.setParameter("status", UserStatus.ENABLED);
    	return (List<User>) q.list();
    }

    public User loadUser(Long id) {
        User user = (User) sessionFactory.getCurrentSession().load(User.class, id);

        return user;
    }

	@SuppressWarnings("unchecked")
	public List<User> getFilteredUsers(String textFilter) {
		textFilter = (textFilter == null || textFilter.length() == 0) ? "%" : "%" + textFilter.toLowerCase() + "%";
		return sessionFactory
				.getCurrentSession()
				.createQuery("select distinct u from User u left join u.groups g where (u.groups is empty and lower (u.username || u.firstName || u.lastName || u.email) like :textFilter) or (g member of u.groups and lower(u.username || u.firstName || u.lastName || u.email || g.name) like :textFilter) ORDER BY u.lastName, u.firstName")
				.setParameter("textFilter", textFilter).list();
	}

    //
    // implements UserDetailsService
    //
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        UserDetails userDetails = (UserDetails) sessionFactory.getCurrentSession().createQuery("from User p where p.username is :search").setString("search", username).uniqueResult();
        if (userDetails == null) {
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            return userDetails;
        }
    }

    public void setPasswordEncoder(PasswordEncoder p) {
        this.passwordEncoder = p;
    }

    @SuppressWarnings("unchecked")
	public List<Group> findallGroups(String name) {
        Query q =  sessionFactory.getCurrentSession().createQuery("from Group g where g.name like :n");
        if (name != null) {
        	q.setString("n", "%" + name + "%");
        } else {
        	q.setString("n", "%");
        }
        return (List<Group>)q.list();
    }
    
    @SuppressWarnings("unchecked")
	public List<Group> findallGroups() {
        return (List<Group>) sessionFactory.getCurrentSession().createQuery("from Group g").list();
    }

    public Group loadGroup(Long id) {
        return (Group) sessionFactory.getCurrentSession().load(Group.class, id);
    }

    public void saveGroup(Group group) {
        sessionFactory.getCurrentSession().saveOrUpdate(group);
    }

	public void delete(Group g) {
		sessionFactory.getCurrentSession().delete(g);
	}

	public void delete(User user) {
		sessionFactory.getCurrentSession().delete(user);
	}

	public void updateLastLogin(User user) {
		// update lastLogin field
        Date now = new Date();
        if (logger.isDebugEnabled()) {
        	logger.debug("Updating last login field of user '" + user.getUsername() + "' to " + now);
        }
        user.setLastLogin(now);
        saveUser(user);
	}
}
