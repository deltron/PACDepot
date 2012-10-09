package com.pacdepot.xml;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.IllegalAddException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Brand;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.City;
import com.pacdepot.domain.Entity;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;
import com.pacdepot.exception.CategoryException;
import com.pacdepot.servlet.PacServlet;

public class PacJDOM extends XMLDocumentFactory {
	User _user = null;
	Sponsor _sponsor = null;

	private Map menuCache = new LinkedHashMap();
	private Map colorCache = new LinkedHashMap();

	protected PacJDOM() {}

	public PacJDOM(User user, Sponsor sponsor) {
		_user = user;
		_sponsor = sponsor;

		_page.addContent(new Element("header"));
		_page.addContent(new Element("css"));
		_page.addContent(SponsorJDOM.produce(sponsor));
		_page.addContent(new Element("footer"));
		produce(colorCache, "colors");
		produceMenu();
	}

	public void addCities() {
		_page.addContent(produceAllCities());
	}

	public void addCategories() {
		_page.addContent(produceAllCategories());
	}

	public void addBrands() {
		_page.addContent(produceAllBrands());
	}

	public void setCriteria(ItemCriteria criteria) {
		produceSearchResultNavigator(criteria);
	}

	public void setItemsQuick(Iterator items) {
		Element element = new Element("items");
		while (items.hasNext())
			element.addContent(produceItemQuick((Item) items.next()));
		_body.addContent(element);
	}

	public void setItems(Iterator items) {
		Element element = new Element("items");
		while (items.hasNext())
			element.addContent(produceItem((Item) items.next()));
		_body.addContent(element);
	}

	public void setItem(Item item) {
		try {
			_body.addContent(produceItem(item));
			if (!item.getErrors().isEmpty())
				_body.addContent(produceErrors(item));
		} catch (Exception e) {}
	}

	public void setState(String state) {
		if (state != null)
			_page.setAttribute("state", state);
	}

	public void setSection(String section) {
		if (section != null)
			_page.setAttribute("section", section);
	}

	public void setTitle(String title) {
		if (title != null)
			_page.setAttribute("title", title);
	}

	public void setUser() {
		_page.addContent(produceUser());
	}

	// Generic way to convert all attributes of an Entity into a JDOM tree
	protected Element produceUser() {
		Element element = new Element("user");
		for (Iterator iter = _user.getAttributeEntrySet().iterator(); iter.hasNext();) {
			try {
				Entry entry = (Entry) iter.next();
				String value = entry.getValue().toString();
				if (value.length() > 0)
					element.setAttribute((String) entry.getKey(), value);
			} catch (Exception e) {}
		}
		return element;
	}

	public void produce(Map cache, String key) {
		Document doc = (Document) cache.get(_sponsor.getUsername());
		if (doc == null) {
			String filename =
				PacServlet.getBaseDir() + "/home/" + _sponsor.getUsername() + "/xml/" + key + ".xml";
			SAXBuilder builder = new SAXBuilder();

			try {
				doc = builder.build(new File(filename));
				cache.put(_sponsor.getUsername(), doc);
			} catch (JDOMException e2) {
				filename = PacServlet.getBaseDir() + "/xml/" + key + ".xml";
				try {
					doc = builder.build(new File(filename));
					cache.put(_sponsor.getUsername(), doc);
				} catch (JDOMException e) {
					e.printStackTrace();
				}
			}
		}
		
		try {
			_page.addContent((Element) doc.detachRootElement().clone());
		} catch (Exception e) {}
	}

	public void produceMenu() {
		Document doc = (Document) menuCache.get(_sponsor.getUsername());
		if (doc == null) {
			String filename =
				PacServlet.getBaseDir() + "/home/" + _sponsor.getUsername() + "/xml/menu.xml";
			SAXBuilder builder = new SAXBuilder();

			try {
				doc = builder.build(new File(filename));
				menuCache.put(_sponsor.getUsername(), doc);
			} catch (JDOMException e2) {
				filename = PacServlet.getBaseDir() + "/xml/menu.xml";
				try {
					doc = builder.build(new File(filename));
					menuCache.put(_sponsor.getUsername(), doc);
				} catch (JDOMException e) {
					e.printStackTrace();
				}
			}
		}

		Element xmlMenu = (Element) doc.getRootElement();
		Element menu = new Element("menu");

		for (Iterator iter = xmlMenu.getChildren().iterator(); iter.hasNext();) {
			Element item = (Element) iter.next();
			item = (Element) item.clone();

			try {
				if (item.getAttributeValue("login").equals("true") && _user.isLoggedIn()) {
					try {
						if (item.getAttributeValue("group").equals(_user.getAttribute("group")))
							menu.addContent(item);
					} catch (Exception e2) {
						menu.addContent(item);
					}
				}
				if (item.getAttributeValue("login").equals("false") && !_user.isLoggedIn()) {
					menu.addContent(item);
				}
			} catch (Exception e) {
				menu.addContent(item);
			}
		}
		_page.addContent(menu);
	}

	protected static Element produceCategoryTree(Item item) {
		Category category = (Category) item.getAttribute("category");
		List list = new ArrayList();
		if (category != null) {
			while (!category.isTopLevel()) {
				Element element = new Element("category");
				element.setAttribute("name", category.getName());
				element.setAttribute("id", Long.toString(category.getId()));
				list.add(element);
				try {
					category = new Category(category.getParentid());
				} catch (CategoryException e) {
					category = new Category();
				}
			}
			if (category.isTopLevel()) {
				try {
					Element element = new Element("category");
					element.setAttribute("name", category.getName());
					element.setAttribute("id", Long.toString(category.getId()));
					for (int i = list.size() - 1; i >= 0; i--)
						element.addContent((Element) list.get(i));
					return element;
				} catch (Exception e) {
					return null;
				}
			}
		}
		return null;
	}

	protected static void descendCategory(Element parent, long parentid) {
		Iterator children = DataAdapter.getInstance().getChildCategories(parentid);
		while (children.hasNext()) {
			Category c = (Category) children.next();
			Element element = new Element("category");
			element.setAttribute("name", c.getName());
			element.setAttribute("parentid", Long.toString(c.getParentid()));
			element.setAttribute("id", Long.toString(c.getId()));
			descendCategory(element, c.getId());
			parent.addContent(element);
		}
	}

	protected static Element produceAllCategories() {
		Iterator parentCategories = DataAdapter.getInstance().getParentCategories();
		Element root = new Element("categories");
		while (parentCategories.hasNext()) {
			Category category = (Category) parentCategories.next();
			if (category != null) {
				Element element = new Element("category");
				element.setAttribute("name", category.getName());
				element.setAttribute("parentid", Long.toString(category.getParentid()));
				element.setAttribute("id", Long.toString(category.getId()));
				PacJDOM.descendCategory(element, category.getId());
				root.addContent(element);
			}
		}
		return root;
	}

	protected static Element produceAllBrands() {
		Iterator brands = DataAdapter.getInstance().getAllBrands();
		Element root = new Element("brands");
		while (brands.hasNext()) {
			Brand brand = (Brand) brands.next();
			Element element = new Element("brand");
			element.setAttribute("name", brand.getName());
			element.setAttribute("id", Long.toString(brand.getId()));
			root.addContent(element);
		}
		return root;
	}

	private static Element produceAge(Item item) {
		Element element = new Element("age");
		try {
			if ((item.getAttributeAsInt("ageInMonths") / 12) > 0)
				element.setAttribute("years", Integer.toString(item.getAttributeAsInt("ageInMonths") / 12));
		} catch (Exception e) {}
		try {
			if ((item.getAttributeAsInt("ageInMonths") % 12) > 0)
				element.setAttribute(
					"months",
					Integer.toString(item.getAttributeAsInt("ageInMonths") % 12));
		} catch (Exception e) {}
		return element;
	}

	private static Element produceUser(Item item) {
		Element user = new Element("user");
		try {
			user.setAttribute("name", (String) ((User) item.getAttribute("user")).getAttribute("name"));
		} catch (Exception e) {}
		try {
			user.setAttribute(
				"telephone",
				(String) ((User) item.getAttribute("user")).getAttribute("telephone"));
		} catch (Exception e) {}
		try {
			user.setAttribute("email", (String) ((User) item.getAttribute("user")).getAttribute("email"));
		} catch (Exception e) {}
		return user;
	}

	protected static Element produceAllSponsors() {
		Element root = new Element("sponsors");
		Iterator sponsors = DataAdapter.getInstance().getAllSponsors();
		while (sponsors.hasNext()) {
			Sponsor sponsor = (Sponsor) sponsors.next();
			if (sponsor.isValid() && sponsor.isPublic()) {
				Element element = new Element("sponsor");
				element.setAttribute("id", Long.toString(sponsor.getId()));
				element.setAttribute("name", sponsor.getName());
				root.addContent(element);
			}
		}
		return root;
	}

	protected static Element produceAllCities() {
		Iterator cities = DataAdapter.getInstance().getAllCities();
		Element root = new Element("cities");
		while (cities.hasNext()) {
			City city = (City) cities.next();
			Element element = new Element("city");
			element.setAttribute("id", Long.toString(city.getId()));
			element.setAttribute("name", city.getName());
			root.addContent(element);
		}
		return root;
	}

	public static void addDescription(Element element, Entity entity) {
		Element descriptionStr = new Element("descriptionStr");
		descriptionStr.addContent((String) entity.getAttribute("description"));
		element.addContent(descriptionStr);
		try {
			element.addContent(produceDescription(entity));
		} catch (Exception ignored) {}
	}

	private static Element produceItem(Item item) {
		Element element = new Element("item");
		try {
			// check this:
			// (from j2sdk api reference)
			//	If the map is modified while an iteration over the set is in progress, the results of the iteration are undefined.
			for (Iterator iter = item.getAttributeEntrySet().iterator(); iter.hasNext();) {
				try {
					Entry entry = (Entry) iter.next();
					String value = entry.getValue().toString();
					if (value.length() > 0)
						element.setAttribute((String) entry.getKey(), value);
				} catch (Exception e) {}
			}

			Element descriptionStr = new Element("descriptionStr");
			descriptionStr.addContent((String) item.getAttribute("description"));
			element.addContent(descriptionStr);

			if (item.hasAttribute("image"))
				element.setAttribute("hasImage", "true");

			try {
				element.addContent(SponsorJDOM.produce((Sponsor) item.getAttribute("sponsor")));
			} catch (IllegalAddException iae) {} catch (NullPointerException e) {}

			try {
				element.addContent(produceUser(item));
			} catch (IllegalAddException iae) {}

			try {
				element.addContent(produceAge(item));
			} catch (IllegalAddException iae) {}

			try {
				element.addContent(produceDescription(item));
			} catch (IllegalAddException iae) {}

			try {
				element.addContent(produceCity(item));
			} catch (IllegalAddException iae) {}

			try {
				element.addContent(produceBrand(item));
			} catch (IllegalAddException iae) {}

			try {
				element.addContent(produceCategoryTree(item));
			} catch (IllegalAddException iae) {}
		} catch (Exception e) {}
		return element;
	}

	protected static Element produceItemQuick(Item item) {
		Element element = new Element("item");

		// check this:
		// (from j2sdk api reference)
		//	If the map is modified while an iteration over the set is in progress, the results of the iteration are undefined.
		for (Iterator iter = item.getAttributeEntrySet().iterator(); iter.hasNext();) {
			try {
				Entry entry = (Entry) iter.next();
				String value = entry.getValue().toString();
				if (value.length() > 0)
					element.setAttribute((String) entry.getKey(), value);
			} catch (Exception e) {}
		}

		element.addContent(SponsorJDOM.produce((Sponsor) item.getAttribute("sponsor")));

		try {
			element.setAttribute(
				"iconid",
				((Sponsor) item.getAttribute("sponsor")).getAttribute("id").toString());
		} catch (Exception iae) {}

		try {
			element.addContent(produceUser(item));
		} catch (IllegalAddException iae) {}

		return element;
	}

	private static Element produceCity(Item item) {
		Element element = null;
		try {
			element = new Element("city");
			element.setAttribute("name", ((City) item.getAttribute("city")).getName());
			element.setAttribute("id", Long.toString(((City) item.getAttribute("city")).getId()));
		} catch (Exception e) {}
		return element;
	}

	private static Element produceDescription(Entity entity) throws JDOMException {
		String description = (String) entity.getAttribute("description");
		if (description != null && description.length() > 0) {
			description = "<description>" + description + "</description>";
			SAXBuilder builder = new SAXBuilder();
			Document descDoc = null;
			descDoc = builder.build(new StringReader(description));
			Element element = new Element("description");
			if (descDoc != null)
				element.setContent(descDoc.getContent());
			return element;
		} else
			return null;
	}

	private static Element produceBrand(Item item) {
		Element element = new Element("brand");
		if (item.getAttribute("brand") != null) {
			element.setAttribute("id", Long.toString(((Brand) item.getAttribute("brand")).getId()));
			String brandHref = ((Brand) item.getAttribute("brand")).getHref();
			if (brandHref != null)
				element.setAttribute("href", brandHref);
		} else
			element.setAttribute("id", "0");
		return element;
	}

	public static Element produceErrors(Entity entity) {
		Element errors = new Element("errors");
		for (Iterator iter = entity.getErrors().iterator(); iter.hasNext();)
			errors.addContent(new Element(((String) iter.next())));
		return errors;
	}

	protected Element produceSearchResultNavigator(ItemCriteria criteria) {
		Element element = new Element("searchResultNavigator");
		element.setAttribute("currentPage", Integer.toString(criteria.getPage()));
		element.setAttribute("totalMatches", Long.toString(criteria.getTotalMatches()));
		element.setAttribute("nextPage", Integer.toString(criteria.getPage() + 1));
		element.setAttribute("previousPage", Integer.toString(criteria.getPage() - 1));

		if (criteria.hasMorePages())
			element.setAttribute("hasMorePages", "true");

		try {
			element.setAttribute("queryString", criteria.getRequestQueryString());
		} catch (Exception e) {}

		_body.addContent(element);
		return element;
	}

	protected static Element produceSponsor(Sponsor sponsor) {
		Element root = new Element("sponsors");
		if (sponsor.isValid()) {
			Element element = new Element("sponsor");
			element.setAttribute("id", Long.toString(sponsor.getId()));
			element.setAttribute("name", sponsor.getName());
			root.addContent(element);
		}
		return root;
	}

	public void printDocumentTrace() {
		XMLOutputter outputter = new XMLOutputter("     ", true);
		try {
			outputter.output(getDocument(), System.out);
		} catch (IOException e) {
			System.err.println(e);
		}
	}
}
