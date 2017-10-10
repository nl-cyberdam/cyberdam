package nl.cyberdam.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 */
@Entity
@Table(name = "roletoplaygroundmapping")
public class RoleToPlaygroundMapping implements java.io.Serializable {
    private Long id;
    private Role role;
    private PlaygroundObject playgroundObject;
    private GameManifest gameManifest;
    private Playground playground;

    public RoleToPlaygroundMapping() {
    }

    public RoleToPlaygroundMapping(GameManifest m, Role r) {
        setRole(r);
        setGameManifest(m);
    }
    
    public RoleToPlaygroundMapping(GameManifest m, Role r, PlaygroundObject p) {
        setRole(r);
        setPlaygroundObject(p);
        setGameManifest(m);
    }

    public RoleToPlaygroundMapping(GameManifest m, Role r, PlaygroundObject po, Playground p) {
        setRole(r);
        setPlaygroundObject(po);
        setGameManifest(m);
        setPlayground(p);
    }

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @ManyToOne
    public PlaygroundObject getPlaygroundObject() {
        return playgroundObject;
    }

    public void setPlaygroundObject(PlaygroundObject playgroundObject) {
        this.playgroundObject = playgroundObject;
    }

    @ManyToOne
    public GameManifest getGameManifest() {
        return gameManifest;
    }

    public void setGameManifest(GameManifest gameManifest) {
        this.gameManifest = gameManifest;
    }

    @Override
    public String toString() {
        return getRole().getName() + " (" + (getPlaygroundObject() != null ? getPlaygroundObject().getName() : "") + ")";
    }
    
    @ManyToOne
	public Playground getPlayground() {
		return playground;
	}

	public void setPlayground(Playground playground) {
		this.playground = playground;
	}
}
