package com.pacdepot.servlet;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.City;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.xml.AddJDOM;

public class AddRenderer extends XSLTRenderer {
	public AddRenderer() {
		_section = "add";
	}

	public void render() {
		super.render();
		try {
			Method action = getClass().getDeclaredMethod(_action, null);
			action.invoke(this, null);
		} catch (Exception e) {
			try {
				if (_user.isLoggedIn() && _session.getAttribute("item") == null) {
					Item item = new Item();
					item.setAttribute("user", _user);
					item.setAttribute("userid", _user.getAttribute("id"));
					item.setAttribute("city", new City((String) _user.getAttribute("cityid")));
					item.setAttribute(
						"sponsor",
						new Sponsor(Long.parseLong((String) _user.getAttribute("sponsorid"))));
					_session.setAttribute("item", item);
				}
			} catch (Exception ignored) {
				ignored.printStackTrace();
			}

			// (1) The data "null" is not legal for a JDOM attribute:
			//		A null is not a legal XML value.
			//
			String state = (String) _session.getAttribute("state");
			if (state == null)
				state = "null";
			// (1) end

			AddJDOM data = new AddJDOM(_user, _sponsor);
			data.setState(state);
			data.setItem((Item) _session.getAttribute("item"));
			_session.removeAttribute("state");
			render("add.xslt", data);
		}
	}

	private void cancelItem() {
		DataAdapter.getInstance().deleteItem(getSessionItem(_request));
		_session.removeAttribute("item");
		_session.setAttribute("state", "cancelled");
		redirect("/add");
	}

	private void confirmItem() {
		_session.removeAttribute("item");
		_session.setAttribute("state", "success");
		redirect("/add");
	}

	private void modifyItem() {
		AddJDOM data = new AddJDOM(_user, _sponsor);
		data.setState("modify");
		data.setItem(getSessionItem(_request));
		render("add.xslt", data);
	}

	private void previewItem() {
		Item sessionItem = getSessionItem(_request);
		Item requestItem = null;
		try {
			requestItem = getRequestItem(_request);
		} catch (ServletException e) {}

		Map attributes;
		try {
			attributes = sessionItem.getAttributes();
			attributes.putAll(requestItem.getAttributes());
		} catch (Exception e1) {
			try {
				attributes = requestItem.getAttributes();
			} catch (Exception e2) {
				redirect("/add");
				return;
			}
		}

		if (attributes.containsKey("deleteImage")) {
			attributes.remove("image");
			attributes.remove("deleteImage");
		}

		Set errors;
		try {
			errors = sessionItem.getErrors();
			errors.retainAll(requestItem.getErrors());
			errors.addAll(requestItem.getErrors());
		} catch (Exception e) {
			errors = requestItem.getErrors();
		}

		Item item = new Item(attributes, errors);
		if (errors.isEmpty()) {
			try {
				try {
					if (_user.isLoggedIn()) {
						item.setAttribute("userid", _user.getAttribute("id"));
						item.setAttribute("user", _user);
					}
				} catch (Exception e) {}

				item.setAttribute("sponsor", _sponsor);

				item.setAttribute("id", DataAdapter.getInstance().createItem(item));
				_session.setAttribute("item", item);

				AddJDOM data = new AddJDOM(_user, _sponsor);
				data.setState("preview");
				data.setItem(item);
				render("browse-item.xslt", data);
			} catch (Exception e1) {
				System.out.println("ERROR	Unable to create item");
				e1.printStackTrace();
			}
		} else {
			_session.setAttribute("item", item);
			AddJDOM data = new AddJDOM(_user, _sponsor);
			data.setState("error");
			data.setItem(item);
			render("add.xslt", data);
		}
	}
}