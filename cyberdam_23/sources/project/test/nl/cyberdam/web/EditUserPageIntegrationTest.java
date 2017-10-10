package nl.cyberdam.web;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;

import net.sf.navigator.menu.MenuLoader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.ModelAndView;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"file:web/WEB-INF/applicationContext.xml", 
		"file:web/WEB-INF/applicationContext-acegi-security.xml", 
		"file:web/WEB-INF/dispatcher-servlet.xml",  
		"classpath:test-config.xml",
		"classpath:test-mockservletcontext.xml"})
public class EditUserPageIntegrationTest {
	
	@Resource
	private EditUserController editUserController;
	
	@Resource
	private MenuLoader menuLoader;
	
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	
	@Before
	public void setup() {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		menuLoader.setMenuConfig("../web/WEB-INF/menu-config.xml");
	}
	
	@Test
	public void testRequest() throws Exception {
		request.setMethod("GET");
		ModelAndView mav = editUserController.handleRequest(request, response);
		assertNotNull(mav);
//		List<SpecialDeal> specials = (List<SpecialDeal>) mav.getModel().get("specials");
//		assertNotNull(specials);
//		assertTrue(specials.size() > 0);
		
	}
}
