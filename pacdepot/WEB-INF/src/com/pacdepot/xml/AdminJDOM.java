package com.pacdepot.xml;

import java.util.Iterator;
import java.util.Map;

import org.jdom.Element;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Brand;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.City;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;

public class AdminJDOM extends PacJDOM {
	public AdminJDOM(User user, Sponsor sponsor) {
		super(user, sponsor);
		producePage();
	}
	
	private void producePage() {
		DataAdapter adapter = DataAdapter.getInstance();
		Iterator sponsors = adapter.getAllSponsors();
		Iterator cities = adapter.getAllCities();
		Iterator brands = adapter.getAllBrands();
		Iterator parentCategories = adapter.getParentCategories();
		Iterator users = adapter.getAllUsers();

		Element content = new Element("content");
		while (sponsors.hasNext()) {
			Element sponsorElem = new Element("sponsor");
			Sponsor sponsor = (Sponsor) sponsors.next();
			sponsorElem.setAttribute("id", Long.toString(sponsor.getId()));
			sponsorElem.setAttribute("username", sponsor.getUsername());
			sponsorElem.setAttribute("name", sponsor.getName());
			sponsorElem.setAttribute("href", sponsor.getHref());
			sponsorElem.setAttribute(
				"public",
				Boolean.toString(sponsor.isPublic()));
			content.addContent(sponsorElem);
		}
		
		while (users.hasNext()) {
			Element userElem = new Element("user");
			User user = (User) users.next();
			for ( Iterator i = user.getAttributeEntrySet().iterator(); i.hasNext(); ) {
				Map.Entry entry = (Map.Entry) i.next();
				userElem.setAttribute((String) entry.getKey(), (String) entry.getValue());
			}
			content.addContent(userElem);
		}			

		while (cities.hasNext()) {
			Element cityElem = new Element("city");
			City city = (City) cities.next();
			cityElem.setAttribute("id", Long.toString(city.getId()));
			cityElem.setAttribute("name", city.getName());
			content.addContent(cityElem);
		}

		while (brands.hasNext()) {
			Element brandElem = new Element("brand");
			Brand brand = (Brand) brands.next();
			brandElem.setAttribute("src", "/images/brand?id=" + brand.getId());
			brandElem.setAttribute("href", brand.getHref());
			brandElem.setAttribute(
				"width",
				Integer.toString(brand.getImage().getWidth()));
			brandElem.setAttribute(
				"height",
				Integer.toString(brand.getImage().getHeight()));
			brandElem.setAttribute("name", brand.getName());
			brandElem.setAttribute("id", Long.toString(brand.getId()));
			content.addContent(brandElem);
		}

		while (parentCategories.hasNext()) {
			Element categoryElem = new Element("category");
			Category category = (Category) parentCategories.next();
			long id = category.getId();
			categoryElem.setAttribute("name", category.getName());
			categoryElem.setAttribute(
				"parentid",
				Long.toString(category.getParentid()));
			categoryElem.setAttribute("id", Long.toString(id));
			descendCategory(categoryElem, id);
			content.addContent(categoryElem);
		}

		_body.addContent(content);
	}
}
