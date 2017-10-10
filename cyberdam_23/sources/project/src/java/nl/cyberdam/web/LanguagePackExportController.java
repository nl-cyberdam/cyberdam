package nl.cyberdam.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.SchemaFactory;

import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.multilanguage.LanguagePack;
import nl.cyberdam.multilanguage.MultiLanguageSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class LanguagePackExportController extends AbstractController {
	private Log log = LogFactory.getLog(getClass());
	private MultiLanguageSource messageSource;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Long languagePackId = new Long(ServletRequestUtils.getRequiredLongParameter(
				request, "languagePack"));
		LanguagePack languagePack = getMessageSource().getLanguagePack(
				languagePackId);
		
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment;filename=\""+languagePack.getLocale()+".xml.zip\"");
        ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
        zos.putNextEntry(new ZipEntry(languagePack.getLocale() + ".xml"));
       
        ByteArrayOutputStream bas = new ByteArrayOutputStream();
        
        try {
        	generateXml(languagePack, bas);
        } catch(JAXBException e) {
        	e.printStackTrace();
        	log.error(e);
        }
        
        zos.write(bas.toByteArray());
        zos.closeEntry();
        response.getOutputStream().flush();
        zos.close();
        
		return null;
	}

	protected void generateXml(LanguagePack languagePack, ByteArrayOutputStream bas) throws JAXBException, IOException {
		JAXBContext jaxbContext = JAXBContext.newInstance(LanguagePack.class);
		Marshaller marshaller = jaxbContext.createMarshaller();

		SchemaFactory sf = SchemaFactory.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			marshaller.setSchema((sf.newSchema(new File(getServletContext()
					.getRealPath(SystemParameters.XMLSCHEMA)))));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Xmlschema doesn't exist. Schemaname:"
					+ SystemParameters.XMLSCHEMA);
		}
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.setProperty( "jaxb.encoding", "UTF-8" );
		
		OutputStreamWriter osw = new OutputStreamWriter(bas, "UTF-8");
		
		StringBuilder sb = new StringBuilder();
		marshaller.marshal(languagePack, new CustomContentHandler(sb));
		
		osw.write(sb.toString());
		osw.flush();
	}

	public MultiLanguageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(MultiLanguageSource messageSource) {
		this.messageSource = messageSource;
	}

}

/**
 * This custom handler exists purely to make the output xml use <![[CDATA escaping around
 * the message elements, and 'normal' escaping otherwise. It is an incomplete handler with
 * special cases for the xml produced by the langugage pack exporter, it is not meant to be
 * used for other situations. (And it does not handle attributes at all!)
 */
class CustomContentHandler implements org.xml.sax.ContentHandler {

	private StringBuilder sb;
	private boolean writeAsCDATA = false;
	private int indentLevel = 0;
	private boolean closedElement = false;
	
	static String MESSAGE = "message";
	
	public CustomContentHandler(StringBuilder sb) {
		this.sb = sb;
	}
	
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (writeAsCDATA) {
			sb.append("<![CDATA[");
			sb.append(ch, start, length);
			sb.append("]]>");
		} else {
			writeEscapedCharacters(ch, start, length, sb);
		}
	}
	
	// escape normal character data (not for use in attributes - they need " escaped as well 
	public void writeEscapedCharacters(char[] ch, int start, int length, StringBuilder sb) {
        int limit = start+length;
        for (int i = start; i < limit; i++) {
            switch (ch[i]) {
            case '&':
            	sb.append("&amp;");
                break;
            case '<':
            	sb.append("&lt;");
                break;
            case '>':
            	sb.append("&gt;");
                break;
            default:
                if (ch[i] > '\u007f') {
                	sb.append("&#");
                	sb.append(Integer.toString(ch[i]));
                	sb.append(';');
                } else {
                	sb.append(ch[i]);
                }
            }
        }
    }


	public void endDocument() throws SAXException {
		// EMPTY
	}

	public void endElement(String uri, String localName, String name)
			throws SAXException {
		if (MESSAGE.equals(name)) {
			writeAsCDATA = false;
		}
		indentLevel--;
		if (closedElement) {
			// newline and indent
			sb.append("\n");
			for (int i = 0; i < indentLevel; i++) {
				sb.append("  ");
			}
		}
		sb.append("</" + name + ">");
		closedElement = true;
	}

	public void endPrefixMapping(String prefix) throws SAXException {
		// EMPTY
	}

	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		sb.append(ch, start, length);
	}

	public void processingInstruction(String target, String data)
			throws SAXException {
		// EMPTY
	}

	public void setDocumentLocator(Locator locator) {
		// EMPTY
	}

	public void skippedEntity(String name) throws SAXException {
		// EMPTY
	}

	public void startDocument() throws SAXException {
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
	}

	public void startElement(String uri, String localName, String name,
			Attributes atts) throws SAXException {
		if (MESSAGE.equals(name)) {
			writeAsCDATA = true;
		}
		// newline and indent
		sb.append("\n");
		for (int i = 0; i < indentLevel; i++) {
			sb.append("  ");
		}
		sb.append("<" + name + ">");
		indentLevel++;
		closedElement = false;
	}

	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		// EMPTY
	}
	
}
