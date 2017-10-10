package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

/**
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "role", propOrder = {
	"id",	
    "name",
    "variables"
})
@Entity
@Table(name="role")
public class Role extends BaseObject{
    
	private Long id;
    @XmlTransient
    private Long listindex;
    private String name;
    @XmlTransient
    private GameModel gameModel;
    @XmlElement(type = Variable.class)
    private List<Variable> variables = new ArrayList<Variable>();
    @XmlTransient
    private int index;
    
    @Transient
    public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	/** Creates a new instance of Role */
    public Role() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne
    @JoinColumn(name="gameModel_id", nullable = false, insertable=false, updatable=false)
    public GameModel getGameModel() {
        return gameModel;
    }

    public void setGameModel(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    @Column(nullable = false, updatable = false, insertable = false)
    public Long getListindex() {
		return listindex;
	}

	public void setListindex(Long listindex) {
		this.listindex = listindex;
	}

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name="role_id") 
	public List<Variable> getVariables() {
	    return Collections.unmodifiableList(variables);
	}

	public void setVariables(List<Variable> variables) {
	    this.variables = variables;
	}

	public void addVariable(Variable v) {
	    variables.add(v);
	}

	public Variable getVariableById(Long variableId) {
		for (Variable v: variables) {
			if (v.getId().equals(variableId)) return v;
		}
        throw new RuntimeException("variable not found");
	}

	public void removeVariableById(Long variableId) {
		Variable variable = null;
		for (Variable v: variables) {
			if (v.getId().equals(variableId)) variable = v;
		}
		if (variable == null) throw new RuntimeException("variable not found");
		// disallow removal if the variable is in use 
		for(Activity activity: getGameModel().getActivities()) {
			if (activity instanceof FormActivity) {
				FormActivity formActivity = (FormActivity) activity;
				for (ActivityVariable activityVariable : formActivity.getActivityVariables())
				{
					if (variable.getId().equals(activityVariable.getVariable().getId()))
					{
						throw new ItemInUseException("variable is in use in form activity: " + 
									formActivity.getName(), "variable.in.use.by", formActivity.getName());
					}
				}
			}
		}
	    variables.remove(variable);
	}
   
	/**
	 * @return a copy of the role (that is: a role with the same name)
	 */
	public Role copy() {
		Role copy = new Role();
		copy.setName(name);
		return copy;
	}

	public boolean equals(Role r) {
		if (r.getId() == null && this.getId() == null) {
			return r.getIndex() == this.getIndex();
		} else if (r.getId() != null && this.getId() != null) {
			return r.getId().equals(this.getId());
		}
		return false;
	}
}
