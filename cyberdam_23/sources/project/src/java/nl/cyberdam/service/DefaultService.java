package nl.cyberdam.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

/**
 * Base service, provides a sessionFactory and access to the LogService to log
 * important game events.
 *
 */
public class DefaultService implements IDefaultService {
	
	// file logger
	Log logger = LogFactory.getLog(getClass());
	// game content logger
	LogService gameLogService;
	
	protected SessionFactory sessionFactory;
    
    /** Creates a new instance of DefaultService */
    public DefaultService() {
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * This is the logger that logs to the database, so the administrator of the system
     * can check what has been going on.
     */
	public LogService getGameLogService() {
		return gameLogService;
	}

	public void setGameLogService(LogService gameLogService) {
		this.gameLogService = gameLogService;
	}    
}
