package com.pacdepot.xml;

import java.util.Iterator;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;

import com.pacdepot.domain.Entity;

public class XMLDocumentFactory {
	Element _page = new Element("page");
	Element _body = new Element("body");
	Element _flags = new Element("flags");

	private Element produceErrors(Entity entity) {
		Element element = new Element("errors");
		for (Iterator iter = entity.getErrors().iterator(); iter.hasNext();) {
			try {
				String error = (String) iter.next();
				element.setAttribute(error, "true");
			} catch (Exception e) {}
		}
		return element;
	}
	
	public Element addEntity(Entity entity) {
		Element element = new Element(entity.getLabel());
		for (Iterator iter = entity.getAttributeEntrySet().iterator(); iter.hasNext();) {
			try {
				Entry entry = (Entry) iter.next();
				String value = entry.getValue().toString();
				if (value.length() > 0)
					element.setAttribute((String) entry.getKey(), value);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (entity.hasErrors())
			element.addContent(produceErrors(entity));
		_page.addContent(element);
		return element;
	}
	

	public void addEntity(String string, Entity entity) {
		entity.setLabel(string);
		addEntity(entity);
	}
	
	
	
	public void setFlag(String flag) {
		_flags.addContent(new Element(flag));
	}

	public Document getDocument() {
		_page.addContent(_body);
		_page.addContent(_flags);
		return new Document(_page);
	}
}