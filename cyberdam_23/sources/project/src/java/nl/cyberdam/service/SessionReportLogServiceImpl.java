package nl.cyberdam.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import nl.cyberdam.domain.Activity;
import nl.cyberdam.domain.EventActivity;
import nl.cyberdam.domain.FileUploadActivity;
import nl.cyberdam.domain.FormActivity;
import nl.cyberdam.domain.Message;
import nl.cyberdam.domain.MessageActivity;
import nl.cyberdam.domain.Participant;
import nl.cyberdam.domain.ProgressActivity;
import nl.cyberdam.domain.Role;
import nl.cyberdam.domain.SessionReportLogItem;
import nl.cyberdam.domain.SessionResource;
import nl.cyberdam.domain.User;
import nl.cyberdam.domain.VariableSessionValue;
import nl.cyberdam.util.GameUtil;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

public class SessionReportLogServiceImpl implements SessionReportLogService {
    protected SessionFactory sessionFactory;
    
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    @SuppressWarnings("unchecked")
    public List<SessionReportLogItem> findAllForSession(Long sessionId) {
        Query q = sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select r from SessionReportLogItem as r where r.sessionId is :sessionId ORDER BY r.date");
        q.setParameter("sessionId", sessionId);
        return (List<SessionReportLogItem>) q.list();
    }
    
    @SuppressWarnings("unchecked")
    public List<SessionReportLogItem> findAllForSessionAndRole(Long sessionId, Role role) {
        Query q = sessionFactory
                .getCurrentSession()
                .createQuery(
                        "select r from SessionReportLogItem as r where r.sessionId is :sessionId AND r.role is :role AND r.activity is NOT NULL ORDER BY r.date");
        q.setParameter("sessionId", sessionId);
        q.setParameter("role", role);
        return (List<SessionReportLogItem>) q.list();
    }
    
    @Transactional
    public void save(SessionReportLogItem sessionReportLogItem) {
        sessionFactory.getCurrentSession().save(sessionReportLogItem);
    }

	private SessionReportLogItem logActivity(Activity activity,	Participant participant) {
		SessionReportLogItem logItem = new SessionReportLogItem();
		User u = GameUtil.getCurrentUser();
		String s = u.getFullName() + " (" + u.getUsername() + ")";
		logItem.setUserName(s);
		logItem.setSessionId(participant.getGameSession().getId());
		logItem.setDate(new Date(System.currentTimeMillis()));
		logItem.setStepOfPlay(participant.getGameSession().getCurrentStepOfPlay().getName());
		logItem.setRole(participant.getRoleAndPlayground().getRole());
		logItem.setActivity(activity);
		if (activity != null) {
			logItem.setType(activity.getType().toUpperCase());
		}
		return logItem;
	}

    public void logMessageActivity(MessageActivity messageActivity,
								   Message message, String receiverList) {
    	SessionReportLogItem sessionReportLogItem = logActivity(messageActivity, message.getSender());
        if (messageActivity == null) {
            sessionReportLogItem.setReceiver(receiverList);
            sessionReportLogItem.setType(new MessageActivity().getType().toUpperCase());
        } else {
            StringBuffer receiver = new StringBuffer("");
            for (Iterator<Role> it = messageActivity.getRecipients().iterator();it.hasNext();) {
            	Role r = it.next();
                receiver.append(r.getName());
                if (it.hasNext()) receiver.append(", ");
            }
            sessionReportLogItem.setReceiver(receiver.toString());
        }
        sessionReportLogItem.setSubject(message.getSubject());
        sessionReportLogItem.setMessageId(message.getId());
        save(sessionReportLogItem);
    }
    
    public void logProgressActivity(ProgressActivity progressActivity, Participant participant) {
    	SessionReportLogItem sessionReportLogItem = logActivity(progressActivity, participant);
        save(sessionReportLogItem);
    }
    
    public void logFileUploadActivity(FileUploadActivity fileUploadActivity,
		  MultipartFile file, Participant participant, SessionResource rs) {
    	SessionReportLogItem sessionReportLogItem = logActivity(fileUploadActivity, participant);
    	if (fileUploadActivity == null) {
            sessionReportLogItem.setType(new FileUploadActivity().getType().toUpperCase());
    	}
        sessionReportLogItem.setFileName(file.getOriginalFilename());
        sessionReportLogItem.setActualName(file.getName());
        sessionReportLogItem.setResourceId(rs.getId());
        save(sessionReportLogItem);
    }

    public void logFormActivity(FormActivity formActivity,
    		List<VariableSessionValue> variableValues, Participant participant) {
    	SessionReportLogItem sessionReportLogItem = logActivity(formActivity, participant);
    	// do some stuff with variableValues
    	save(sessionReportLogItem);
    }

	public void logEventActivity(EventActivity eventActivity, Participant participant) {
		SessionReportLogItem sessionReportLogItem = logActivity(eventActivity, participant);
		save(sessionReportLogItem);
	}
}
