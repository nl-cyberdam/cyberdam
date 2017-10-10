package nl.cyberdam.web;

import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nl.cyberdam.domain.LogEntry;
import nl.cyberdam.service.PasswordGenerator;
import nl.cyberdam.tools.UserImporter;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 * Process the incoming file, create users in the system and show the results.
 */
public class UploadUsersController extends AbstractGameController {

	private PasswordGenerator passwordGenerator;
	private UserImporter userImporter;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = (MultipartFile) multipartRequest.getFile("file");
		StringWriter output = new StringWriter();
		
		getGameManager().getGameLogService().addLog(LogEntry.Module.SYSTEM, "uploading userfile", file.getOriginalFilename());
		boolean briefFormat = request.getParameter("brief") != null;
		userImporter.addUsers(new InputStreamReader(file.getInputStream()), output, getUserManager(), 
				passwordGenerator, briefFormat ? UserImporter.NO_ADMIN_FLAGS : UserImporter.WITH_ADMIN_FLAGS);
		
		ModelAndView mav = new ModelAndView("uploadusers", "result", output);
		mav.addObject("returnView", briefFormat ? "gamemaster" : "useradministration");
		return mav;
	}

	/**
	 * should be injected 
	 */
	public void setPasswordGenerator(PasswordGenerator passwordGenerator) {
		this.passwordGenerator = passwordGenerator;
	}

	/**
	 * should be injected 
	 */
	public void setUserImporter(UserImporter userImporter) {
		this.userImporter = userImporter;
	}

}
