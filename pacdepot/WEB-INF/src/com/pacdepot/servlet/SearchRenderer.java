package com.pacdepot.servlet;

import java.lang.reflect.Method;
import java.util.Iterator;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.City;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.xml.SearchJDOM;

public class SearchRenderer extends XSLTRenderer {
	SearchRenderer() {
		_section = "search";
	}

	public void render() {
		super.render();
		try {
			Method action = getClass().getDeclaredMethod(_action, null);
			action.invoke(this, null);
		} catch (Exception e) {
			SearchJDOM data = new SearchJDOM(_user, _sponsor);
			render("search.xslt", data);
		}
	}

	private void item() {
		ItemCriteria criteria;
		try {
			criteria = new ItemCriteria(_request.getQueryString(), _request.getParameter("page"));
		} catch (Exception e) {
			criteria = new ItemCriteria(_request.getQueryString());
		}

		try {
			criteria.setLuceneQueryString(
				new String(_request.getParameter("query").getBytes(), "UTF-8"));
		} catch (Exception e) {}

		try {
			String[] cities = _request.getParameterValues("cities");
			for (int i = 0; i < cities.length; i++)
				try {
					criteria.addCity(new City(cities[i]));
				} catch (Exception e) {
					if (cities[i].matches("all"))
						criteria.addAllCities();
				}
		} catch (Exception e) {
			criteria.addAllCities();
		}

		try {
			String[] categories = _request.getParameterValues("categories");
			for (int i = 0; i < categories.length; i++)
				try {
					Category category = new Category(categories[i]);
					criteria.addCategory(category);
					descendCategory(criteria, category);
				} catch (Exception e) {
					if (categories[i].matches("all"))
						criteria.addAllCategories();
				}
		} catch (Exception e) {
			criteria.addAllCategories();
		}
		
		try {
			if (_request.getParameter("localSearch").equals("on")) {
				criteria.addSponsor(_sponsor);	
			} else {
				if (_sponsor.isPublic()) {
					for (Iterator iter = DataAdapter.getInstance().getPublicSponsors(); iter.hasNext();) {
						criteria.addSponsor((Sponsor) iter.next());
					}
				} else
					criteria.addSponsor(_sponsor);				
			}
		} catch (Exception e) {
			if (_sponsor.isPublic()) {
				for (Iterator iter = DataAdapter.getInstance().getPublicSponsors(); iter.hasNext();) {
					criteria.addSponsor((Sponsor) iter.next());
				}
			} else
				criteria.addSponsor(_sponsor);
		}

		SearchJDOM data =
			new SearchJDOM(
				_user,
				_sponsor,
				DataAdapter.getInstance().getItemsQuickSearch(criteria),
				criteria);

		render("search-item.xslt", data);
	}
}