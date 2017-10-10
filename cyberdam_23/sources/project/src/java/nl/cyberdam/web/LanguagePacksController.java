package nl.cyberdam.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.multilanguage.LanguagePack;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

public class LanguagePacksController extends AbstractController {
	protected final static String ATTR_LIST = "list";
	protected SessionFactory sessionFactory;
	private String view;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = new ModelAndView(getView());
		Session session = getSessionFactory().openSession();
		modelAndView.addObject(ATTR_LIST, LanguagePack.findAll(session));
		session.close();
		return modelAndView;
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	// Injected by Spring
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public String getView() {
		return view;
	}

	// Injected by Spring
	public void setView(String view) {
		this.view = view;
	}

}
