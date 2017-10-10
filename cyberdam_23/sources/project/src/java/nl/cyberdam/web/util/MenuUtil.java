package nl.cyberdam.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.navigator.menu.MenuRepository;
import nl.cyberdam.domain.GameManifest;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;
import nl.cyberdam.util.GameUtil;

import org.springframework.context.MessageSource;

/**
 * Service that provides translation between internal and Struts menu representation  
 *
 */
public class MenuUtil {

	public static final String INDEX_MENU = "INDEX_MENU";
	public static final String GAME_MENU = "GAME_MENU";
	public static final String SYSTEM_MENU = "SYSTEM_MENU";
	public static final String PLAYGROUND_MENU = "PLAYGROUND_MENU";
	public static final String MENU_REPOSITORY_KEY = "menurepository";

	MessageSource messageSource;
	GameManager gameManager;

	Map<String, ArrayList<MenuElement>> menu;
	ArrayList<MenuElement> indexMenu;

	public MenuUtil() {
		menu = new HashMap<String, ArrayList<MenuElement>>();
		indexMenu = new ArrayList<MenuElement>();
	}

	@SuppressWarnings("unused")
	private void initialize() {
		indexMenu
				.add(new SimpleMenuItem("playgroundlistpage.htm",
						"playgroundlistpage.htm", "playgroundlistpage.link",
						"ROLE_PLAYGROUNDAUTHOR,ROLE_LCMSADMININSTRATOR,ROLE_SYSTEMADMINISTRATOR"));
		indexMenu.add(new SimpleMenuItem("playgroundwindow.htm",
				"javascript:raw_popup_returnfalse('playgroundwindow.htm','playground','width=800,height=800,resizable=yes,scrollbars=yes')",
				"playgroundwindow", null));
		indexMenu
				.add(new SimpleMenuItem("gameauthor.htm", "gameauthor.htm",
						"link.gameauthor",
						"ROLE_GAMEAUTHOR,ROLE_LCMSADMININSTRATOR,ROLE_SYSTEMADMINISTRATOR"));

		indexMenu
				.add(new SimpleMenuItem("gamemanifestcomposer.htm",
						"gamemanifestcomposer.htm",
						"link.gamemanifestcomposer",
						"ROLE_GAMEMANIFESTCOMPOSER,ROLE_LCMSADMININSTRATOR,ROLE_SYSTEMADMINISTRATOR"));
		indexMenu
				.add(new SimpleMenuItem(
						"gamemaster.htm",
						"gamemaster.htm",
						"link.gamemaster",
						"ROLE_GAMESESSIONMASTER,ROLE_LMSADMINISTRATOR,ROLE_VLEADMINISTRATOR,ROLE_SYSTEMADMINISTRATOR"));
		indexMenu.add(new SimpleMenuItem("gameparticipant.htm",
				"gameparticipant.htm?forceRefresh=true", "link.gameparticipant", null));
		indexMenu.add(new SimpleMenuItem("useradministrator.htm",
				"useradministrator.htm", "link.useradministratorpage",
				"ROLE_USERADMINISTRATOR,ROLE_SYSTEMADMINISTRATOR"));
		indexMenu.add(new SimpleMenuItem("systemadministration.htm",
				"systemadministration.htm", "link.systemadministrationpage",
				"ROLE_SYSTEMADMINISTRATOR"));
		menu.put(INDEX_MENU, indexMenu);

		ArrayList<MenuElement> gameMenu = new ArrayList<MenuElement>();

		gameMenu.add(new SimpleMenuItem("session.htm",
				"session.htm?participantId=${participant.id}", "sessionhome",
				null));

		gameMenu.add(new SimpleMenuItem("messages.htm",
				"messages.htm?participantId=${participant.id}", "messages",
				null));

		gameMenu.add(new SimpleMenuItem("activities.htm",
				"activities.htm?participantId=${participant.id}", "activities",
				null));

		gameMenu.add(new SimpleMenuItem("filedirectory.htm",
				"filedirectory.htm?participantId=${participant.id}",
				"filedirectory", null));

		gameMenu.add(new SimpleMenuItem("playgroundwindow.htm",
				"javascript:raw_popup_returnfalse('playgroundwindow.htm%3fparticipantId=${participant.id}','playground','width=800,height=800,resizable=yes,scrollbars=yes')",
				"playgroundwindow", null));

		gameMenu.add(new SimpleMenuItem("gameintro.htm",
				"gameintro.htm?participantId=${participant.id}", "gameintro",
				null));

		gameMenu.add(new SimpleMenuItem("aboutsession.htm",
				"aboutsession.htm?participantId=${participant.id}", "about",
				null));

		menu.put(GAME_MENU, gameMenu);

		MenuElement element = new PlaygroundMenuItem(gameManager,"playgroundMenu", null, null,
				null);
//		gameMenu.add(element);
		element.addSubMenu(new SimpleMenuItem("playground.htm",
				"playground.htm?participantId=${participant.id}",
				"playgroundmap", null));
		element.addSubMenu(new SimpleMenuItem("playgrounddirectory.htm",
				"playgrounddirectory.htm?sort.property=name&participantId=${participant.id}",
				"playgrounddirectory", null));
		element.addSubMenu(new SimpleMenuItem("playgroundintro.htm",
				"playgroundintro.htm?participantId=${participant.id}",
				"playgroundintro", null));
		ArrayList<MenuElement> playgroundMenu = new ArrayList<MenuElement> ();
		playgroundMenu.add (element);
		menu.put (PLAYGROUND_MENU, playgroundMenu);

		ArrayList<MenuElement> systemMenu = new ArrayList<MenuElement>();

		systemMenu.add(new SimpleMenuItem("systemparameters.htm",
				"systemparameters.htm", "link.systemparameters",
				"ROLE_SYSTEMADMINISTRATOR"));
		systemMenu.add(new SimpleMenuItem("languagepacks.htm",
				"languagepacks.htm", "link.languagepacks",
				"ROLE_SYSTEMADMINISTRATOR"));
		systemMenu.add(new SimpleMenuItem("log.htm", "log.htm", "link.log",
				"ROLE_SYSTEMADMINISTRATOR"));
		systemMenu.add(new SimpleMenuItem("housekeeping.htm", "housekeeping.htm",
				"link.housekeeping", "ROLE_SYSTEMADMINISTRATOR"));
		systemMenu.add(new SimpleMenuItem("swfdirectory.htm", "swfdirectory.htm",
				"link.swfdirectory", "ROLE_SYSTEMADMINISTRATOR"));
		systemMenu.add(new SimpleMenuItem("variable.htm", "variable.htm",
				"link.variable", "ROLE_SYSTEMADMINISTRATOR"));

		menu.put(SYSTEM_MENU, systemMenu);
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
	/**
	 * Generates Struts menu repository for the Struts menu jsp tags
	 * 
	 */
	public void generateMenu(HttpServletRequest request, String menuType) {
		generateMenu(request, menuType, null);
	}
	
	public void generateMenu(HttpServletRequest request, String menuType, GameManifest manifest) {
		MenuRepository repository = new MenuRepository();
		MenuRepository defaultRepository = (MenuRepository) request
				.getSession().getServletContext().getAttribute(
						MenuRepository.MENU_REPOSITORY_KEY);
		repository.setDisplayers(defaultRepository.getDisplayers());

		User currentUser = GameUtil.getCurrentUser();

		//if (true) {// currentUser != null) {
			ArrayList<MenuElement> menuData = menu.get(menuType);
			if (menuData != null) {

				for (MenuElement mc : menuData) {
					repository = mc.append(repository, messageSource, manifest);
				}
			}
			request.setAttribute(MENU_REPOSITORY_KEY, repository);
		//}
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
}
