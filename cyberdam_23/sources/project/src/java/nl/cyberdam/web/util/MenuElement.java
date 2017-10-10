package nl.cyberdam.web.util;

import java.util.ArrayList;
import java.util.Locale;

import net.sf.navigator.menu.MenuComponent;
import net.sf.navigator.menu.MenuRepository;
import nl.cyberdam.domain.GameManifest;

import org.springframework.context.MessageSource;

/*
 * Internal menu representation base class
 * 
 */
public abstract class MenuElement {
	private String name;
	private String title;
	private String link;
	private ArrayList<MenuElement> subMenus;
	private String roles;

	public MenuElement(String name, String link, String title, String roles) {
		this();
		this.name = name;
		this.title = title;
		this.link = link;
		this.roles = roles;
	}

	public MenuElement() {
		subMenus = new ArrayList<MenuElement>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public ArrayList<MenuElement> getSubMenus() {
		return subMenus;
	}

	public void addSubMenu(MenuElement me) {
		this.subMenus.add(me);
	}

	public String getRoles() {
		return roles;
	}

	public void setRoles(String roles) {
		this.roles = roles;
	}

	public MenuComponent getMenuElement(MessageSource messageSource,
			MenuElement element, Locale locale) {
		MenuComponent menu = null;
		if (element != null) {
			menu = new MenuComponent();
			menu.setName(element.getName());
			if (element.getTitle() != null) {
				menu.setTitle(messageSource.getMessage(element.getTitle(),
						null, locale));
			}
			menu.setLocation(element.getLink());
			menu.setRoles(element.getRoles());
			for (MenuElement subElement : element.getSubMenus()) {
				MenuComponent mc = getMenuElement(messageSource, subElement,
						locale);
				menu.addMenuComponent(mc);
			}
		}
		return menu;
	}
	
	public abstract MenuRepository append(MenuRepository menurepository, MessageSource messageSource, GameManifest manifest);

}