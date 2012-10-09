package com.pacdepot.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom.Document;
import org.jdom.output.XMLOutputter;

import com.oreilly.javaxslt.util.StylesheetCache;
import com.oreilly.servlet.MultipartRequest;
import com.pacdepot.domain.Entity;
import com.pacdepot.xml.PacJDOM;

/**
 * A helper class that makes rendering of XSLT easier. This
 * eliminates the need to duplicate a lot of code for each
 * of the web pages in this app.
 * 
 * From Java & XSLT 
 */
public abstract class XSLTRenderer extends Renderer {
	private Document _document;
	private static Map filenameCache = new HashMap();

	private void renderXSLTFile(String path) throws ServletException, TransformerException, IOException {
		String xsltFileName = null;
		// figure out the complete XSLT stylesheet file name
		synchronized (filenameCache) {
			xsltFileName = (String) filenameCache.get(path);
			if (xsltFileName == null) {
				xsltFileName = _servlet.getServletContext().getRealPath(path);
				filenameCache.put(path, xsltFileName);
			}
		}

		// write the JDOM data to a StringWriter
		StringWriter sw = new StringWriter();
		XMLOutputter xmlOut = new XMLOutputter("", false, "UTF-8");
		xmlOut.output(_document, sw);

		_response.setContentType("text/html");
		Transformer trans = StylesheetCache.newTransformer(xsltFileName);

		trans.transform(
			new StreamSource(new StringReader(sw.toString())),
			new StreamResult(_response.getWriter()));
	}

	private void renderXSLT(String filename, Document xmlJDOMData) throws IOException, ServletException {
		_document = xmlJDOMData;

		try {
			renderXSLTFile("/WEB-INF/home/" + _sponsor.getUsername() + "/xslt/" + filename);
		} catch (Exception e) {
			if (e.getCause().toString().matches(".*FileNotFoundException.*")) {
				try {
					renderXSLTFile("/WEB-INF/xslt/" + filename);
				} catch (Exception e2) {
					throw new ServletException(e2);
				}
			} else {
				throw new ServletException(e);
			}
		}
	}

	public void render(String filename, PacJDOM data) {
		try {
			render(filename, data.getDocument());
		} catch (Exception ignored) {}
	}

	public void render(String filename, Document document) {
		try {
			renderXSLT(filename, document);
		} catch (Exception ignored) {
			ignored.printStackTrace();
		}
	}

	protected void setRequiredAttribute(
		MultipartRequest multi,
		Entity entity,
		String attribute,
		int length) { 
		try {
			if (truncate(multi, attribute, length).length() > 0)
				entity.setAttribute(attribute, truncate(multi, attribute, length));
			else
				entity.setError(attribute);
		} catch (Exception e) {
			entity.setError(attribute);
		}
	}

	protected void setOptionalAttribute(
		MultipartRequest multi,
		Entity entity,
		String attribute,
		int length) {
		try {
			if (truncate(multi, attribute, length).length() > 0)
				entity.setAttribute(attribute, truncate(multi, attribute, length));
		} catch (Exception e) {}
	}

	protected void setBooleanAttribute(MultipartRequest multi, Entity entity, String attribute) {
		try {
			if (truncate(multi, attribute, 8).length() > 0)
				entity.setAttribute(attribute, Boolean.valueOf(truncate(multi, attribute, 8)));
		} catch (Exception e) {
			entity.setAttribute(attribute, false);
		}
	}
}