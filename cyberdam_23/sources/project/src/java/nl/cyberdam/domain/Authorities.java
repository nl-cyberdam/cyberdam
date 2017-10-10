package nl.cyberdam.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

import org.acegisecurity.GrantedAuthority;

/**
 * grouping class for roles/authorities in the game
 */
@Embeddable
public class Authorities implements Serializable {

//    private boolean participant;
    private boolean gameSessionMaster;
    private boolean gameManifestComposer;
    private boolean gameAuthor;
    private boolean playgroundAuthor;
    private boolean lcmsAdministrator;
    private boolean lmsAdministrator;
    private boolean vleAdministrator;
    private boolean userAdministrator;
    private boolean systemAdministrator;
    
//    @Column(name = "ROLE_PARTICIPANT")
//	public boolean isParticipant() {
//		return participant;
//	}
//	public void setParticipant(boolean participant) {
//		this.participant = participant;
//	}
	@Column(name = "ROLE_GAMESESSIONMASTER")
	public boolean isGameSessionMaster() {
		return gameSessionMaster;
	}
	public void setGameSessionMaster(boolean gameSessionMaster) {
		this.gameSessionMaster = gameSessionMaster;
	}
	@Column(name = "ROLE_GAMEMANIFESTCOMPOSER")
	public boolean isGameManifestComposer() {
		return gameManifestComposer;
	}
	public void setGameManifestComposer(boolean gameManifestComposer) {
		this.gameManifestComposer = gameManifestComposer;
	}
	@Column(name = "ROLE_GAMEAUTHOR")
	public boolean isGameAuthor() {
		return gameAuthor;
	}
	public void setGameAuthor(boolean gameAuthor) {
		this.gameAuthor = gameAuthor;
	}
	@Column(name = "ROLE_PLAYGROUNDAUTHOR")
	public boolean isPlaygroundAuthor() {
		return playgroundAuthor;
	}
	public void setPlaygroundAuthor(boolean playgroundAuthor) {
		this.playgroundAuthor = playgroundAuthor;
	}
	@Column(name = "ROLE_LCMSADMINISTRATOR")
	public boolean isLcmsAdministrator() {
		return lcmsAdministrator;
	}
	public void setLcmsAdministrator(boolean lcmsAdministrator) {
		this.lcmsAdministrator = lcmsAdministrator;
	}
	@Column(name = "ROLE_LMSADMINISTRATOR")
	public boolean isLmsAdministrator() {
		return lmsAdministrator;
	}
	public void setLmsAdministrator(boolean lmsAdministrator) {
		this.lmsAdministrator = lmsAdministrator;
	}
	@Column(name = "ROLE_VLEADMINISTRATOR")
	public boolean isVleAdministrator() {
		return vleAdministrator;
	}
	public void setVleAdministrator(boolean vleAdministrator) {
		this.vleAdministrator = vleAdministrator;
	}
	@Column(name = "ROLE_USERADMINISTRATOR")
	public boolean isUserAdministrator() {
		return userAdministrator;
	}
	public void setUserAdministrator(boolean userAdministrator) {
		this.userAdministrator = userAdministrator;
	}
	@Column(name = "ROLE_SYSTEMADMINISTRATOR")
	public boolean isSystemAdministrator() {
		return systemAdministrator;
	}
	public void setSystemAdministrator(boolean systemAdministrator) {
		this.systemAdministrator = systemAdministrator;
	}
	
	@Transient
	public GrantedAuthority[] getAuthorities() {

		List<GrantedAuthority> l = new ArrayList<GrantedAuthority>();
		
		l.add(Authority.ROLE_PARTICIPANT);
		if (isGameAuthor()) {
			l.add(Authority.ROLE_GAMEAUTHOR);
		}
		if (isGameManifestComposer()) {
			l.add(Authority.ROLE_GAMEMANIFESTCOMPOSER);
		}
		if (isGameSessionMaster()) {
			l.add(Authority.ROLE_GAMESESSIONMASTER);
		}
		if (isLcmsAdministrator()) {
			l.add(Authority.ROLE_LCMSADMININSTRATOR);
		}
		if (isLmsAdministrator()) {
			l.add(Authority.ROLE_LMSADMINISTRATOR);
		}
		if (isPlaygroundAuthor()) {
			l.add(Authority.ROLE_PLAYGROUNDAUTHOR);
		}
		if (isSystemAdministrator()) {
			l.add(Authority.ROLE_SYSTEMADMINISTRATOR);
		}
		if (isUserAdministrator()) {
			l.add(Authority.ROLE_USERADMINISTRATOR);
		}
		if (isVleAdministrator()) {
			l.add(Authority.ROLE_VLEADMINISTRATOR);
		}
		return l.toArray(new GrantedAuthority[l.size()]);
	}
}
