package com.pacdepot.domain;

import java.util.Map;
import java.util.Set;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.exception.ItemException;

public class Item extends Entity {
	public Item() {
		super();
	}

	public Item(Map attributes, Set errors) {
		super(attributes, errors);
	}

	public Item(Long id) throws ItemException {
		this(id.longValue());
	}

	public Item(long id) throws ItemException {
		Item item = DataAdapter.getInstance().getItem(id);
		setAttribute("id", item.getAttribute("id"));
		setAttribute("type", item.getAttribute("type"));
		setAttribute("nature", item.getAttribute("nature"));
		setAttribute("title", item.getAttribute("title"));
		setAttribute("description", item.getAttribute("description"));
		setAttribute("href", item.getAttribute("href"));
		setAttribute("price", item.getAttribute("price"));
		setAttribute("quantity", item.getAttribute("quantity"));
		setAttribute("ageInMonths", item.getAttribute("ageInMonths"));
		setAttribute("lifespan", item.getAttribute("lifespan"));
		setAttribute("userbrand", item.getAttribute("userbrand"));
		setAttribute("user", item.getAttribute("user"));
		setAttribute("brand", item.getAttribute("brand"));
		setAttribute("image", item.getAttribute("image"));
		setAttribute("city", item.getAttribute("city"));
		setAttribute("category", item.getAttribute("category"));
		setAttribute("sponsor", item.getAttribute("sponsor"));
		setAttribute("date", item.getAttribute("date"));
		setAttribute("image", item.getAttribute("image"));
		setAttribute("confirmed", item.getAttribute("confirmed"));
		setAttribute("userid", item.getAttribute("userid"));
		setAttribute("sponsorid", ((Sponsor) item.getAttribute("sponsor")).getId());
	}

	public long getSponsorId() {
		try {
			return getAttributeAsLong("sponsorid");
		} catch (Exception e) {
			return Long.parseLong(getAttribute("sponsorid").toString());
		}
	}

	public boolean belongsTo(Sponsor sponsor) {
		return getSponsorId() == sponsor.getId();
	}
}