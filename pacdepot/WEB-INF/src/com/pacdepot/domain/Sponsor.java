package com.pacdepot.domain;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.exception.SponsorException;

/** 
 * TODO: Change to attribute-based
 * 
 */
public class Sponsor extends Entity {
	private long _id = 0;
	private String _username;
	private String _name;
	private String _href;
	private String _homepage;
	private boolean _public;
	private boolean _customTemplates;
	private Icon _icon;
	private Banner _banner;
	private boolean _loggedIn = false;
	private boolean _valid = false;

	public Sponsor(long id) throws SponsorException {
		try {
			Sponsor sponsor = DataAdapter.getInstance().getSponsor(id);
			setAttribute("id", id);
			_id = id;
			_username = sponsor.getUsername();
			_name = sponsor.getName();
			_href = sponsor.getHref();
			_public = sponsor.isPublic();
			_homepage = sponsor.getHomepage();
			_customTemplates = sponsor.hasCustomTemplates();
			//			_valid = setBanner(id) && setIcon(id);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new SponsorException("could not retrieve sponsor #" + id + " from database");
		}
	}

	public Sponsor(String username) throws SponsorException {
		if (!(username.length() > 0))
			throw new NullPointerException();
		try {
			_id = DataAdapter.getInstance().getSponsorId(username);
			Sponsor sponsor = new Sponsor(_id);
			_username = sponsor.getUsername();
			_name = sponsor.getName();
			_href = sponsor.getHref();
			_public = sponsor.isPublic();
			_homepage = sponsor.getHomepage();
			_customTemplates = sponsor.hasCustomTemplates();
			//			_valid = setBanner(_id)  && setIcon(_id);
		} catch (Exception e) {
			throw new SponsorException();
		}
	}

	public Sponsor(
		long id,
		String username,
		String name,
		String href,
		boolean pub,
		String homepage,
		boolean customTemplates)
		throws SponsorException {
		setAttribute("id", id);
		_id = id;
		_username = username;
		_name = name;
		_href = href;
		_public = pub;
		_homepage = homepage;
		_customTemplates = customTemplates;
	}

	public long getId() {
		if (hasAttribute("id"))
			_id = getAttributeAsLong("id");
		return _id;
	}
	public String getUsername() {
		return _username;
	}
	public boolean isPublic() {
		return _public;
	}
	public boolean isValid() {
		return _valid;
	}
	public boolean isLoggedIn() {
		return _loggedIn;
	}
	public String getName() {
		return _name;
	}
	public String getHref() {
		return _href;
	}
	public Banner getBanner() {
		return _banner;
	}
	public Icon getIcon() {
		return _icon;
	}
	public String getHomepage() {
		return _homepage;
	}
	public void logout() {
		_loggedIn = false;
	}

	public void setHomepage(String homepage) {
		_homepage = homepage;
	}

	public void login(boolean b) {
		_loggedIn = b;
	}
	public boolean hasCustomTemplates() {
		return _customTemplates;
	}

	public void setCustomTemplates(boolean customTemplates) {
		_customTemplates = customTemplates;
	}

}
