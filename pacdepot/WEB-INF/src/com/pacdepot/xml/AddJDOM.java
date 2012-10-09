package com.pacdepot.xml;

import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;

public class AddJDOM extends PacJDOM {
	public AddJDOM(User user, Sponsor sponsor) {
		super(user, sponsor);
		setSection("add");
		_page.setAttribute("title", "Ajouter");
		addCategories();
		addCities();
		addBrands();
	}
}