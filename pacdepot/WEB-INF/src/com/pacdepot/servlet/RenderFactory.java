package com.pacdepot.servlet;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;

public class RenderFactory {
	private static Map accessCache = new LinkedHashMap();
	private static Map renderMap = new HashMap();
	private Renderer _defaultRenderer;

	public RenderFactory(Renderer defaultR) throws InstantiationException, IllegalAccessException {
		_defaultRenderer = (Renderer) defaultR.getClass().newInstance();
	}

	public static void flushAll() {
		accessCache.clear();
	}

	public void addRenderer(Renderer r) {
		renderMap.put(r.getSection(), r);
	}

	public Renderer build(HttpServlet servlet, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Sponsor sponsor = (Sponsor) session.getAttribute("sponsor");
		Renderer renderer = null;
		StringTokenizer st = new StringTokenizer(request.getPathInfo(), "/");
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			try {
				sponsor = new Sponsor(token);
				session.setAttribute("sponsor", sponsor);
			} catch (Exception e1) {}

			try {
				renderer = RenderFactory.build(token);
			} catch (Exception e) {}
		}

		User user = (User) session.getAttribute("user");
		if (user == null)
			user = new User();

		if (sponsor == null)
			try {
				sponsor = PacServlet.getDefaultSponsor();
			} catch (ServletException e) {}

		/////////////////////////////////////////////////////////////
		// access rights frankensteinned from menu.xml and PacJDOM 

		try {
			Document doc = (Document) accessCache.get(sponsor);
			if (doc == null) {
				String filename =
					PacServlet.getBaseDir() + "/home/" + sponsor.getUsername() + "/xml/access.xml";
				SAXBuilder builder = new SAXBuilder();

				try {
					doc = builder.build(new File(filename));
					accessCache.put(sponsor, doc);
				} catch (JDOMException e2) {
					filename = PacServlet.getBaseDir() + "/xml/access.xml";
					try {
						doc = builder.build(new File(filename));
						accessCache.put(sponsor, doc);
					} catch (JDOMException e) {
						e.printStackTrace();
					}
				}
			}

			Element access = (Element) doc.getRootElement();
			for (Iterator sections = access.getChildren().iterator(); sections.hasNext();) {
				Element section = (Element) sections.next();
			}

			for (Iterator sections = access.getChildren().iterator(); sections.hasNext();) {
				Element section = (Element) sections.next();

				if (renderer.getSection().equals(section.getAttributeValue("name"))) {
					for (Iterator actions = section.getChildren().iterator(); actions.hasNext();) {
						Element action = (Element) actions.next();

						if (Renderer.getAction(request).equals(action.getAttributeValue("name"))) {
							if (user.getAttribute("group") == null
								|| !user.getAttribute("group").equals(action.getAttributeValue("group"))) {
								System.out.println(
									"Access violation for section "
										+ renderer.getSection()
										+ ", action "
										+ Renderer.getAction(request));
								renderer = null;
							}
						}
						break;
					}
					break;
				}
			}
		} catch (Exception ignored) {}

		// end access rights
		/////////////////////////////////////////////////////////////

		if (renderer == null)
			try {
				renderer = (Renderer) _defaultRenderer.getClass().newInstance();
			} catch (InstantiationException e) {} catch (IllegalAccessException e) {}

		renderer.setServlet(servlet);
		renderer.setSession(session);
		renderer.setRequest(request);
		renderer.setResponse(response);
		renderer.setSponsor(sponsor);
		renderer.setUser(user);

		// special cases
		try {
			if (!(renderer.getSection().equals("add")
				|| renderer.getSection().equals("images")
				|| renderer.getSection().equals("members")))
				session.removeAttribute("item");

			if (renderer.getSection().equals("add")
				&& session.getAttribute("sessionItemIsFromMembersList").equals("true")) {
				session.removeAttribute("item");
				session.removeAttribute("sessionItemIsFromMembersList");
			}
		} catch (Exception e) {}
		// end of special cases

		return renderer;
	}

	private static Renderer build(String token) throws InstantiationException, IllegalAccessException {
		return (Renderer) renderMap.get(token).getClass().newInstance();
	}
}
