package nl.cyberdam.web;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractFormController;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.WebUtils;


public class SwfUploadController extends AbstractFormController {

	private String swfPath;
	  
   public SwfUploadController() {
        setCommandClass(FileUploadBean.class);
        setSessionForm(true);
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request) throws Exception {
    	return new FileUploadBean();
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request,
			HttpServletResponse response, Object command, BindException errors)
			throws Exception {
    	    	
    	// cast the bean
    	FileUploadBean bean = (FileUploadBean) command;
        
    	// check for _cancel
    	if(WebUtils.hasSubmitParameter(request, "_cancel")) {
    		return new ModelAndView(new RedirectView("swfdirectory.htm", true));
    	}
        
        // let's see if there's content there
        MultipartFile file = bean.getFile();
        FileOutputStream out; // declare a file output object
     
        try
        
        {
                out = new FileOutputStream(new File(getServletContext().getRealPath(swfPath),
                		                   file.getOriginalFilename()));
                write(file.getInputStream(), out);
                out.close();
        }
        catch (Exception e)
        {
        	errors.reject("file.error");
        }
        // check is there were errors - if so: display the upload form again
        if (errors.hasErrors()) {
        	return showForm(request, response, errors);
        }

        
        // well, let's do nothing with the bean for now and return
        return new ModelAndView(new RedirectView("swfdirectory.htm?forceRefresh=true", true));
    }
    
	@Override
	protected ModelAndView showForm(HttpServletRequest request,
			HttpServletResponse response, BindException errors)
			throws Exception {
		
		Map<String, Object> data = new HashMap<String, Object>();
			
		if (errors.hasErrors()) {
			// if we had any errors: add them to the 'errors' list?
			data.put("errors", errors.getGlobalErrors());
		}	
		
		return new ModelAndView("swfupload", data);
	}
	
	protected void write(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[18024];
		if (in != null) {
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);

			}
			in.close();
		}
		out.flush();
	}
	
	 public void setSwfPath(String swfPath) {
			logger.info("setSwfPath: " + swfPath);
			this.swfPath = swfPath;
		}
}