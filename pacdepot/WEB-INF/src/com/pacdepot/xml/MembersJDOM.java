package com.pacdepot.xml;

import java.util.Iterator;

import org.jdom.Element;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Ad;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;

public class MembersJDOM extends PacJDOM {
	public MembersJDOM(User user, Sponsor sponsor) {
		super(user, sponsor);
		_page.addContent(produceUser());
		setSection("members");
	}

	public MembersJDOM(User user, Sponsor sponsor, Iterator items) {
		this(user, sponsor);
		setItemsQuick(items);
	}
	
	public MembersJDOM(User user, Sponsor sponsor, Iterator items, ItemCriteria criteria) {
		this(user, sponsor, items);
		setCriteria(criteria);
	}
	
	public void addAds() {
		_body.addContent(produceAds());	
	}

	private Element produceAds() {
		/**
		 * @todo: should not interface with DataAdapter
		 */
		Element element = new Element("ads");
		Iterator ads = DataAdapter.getInstance().getAllAds(_sponsor);
		while (ads.hasNext()) {
			Element adElem = new Element("ad");
			Ad ad = (Ad) ads.next();
			adElem.setAttribute("id", Long.toString(ad.getId()));
			adElem.setAttribute("width", Integer.toString(ad.getImage().getWidth()));
			adElem.setAttribute("height", Integer.toString(ad.getImage().getHeight()));
			adElem.setAttribute("href", ad.getHref());
			element.addContent(adElem);
		}
		return element;
	}
}