package nl.cyberdam.web.util;

import java.io.IOException;

import javax.servlet.jsp.JspException;

import net.sf.navigator.displayer.ListMenuDisplayer;
import net.sf.navigator.menu.MenuComponent;
import org.apache.commons.lang.StringUtils;

public class MyListMenuDisplayer extends ListMenuDisplayer {
	protected void displayComponents(MenuComponent menu, int level)
    throws JspException, IOException {
        MenuComponent[] components = menu.getMenuComponents();

        if (components.length > 0) {
            // eliminate spaces in string used for Id
            String domId = StringUtils.deleteWhitespace(getMessage(menu.getName()));
            // added to create a unique id everytime
            //domId += ((int) (1000*Math.random()));

            String menuClass = "menu";

            if (level >= 1) {
                menuClass = "submenu";
            }

            // if there is a location/page/action tag on base item use it
            if (menu.getUrl() != null ){
                out.println(displayStrings.getMessage("lmd.menu.actuator.link",
                            domId, getMessage(menu.getTitle()), menuClass,
                            getMessage(menu.getUrl())));
            } else {
                out.println(displayStrings.getMessage("lmd.menu.actuator.top",
                        domId,
                        getMessage(menu.getTitle()),
                        menuClass));
            }
            
            for (int i = 0; i < components.length; i++) {
                // check the permissions on this component
                if (isAllowed(components[i])) {
                    if (components[i].getMenuComponents().length > 0) {
                        out.println("<li>");

                        displayComponents(components[i], level + 1);

                        out.println(displayStrings.getMessage("lmd.menu.actuator.bottom"));
                    } else {
                        out.println(displayStrings.getMessage("lmd.menu.item",
                                                              components[i].getUrl(),
                                                              super.getMenuToolTip(components[i]),
                                                              getExtra(components[i]),
                                                              this.getMessage(components[i].getTitle())));
                    }
                }
            }

            // close the </ul> for the top menu
            if (menuClass.equals("menu")) {
                out.println("</ul>");
            }
        } else {
            if (menu.getParent() == null) {
                out.println(displayStrings.getMessage("lmd.menu.standalone",
                                                      menu.getUrl(),
                                                      super.getMenuToolTip(menu),
                                                      getExtra(menu),
                                                      getMessage(menu.getTitle())));
            } else {
                out.println(displayStrings.getMessage("lmd.menu.item",
                                                      menu.getUrl(),
                                                      super.getMenuToolTip(menu),
                                                      getExtra(menu),
                                                      getMessage(menu.getTitle())));
            }
        }
    }
}
