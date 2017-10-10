package nl.cyberdam.web.util;

import static org.testng.AssertJUnit.*;

import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import nl.cyberdam.domain.PlaygroundObject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.testng.annotations.Test;

@ContextConfiguration(locations = {"file:web/WEB-INF/applicationContext.xml","file:web/WEB-INF/applicationContext-acegi-security.xml",  "classpath:test-config.xml"}) 
public class PlaygroundObjectXmlConverterTest extends AbstractTransactionalTestNGSpringContextTests {

	int PARTICIPANT_ID = 1;
	
	@Test
	public void testEmpty() {

		// create empty list of playgroundobjects
		List<PlaygroundObject> l = new ArrayList<PlaygroundObject>();

		// now perform the actual test
		Writer w = new StringWriter();
		PlaygroundObjectXmlConverter.returnXmlString(l, w, null, PARTICIPANT_ID);
		String result = w.toString();
		assertNotNull(result);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><objects_for_flashmap/>",
				result);
	}

	@Test
	public void testOnlyName() {

		// create list of playgroundobjects
		PlaygroundObject p = new PlaygroundObject();
		p.setName("test");

		List<PlaygroundObject> l = new ArrayList<PlaygroundObject>();

		l.add(p);

		// now perform the actual test
		Writer w = new StringWriter();
		PlaygroundObjectXmlConverter.returnXmlString(l, w, null, PARTICIPANT_ID);
		String result = w.toString();
		assertNotNull(result);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><objects_for_flashmap><object num=\"1\"><id></id><name>test</name><url></url><thumbnail_url></thumbnail_url><description/><locationx>0</locationx><locationy>0</locationy><type></type></object></objects_for_flashmap>",
				result);
	}
	
	@Test
	public void testOnlyNameTwoElements() {

		// create list of playgroundobjects
		PlaygroundObject p = new PlaygroundObject();
		p.setName("test1");
		PlaygroundObject p2 = new PlaygroundObject();
		p2.setName("test2");

		List<PlaygroundObject> l = new ArrayList<PlaygroundObject>();

		l.add(p);
		l.add(p2);

		// now perform the actual test
		Writer w = new StringWriter();
		PlaygroundObjectXmlConverter.returnXmlString(l, w, null, PARTICIPANT_ID);
		String result = w.toString();
		assertNotNull(result);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><objects_for_flashmap>" +
				"<object num=\"1\"><id></id><name>test1</name><url></url><thumbnail_url></thumbnail_url><description/><locationx>0</locationx><locationy>0</locationy><type></type></object>" +
				"<object num=\"2\"><id></id><name>test2</name><url></url><thumbnail_url></thumbnail_url><description/><locationx>0</locationx><locationy>0</locationy><type></type></object>" +
				"</objects_for_flashmap>",
				result);
	}
	
	@Test
	public void testAllSettings() {

		// create list of playgroundobjects
		PlaygroundObject p = new PlaygroundObject();
		p.setName("test");
		p.setCaption("testdescription");
		p.setX(Integer.valueOf(123));
		p.setY(Integer.valueOf(456));

		List<PlaygroundObject> l = new ArrayList<PlaygroundObject>();

		l.add(p);

		// now perform the actual test
		Writer w = new StringWriter();
		PlaygroundObjectXmlConverter.returnXmlString(l, w, null, PARTICIPANT_ID);
		String result = w.toString();
		assertNotNull(result);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><objects_for_flashmap><object num=\"1\"><id></id><name>test</name><url></url><thumbnail_url></thumbnail_url><description>testdescription</description><locationx>123</locationx><locationy>456</locationy><type></type></object></objects_for_flashmap>",
				result);
	}

}
