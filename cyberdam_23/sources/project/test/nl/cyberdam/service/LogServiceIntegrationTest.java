package nl.cyberdam.service;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.annotation.Resource;

import nl.cyberdam.domain.LogEntry;

import org.acegisecurity.Authentication;
import org.acegisecurity.context.SecurityContextHolder;
import org.acegisecurity.providers.AuthenticationProvider;
import org.acegisecurity.providers.UsernamePasswordAuthenticationToken;
import org.hibernate.SessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={ "file:web/WEB-INF/applicationContext.xml",
								  "file:web/WEB-INF/applicationContext-acegi-security.xml",
		                          "classpath:test-config.xml" })
@Transactional
public class LogServiceIntegrationTest {

	@Resource
	private UserManager userManager;
	@Resource
	private SessionFactory sessionFactory;
	@Resource
	private AuthenticationProvider provider;
	@Resource
	private LogService logService;

	@Test
	public void testLog() {

		// add a user that we are going to authenticate as...
		userManager.addUser("testuser", "dummy@example.com", "testpass");

		// insert user in acegi security context
		// AuthenticationProvider provider = (AuthenticationProvider)
		// ctx.getBean("authenticationProvider");
		// provider is retrieved from the spring config (see top of class)
		Authentication auth = provider
				.authenticate(new UsernamePasswordAuthenticationToken(
						"testuser", "testpass"));
		SecurityContextHolder.getContext().setAuthentication(auth);

		logService.addLog("hallo log!");

		List<LogEntry> entries = logService.findAllLogEntries("");
		assertTrue("There should be exactly one LogEntry, but there were: "
				+ entries.size(), 1 == entries.size());

	}

}
