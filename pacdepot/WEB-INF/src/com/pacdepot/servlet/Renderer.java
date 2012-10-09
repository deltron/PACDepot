package com.pacdepot.servlet;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import com.oreilly.servlet.MultipartRequest;
import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Brand;
import com.pacdepot.domain.Category;
import com.pacdepot.domain.City;
import com.pacdepot.domain.DImage;
import com.pacdepot.domain.Entity;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.ItemCriteria;
import com.pacdepot.domain.Sponsor;
import com.pacdepot.domain.User;
import com.pacdepot.exception.BrandException;
import com.pacdepot.exception.ImageException;

public abstract class Renderer {
	protected HttpServletRequest _request = null;
	protected HttpServletResponse _response = null;
	protected HttpSession _session = null;
	protected User _user = null;
	protected Sponsor _sponsor = null;
	protected HttpServlet _servlet = null;
	protected String _action = null;
	protected String _section = null;
	private static long _hits = 0;

	public String getSection() {
		return _section;
	}

	protected String getAction() {
		return _request.getPathInfo().substring(
			_request.getPathInfo().lastIndexOf('/') + 1);
	}

	public static String getAction(HttpServletRequest request) {
		return request.getPathInfo().substring(
			request.getPathInfo().lastIndexOf('/') + 1);
	}

	protected Item getSessionItem(HttpServletRequest request) {
		return ((Item) _session.getAttribute("item"));
	}

	//
	// This is kind of hacked together because getScaledInstance() returns 
	// an Image and ImageIO.write() only works with BufferedImage.... !$%!# Java Graphics API!!!
	//				
	// Everything must be JPEG because there's no GIF encoder (use Acme ?)...use PNG?
	protected DImage getImageFile(
		MultipartRequest multi,
		int maxWidth,
		int maxHeight)
		throws ImageException {
		DImage image = null;
		Enumeration files = multi.getFileNames();

		try {
			if (files.hasMoreElements()) {
				String name = (String) files.nextElement();
				if (multi.getFile(name) != null) {
					Image im = ImageIO.read(multi.getFile(name));

					if (im.getWidth(null) > maxWidth)
						im =
							im.getScaledInstance(
								maxWidth,
								-1,
								Image.SCALE_SMOOTH);
					if (im.getHeight(null) > maxHeight)
						im =
							im.getScaledInstance(
								-1,
								maxHeight,
								Image.SCALE_SMOOTH);

					BufferedImage bi =
						new BufferedImage(
							im.getWidth(null),
							im.getHeight(null),
							BufferedImage.TYPE_INT_RGB);
					bi.createGraphics().drawImage(im, null, null);
					File file = File.createTempFile("item", ".jpg");
					ImageIO.write(bi, "jpg", file);
					image =
						new DImage(
							file,
							"image/jpg",
							bi.getWidth(),
							bi.getHeight());
				} else
					throw new ImageException("no file name");
			} else
				throw new ImageException("no file in request");
		} catch (Exception e) {
			throw new ImageException(e.getMessage());
		}
		return image;
	}

	protected Item getRequestItem(HttpServletRequest request)
		throws ServletException {
		Item item = new Item();
		MultipartRequest multi;
		try {
			multi =
				new MultipartRequest(request, PacServlet.getTempDir(), "UTF-8");
		} catch (Exception ioe) {
			try {
				item.setError("size");
				multi =
					new MultipartRequest(
						request,
						PacServlet.getTempDir(),
						"UTF-8");
			} catch (Exception e) {
				return null;
			}
		}

		try {
			item.setAttribute("type", multi.getParameter("type"));
		} catch (Exception e) {
			item.setError("type");
		}

		try {
			String titleStr = truncate(multi, "title", 60);
			if (titleStr.length() > 0)
				item.setAttribute("title", titleStr);
			else
				item.setError("title");
		} catch (Exception e) {
			item.setError("title");
		}
		
		item.setAttribute("href", truncate(multi, "href", 255));
		item.setAttribute("nature", multi.getParameter("nature"));
		setLifespan(item, multi);
		setQuantity(item, multi);
		setDescription(item, multi);
		setPrice(item, multi);
		setAge(item, multi);
		setCity(item, multi);
		setCategory(item, multi);
		setSponsor(item, multi);
		setBrand(item, multi);
		setUser(item, multi);
		setImage(item, multi);

		return item;
	}

	private void setUser(Item item, MultipartRequest multi) {
		User user = new User();
		try {
			user.setAttribute("name", truncate(multi, "userName", 40));
		} catch (Exception e) {
		}

		try {
			user.setAttribute(
				"telephone",
				truncate(multi, "userTelephone", 40));
		} catch (Exception e) {
		}

		try {
			user.setAttribute("email", truncate(multi, "userEmail", 40));
		} catch (Exception e) {
		}

		item.setAttribute("user", user);
	}

	private void setLifespan(Item item, MultipartRequest multi) {
		try {
			int lifespan =
				(new Integer(multi.getParameter("lifespan"))).intValue();
			if (lifespan > 0)
				item.setAttribute("lifespan", lifespan);
		} catch (Exception e) {
			item.setAttribute("lifespan", 90);
		}
	}

	private void setQuantity(Item item, MultipartRequest multi) {
		//	Be careful! 
		// JDK is inconsistant about what a blank string returns for NumberFormatException
		// for different type (Double, Integer, etc.)
		try {
			int quantity =
				(new Integer(multi
					.getParameter("quantity")
					.replaceAll(",", "")))
					.intValue();
			if (quantity > 0)
				item.setAttribute("quantity", quantity);
		} catch (Exception e) {
		}
	}

	protected void setDescription(Entity entity, MultipartRequest multi) {
		try {
			String description = truncate(multi, "description", 10000);
			entity.setAttribute("description", description);
			new SAXBuilder().build(
				new StringReader("<p>" + description + "</p>"));
		} catch (JDOMException je) {
			entity.setError("description");
			entity.setError("html");
		} catch (Exception e) {
		}
	}

	private void setPrice(Item item, MultipartRequest multi) {
		//	Be careful! 
		// JDK is inconsistant about what a blank string returns for NumberFormatException
		// for different type (Double, Integer, etc.)

		try {
			double price =
				(new Double(multi
					.getParameter("price")
					.replaceAll(" ", "")
					.replaceAll("[$]", "")
					.replaceAll(",", "")))
					.doubleValue();
			if (price > 0)
				item.setAttribute(
					"price",
					(new DecimalFormat("#0.00").format(price)).toString());
		} catch (Exception e) {
		}
	}

	protected String truncate(
		MultipartRequest multi,
		String parameter,
		int length) {
		try {
			String s =
				new String(multi.getParameter(parameter).getBytes(), "UTF-8");
			if (s.length() > length)
				s = new String(s.substring(0, length) + "...");
			return s;
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	private void setBrand(Item item, MultipartRequest multi) {
		String brandStr = multi.getParameter("userbrand");
		if (brandStr != null && brandStr.length() > 0) {
			item.setAttribute("userbrand", brandStr);
			try {
				item.setAttribute("brand", new Brand(1));
			} catch (BrandException e) {
			}
		} else
			try {
				item.setAttribute("userbrand", new String());
				item.setAttribute(
					"brand",
					new Brand(Long.parseLong(multi.getParameter("brandid"))));
			} catch (Exception e1) {
				try {
					item.setAttribute("brand", new Brand(1));
				} catch (Exception e2) {
					System.out.println(
						"FATAL	Unable to set default brand (id #1)");
				}
			}
	}

	private void setAge(Item item, MultipartRequest multi) {
		int ageY = 0;
		int ageM = 0;

		try {
			String ageYears = multi.getParameter("ageYears");
			if (ageYears.equals(""))
				ageY = 0;
			else
				ageY = (new Integer(ageYears)).intValue();
		} catch (NumberFormatException e) {
			item.setError("age");
			item.setError("ageYY");
			item.setAttribute("ageYY", multi.getParameter("ageYears"));
		} catch (Exception e) {
		}

		try {
			String ageMonths = multi.getParameter("ageMonths");
			if (ageMonths.equals(""))
				ageM = 0;
			else
				ageM = (new Integer(ageMonths)).intValue();
			ageY += ageM / 12;
			ageM = ageM % 12;
		} catch (NumberFormatException e) {
			item.setError("age");
			item.setError("ageMM");
			item.setAttribute("ageMM", multi.getParameter("ageMonths"));
		} catch (Exception e) {
		}

		int months = (ageY * 12) + ageM;
		if (months > 0)
			item.setAttribute("ageInMonths", months);
	}

	protected void setCategory(Item item, MultipartRequest multi) {
		try {
			item.setAttribute(
				"category",
				new Category(new Long(multi.getParameter("categoryid"))));
		} catch (Exception e) {
			item.setError("categoryid");
		}
	}

	private void setCity(Item item, MultipartRequest multi) {
		try {
			item.setAttribute(
				"city",
				new City(Long.parseLong(multi.getParameter("cityid"))));
		} catch (Exception e) {
			item.setError("cityid");
		}
	}

	protected void setImage(Entity entity, MultipartRequest multi) {
		try {
			if (!entity.hasError("size")) {
				Category category = (Category) entity.getAttribute("category");
				try {
					if (category.getParentid() == 0
						&& category.getName().matches("Photos")) {
						entity.setAttribute(
							"image",
							getImageFile(multi, 635, 455));
						// about 7x5"
					} else {
						entity.setAttribute(
							"image",
							getImageFile(multi, 400, 300));
					}
				} catch (Exception e) {
					entity.setAttribute("image", getImageFile(multi, 400, 300));
				}
			}
		} catch (ImageException e) {
			if (e.getMessage() == null)
				entity.setError("photo");
		}

		try {
			if (multi.getParameter("deleteImage").matches("true"))
				entity.setAttribute("deleteImage");
		} catch (Exception e) {
		}
	}

	protected void setSponsor(Item item, MultipartRequest multi)
		throws ServletException {
		try {
			item.setAttribute(
				"sponsor",
				new Sponsor(Long.parseLong(multi.getParameter("sponsorid"))));
		} catch (Exception e1) {
			item.setAttribute("sponsor", PacServlet.getDefaultSponsor());
		}
	}

	protected static void descendCategory(
		ItemCriteria criteria,
		Category parent) {
		for (Iterator children =
			DataAdapter.getInstance().getChildCategories(parent.getId());
			children.hasNext();
			) {
			Category child = (Category) children.next();
			try {
				criteria.addCategory(new Category(child.getId()));
			} catch (Exception e) {
			}
			descendCategory(criteria, child);
		}
	}

	protected String getParameter(String name) {
		try {
			return new String(_request.getParameter(name).getBytes(), "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	public void setRequest(HttpServletRequest request) {
		try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		_request = request;
	}

	public void setResponse(HttpServletResponse response) {
		_response = response;
	}

	public void log() {
		log(new String());
	}

	public void log(String message) {
		System.out.println(
			new Date(System.currentTimeMillis())
			    + " - "
			    + _request.getServerName()
				+ " - "
				+ (++_hits)
				+ " - "
				+ _request.getRemoteAddr()
				+ " - "
				+ ((_sponsor.getUsername() == null)
					? "null"
					: _sponsor.getUsername())
				+ " - "
				+ _session.getId()
				+ " - "
				+ _request.getPathInfo()
				+ " - "
				+ _request.getQueryString()
				+ " - "
				+ message);
	}

	public void render() {
		log();
		_action = getAction();
	}

	public void setSponsor(Sponsor sponsor) {
		_sponsor = sponsor;
	}

	public void setUser(User user) {
		_user = user;
	}

	public void setServlet(HttpServlet servlet) {
		_servlet = servlet;
	}

	public void setSession(HttpSession session) {
		_session = session;
	}

	public void redirect(String url) {
		try {
			_response.sendRedirect(url);
		} catch (Exception ignored) {
		}
	}

	public void setAttributeFromRequest(Entity entity, String attribute) {
		String value = getParameter(attribute);
		try {
			if (value.length() > 0)
				entity.setAttribute(attribute, value);
			else {
				entity.setAttribute(attribute, new String());
				entity.setError(attribute);
			}
		} catch (Exception e) {
			entity.setError(attribute);
		}
	}
}