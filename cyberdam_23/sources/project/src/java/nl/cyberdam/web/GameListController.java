package nl.cyberdam.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.support.PagedListSourceProvider;
import org.springframework.beans.support.RefreshablePagedListHolder;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * Support the easy building of list pages. Dont forget to set the
 * LISTHOLDER_ATTR! Use referenceData() to supply extra parameters - default
 * implementation returns null. User setListSourceProvider() to set the
 * listsource, and setFilter() to set a filter object.
 */
public class GameListController extends AbstractGameController {

	private String LISTHOLDER_ATTR;
	private PagedListSourceProvider listSourceProvider;
	private Class<?> filter;
	private String view;

	public GameListController(String attributeName, String viewName,
			PagedListSourceProvider listSourceProvider, Class<?> filter) {
		LISTHOLDER_ATTR = attributeName;
		setView(view);
		setListSourceProvider(listSourceProvider);
		this.filter = filter;
	}

	public GameListController(String attributeName, String viewName) {
		LISTHOLDER_ATTR = attributeName;
		setView(view);
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

	/**
	 * use this method to supply extra data to the view in subclasses (similar
	 * to the referenceData method in the spring FormController).
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	protected Map<String, Object> referenceData(HttpServletRequest request)
			throws Exception {
		return null;
	}

	/**
	 * if you override this method in a subclass you have to implement all
	 * funtionality yourself
	 */
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		RefreshablePagedListHolder listHolder = (RefreshablePagedListHolder) request
				.getSession(true).getAttribute(LISTHOLDER_ATTR);

		if (listHolder == null) {
			listHolder = new RefreshablePagedListHolder();
			listHolder.setPageSize(gameManager.loadSystemParameters()
					.getDefaultRows());
			listHolder.setSourceProvider(getListSourceProvider());
			if (filter != null) {
				listHolder.setFilter(filter.newInstance());
			}
			request.getSession(true).setAttribute(LISTHOLDER_ATTR, listHolder);
		}

		ServletRequestDataBinder binder = new ServletRequestDataBinder(
				listHolder, LISTHOLDER_ATTR);
		binder.bind(request);

		listHolder.setLocale(RequestContextUtils.getLocale(request));
		boolean forceRefresh = request.getParameter("forceRefresh") != null;
		listHolder.refresh(forceRefresh);

		Map data = binder.getBindingResult().getModel();
		Map referenceData = referenceData(request);
		if (referenceData != null) {
			data.putAll(referenceData);
		}
		ModelAndView mav = new ModelAndView(getView(), data);
	        mav.addObject("filter", listHolder.getFilter ());
		return mav;
	}

	public PagedListSourceProvider getListSourceProvider() {
		return listSourceProvider;
	}

	public void setListSourceProvider(PagedListSourceProvider listSourceProvider) {
		this.listSourceProvider = listSourceProvider;
	}
}
