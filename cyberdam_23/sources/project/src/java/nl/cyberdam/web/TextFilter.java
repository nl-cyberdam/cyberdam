package nl.cyberdam.web;

public class TextFilter {

    private String searchText;

    public TextFilter() {
    }

    public boolean equals(Object object) {
	if (object == this)
	    return true;

	if (object instanceof TextFilter) {
	    TextFilter other = (TextFilter) object;

	    return (searchText == null ? other.searchText == null : searchText
		    .equals(other.searchText));
	}
	return false;
    }

    public int hashCode() {

	int hash = (searchText == null ? 0 : searchText.hashCode());
	return hash;
    }

    public String getSearchText() {
	return searchText;
    }

    public void setSearchText(String searchText) {
	this.searchText = searchText;
    }
}
