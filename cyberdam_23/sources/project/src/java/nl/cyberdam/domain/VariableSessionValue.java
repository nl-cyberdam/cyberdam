package nl.cyberdam.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Overrides of variable values in a game session
 */
@Entity
@Table(name="variable_session_value")
public class VariableSessionValue extends BaseObject implements Serializable {

    private Long id;

    private String value = "";

    private GameSession gameSession;

    private Variable variable;
    
    private ActivityVariable activityVariable;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    public void setGameSession(GameSession session) {
	this.gameSession = session;
    }

    @ManyToOne
    @JoinColumn(name="gameSession_id")
    public GameSession getGameSession() {
	return gameSession;
    }

    public void setVariable(Variable variable) {
	this.variable = variable;
    }

    @ManyToOne
    @JoinColumn(name="variable_id")
    public Variable getVariable() {
	return variable;
    }

    // transient, because sometimes it's not easy to map a value back to the activityvariable
    @Transient
    public ActivityVariable getActivityVariable() {
	return activityVariable;
    }

    public void setActivityVariable(ActivityVariable activityVariable) {
	this.activityVariable = activityVariable;
    }

}
