package nl.cyberdam.web;

/**
 *
 */
public class GroupsFilter {
    
    private String name;
   
    public GroupsFilter() {
    }

    public String getName() {
    	return name;
    }
    
    public void setName(String name) {
    	this.name = name;
    }

    public boolean equals(Object object) {
        
        boolean result;
        
        if (object == this) return true;
        
        if (object instanceof GroupsFilter) {
        	GroupsFilter other = (GroupsFilter) object;
            
            result = (name == null ? other.name == null : name.equals(other.name));
        } else {
            return false;
        }
        return result;
    }

    public int hashCode() {

        int hash = (name == null ? 0 : name.hashCode());
        return hash;
    }
}
