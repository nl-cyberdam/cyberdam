package nl.cyberdam.web.util;

import java.io.Writer;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import nl.cyberdam.domain.PlaygroundObject;
import nl.cyberdam.domain.Resource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class PlaygroundObjectXmlConverter {

	/**
	 * pass in a set of playgroundobjects and get it back as an xml string :-)
	 * 
	 * @param contextPath the result of request.getContextPath() - no trailing slash
	 */
	public static void returnXmlString(
			List<PlaygroundObject> playgroundObjects, Writer writer, String contextPath, long participantId) {

		// Build the request document
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return;
		}
		Document playgroundxml = builder.newDocument();

		Element allObjects = playgroundxml
				.createElement("objects_for_flashmap");
		playgroundxml.appendChild(allObjects);

		// counter for numbering the objects (needed by the flash code)
		int counter = 1;
		for (PlaygroundObject po : playgroundObjects) {
			// <object num="1">
			// <id>sd001</id>
			// <name>Achterhaven</name>
			// <url>object/sd-001</url>
			// <thumbnail_url>object-content/th_sd001.jpg</thumbnail_url>
			// <description>De Achterhaven is een van de drie oude havens van
			// Sieberdam.</description>
			// <locationx>100</locationx>
			// <locationy>100</locationy>
			// <type>btn_club</type>
			// </object>
			Element playgroundElement = playgroundxml.createElement("object");
			playgroundElement.setAttribute("num", Integer.toString(counter++));
			
			Element id = playgroundxml.createElement("id");
			Text idText = playgroundxml.createTextNode(po.getId().toString());
			id.appendChild(idText);
			playgroundElement.appendChild(id);
			
			Element name = playgroundxml.createElement("name");
			String n = po.getName();
			if (n == null) {
				n = "";
			}
			Text nameText = playgroundxml.createTextNode(n);
			name.appendChild(nameText);
			playgroundElement.appendChild(name);
			
			Element url = playgroundxml.createElement("url");
			String pourl = contextPath + "/playgroundobject.htm?participantId=" + ((participantId != 0) ? participantId : "") +"&playgroundObjectId=" + po.getId().toString();
			Text urlText = playgroundxml.createTextNode(pourl);
			url.appendChild(urlText);
			playgroundElement.appendChild(url);
			
			Element thumbnail_url = playgroundxml
					.createElement("thumbnail_url");
			Resource thumbResource = po.getThumbnail();
			String t;
			if (thumbResource == null) {
				t = "";
			} else {
			  t = contextPath + "/resource?resourceId=" + thumbResource.getId().toString();
			}
			Text thumbText = playgroundxml.createTextNode(t);
			thumbnail_url.appendChild(thumbText);
			playgroundElement.appendChild(thumbnail_url);
			
			Element description = playgroundxml.createElement("description");
			String caption;
			if (po.getCaption() != null) {
				caption = po.getCaption();
			} else {
				caption = "x";
			}
			Text descriptionText = playgroundxml.createTextNode(caption);
			description.appendChild(descriptionText);
			playgroundElement.appendChild(description);
			
			Element locationx = playgroundxml.createElement("locationx");
			Text xText = playgroundxml.createTextNode(po.getX().toString());
			locationx.appendChild(xText);
			playgroundElement.appendChild(locationx);
			Element locationy = playgroundxml.createElement("locationy");
			Text yText = playgroundxml.createTextNode(po.getY().toString());
			locationy.appendChild(yText);
			playgroundElement.appendChild(locationy);
			
			Element type = playgroundxml.createElement("type");
			String potype = ""; 
			switch(po.getCategory()) {
			case ASSOCIATION: potype = "btn_instelling"; break;
			case ENTERPRISE: potype = "btn_ondernemingen"; break;
			case GOVERNMENT_BODY: potype = "btn_overheid"; break;
			case PHOTO_SHOT: potype = "btn_foto"; break;
			case PRIVATE_HOUSEHOLD: potype = "btn_personen"; break;
			case TBD: potype = "btn_tbd"; break;
			case UTILITY: potype = "btn_infrastructuur"; break;
			}
			Text typeText = playgroundxml.createTextNode(potype);
			type.appendChild(typeText);
			playgroundElement.appendChild(type);

			// add newly create object to all objects node
			allObjects.appendChild(playgroundElement);
		}

		TransformerFactory xformFactory = TransformerFactory.newInstance();
		try {
			Transformer idTransform = xformFactory.newTransformer();
			Source input = new DOMSource(playgroundxml);
			Result output = new StreamResult(writer);
			idTransform.transform(input, output);
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

}
