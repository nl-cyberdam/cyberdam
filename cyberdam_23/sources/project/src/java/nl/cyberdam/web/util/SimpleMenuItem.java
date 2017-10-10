package nl.cyberdam.web.util;

import net.sf.navigator.menu.MenuRepository;
import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.User;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;

/** Class representing a internal menu item that is convertable to exactly one Struts Menucomponent
 * 
 */
public class SimpleMenuItem extends MenuElement {

	public SimpleMenuItem(String name, String link, String title, String roles) {
		super(name, link, title, roles);
	}

	public MenuRepository append(MenuRepository menurepository,
			MessageSource messageSource, GameManifest manifest) {
		User currentUser = GameUtil.getCurrentUser();

		if (currentUser != null) {
			menurepository.addMenu(getMenuElement(messageSource, this,
					currentUser.getLocale()));
		}

		return menurepository;
	}

}
