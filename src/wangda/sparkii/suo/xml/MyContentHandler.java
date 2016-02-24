package wangda.sparkii.suo.xml;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import wangda.sparkii.suo.concept.WordExpInfo;

public class MyContentHandler extends DefaultHandler {
	private List<WordExpInfo> infos = null;
	private WordExpInfo wordExp = null;
	private String name, expid, user, exp;
	private String tagName;

	public MyContentHandler(List<WordExpInfo> infos) {
		super();
		this.infos = infos;
	}

	public List<WordExpInfo> getInfos() {
		return infos;
	}

	public void setInfos(List<WordExpInfo> infos) {
		this.infos = infos;
	}

	public void startDocument() throws SAXException {
		System.out.println("MyContentHandler: ````````begin parser````````");
	}

	public void endDocument() throws SAXException {
		System.out.println("MyContentHandler: ````````end parser````````");
	}

	public void startElement(String namespaceURI, String localName,
			String qName, Attributes attr) throws SAXException {
		this.tagName = localName;
		if (localName.equals("exp")) {
			// get tag attributes and put them into WordExpInfo
			wordExp = new WordExpInfo();
			expid = attr.getValue(0);
			user = attr.getValue(1);
		}
	}

	public void endElement(String namespaceURI, String localName, String qName)
			throws SAXException {
		// 在word标签解析完之后，会打印出所有得到的数据
		tagName = "";
		if (localName.equals("exp")) {
			wordExp.setExpId(expid);
			wordExp.setExpUser(user);
			wordExp.setName(name);
			wordExp.setExp(exp);
			infos.add(wordExp);
		}
	}

	public void characters(char[] ch, int start, int length)
			throws SAXException {
		if (tagName.equals("name")) {
			name = new String(ch, start, length);
		} else if (tagName.equals("exp")) {
			exp = new String(ch, start, length);
		}
	}
}
