package nl.cyberdam.web;


public class SessionFileUploadBean extends FileUploadBean {
	
    private Long activityId;
    private Long participantId;
    private String playgrounduri;
    
	public Long getActivityId() {
		return activityId;
	}
	public void setActivityId(Long activityId) {
		this.activityId = activityId;
	}
	public Long getParticipantId() {
		return participantId;
	}
	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}
	public String getPlaygrounduri() {
		return playgrounduri;
	}
	public void setPlaygrounduri(String playgrounduri) {
		this.playgrounduri = playgrounduri;
	}

}
