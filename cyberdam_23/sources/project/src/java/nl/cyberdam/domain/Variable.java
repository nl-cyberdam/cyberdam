package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;


/**
 * This class represents the variable name that needs to be filled in in the form activity
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "variable", propOrder = {
		"id",
		"name",
		"initialValue"
})
@Entity
@Table(name = "variable")
public class Variable extends BaseObject {

	private Long id;
	private String name;
	private String initialValue;
	@XmlTransient	
	private GameModel gameModel;
	@XmlTransient	
	private Role role;
	@XmlTransient	
	private List<ActivityVariable> activityVariables = new ArrayList<ActivityVariable>();
	@XmlTransient 
	private List<VariableSessionValue> sessionVariableValues;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="variable_id")
	public List<ActivityVariable> getActivityVariables() {
		return activityVariables;
	}

	public void setActivityVariables(List<ActivityVariable> activityVariables) {
		this.activityVariables = activityVariables;
	}

	public Variable () {
	}

	public Variable (String name, String initialValue) {
		this.name = name;
		this.initialValue = initialValue;
	}

	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(length=65535) /* TEXT see: http://help.scibit.com/Mascon/masconMySQL_Field_Types.html */
	@Lob
	public String getInitialValue() { 
		return this.initialValue;
	}

	public void setInitialValue(String initialValue) {
		this.initialValue = initialValue;
	}

	@ManyToOne
	@JoinColumn(name="gameModel_id", insertable=false, updatable=false)
	public GameModel getGameModel() { 
		return this.gameModel;
	}

	public void setGameModel(GameModel gameModel) {
		this.gameModel = gameModel;
	}

	@ManyToOne
	@JoinColumn(name="role_id", insertable=false, updatable=false)
	public Role getRole() { 
		return this.role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="variable_id", insertable=false, updatable=false)
	public List<VariableSessionValue> getSessionVariableValues() {
		return sessionVariableValues;
	}

	public void setSessionVariableValues(List<VariableSessionValue> sessionVariableValues) {
		this.sessionVariableValues = sessionVariableValues;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Variable copy() {
		return new Variable(getName(), getInitialValue());
	}
}
