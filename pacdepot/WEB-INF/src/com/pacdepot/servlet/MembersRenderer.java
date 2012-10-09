package com.pacdepot.servlet;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import com.oreilly.servlet.MultipartRequest;
import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Ad;
import com.pacdepot.domain.Answer;
import com.pacdepot.domain.Banner;
import com.pacdepot.domain.Icon;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Question;
import com.pacdepot.domain.RegistrationBitmap;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;
import com.pacdepot.exception.ItemException;
import com.pacdepot.xml.MembersJDOM;

/**
 * 		TODO: split user, sponsor, etc. to seperate classes
 *      TODO: make redirect() reference _section variable
 */
public class MembersRenderer extends XSLTRenderer {
	MembersRenderer() {
		_section = "members";
	}

	public void render() {
		super.render();
		try {
			Method action = getClass().getDeclaredMethod(_action, null);
			action.invoke(this, null);
		} catch (Exception e) {
			e.printStackTrace();
			redirect("/members/login");
		}
	}

	private void members() {
		redirect("/members/login");
	}

	/* ================================================================= */
	/* LOGIN / LOGOUT
	/* ================================================================= */
	private void login() {
		String state = null;
		User user = new User();

		try {
			if (_request.getParameter("username").length() > 0)
				user.setAttribute("username", _request.getParameter("username"));
			else
				throw new Exception();
		} catch (Exception e) {
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			data.setState("start");
			data.setTitle("Membres");
			render("members.xslt", data);
			return;
		}

		try {
			user = DataAdapter.getInstance().getUser(user);

			if (((String) user.getAttribute("password")).matches(_request.getParameter("password"))) {
				user.setAttribute("loggedIn", "true");
				_session.setAttribute("user", user);
				_user = user;
				_sponsor = new Sponsor(Long.parseLong((String) user.getAttribute("sponsorid")));

				if (((String) user.getAttribute("group")).matches("administrator")) {
					_sponsor = new Sponsor(Long.parseLong((String) user.getAttribute("sponsorid")));
					_sponsor.login(true);
					_session.setAttribute("sponsor", _sponsor);
				}
				redirect("/members/list");
				return;
			} else
				state = "error";
		} catch (Exception e) {
			state = "error";
		}

		MembersJDOM data = new MembersJDOM(_user, _sponsor);
		data.setState(state);
		data.setTitle("Membres");
		render("members.xslt", data);
	}

	private void logout() {
		_sponsor.logout();
		_user.logout();
		_session.setAttribute("sponsor", _sponsor);
		_session.removeAttribute("user");
		_session.removeAttribute("item");
		redirect("/");
	}
	/* ================================================================= */

	/* ================================================================= */
	/* Items: SELECT, LIST, PRINT */
	/* ================================================================= */
	private void select() {
		try {
			List items = new ArrayList();
			String id[] = _request.getParameterValues("selection");
			if (id != null)
				for (int i = 0; i < id.length; i++)
					try {
						Item item = new Item(Long.parseLong(id[i]));
						if (checkPermission(item))
							items.add(item);
					} catch (Exception e) {
						e.printStackTrace();
					}
			if (_request.getParameter("action").equals("deleteItems")) {
				for (Iterator iter = items.iterator(); iter.hasNext();) {
					Item item = (Item) iter.next();
					if (checkPermission(item)) {
						DataAdapter.getInstance().deleteItem(item);
						try {
							cancelAnswers(item);
							cancelRegistration(item);
							cancelQuestions(item);
							cancelUsers(item);
						} catch (Exception ignored) {
							ignored.printStackTrace();
						}
					}
				}
			} else if (_request.getParameter("action").equals("printItems")) {
				MembersJDOM data = new MembersJDOM(_user, _sponsor);
				data.setItems(items.iterator());
				render("members-print.xslt", data);
			}
		} catch (Exception ignored) {} finally {
			redirect("/members/list");
		}

	}

	private void list() {
		if (_sponsor.isLoggedIn() || _user.isLoggedIn()) {

			ItemCriteria criteria;
			try {
				criteria = new ItemCriteria(_request.getQueryString(), _request.getParameter("page"));
			} catch (Exception e) {
				criteria = new ItemCriteria();
			}

			if (_sponsor.isLoggedIn())
				criteria.setSponsor(_sponsor);
			else if (_user.isLoggedIn())
				criteria.setUser(_user);
			else
				redirect("/members/login");

			criteria.removeCriteria("confirmed");

			Iterator items = DataAdapter.getInstance().getItemsQuick(criteria);
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			data.setItemsQuick(items);
			data.setCriteria(criteria);
			data.setTitle("Gestion");
			render("members-list.xslt", data);
		} else
			redirect("/members/login");

	}

	private void print() {
		if (_sponsor.isLoggedIn() || _user.isLoggedIn()) {

			ItemCriteria criteria = new ItemCriteria();
			if (_sponsor.isLoggedIn())
				criteria.setSponsor((Sponsor) _session.getAttribute("sponsor"));
			else
				criteria.setUser(_user);

			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			data.setCriteria(criteria);
			data.setItems(DataAdapter.getInstance().getItems(criteria));
			render("members-print.xslt", data);
		} else
			redirect("/members/login");

	}
	/* ================================================================= */

	/* ================================================================= */
	/* Actions on Items */
	/*    - modify */
	/*    - cancel */
	/*    - confirm */
	/*    - retire */
	/*    - preview */
	/* ================================================================= */
	private void modifyItem() throws IOException, ServletException {
		Item item = getSessionItem(_request);

		if (checkPermission(item)) {
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			data.setState("modify");
			data.addBrands();
			data.addCategories();
			data.addCities();
			data.setItem(item);
			data.setTitle("Gestion");
			_session.removeAttribute("state");
			render("add.xslt", data);
		} else
			redirect("/members");
	}

	private void cancelItem() {
		Item item = (Item) _session.getAttribute("item");

		if (checkPermission(item)) {
			DataAdapter.getInstance().deleteItem(item);
			try {
				DataAdapter.getInstance().deleteRegistrationBitmap(
					DataAdapter.getInstance().getRegistrationBitmap(item.getId()));
				_session.removeAttribute("registrationBitmap");
				cancelQuestions(item);
				cancelAnswers(item);
				cancelUsers(item);
			} catch (Exception ignored) {}
		}

		_session.removeAttribute("item");
		redirect("/members/list");
	}

	private void confirmItem() {
		Item item = getSessionItem(_request);
		if (checkPermission(item))
			DataAdapter.getInstance().confirmItem(item);
		_session.removeAttribute("item");
		redirect("/members/list");
	}

	private void retireItem() {
		Item item = (Item) _session.getAttribute("item");
		if (checkPermission(item))
			DataAdapter.getInstance().retireItem(item);
		redirect("/members/list");
	}

	private void previewItem() throws ServletException {
		_session.setAttribute("sessionItemIsFromMembersList", "true");
		_session.removeAttribute("returnToSummary");

		Item item;
		try {
			item = new Item(Long.parseLong(_request.getParameter("id")));
			if (checkPermission(item)) {
				item.setAttribute("sponsor", _sponsor);
				_session.setAttribute("item", item);
				MembersJDOM data = new MembersJDOM(_user, _sponsor);
				data.setItem(item);
				data.setState("preview");
				data.setTitle("Gestion");
				render("browse-item.xslt", data);
			} else
				redirect("/members/login");
		} catch (Exception e) {
			Item sessionItem = getSessionItem(_request);
			Item requestItem = getRequestItem(_request);

			requestItem.setAttribute("sponsorid", _sponsor.getId());
			requestItem.setAttribute("sponsor", _sponsor);
			
			Map attributes;
			try {
				attributes = sessionItem.getAttributes();
				attributes.putAll(requestItem.getAttributes());
			} catch (Exception e1) {
				attributes = requestItem.getAttributes();
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
			} catch (Exception e1) {
				errors = requestItem.getErrors();
			}

			Item localitem = new Item(attributes, errors);
			if (checkPermission(localitem)) {
				if (errors.isEmpty()) {
					try {
						localitem.setAttribute("id", DataAdapter.getInstance().createItem(localitem));
						_session.setAttribute("item", localitem);
						MembersJDOM data = new MembersJDOM(_user, _sponsor);
						data.setState("preview");
						data.setItem(localitem);
						data.setTitle("Gestion");
						render("browse-item.xslt", data);
					} catch (Exception e1) {
						System.out.println("ERROR	Unable to create item");
						e1.printStackTrace();
					}

				} else {
					MembersJDOM data = new MembersJDOM(_user, _sponsor);
					_session.setAttribute("item", localitem);
					data.setState("preview");
					data.setItem(localitem);
					data.setTitle("Gestion");
					render("browse-item.xslt", data);
				}
			}
		}
	}
	/* ================================================================= */

	/* ================================================================= */
	/* Actions on RegistrationBitmaps */
	/*    - modify */
	/*    - cancel */
	/*    - confirm */
	/*    - preview */
	/*    - registration */
	/* ================================================================= */
	private void modifyRegistration() {
		RegistrationBitmap rb = null;
		long id = 0;
		try {
			id = Long.parseLong(_request.getParameter("id"));
			rb = DataAdapter.getInstance().getRegistrationBitmap(id);
			_session.setAttribute("registrationBitmap", rb);
		} catch (Exception e) {
			rb = (RegistrationBitmap) _session.getAttribute("registrationBitmap");
		}

		if (rb == null) {
			rb = new RegistrationBitmap();
			if (id != 0)
				rb.setAttribute("id", id);
			_session.setAttribute("registrationBitmap", rb);
		}

		if (checkPermission(rb)) {
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			data.setState("modify");
			Element q = data.addEntity(rb);
			MembersJDOM.addDescription(q, rb);
			render("add-registration.xslt", data);
		} else
			redirect("/members/login");
	}

	private void cancelRegistration(Item item) {
		if (checkPermission(item)) {
			RegistrationBitmap rb = DataAdapter.getInstance().getRegistrationBitmap(item.getId());
			DataAdapter.getInstance().deleteRegistrationBitmap(rb);
		} else {
			redirect("/members/login");
		}
	}

	private void previewRegistration() {
		RegistrationBitmap sessionRB = (RegistrationBitmap) _session.getAttribute("registrationBitmap");
		RegistrationBitmap requestRB = new RegistrationBitmap();
		MultipartRequest multi;
		try {
			multi = new MultipartRequest(_request, PacServlet.getTempDir(), 128 * 1024, "UTF-8");
		} catch (Exception e1) {
			try {
				requestRB.setError("size");
				multi = new MultipartRequest(_request, PacServlet.getTempDir(), 64 * 1024 * 1024, "UTF-8");
			} catch (Exception e2) {
				return;
			}
		}

		setBooleanAttribute(multi, requestRB, "firstname");
		setBooleanAttribute(multi, requestRB, "lastname");
		setBooleanAttribute(multi, requestRB, "telephone");
		setBooleanAttribute(multi, requestRB, "address");
		setBooleanAttribute(multi, requestRB, "city");
		setBooleanAttribute(multi, requestRB, "postalcode");
		setBooleanAttribute(multi, requestRB, "country");
		setBooleanAttribute(multi, requestRB, "province");
		setBooleanAttribute(multi, requestRB, "email");
		setBooleanAttribute(multi, requestRB, "sex");
		setOptionalAttribute(multi, requestRB, "title", 128);
		setDescription(requestRB, multi);
		setImage(requestRB, multi);

		Map attributes;
		try {
			attributes = sessionRB.getAttributes();
			attributes.putAll(requestRB.getAttributes());
		} catch (Exception e1) {
			try {
				attributes = requestRB.getAttributes();
			} catch (Exception e2) {
				e2.printStackTrace();
				redirect("/add/registration");
				return;
			}
		}

		if (attributes.containsKey("deleteImage")) {
			attributes.remove("image");
			attributes.remove("hasImage");
			attributes.remove("deleteImage");
		}

		Set errors;
		try {
			errors = sessionRB.getErrors();
			errors.retainAll(requestRB.getErrors());
			errors.addAll(requestRB.getErrors());
		} catch (Exception e) {
			errors = requestRB.getErrors();
		}

		RegistrationBitmap rb = new RegistrationBitmap(attributes, errors);
		if (rb.hasAttribute("image"))
			rb.setAttribute("hasImage", "true");

		MembersJDOM data = new MembersJDOM(_user, _sponsor);

		if (errors.isEmpty()) {
			rb.setAttribute("id", DataAdapter.getInstance().createRegistrationBitmap(rb));
			_session.setAttribute("registrationBitmap", rb);
			Element q = data.addEntity(rb);
			MembersJDOM.addDescription(q, rb);
			data.setState("preview");
			render("browse-registration.xslt", data);
		} else {
			_session.setAttribute("registrationBitmap", rb);
			data.setSection(getSection());
			Element q = data.addEntity(rb);
			MembersJDOM.addDescription(q, rb);
			render("add-registration.xslt", data);
		}
	}

	private void cancelRegistration() {
		RegistrationBitmap rb = null;
		try {
			long id = Long.parseLong(_request.getParameter("id"));
			rb = DataAdapter.getInstance().getRegistrationBitmap(id);
		} catch (Exception e) {
			rb = (RegistrationBitmap) _session.getAttribute("registrationBitmap");
		}
		if (checkPermission(rb)) {
			DataAdapter.getInstance().deleteRegistrationBitmap(rb);
			_session.removeAttribute("registrationBitmap");
			_session.setAttribute("state", "cancelled");
			redirect("/members/questionSummary");
		} else
			redirect("/members/login");
	}

	private void confirmRegistration() {
		_session.removeAttribute("registrationBitmap");
		_session.setAttribute("state", "success");
		redirect("/members/registration");
	}

	private void registration() {
		MembersJDOM data = new MembersJDOM(_user, _sponsor);
		RegistrationBitmap rb = new RegistrationBitmap();
		Item item = (Item) _session.getAttribute("item");
		if (checkPermission(item)) {
			rb.setAttribute("id", item.getId());
			_session.setAttribute("registrationBitmap", rb);
			render("add-registration.xslt", data);
		} else {
			redirect("/members/login");
		}
	}
	/* ================================================================= */

	/* ================================================================= */
	/* Actions on Questions */
	/*    - summary */
	/*    - question */
	/*    - preview */
	/*    -  */
	/* ================================================================= */
	private void questionSummary() {
		Item item = (Item) _session.getAttribute("item");
		if (checkPermission(item)) {
			_session.removeAttribute("registrationBitmap");
			_session.removeAttribute("question");
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			data.setItem(item);

			try {
				RegistrationBitmap rb = DataAdapter.getInstance().getRegistrationBitmap(item.getId());
				Element q = data.addEntity(rb);
				MembersJDOM.addDescription(q, rb);
			} catch (Exception ignored) {}

			for (Iterator iter = DataAdapter.getInstance().getQuestions(item.getId()); iter.hasNext();) {
				Question question = (Question) iter.next();
				Element questionElement = data.addEntity(question);
				MembersJDOM.addDescription(questionElement, question);
			}

			render("browse-questionSummary.xslt", data);
		} else {
			redirect("/members/login");
		}
	}

	private void question() {
		Item item = (Item) _session.getAttribute("item");
		if (checkPermission(item)) {
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			Question question = new Question();
			question.setAttribute("itemid", item.getId());
			_session.setAttribute("question", question);
			data.addEntity(question);
			render("add-question.xslt", data);
		} else {
			redirect("/members/login");
		}
	}

	private void previewQuestion() {
		Question sessionQuestion = (Question) _session.getAttribute("question");
		Question requestQuestion = new Question();
		MultipartRequest multi;
		try {
			multi = new MultipartRequest(_request, PacServlet.getTempDir(), "UTF-8");
		} catch (Exception e1) {
			try {
				requestQuestion.setError("size");
				multi = new MultipartRequest(_request, PacServlet.getTempDir(), "UTF-8");
			} catch (Exception e2) {
				return;
			}
		}

		setOptionalAttribute(multi, requestQuestion, "title", 72);
		setOptionalAttribute(multi, requestQuestion, "href", 128);
		setOptionalAttribute(multi, requestQuestion, "minLabel", 72);
		setOptionalAttribute(multi, requestQuestion, "maxLabel", 72);
		setOptionalAttribute(multi, requestQuestion, "choice1", 132);
		setOptionalAttribute(multi, requestQuestion, "choice2", 132);
		setOptionalAttribute(multi, requestQuestion, "choice3", 132);
		setOptionalAttribute(multi, requestQuestion, "choice4", 132);
		setOptionalAttribute(multi, requestQuestion, "choice5", 132);
		setOptionalAttribute(multi, requestQuestion, "choice6", 132);
		setOptionalAttribute(multi, requestQuestion, "choice7", 132);
		setOptionalAttribute(multi, requestQuestion, "choice8", 132);
		setRequiredAttribute(multi, requestQuestion, "type", 32);
		setDescription(requestQuestion, multi);
		setImage(requestQuestion, multi);

		Map attributes;
		try {
			attributes = sessionQuestion.getAttributes();
			attributes.putAll(requestQuestion.getAttributes());
		} catch (Exception e1) {
			try {
				attributes = requestQuestion.getAttributes();
			} catch (Exception e2) {
				e2.printStackTrace();
				redirect("/add/question");
				return;
			}
		}

		if (attributes.containsKey("deleteImage")) {
			attributes.remove("image");
			attributes.remove("hasImage");
			attributes.remove("deleteImage");
		}

		Set errors;
		try {
			errors = sessionQuestion.getErrors();
			errors.retainAll(requestQuestion.getErrors());
			errors.addAll(requestQuestion.getErrors());
		} catch (Exception e) {
			errors = requestQuestion.getErrors();
		}

		Question question = new Question(attributes, errors);
		if (question.hasAttribute("image"))
			question.setAttribute("hasImage", "true");

		if (checkPermission(question)) {
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			if (errors.isEmpty()) {
				question.setAttribute("id", DataAdapter.getInstance().createQuestion(question));
				_session.setAttribute("question", question);
				Element q = data.addEntity(question);
				MembersJDOM.addDescription(q, question);
				data.setState("preview");
				render("browse-question.xslt", data);
			} else {
				_session.setAttribute("question", question);
				data.setSection(getSection());
				Element q = data.addEntity(question);
				MembersJDOM.addDescription(q, question);
				render("add-question.xslt", data);
			}
		}
	}

	private void cancelQuestions(Item item) {
		if (checkPermission(item)) {
			for (Iterator iter = DataAdapter.getInstance().getQuestions(item.getId()); iter.hasNext();) {
				Question question = (Question) iter.next();
				DataAdapter.getInstance().deleteQuestion(question);
			}
		} else
			redirect("/members/login");
	}

	private void cancelQuestion() {
		Question q = null;
		try {
			long id = Long.parseLong(_request.getParameter("id"));
			q = DataAdapter.getInstance().getQuestion(id);
		} catch (Exception e) {
			q = (Question) _session.getAttribute("question");
		}

		if (checkPermission(q)) {
			DataAdapter.getInstance().deleteQuestion(q);
			_session.removeAttribute("question");
			_session.setAttribute("state", "cancelled");
			redirect("/members/questionSummary");
		} else
			redirect("/members/login");
	}

	private void confirmQuestion() {
		Question next = new Question();
		Question current = (Question) _session.getAttribute("question");
		next.setAttribute("itemid", current.getAttribute("itemid"));
		long sequence = current.getAttributeAsLong("sequence");
		sequence++;
		next.setAttribute("sequence", Long.toString(sequence));
		_session.setAttribute("question", next);
		_session.setAttribute("state", "success");
		MembersJDOM data = new MembersJDOM(_user, _sponsor);
		data.addEntity(next);
		render("add-question.xslt", data);
	}

	private void modifyQuestion() {
		MembersJDOM data = new MembersJDOM(_user, _sponsor);
		data.setState("modify");
		Question question = null;
		try {
			long id = Long.parseLong(_request.getParameter("id"));
			question = DataAdapter.getInstance().getQuestion(id);
		} catch (Exception e) {
			question = (Question) _session.getAttribute("question");
		}
		Element q = data.addEntity(question);
		MembersJDOM.addDescription(q, question);
		_session.setAttribute("question", question);
		render("add-question.xslt", data);
	}
	/* ================================================================= */

	/* ================================================================= */
	/* Answers:
	 * 	  - cancelUsers
	 *    - cancelAnswers
	 *    - answers
	 */
	private void cancelUsers(Item item) {
		if (checkPermission(item)) {
			for (Iterator iter = DataAdapter.getInstance().getUsers(item); iter.hasNext();) {
				User user = (User) iter.next();
				DataAdapter.getInstance().deleteUser(user);
			}
		}
	}

	private void cancelAnswers(Item item) {
		if (checkPermission(item)) {
			for (Iterator iter = DataAdapter.getInstance().getAnswers(item); iter.hasNext();) {
				Answer answer = (Answer) iter.next();
				DataAdapter.getInstance().deleteAnswer(answer);
			}
		}
	}

	private void answers() {
		// Who said MySQL doesn't have subselects!? :-)
		MembersJDOM data = new MembersJDOM(_user, _sponsor);
		Item item;
		try {
			item = (Item) _session.getAttribute("item");
			data.setItem(item);
			if (_sponsor.isLoggedIn() && item.belongsTo(_sponsor)) {
				for (Iterator users = DataAdapter.getInstance().getAnswersDistinctUsers(item);
					users.hasNext();
					) {
					User user = (User) users.next();
					data.addEntity("answerUser", user);
					for (Iterator answers = DataAdapter.getInstance().getAnswers(item, user);
						answers.hasNext();
						) {
						Answer answer = (Answer) answers.next();
						data.addEntity(answer);
					}
				}
				render("members-answers.xslt", data);
			} else {
				// no access
				redirect("/members/list");
			}
		} catch (Exception e) {
			e.printStackTrace();
			redirect("/members/list");
		}
	}
	/* ================================================================= */

	/* ================================================================= */
	/* Actions for Sponsor's Profile */
	/*		- createAd */
	/*		- deleteAds */
	/*		- updateHomepage */
	/*		- updateBanner */
	/*		- updateIcon */
	/*		- profile */
	/* ================================================================= */
	private void deleteAds() {
		if (_sponsor.isLoggedIn()) {
			List ads = new ArrayList();
			String id[] = _request.getParameterValues("delete");
			if (id != null)
				for (int i = 0; i < id.length; i++) {
					Ad ad = new Ad(new Long(id[i]).longValue());
					if (ad.getSponsor().getId() == _sponsor.getId())
						ads.add(ad);
				}
			DataAdapter.getInstance().deleteAds(ads.iterator());
		}
		redirect("/members/profile");
	}

	private void updateHomepage() {
		if (_sponsor.isLoggedIn()) {

			String homepage = getParameter("homepage");
			try {
				new SAXBuilder().build(new StringReader("<root>" + homepage + "</root>"));
				DataAdapter.getInstance().updateHomepage(_sponsor, homepage);
				_sponsor.setHomepage(homepage);
				_sponsor.removeError("homepage");
				_sponsor.removeAttribute("homepageStr");
			} catch (Exception e) {
				_sponsor.setError("homepage");
				_sponsor.setAttribute("homepageStr", homepage);
			}
			_session.setAttribute("sponsor", _sponsor);
			redirect("/members/profile#homepage");
		}
		redirect("/members/profile#homepage");
	}

	private void updateBanner() {
		try {
			if (_sponsor.isLoggedIn()) {
				Banner banner = new Banner();
				banner.setAttribute("id", _sponsor.getId());
				banner.setAttribute(
					"image",
					getImageFile(
						new MultipartRequest(_request, PacServlet.getTempDir(), 128 * 1024),
						1024,
						1024));
				DataAdapter.getInstance().updateBanner(banner);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redirect("/members/profile#banner");
		}
	}

	private void createAd() {
		try {
			MultipartRequest multi = new MultipartRequest(_request, PacServlet.getTempDir(), 128 * 1024);
			if (_sponsor.isLoggedIn())
				DataAdapter.getInstance().createAd(
					new Ad(
						(Sponsor) _session.getAttribute("sponsor"),
						getImageFile(multi, 468, 60),
						multi.getParameter("href")));
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			redirect("/members/profile");
		}
	}

	private void profile() {
		if (_sponsor.isLoggedIn()) {
			MembersJDOM data = new MembersJDOM(_user, _sponsor);
			data.addAds();
			data.setTitle("Profile");
			render("members-profile.xslt", data);
		} else
			redirect("/members/login");
	}

	private void updateIcon() {
		try {
			if (_sponsor.isLoggedIn()) {
				DataAdapter.getInstance().updateIcon(
					new Icon(
						_sponsor,
						getImageFile(
							new MultipartRequest(_request, PacServlet.getTempDir(), 128 * 1024, "UTF-8"),
							88,
							31)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redirect("/members/profile#icon");
		}
	}
	/* ================================================================= */

	/* ================================================================= */
	/* Permissions                                                       */
	/*   - question */
	/*   - item */
	/*   - registrationBitmap */
	/*   - TODO: move to seperate file */
	/*   - TODO: use item.belongsTo() */
	/* ================================================================= */
	private boolean checkPermission(Item item) {
		boolean legal = false;
		if (_sponsor.isLoggedIn()) {
			if (_sponsor
				.getAttribute("id")
				.equals(((Sponsor) item.getAttribute("sponsor")).getAttribute("id")))
				legal = true;
		} else if (_user.isLoggedIn()) {			
			if (_user.getAttribute("id") == null || item.getAttribute("userid") == null) {
				legal = false;
			} else if (item.getAttribute("userid").equals(_user.getAttribute("id")))
				legal = true;
		}
		return legal;
	}

	private boolean checkPermission(RegistrationBitmap rb) {
		try {
			Item item = DataAdapter.getInstance().getItem(rb.getAttributeAsLong("id"));
			return checkPermission(item);
		} catch (ItemException e) {
			return false;
		}
	}

	private boolean checkPermission(Question q) {
		try {
			Item item = DataAdapter.getInstance().getItem(q.getAttributeAsLong("itemid"));
			return checkPermission(item);
		} catch (ItemException e) {
			return false;
		}
	}
	/* ================================================================= */
}