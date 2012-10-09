package com.pacdepot.xml;

import java.util.Iterator;

import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;

public class SearchJDOM extends PacJDOM {
	public SearchJDOM(User user, Sponsor sponsor) {
		super(user, sponsor);
		_page.setAttribute("title", "Recherche");
		addCities();
		addCategories();
	}

	public SearchJDOM(User user, Sponsor sponsor, Iterator items, ItemCriteria criteria) {
		this(user, sponsor);
		setItemsQuick(items);
		setCriteria(criteria);
	}
}
