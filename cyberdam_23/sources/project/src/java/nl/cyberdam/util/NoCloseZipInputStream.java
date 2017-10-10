package nl.cyberdam.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipInputStream;

/**
 * Jaxb's unmarshall method always closes the ZipInputStream, but this prevents us from reading further resources out of the stream.
 * Thus we need to fake out the close method and create a custom one.
 * See http://weblogs.java.net/blog/kohsuke/archive/2005/07/socket_xml_pitf.html for details.
 */

public class NoCloseZipInputStream extends ZipInputStream {
	public NoCloseZipInputStream(InputStream in) {
		super(in);
	}

	public void close() {
	} // ignore close

	public void realyClose() throws IOException {
		super.close();
	}
}