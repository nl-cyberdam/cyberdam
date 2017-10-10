package nl.cyberdam.service;

import java.util.List;

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
import nl.cyberdam.domain.VariableSessionValue;

import org.springframework.web.multipart.MultipartFile;

public interface SessionReportLogService {
    
    public List<SessionReportLogItem> findAllForSession(Long sessionId);
    
    public List<SessionReportLogItem> findAllForSessionAndRole(Long sessionId, Role role);
    
    public void logMessageActivity(MessageActivity messageActivity,
            Message message, String receiverList);
    
    public void logProgressActivity(ProgressActivity progressActivity, Participant participant);
    
    public void logFileUploadActivity(FileUploadActivity fileUploadActivity,
            MultipartFile file, Participant participant, SessionResource rs);

    public void logFormActivity(FormActivity formActivity,
    		List<VariableSessionValue> variableValues, Participant participant);

	public void logEventActivity(EventActivity eventActivity, Participant participant);

	public void save(SessionReportLogItem sessionReportLogItem);
}
