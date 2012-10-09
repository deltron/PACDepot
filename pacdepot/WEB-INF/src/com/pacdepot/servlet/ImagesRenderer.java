package com.pacdepot.servlet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.domain.Ad;
import com.pacdepot.domain.Banner;
import com.pacdepot.domain.Brand;
import com.pacdepot.domain.DImage;
import com.pacdepot.domain.Icon;
import com.pacdepot.domain.Item;
import com.pacdepot.domain.Question;
import com.pacdepot.domain.RegistrationBitmap;
import com.pacdepot.exception.BrandException;
import com.pacdepot.exception.IconException;
import com.pacdepot.exception.ItemException;

public class ImagesRenderer extends Renderer {
	private static Map cacheFile = new LinkedHashMap();
	private static Map cacheFileLen = new LinkedHashMap();

	ImagesRenderer() {
		_section = "images";
	}

	public static void flushAll() {
		cacheFile.clear();
		cacheFileLen.clear();
	}

	public void render() {
		super.render();

		if (_action.matches(".*(gif)"))
			renderFile(_response, _action, "image/gif");
		else if (_action.matches(".*(jpg)"))
			renderFile(_response, _action, "image/jpg");
		else {
			try {
				Method action = getClass().getDeclaredMethod(_action, null);
				action.invoke(this, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void icon() {
		try {
			renderImage(new Icon(Long.parseLong(_request.getParameter("id"))).getImage());
		} catch (IconException e) {}
	}

	private void banner() {
		long id = Long.parseLong(_request.getParameter("id"));
		Banner banner = DataAdapter.getInstance().getBanner(id);
		renderImage((DImage) banner.getAttribute("image"));
	}

	private void ad() {
		try {
			long id = Long.parseLong(_request.getParameter("id"));
			Ad ad = DataAdapter.getInstance().getAd(id);
			renderImage(ad.getImage());
		} catch (Exception e) {}
	}

	private void brand() {
		long id = Long.parseLong(_request.getParameter("id"));
		if (id == 1) // no image
			renderFile(_response, "spacer.gif", "image/gif");
		else
			try {
				renderImage(new Brand(id).getImage());
			} catch (BrandException be) {
				System.out.println(be);
			}
	}

	private void item() {
		Item item = null;
		String idParameter = null;
		if ((item = (Item) _session.getAttribute("item")) != null) {
			if (item.hasAttribute("image"))
				renderImageNoCache((DImage) item.getAttribute("image"));
			else if ((idParameter = _request.getParameter("id")) != null) {
				try {
					item = new Item(Long.parseLong(idParameter));
				} catch (ItemException e) {}
				if (item.hasAttribute("image"))
					renderImage((DImage) item.getAttribute("image"));
			}
		} else if ((idParameter = _request.getParameter("id")) != null) {
			try {
				item = new Item(Long.parseLong(idParameter));
			} catch (ItemException e) {}
			if (item.hasAttribute("image"))
				renderImageNoCache((DImage) item.getAttribute("image"));
		}
	}

	private void question() {
		Question question = null;
		String idParameter = null;
		if ((question = (Question) _session.getAttribute("question")) != null) {
			if (question.hasAttribute("image"))
				renderImageNoCache((DImage) question.getAttribute("image"));
			else if ((idParameter = _request.getParameter("id")) != null) {
				question = DataAdapter.getInstance().getQuestion(Long.parseLong(idParameter));
				if (question.hasAttribute("image"))
					renderImageNoCache((DImage) question.getAttribute("image"));
			}
		} else if ((idParameter = _request.getParameter("id")) != null) {
			question = DataAdapter.getInstance().getQuestion(Long.parseLong(idParameter));
			if (question.hasAttribute("image")) {
				renderImageNoCache((DImage) question.getAttribute("image"));
			}
		}
	}

	private void registrationBitmap() {
		RegistrationBitmap rb = null;
		String idParameter = null;
		if ((rb = (RegistrationBitmap) _session.getAttribute("registrationBitmap")) != null) {
			if (rb.hasAttribute("image"))
				renderImageNoCache((DImage) rb.getAttribute("image"));
			else if ((idParameter = _request.getParameter("id")) != null) {
				rb = DataAdapter.getInstance().getRegistrationBitmap(Long.parseLong(idParameter));
				if (rb.hasAttribute("image"))
					renderImageNoCache((DImage) rb.getAttribute("image"));
			}
		} else if ((idParameter = _request.getParameter("id")) != null) {
			rb = DataAdapter.getInstance().getRegistrationBitmap(Long.parseLong(idParameter));
			if (rb.hasAttribute("image"))
				renderImageNoCache((DImage) rb.getAttribute("image"));
		}
	}

	public void renderImageNoCache(DImage image) {
		renderByteArrayNoCache(image.getBytes(), image.getContentType());
	}

	public void renderImage(DImage image) {
		renderByteArray(image.getBytes(), image.getContentType());
	}

	private void renderFile(HttpServletResponse response, String filename, String contentType) {
		try {
			OutputStream out = _response.getOutputStream();
			_response.setContentType(contentType);
			byte[] image = null;
			int len = 0;
			if (cacheFile.containsKey(filename)) {
				image = (byte[]) cacheFile.get(filename);
				len = ((Integer) cacheFileLen.get(filename)).intValue();
			} else if (cacheFile.containsKey(_sponsor.getUsername() + "/" + filename)) {
				image = (byte[]) cacheFile.get(_sponsor.getUsername() + "/" + filename);
				len = ((Integer) cacheFileLen.get(_sponsor.getUsername() + "/" + filename)).intValue();
			} else {
				try {
					FileInputStream file =
						new FileInputStream(
							PacServlet.getBaseDir()
								+ "/home/"
								+ _sponsor.getUsername()
								+ "/images/"
								+ filename);
					image = new byte[1024 * 64]; // only files <65k from filesystem
					len = file.read(image);
					cacheFile.put(_sponsor.getUsername() + "/" + filename, image);
					cacheFileLen.put(_sponsor.getUsername() + "/" + filename, new Integer(len));
				} catch (IOException e) {
					FileInputStream file =
						new FileInputStream(PacServlet.getBaseDir() + "/images/" + filename);
					image = new byte[1024 * 64]; // only files <65k from filesystem
					len = file.read(image);
					cacheFile.put(filename, image);
					cacheFileLen.put(filename, new Integer(len));
				}
			}
			out.write(image, 0, len);
			out.flush();
			out.close();
		} catch (Exception e) {}
	}

	private void renderByteArray(byte[] array, String contentType) {
		try {
			_response.setContentType(contentType);
			OutputStream out = _response.getOutputStream();
			out.write(array, 0, array.length);
			out.flush();
			out.close();
		} catch (IOException e) {}
	}

	private void renderByteArrayNoCache(byte[] array, String contentType) {
		_response.addHeader("Pragma", "no-cache");
		_response.addHeader("Cache-Control", "no-cache");
		_response.setDateHeader("Expires", 0);

		renderByteArray(array, contentType);
	}
}
