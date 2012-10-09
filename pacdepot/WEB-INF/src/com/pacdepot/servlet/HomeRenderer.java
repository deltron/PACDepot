package com.pacdepot.servlet;

import com.pacdepot.xml.PacJDOM;

public class HomeRenderer extends XSLTRenderer {
	HomeRenderer() {
		_section = "home";
	}

	public void render() {
		super.render();
		_response.addHeader("Pragma", "no-cache");
		_response.addHeader("Cache-Control","no-cache");
		_response.setDateHeader ("Expires", 0);
		PacJDOM data = new PacJDOM(_user, _sponsor);
		render("home.xslt", data);
	}
}