package nl.cyberdam.service;

import java.util.List;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.domain.User;

public interface LogService {

	void addLog(String message);
	void addLog(LogEntry.Module module, String action, String parameter);

	List<LogEntry> findAllLogEntries(String textFilter);
	void userLoggedIn(User u);
	void userLoggedOut(User u);

}