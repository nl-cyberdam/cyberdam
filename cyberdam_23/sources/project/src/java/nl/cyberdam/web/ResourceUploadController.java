package nl.cyberdam.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.GameModel;
import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Resource;
import nl.cyberdam.domain.User;
import nl.cyberdam.service.GameManager;

import org.acegisecurity.context.SecurityContextHolder;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;




public class ResourceUploadController extends SimpleFormController {

    private GameManager gameManager;

    public ResourceUploadController() {
        setCommandClass(ResourceUploadBean.class);
        setSessionForm(true);
    }

    public void setGameManager(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
        ResourceUploadBean b = new ResourceUploadBean();
        ServletRequestDataBinder binder = new ServletRequestDataBinder(b);
        // register custom editors, if desired
        // binder.registerCustomEditor(...);
        // trigger actual binding of request parameters
        binder.bind(request);
        // optionally evaluate binding errors
        // Errors errors = binder.getBindingResult(). getErrors();
        return b;
    }

    @Override
	protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
    	    	
    	// cast the bean
        ResourceUploadBean bean = (ResourceUploadBean) command;
    	// check for _cancel
    	if(WebUtils.hasSubmitParameter(request, "_cancel")) {
    		return new ModelAndView(new RedirectView(bean.getRedirectTo() + "#resources_section", true), "id", bean.getId());
    	}

        // let's see if there's content there
        MultipartFile file = bean.getFile();
        if (file.getSize() > gameManager.loadSystemParameters().getUploadSizeMaxBytes()) {
        	errors.reject("filesize.too.large");
        }

        // check if there were errors - if so: display the upload form again
        if (errors.hasErrors()) {
        	return showForm(request, response, errors);
        }
        Resource r = new Resource();
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        r.setName(file.getOriginalFilename());
        r.setFileName(file.getOriginalFilename());
        r.setFileSize((int) file.getSize());
        r.setLastModified(new Date());
        r.setCreated(new Date());
        r.setContentType(file.getContentType());
        r.setContent(file.getBytes());
        r.setCreator(currentUser);
        r.setLastModifier(currentUser);
        
		String url = bean.getRedirectTo();
		if (bean.getTarget().length() > 0) {
			PlaygroundObject p = gameManager.loadPlaygroundObject(bean.getId());

			if ("thumbnail".equals(bean.getTarget())) {
				p.setThumbnail(r);
			} else if ("picture".equals(bean.getTarget())) {
				p.setPicture(r);
			}
			gameManager.save(p);
		} else {
			GameModel m = gameManager.load(bean.getId());
			m.addResource(r);
			gameManager.save(m);
			url += "#resources_section";
		}

        // well, let's do nothing with the bean for now and return
		ModelAndView mav = new ModelAndView(new RedirectView(url, true));
		mav.addObject("id", bean.getId());
		mav.addObject("forceRefresh", "true");
		return mav;
    }
    
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		
		Map<String, Object> data = new HashMap<String, Object>();
		String target = ServletRequestUtils.getStringParameter(request, "target");
		if (target != null) {
			// this means id refers to a playground
			Long id = new Long(ServletRequestUtils.getRequiredLongParameter(request, "id"));
        	PlaygroundObject p = gameManager.loadPlaygroundObject(id);
        	// this is to set the breadcrumb correctly
			data.put("playgroundId", p.getPlayground().getId());
		}
		if (errors.hasErrors()) {
			// if we had any errors: add them to the 'errors' list?
			data.put("errors", errors.getGlobalErrors());
		}	
		
		return new ModelAndView("resourceupload", data);
	}
}