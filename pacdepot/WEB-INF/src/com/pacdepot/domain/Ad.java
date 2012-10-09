package com.pacdepot.domain;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.pacdepot.database.DataAdapter;

/** todo: refactor */
public class Ad {
	private long _id = 0;
	private Sponsor _sponsor = null;
	private String _href = null;
	private DImage _image = null;
	private static Map _cache = new LinkedHashMap();
	private static Iterator _list = null;
	private boolean _valid = false;

	public Ad(Sponsor sponsor, DImage image, String href) {
		_sponsor = sponsor;
		_image = image;
		_href = href;
	}

	public Ad(long id, Sponsor sponsor, DImage image, String href) {
		_id = id;
		_sponsor = sponsor;
		_image = image;
		_href = href;
		_valid = true;
	}

	public Ad(long id) {
		Ad ad = DataAdapter.getInstance().getAd(id);
		if (ad != null) {
			_id = ad.getId();
			_sponsor = ad.getSponsor();
			_image = ad.getImage();
			_href = ad.getHref();
			_valid = true;
		}
	}

	public Ad(Sponsor sponsor) {
		if (_cache.containsKey(new Long(sponsor.getId())))
			_list = (Iterator) _cache.get(new Long(sponsor.getId()));
		else
			_list = DataAdapter.getInstance().getAllAds(sponsor);

		if (_list == null || !_list.hasNext()) {
			_list = DataAdapter.getInstance().getAllAds(sponsor);
			_cache.put(new Long(sponsor.getId()), _list);
		}

		if (_list.hasNext()) {
			Ad ad = (Ad) _list.next();
			_list.remove();
			_cache.put(new Long(sponsor.getId()), _list);
			_id = ad.getId();
			_sponsor = ad.getSponsor();
			_image = ad.getImage();
			_href = ad.getHref();
			_valid = true;
		}
	}

	public boolean isValid() {
		return _valid;
	}
	public long getId() {
		return _id;
	}
	public Sponsor getSponsor() {
		return _sponsor;
	}
	public DImage getImage() {
		return _image;
	}
	public String getHref() {
		return _href;
	}
}
