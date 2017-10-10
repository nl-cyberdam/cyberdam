package nl.cyberdam.web;

/**
 * This interface defines an object that can be selected
 * such as with a drop down list.
 *
 * @author Ted Bergeron
 * @version $Id: Selectable.java,v 1.3 2006/01/12 23:33:31 ted Exp $
 */
public interface Selectable {
    String getIdentifyingValue();

    String getDisplayValue();
}
