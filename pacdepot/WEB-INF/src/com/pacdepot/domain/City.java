package com.pacdepot.domain;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.exception.CityException;

public class City {
	private long _id = 0;
	private String _name = null;

	public City(long id, String name) {
		_id = id;
		_name = name;
	}

	public City(long id) throws CityException {
		try {
			_id = id;
			_name = DataAdapter.getInstance().getCity(id).getName();
		} catch (Exception e) {
			throw new CityException(e.getMessage());
		}
	}

	public City(String string) throws CityException {
		this(Long.parseLong(string));
	}

	public long getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}
}
