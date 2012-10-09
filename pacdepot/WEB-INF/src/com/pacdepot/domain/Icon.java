package com.pacdepot.domain;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.exception.IconException;

public class Icon implements ImageEntity {
	private long _id = 0;
	private DImage _image;
	
	public Icon(Sponsor sponsor, DImage image) {
		this(sponsor.getId(), image);
	}
	
	public Icon(long id, DImage image) {
		_id = id;
		_image = image;
	}
	
	public Icon(long id) throws IconException {
		try {
			_id = id;
			_image = DataAdapter.getInstance().getIcon(id).getImage();
		} catch ( NullPointerException e ) {
		}
	}
	
	public long getId() { return _id; }
	public DImage getImage() { return _image; }
}
