package nl.cyberdam.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Overrides of variable values in a game session
 */
@Entity
@Table(name="session_playgroundobject")
public class SessionPlaygroundObject extends BaseObject implements Serializable {

    private Long id;

    private String description;

    private GameSession gameSession;

    private PlaygroundObject playgroundObject;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    @Lob
    @Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGameSession(GameSession session) {
	this.gameSession = session;
    }

    @ManyToOne
    @JoinColumn(name="gameSession_id")
    public GameSession getGameSession() {
	return gameSession;
    }

    public void setPlaygroundObject(PlaygroundObject playgroundObject) {
	this.playgroundObject = playgroundObject;
    }

    @ManyToOne
    @JoinColumn(name="playgroundObject_id")
    public PlaygroundObject getPlaygroundObject() {
	return playgroundObject;
    }
}
