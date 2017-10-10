package nl.cyberdam.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.HashMap;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;

import nl.cyberdam.domain.ObjectFactory;
import nl.cyberdam.domain.Resource;
import nl.cyberdam.domain.Root;
import nl.cyberdam.domain.SystemParameters;
import nl.cyberdam.domain.User;
import nl.cyberdam.util.NoCloseZipInputStream;
import nl.cyberdam.web.XmlTransformException;

import org.springframework.web.context.ServletContextAware;
import org.xml.sax.SAXException;

public abstract class ExchangeManager<K> extends DefaultService implements ServletContextAware{
	public static final String SWF_NOT_SAVED = "SWF_NOT_SAVED";

	protected GameManager gameManager;

	public static class ExchangeBean<K> {
		NoCloseZipInputStream zipFile;

		K exchangeObject;

		public K getExchangeObject() {
			return exchangeObject;
		}

		public void setExchangeObject(K exchangeObject) {
			this.exchangeObject = exchangeObject;
		}

		public NoCloseZipInputStream getZipFile() {
			return zipFile;
		}

		public void setZipFile(NoCloseZipInputStream zipFile) {
			this.zipFile = zipFile;
		}

	}

	private ServletContext servletContext;

	protected void zip(ByteArrayOutputStream xml, InputStream is,
			OutputStream os, HashMap<String, ByteArrayInputStream> resources)
			throws IOException {

		ZipOutputStream out = null;
		try {
			out = new ZipOutputStream(os);
			out.setLevel(Deflater.DEFAULT_COMPRESSION);
			out.putNextEntry(new ZipEntry("xml"));
			out.write(xml.toByteArray());
			out.closeEntry();

			if (resources != null && !resources.isEmpty()) {
				for (String key : resources.keySet()) {
					writeEntry(key, resources.get(key), out);
				}
			}
			writeEntry("swf", is, out);

			xml.close();
			os.flush();
			out.close();
		} catch (SocketException iae) {
			// Client cancel
		}
	}

	private void writeEntry(String key, InputStream in, ZipOutputStream out)
			throws IOException {
		byte[] buffer = new byte[18024];
		if (in != null) {
			out.putNextEntry(new ZipEntry(key));
			int len;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);

			}
			out.closeEntry();
			in.close();
		}
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

	public String getNext(NoCloseZipInputStream inputStream) throws IOException {

		ZipEntry entry = null;
		if ((entry = inputStream.getNextEntry()) != null) {

			return entry.getName();
		}
		return null;
	}

	public abstract void export(K k, OutputStream zip, String swfPath) throws IOException,
			JAXBException;

	public abstract HashMap<String, Integer> save(ExchangeBean<K> k,
			String playgroundUri, User user, String swfPath) throws IOException, JAXBException;

	public abstract void setExchangeObject(ExchangeBean<K> k, Root root);

	public abstract boolean existsExchangeObject(K exchangeObject, String uri);

	public ByteArrayOutputStream marshall(Root root) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance("nl.cyberdam.domain");

		Marshaller marshaller = jaxbContext.createMarshaller();
		SchemaFactory sf = SchemaFactory
				.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		try {
			marshaller.setSchema((sf.newSchema(new File(getServletContext()
					.getRealPath(SystemParameters.XMLSCHEMA)))));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Xmlschema doesn't exist. Schemaname:"
					+ SystemParameters.XMLSCHEMA);
		}
		marshaller.setEventHandler(new XmlTransformException());
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		marshaller.marshal(root, baos);
		return baos;
	}

	public ExchangeBean<K> getExchangeBean() {
		return new ExchangeBean<K>();
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public NoCloseZipInputStream getNewNoCloseinputStream(InputStream is) {
		return new NoCloseZipInputStream(is);
	}

	public void setGameManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}

	public void imp(ExchangeBean<K> exchangeBean)
			throws IOException, JAXBException, SAXException {
		JAXBContext jaxbContext;
		jaxbContext = JAXBContext.newInstance("nl.cyberdam.domain");

		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		unmarshaller.setEventHandler(new XmlTransformException());
		SchemaFactory sf = SchemaFactory
				.newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
		unmarshaller.setSchema((sf.newSchema(new File(getServletContext()
				.getRealPath(SystemParameters.XMLSCHEMA)))));
		String xmlName = getNext(exchangeBean.getZipFile());
		ObjectFactory factory = new ObjectFactory();
		Root xml = factory.createRoot();
		if (xmlName != null) {
			JAXBElement<?> element = (JAXBElement<?>) unmarshaller
					.unmarshal(exchangeBean.getZipFile());
			xml = (Root) element.getValue();
			setExchangeObject(exchangeBean, xml);
		}
	}

	protected void getResource(Resource resource,
			HashMap<String, ByteArrayInputStream> resources) {
		if (resource != null && resource.getContent() != null) {
			String name = resource.getFileName().substring(
					resource.getFileName().lastIndexOf(".") + 1);
			resources.put(resource.getId().toString().concat(".").concat(name),
					new ByteArrayInputStream(resource.getContent()));
		}
	}

	protected void setResource(Resource resource, String name,
			ByteArrayOutputStream out) {

		if (resource != null && resource.getId().toString().equals(name)) {
			resource.setContent(out.toByteArray());

		}
	}
	protected String getMLCodeFromClass(String className) {
		return "import.summary."+className+"count";
	}
}
