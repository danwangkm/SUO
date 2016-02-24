package wangda.sparkii.suo.xml;

import java.io.ByteArrayOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XmlEncoder {
	private static String generateXmlStringByDocument(Document document)
			throws Exception {
		// step9: get a TransformerFactory object
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		// step10: get a Transformer object
		Transformer transformer = transformerFactory.newTransformer();
		// step11: use DOMSource object to encapsulate a document
		DOMSource source = new DOMSource(document);
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.VERSION, "1.0");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		transformer.transform(source, new StreamResult(bos));
		String xmlString = bos.toString();

		return xmlString;
	}

	private static Document createEmptyDocument() throws Exception {
		// step1: get a DocumentBuilderFactory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// step2: get a DocumentBuilder
		DocumentBuilder db = factory.newDocumentBuilder();
		// step3: create a document
		Document document = db.newDocument();

		return document;
	}

	/**
	 * transform explanation into xml string
	 * 
	 * @param name
	 * @param user
	 * @param exp
	 * @return
	 * @throws Exception
	 */
	public static String encodeExp(String name, String user, String exp)
			throws Exception {
		Document document = createEmptyDocument();

		Element rootElement = document.createElement("word");

		rootElement.setAttribute("name", name);
		rootElement.setAttribute("user", user);
		rootElement.setTextContent(exp);

		document.appendChild(rootElement);

		return generateXmlStringByDocument(document);
	}
}
