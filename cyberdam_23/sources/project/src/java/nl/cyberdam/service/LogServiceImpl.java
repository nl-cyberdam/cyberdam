package nl.cyberdam.service;

import java.util.Date;
import java.util.List;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.User;

import org.hibernate.Query;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LogServiceImpl extends DefaultService implements LogService {
	
	/* (non-Javadoc)
	 * @see nl.cyberdam.service.LogService#addLog(java.lang.String)
	 */
	public void addLog(String message) {
		addLog(LogEntry.Module.SYSTEM, "addLog", message);
	}

	public void addLog(LogEntry.Module module, String action, String parameter) {
		Date now = new Date();
		User currentUser = (User) SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		LogEntry l = new LogEntry(module, action, parameter, now, currentUser);
		sessionFactory.getCurrentSession().save(l);
	}

	/*
	 * order by date (descending)
	 */
	@SuppressWarnings("unchecked")
	public List<LogEntry> findAllLogEntries(String textFilter) {
		Query q;
		textFilter = (textFilter == null || textFilter.length () == 0) ? null : "%" + textFilter.toLowerCase () + "%";
		String searchCondition = (textFilter == null) ? ":textFilter is NULL" : "lower (l.module || l.action || l.parameter || l.userString) like :textFilter";
		q = sessionFactory.getCurrentSession().createQuery(
				"from LogEntry l WHERE " + searchCondition + " ORDER BY l.date DESC");
		q.setParameter("textFilter", textFilter);
		return (List<LogEntry>) q.list();
	}

	/**
	 * log a user login (separate method since it is needed to bypass user lookup in SecurityContext)
	 */
	public void userLoggedIn(User u) {
		Date now = new Date();
		LogEntry l = new LogEntry(LogEntry.Module.SYSTEM, "login", u.getUsername(), now, u);
		sessionFactory.getCurrentSession().save(l);
	}

	public void userLoggedOut(User u) {
		Date now = new Date();
		LogEntry l = new LogEntry(LogEntry.Module.SYSTEM, "logout", u.getUsername(), now, u);
		sessionFactory.getCurrentSession().save(l);
	}
}
