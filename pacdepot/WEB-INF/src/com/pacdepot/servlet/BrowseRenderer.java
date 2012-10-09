package com.pacdepot.servlet;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.jdom.Element;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Answer;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Question;
import com.pacdepot.domain.RegistrationBitmap;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;
import com.pacdepot.exception.CategoryException;
import com.pacdepot.exception.ItemException;
import com.pacdepot.xml.BrowseJDOM;

public class BrowseRenderer extends XSLTRenderer {
	BrowseRenderer() {
		_section = "browse";
	}

	public void render() {
		super.render();
		try {
			Method action = getClass().getDeclaredMethod(_action, null);
			action.invoke(this, null);
		} catch (Exception e) {
			redirect("/browse/category");
		}
	}

	private void item() {
		String idStr = null;
		if ((idStr = _request.getParameter("id")) != null) {
			try {
				Item item = new Item(Long.parseLong(idStr));
				Sponsor itemSponsor = (Sponsor) item.getAttribute("sponsor");
				if (itemSponsor.getId() != _sponsor.getId()) {
					User user = (User) _session.getAttribute("user");
					if (user != null)
						user.logout();
					_session.setAttribute("sponsor", itemSponsor);
					_sponsor = itemSponsor;
					_session.setAttribute("user", user);
				}

				Category category = (Category) item.getAttribute("category");
				String xsltFile = "browse";

				if (category.getTemplate() != null && category.getTemplate().length() > 0) {
					if (itemSponsor.hasCustomTemplates())
						xsltFile += ("-" + itemSponsor.getUsername());

					xsltFile += ("-" + category.getTemplate());
				} else {
					if (itemSponsor.hasCustomTemplates())
						xsltFile += ("-" + itemSponsor.getUsername());
					else
						xsltFile += "-item";
				}
				xsltFile += ".xslt";

				BrowseJDOM data = new BrowseJDOM(_user, itemSponsor);
				data.setItem(item);
				data.setTitle("Consulter");
				render(xsltFile, data);
			} catch (ItemException e) {
				e.printStackTrace();
			}
		}
	}

	private void register() {
		_session.removeAttribute("questions");
		long id;
		try {
			id = Long.parseLong(_request.getParameter("id"));
			_session.setAttribute("itemid", new Long(id));
		} catch (Exception e) {
			id = ((Long) _session.getAttribute("itemid")).longValue();
		}

		RegistrationBitmap rb = DataAdapter.getInstance().getRegistrationBitmap(id);
		_session.setAttribute("/browse/registrationBitmap", rb);
		if (rb == null)
			redirect("/browse/question");
		BrowseJDOM data = new BrowseJDOM(_user, _sponsor);
		_user.setLabel("user");
		data.addEntity(_user);
		Element r = data.addEntity(rb);
		BrowseJDOM.addDescription(r, rb);
		render("browse-registration.xslt", data);
	}

	private void registerUser() {
		RegistrationBitmap rb = (RegistrationBitmap) _session.getAttribute("/browse/registrationBitmap");

		_user.clearErrors();

		if (rb.getAttributeAsBoolean("lastname"))
			setAttributeFromRequest(_user, "lastname");

		if (rb.getAttributeAsBoolean("firstname"))
			setAttributeFromRequest(_user, "firstname");

		if (rb.getAttributeAsBoolean("telephone"))
			setAttributeFromRequest(_user, "telephone");

		if (rb.getAttributeAsBoolean("address"))
			setAttributeFromRequest(_user, "address");

		if (rb.getAttributeAsBoolean("city"))
			setAttributeFromRequest(_user, "city");

		if (rb.getAttributeAsBoolean("province"))
			setAttributeFromRequest(_user, "province");

		if (rb.getAttributeAsBoolean("postalcode"))
			setAttributeFromRequest(_user, "postalcode");

		if (rb.getAttributeAsBoolean("country"))
			setAttributeFromRequest(_user, "country");

		if (rb.getAttributeAsBoolean("email"))
			setAttributeFromRequest(_user, "email");

		if (rb.getAttributeAsBoolean("sex"))
			setAttributeFromRequest(_user, "sex");
			
		_user.setAttribute("itemid", rb.getId());
		_session.setAttribute("user", _user);

		if (_user.hasErrors()) {
			redirect("/browse/register");
		} else {
			long id = DataAdapter.getInstance().createUser(_user);
			_user.setAttribute("id", id);
			redirect("/browse/question");
		}
	}

	private void question() {
		try {
			Iterator questions = (Iterator) _session.getAttribute("questions");
			BrowseJDOM data = new BrowseJDOM(_user, _sponsor);

			if (questions.hasNext()) {
				Question question = (Question) questions.next();
				_session.setAttribute("questionid", new Long(question.getId()));
				_session.setAttribute("question", question);
				Element q = data.addEntity(question);
				BrowseJDOM.addDescription(q, question);
			} else {
				_session.removeAttribute("questions");
				_session.removeAttribute("itemid");
				redirect("/browse/index");
			}

			render("browse-question.xslt", data);
		} catch (Exception e) {
			long itemid = ((Long) _session.getAttribute("itemid")).longValue();
			Iterator questions = DataAdapter.getInstance().getQuestions(itemid);
			_session.setAttribute("questions", questions);
			redirect("/browse/question");
		}
	}

	private void answer() {
		Answer answer = new Answer();
		answer.setAttribute("questionid", _session.getAttribute("questionid").toString());
		answer.setAttribute("itemid", _session.getAttribute("itemid").toString());
		try {
			answer.setAttribute("userid", _user.getId());
		} catch (Exception ignored) {
			answer.setAttribute("userid", new String());
		}
		setAttributeFromRequest(answer, "openanswer");
		setAttributeFromRequest(answer, "multichoice");
		setAttributeFromRequest(answer, "scalevalue");
		
		Question question = (Question) _session.getAttribute("question");
		
		if ( answer.getErrors().size() == 3 && !question.getAttribute("type").equals("none")) {
			BrowseJDOM data = new BrowseJDOM(_user, _sponsor);
			Element q = data.addEntity(question);
			data.setFlag("error");
			BrowseJDOM.addDescription(q, question);
			render("browse-question.xslt", data);
		} else {
			DataAdapter.getInstance().createAnswer(answer);
			redirect("/browse/question");
		}
	}

	private void category() {
		// build ItemCriteria for this category and it's children
		String idStr = null;
		if ((idStr = _request.getParameter("id")) != null) {
			ItemCriteria criteria;
			try {
				criteria = new ItemCriteria(_request.getQueryString(), _request.getParameter("page"));
			} catch (Exception e) {
				criteria = new ItemCriteria(_request.getQueryString());
			}
			criteria.setSponsor(_sponsor);
			try {
				Category category = new Category(idStr);
				criteria.addCategory(category);
				descendCategory(criteria, category);
				BrowseJDOM data = new BrowseJDOM(_user, _sponsor);
				data.setItemsQuick(DataAdapter.getInstance().getItemsQuick(criteria));
				data.setCriteria(criteria);						
				data.setTitle("Consulter");
				data.setCategory(new Category(Long.parseLong(idStr)));
				render("browse-category.xslt", data);
			} catch (CategoryException e) {
				displayCategoryList();
			}
		} else
			displayCategoryList();
	}

	private void index() {
		ItemCriteria criteria;
		try {
			criteria = new ItemCriteria(_request.getQueryString(), _request.getParameter("page"));
		} catch (Exception e) {
			criteria = new ItemCriteria();
		}
		criteria.setSponsor(_sponsor);

		BrowseJDOM data = new BrowseJDOM(_user, _sponsor);
		data.setItemsQuick(DataAdapter.getInstance().getItemsQuick(criteria));
		data.setCriteria(criteria);
		data.setTitle("Index");
		render("browse-sponsor.xslt", data);
	}

	private void displayCategoryList() {
		BrowseJDOM data = new BrowseJDOM(_user, _sponsor);
		data.produceCategoryPage();
		data.setTitle("Consulter");
		render("browse-category.xslt", data);
	}
}