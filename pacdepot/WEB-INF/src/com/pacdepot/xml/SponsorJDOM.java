package com.pacdepot.xml;

import java.io.StringReader;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalAddException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.pacdepot.domain.Ad;
import com.pacdepot.domain.Banner;
import com.pacdepot.domain.Icon;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.exception.IconException;
import com.pacdepot.servlet.PacServlet;

public class SponsorJDOM extends PacJDOM {
	public static Element produce(Sponsor sponsor) {
		Element element = new Element("sponsor");

		try {
			element.setAttribute("id", Long.toString(sponsor.getId()));
			element.setAttribute("name", sponsor.getName());
			element.setAttribute("username", sponsor.getUsername());
			element.setAttribute("href", sponsor.getHref());
			element.setAttribute("public", Boolean.toString(sponsor.isPublic()));

			if (sponsor.isLoggedIn())
				element.setAttribute("isLoggedIn", "true");

			Element homepageStr = new Element("homepageStr");
			String homePage = null;
			try {
				homePage = (String) sponsor.getAttribute("homepageStr");
			} catch (Exception e) {}
			if (homePage == null)
				homePage = sponsor.getHomepage();
			homepageStr.addContent(homePage);
			element.addContent(homepageStr);

			try {
				element.addContent(produceHomepage(sponsor));
			} catch (Exception e) {}

			try {
				element.addContent(produceIcon(sponsor));
			} catch (Exception ignored) {}

			try {
				element.addContent(produceBanner(sponsor));
			} catch (Exception ignored) {}

			try {
				element.addContent(produceAd(sponsor));
			} catch (Exception ignored) {}

			try {
				element.addContent(produceErrors(sponsor));
			} catch (IllegalAddException e) {}

		} catch (Exception e) {
			try {
				produce(PacServlet.getDefaultSponsor());
			} catch (Exception e2) {}
		}
		return element;
	}

	public static Element produceQuick(Long sponsorid) {
		Element element = null;

		try {
			element = new Element("sponsor");
			element.setAttribute("id", sponsorid.toString());

			Icon icon = new Icon(sponsorid.longValue());
			Element iconElement = new Element("icon");
			iconElement.setAttribute("id", Long.toString(icon.getId()));
			iconElement.setAttribute("width", Integer.toString(icon.getImage().getWidth()));
			iconElement.setAttribute("height", Integer.toString(icon.getImage().getHeight()));
			element.addContent(iconElement);
		} catch (Exception e) {}

		return element;
	}

	private static Element produceBanner(Sponsor sponsor) {
		Banner banner = new Banner();
		banner.setAttribute("id", sponsor.getId());
		banner.fetch();
		
		Element element = new Element("banner");
		element.setAttribute("id", Long.toString(banner.getId()));
		element.setAttribute("width", Integer.toString(banner.getImage().getWidth()));
		element.setAttribute("height", Integer.toString(banner.getImage().getHeight()));
		return element;
	}

	private static Element produceAd(Sponsor sponsor) {
		Element element = new Element("ad");
		Ad ad = new Ad(sponsor);
		if (ad.isValid()) {
			element.setAttribute("id", Long.toString(ad.getId()));
			if (ad.getHref() != null)
				element.setAttribute("href", ad.getHref());
			element.setAttribute("width", Integer.toString(ad.getImage().getWidth()));
			element.setAttribute("height", Integer.toString(ad.getImage().getHeight()));
		}
		return element;
	}

	private static Element produceIcon(Sponsor sponsor) {
		Element element = null;
		try {
			Icon icon = new Icon(sponsor.getId());
			element = new Element("icon");
			element.setAttribute("id", Long.toString(icon.getId()));
			element.setAttribute("width", Integer.toString(icon.getImage().getWidth()));
			element.setAttribute("height", Integer.toString(icon.getImage().getHeight()));
		} catch (IconException e) {
			System.out.println(e);
		}
		return element;
	}

	private static Element produceHomepage(Sponsor sponsor) {
		try {
			String homepage = sponsor.getHomepage();
			SAXBuilder builder = new SAXBuilder();
			Document homeDoc = null;
			try {
				homeDoc = builder.build(new StringReader("<homepage>" + homepage + "</homepage>"));
			} catch (JDOMException jde) {
				System.out.println(
					"INFO		HTML in database for homepage of sponsor #"
						+ sponsor.getId()
						+ " is not valid");
			}
			return homeDoc.getRootElement();
		} catch (Exception e) {
			return null;
		}
	}
}