package com.pacdepot.xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;

public class BrowseJDOM extends PacJDOM {
	public BrowseJDOM(User user, Sponsor sponsor) {
		super(user, sponsor);
		setSection("browse");
	}
	

	/**
	 * Produce the top-level category page (no items)
	 */
	public void produceCategoryPage() {
		Iterator parentCategories = DataAdapter.getInstance().getParentCategories();
		while (parentCategories.hasNext()) {
			Element categoryElem = new Element("category");
			Category category = (Category) parentCategories.next();
			long id = category.getId();
			categoryElem.setAttribute("name", category.getName());
			categoryElem.setAttribute("parentid", Long.toString(category.getParentid()));
			categoryElem.setAttribute("id", Long.toString(id));
			descendCategory(categoryElem, id);
			_body.addContent(categoryElem);
		}
	}

	/**
	 * Produce a subcategory page with item list
	 */
	public void setCategory(Category category) {
		setState("subcategory");

		Element categoryElem = new Element("category");
		categoryElem.setAttribute("name", category.getName());
		categoryElem.setAttribute("parentid", Long.toString(category.getParentid()));
		categoryElem.setAttribute("id", Long.toString(category.getId()));
		descendCategory(categoryElem, category.getId());
		_body.addContent(categoryElem);

		Element ancestors = new Element("ancestor");
		if (category.getParentid() > 0) {
			try {
				category = new Category(category.getParentid());
			} catch (Exception e) {
				category = null;
			}
			List list = new ArrayList();
			if (category != null) {
				while (!category.isTopLevel()) {
					Element e = new Element("ancestor");
					e.setAttribute("name", category.getName());
					e.setAttribute("id", Long.toString(category.getId()));
					list.add(e);
					try {
						category = new Category(category.getParentid());
					} catch (Exception e1) {
						break;
					}
				}

				if (category.isTopLevel()) {
					Element e = new Element("ancestor");
					e.setAttribute("name", category.getName());
					e.setAttribute("id", Long.toString(category.getId()));
					if (!list.isEmpty())
						for (int i = list.size() - 1; i >= 0; i--)
							e.addContent((Element) list.get(i));
					ancestors.addContent(e);
				}
			}
			_body.addContent(ancestors);
		}
	}
}