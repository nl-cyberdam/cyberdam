package nl.cyberdam.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * a 'box' for storing SessionResource objects uploaded by a Participant during a GameSession.
 */
@Entity
@Table(name="sessionresourcebox")
public class SessionResourceBox {
	
	@Id @GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@ManyToMany(cascade=CascadeType.ALL)
    private Set<SessionResource> resources = new HashSet<SessionResource>();
    
	public Long getId() {
		return id;
	}

	public Set<SessionResource> getResources() {
		return resources;
	}

	@Transient
	public List<SessionResource> getSortedResources() {
		List<SessionResource> sr = new ArrayList<SessionResource>(this.resources);
		Collections.sort(sr, new Comparator<SessionResource>(){
			public int compare(SessionResource sr1, SessionResource sr2) {
				return sr1.getName().compareTo(sr2.getName());
			}
		});
		return sr;
	}
}
