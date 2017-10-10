package nl.cyberdam.web.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.menu.MenuRepository;
import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.Playground;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;

/**
 * Class representing a generic Playground menu item. This menu item is converted into multiple Struts
 * Menucomponents based on available playgrounds  
 *
 */
public class PlaygroundMenuItem extends MenuElement {

	private GameManager gameManager;

	public PlaygroundMenuItem(GameManager gameManager, String name,
			String link, String title, String roles) {
		super(name, link, title, roles);
		this.gameManager = gameManager;
	}

	public MenuRepository append(MenuRepository menurepository,
			MessageSource messageSource, GameManifest manifest) {

		User currentUser = GameUtil.getCurrentUser();
		Locale locale = (currentUser != null) ? currentUser
				.getLocale() : null;  
		MenuComponent mc = getMenuElement(messageSource, this, locale);
		List<Playground> playgroundList = null;
		playgroundList = new ArrayList<Playground>((manifest == null) ? gameManager.findAllPublicPlaygrounds() : gameManager.findAllPlaygroundsFormanifest(manifest));
		Collections.sort(playgroundList, new Comparator<Playground>() {
			public int compare(Playground o1, Playground o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		if (playgroundList != null && !playgroundList.isEmpty()) {
			for (Playground pg : playgroundList) {
				mc.setTitle(pg.getName());
				mc.setName(pg.getId().toString());
				menurepository.addMenu(transformMenuElement(mc, pg));
			}
		}
		return menurepository;
	}

	private MenuComponent transformMenuElement(MenuComponent menuComponent,
			Playground playground) {

		MenuComponent mc = null;
		if (menuComponent != null) {
			mc = new MenuComponent();
			mc.setName(menuComponent.getName());
			if (menuComponent.getLocation() != null) {
				mc.setLocation(menuComponent.getLocation() + "&playgroundId="
						+ playground.getId());
			}
			mc.setTitle(menuComponent.getTitle());
			for (MenuComponent subMenu : menuComponent.getMenuComponents()) {
				if (!"playground.htm".equals(subMenu.getName()) || (playground.getLink()!=null && !"".equals(playground.getLink()))) {
					mc.addMenuComponent(transformMenuElement(subMenu, playground));
				}	
			}
		}
		return mc;
	}

}
