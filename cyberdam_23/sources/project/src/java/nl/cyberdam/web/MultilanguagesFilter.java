package nl.cyberdam.web;

/**
 *
 */
public class MultilanguagesFilter {
    
    private String code;
    private String message;
   
    public MultilanguagesFilter() {
    }

    public String getCode() {
    	return code;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean equals(Object object) {
        
        boolean result;
        
        if (object == this) return true;
        
        if (object instanceof MultilanguagesFilter) {
        	MultilanguagesFilter other = (MultilanguagesFilter) object;
            
            result = (code == null ? other.code == null : code.equals(other.code));
        } else {
            return false;
        }
        return result;
    }
    
    public int hashCode() {

        int hash = (code == null ? 0 : code.hashCode());
        return hash;
    }
}
