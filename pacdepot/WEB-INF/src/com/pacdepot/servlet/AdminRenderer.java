package com.pacdepot.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.oreilly.javaxslt.util.ResultCache;
import com.oreilly.javaxslt.util.StylesheetCache;
import com.oreilly.servlet.MultipartRequest;
import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Brand;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.City;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;
import com.pacdepot.exception.SponsorException;
import com.pacdepot.xml.AdminJDOM;

public class AdminRenderer extends XSLTRenderer {
	AdminRenderer() {
		_section = "admin";
	}

	public void render() {
		//  Use this to block hosts from accessing the admin page
		//   -> taken as the domain name from web.xml config file
		//
		//	if (!request.getRemoteHost().endsWith(PacServlet.getAdminHost())) {
		//		response.sendRedirect("/");
		//		return;
		//	}

		super.render();
		try {
			Method action = getClass().getDeclaredMethod(_action, null);
			action.invoke(this, null);
		} catch (Exception e) {
			AdminJDOM data = new AdminJDOM(_user, _sponsor);
			render("admin.xslt", data);
		}
	}

	private void createSponsor() {
		DataAdapter.getInstance().createSponsor(
			getParameter("username"),
			getParameter("name"),
			getParameter("href"),
			Boolean.valueOf(_request.getParameter("public")).booleanValue());
		try {
			_response.sendRedirect("/admin#sponsors");
		} catch (IOException e) {}
	}

	private void deleteSponsors() {
		DataAdapter adapter = DataAdapter.getInstance();
		List members = new ArrayList();
		String id[] = _request.getParameterValues("delete");
		if (id != null)
			for (int i = 0; i < id.length; i++)
				try {
					members.add(new Sponsor(new Long(id[i]).longValue()));
				} catch (SponsorException se) {
					System.out.println(se);
				}
		adapter.deleteSponsors(members.iterator());
		try {
			_response.sendRedirect("/admin#sponsors");
		} catch (IOException e) {}
	}

	private void createCity() {
		String name = null;
		try {
			name = new String(_request.getParameter("name").getBytes(), "UTF-8");
		} catch (UnsupportedEncodingException e) {}
		DataAdapter.getInstance().createCity(name);
		try {
			_response.sendRedirect("/admin#cities");
		} catch (IOException e) {}
	}

	private void deleteCities() {
		DataAdapter adapter = DataAdapter.getInstance();
		List cities = new ArrayList();
		String id[] = _request.getParameterValues("delete");
		if (id != null)
			for (int i = 0; i < id.length; i++)
				try {
					cities.add(new City(new Long(id[i]).longValue()));
				} catch (Exception e) {}

		adapter.deleteCities(cities.iterator());
		try {
			_response.sendRedirect("/admin#cities");
		} catch (IOException e) {}
	}

	private void createBrand() {
		try {
			MultipartRequest multi =
				new MultipartRequest(_request, PacServlet.getTempDir(), 128 * 1024, "UTF-8");

			DataAdapter.getInstance().createBrand(
				new Brand(
					multi.getParameter("name"),
					multi.getParameter("href"),
					getImageFile(multi, 128, 64)));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				_response.sendRedirect("/admin#brands");
			} catch (IOException e) {}
		}
	}

	private void deleteBrands() {
		DataAdapter adapter = DataAdapter.getInstance();
		List brands = new ArrayList();
		String id[] = _request.getParameterValues("delete");
		for (int i = 0; i < id.length; i++)
			try {
				brands.add(new Brand(new Long(id[i]).longValue()));
			} catch (Exception e) {
				System.out.println(e);
			}
		adapter.deleteBrands(brands.iterator());
		try {
			_response.sendRedirect("/admin#brands");
		} catch (IOException e) {}
	}

	private void createCategory() {
		try {
			DataAdapter.getInstance().createCategory(
				new Category(
					-1,
					(new Long(_request.getParameter("parentid"))).longValue(),
					new String(_request.getParameter("name").getBytes(), "UTF-8")));
		} catch (UnsupportedEncodingException e) {}
		try {
			_response.sendRedirect("/admin#categories");
		} catch (IOException e) {}
	}

	private void deleteCategories() {
		List categories = new ArrayList();
		String id[] = _request.getParameterValues("delete");
		if (id != null)
			for (int i = 0; i < id.length; i++)
				try {
					categories.add(new Category(new Long(id[i]).longValue()));
				} catch (Exception e) {}
		DataAdapter.getInstance().deleteCategories(categories.iterator());
		try {
			_response.sendRedirect("/admin#categories");
		} catch (IOException e) {}

	}

	private void createUser() {
		User user = new User();
		user.setAttribute("username", getParameter("username"));
		user.setAttribute("password", getParameter("password"));
		user.setAttribute("name", getParameter("name"));
		user.setAttribute("group", getParameter("group"));
		user.setAttribute("email", getParameter("email"));
		user.setAttribute("telephone", getParameter("telephone"));
		user.setAttribute("cityid", getParameter("cityid"));
		user.setAttribute("sponsorid", getParameter("sponsorid"));
		DataAdapter.getInstance().createUser(user);
		try {
			_response.sendRedirect("/admin#users");
		} catch (IOException e) {}

	}
	private void deleteUsers() {
		List users = new ArrayList();
		String id[] = _request.getParameterValues("delete");
		if (id != null)
			for (int i = 0; i < id.length; i++)
				try {
					users.add(new User(new Long(id[i]).longValue()));
				} catch (Exception e) {}

		for (Iterator iter = users.iterator(); iter.hasNext();) {
			User user = (User) iter.next();
			DataAdapter.getInstance().deleteUser(user);
		}

		try {
			_response.sendRedirect("/admin#users");
		} catch (IOException e) {}
	}

	private void flushXSLTCache() {
		StylesheetCache.flushAll();
		ResultCache.flushAll();
		try {
			_response.sendRedirect("/admin");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void flushAccessCache() {
		RenderFactory.flushAll();
		try {
			_response.sendRedirect("/admin");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void flushImageCache() {
		ImagesRenderer.flushAll();
		try {
			_response.sendRedirect("/admin");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
