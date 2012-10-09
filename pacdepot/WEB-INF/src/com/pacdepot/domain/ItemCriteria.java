package com.pacdepot.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ItemCriteria {
	private Map _SQL = new HashMap(); // each element will be ANDed
	private int _page = 0;
	private String _requestQueryString;
	private Set _ids = new HashSet();
	private Set _cities = new HashSet();
	private Set _categories = new HashSet();
	private Set _sponsors = new HashSet();
	private long _totalMatches = 0;
	private String _luceneQueryString;
	private boolean _hasMorePages = true;
	private boolean _defaultCriteria = true;

	public ItemCriteria() {
		this(0);
	}

	public ItemCriteria(int page) {
		this(null, page);
	}

	public ItemCriteria(String queryString) {
		this(queryString.replaceAll("&page=[0-9]*", ""), 0);
	}

	public ItemCriteria(String queryString, String page) {
		this(queryString, Integer.parseInt(page));
	}

	public ItemCriteria(String queryString, int page) {
		try {
			_requestQueryString = queryString.replaceAll("&page=[0-9]*", "");
		} catch (Exception e) {}
		_page = page;
		_SQL.put("confirmed", " confirmed = 1 ");
	}

	/** @deprecated, @see addSponsor() */
	public void setSponsor(Sponsor sponsor) {
		addSponsor(sponsor);
	}

	public String getSQL() {
		try {
			generateCityCriteria();
		} catch (Exception e) {}
		try {
			generateCategoryCriteria();
		} catch (Exception e) {}
		try {
			generateSponsorCriteria();
		} catch (Exception e) {}
		String sql = new String();
		Iterator i = _SQL.entrySet().iterator();
		while (i.hasNext())
			sql += " ( " + ((Map.Entry) i.next()).getValue() + " )   AND ";
		return sql.substring(0, sql.length() - 4); // chop the last AND
	}

	public boolean isDefault() {
		return _defaultCriteria;
	}

	public boolean isEmpty() {
		return _SQL.isEmpty();
	}

	public void setTotalMatches(int totalMatches) {
		_totalMatches = totalMatches;
	}

	public long getTotalMatches() {
		return _totalMatches;
	}

	public int getPage() {
		return _page;
	}

	public void hasMorePages(boolean hasMorePages) {
		_hasMorePages = hasMorePages;
	}

	public boolean hasMorePages() {
		return _hasMorePages;
	}

	public String getRequestQueryString() {
		return _requestQueryString;
	}

	public void addCity(City city) {
		_defaultCriteria = false;
		_cities.add(city);
	}

	public void setLuceneQueryString(String query) {
		_luceneQueryString = query;
	}

	public String getLuceneQueryString() {
		return _luceneQueryString;
	}

	public void addCategory(Category category) {
		_defaultCriteria = false;
		_categories.add(category);
	}
	
	public void setUser(User user) {
		_SQL.put("userid", "userid = " + user.getAttribute("id"));
	}	

	public void addSponsor(Sponsor sponsor) {
		_defaultCriteria = false;
		_sponsors.add(sponsor);
	}

	public void addAllCities() {
		_SQL.remove("cities");
	}

	public void addAllCategories() {
		_SQL.remove("categories");
	}

	public void addAllSponsors() {
		_SQL.remove("sponsors");
	}

	private void generateCityCriteria() {
		String sql = new String();
		Iterator cities = _cities.iterator();
		while (cities.hasNext())
			sql += (" cityid = " + ((City) cities.next()).getId() + "    OR");
		_SQL.put("cities", sql.substring(0, sql.length() - 4)); // chop the last OR
	}

	private void generateCategoryCriteria() {
		String sql = new String();
		Iterator categories = _categories.iterator();
		while (categories.hasNext())
			sql += (" categoryid = " + ((Category) categories.next()).getId() + "   OR");
		_SQL.put("categories", sql.substring(0, sql.length() - 4)); // chop the last OR
	}

	private void generateSponsorCriteria() {
		String sql = new String();
		Iterator sponsors = _sponsors.iterator();
		while (sponsors.hasNext())
			sql += (" sponsorid = " + ((Sponsor) sponsors.next()).getId() + "   OR");
		_SQL.put("sponsors", sql.substring(0, sql.length() - 4)); // chop the last OR
	}
	
	public void removeCriteria(String key) {
		_SQL.remove(key);
	}
}
