package com.pacdepot.domain;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.exception.BrandException;

/**
 * Brand entity
 */
public class Brand {
	private long _id = 0;
	private DImage _image;
	private String _name;
	private String _href;

	public Brand(String name, String href, DImage image) {
		_name = name;
		_href = href;
		_image = image;
	}

	public Brand(long id, String name, String href, DImage image) {
		_id = id;
		_name = name;
		_href = href;
		_image = image;
	}

	public Brand(long id) throws BrandException {
		try {
			Brand brand = DataAdapter.getInstance().getBrand(id);
			_id = brand.getId();
			_name = brand.getName();
			_href = brand.getHref();
			_image = brand.getImage();
		} catch (Exception e) {
	//		throw new BrandException("Unable to retrieve brand");
		}
	}

	public long getId() {
		return _id;
	}
	public String getName() {
		return _name;
	}
	public String getHref() {
		return _href;
	}
	public DImage getImage() {
		return _image;
	}
}
