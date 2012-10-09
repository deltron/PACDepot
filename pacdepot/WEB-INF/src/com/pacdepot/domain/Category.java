package com.pacdepot.domain;

import com.pacdepot.database.DataAdapter;
import com.pacdepot.exception.CategoryException;

public class Category /* TODO: extends Entity  */ {
	private long _id;
	private long _parentId;
	private String _name = null;
	private String _template = null;

	public Category() {}


	public Category(long id, long parentid, String name) {
		_id = id;
		_parentId = parentid;
		_name = name;
	}

	public Category(long id, long parentid, String name, String template) {
		_id = id;
		_parentId = parentid;
		_name = name;
		_template = template;
	}

	public Category(long id) throws CategoryException {
		_id = id;
		try {
			Category category = DataAdapter.getInstance().getCategory(_id);
			_parentId = category.getParentid();
			_name = category.getName();
			_template = category.getTemplate();
		} catch (Exception e) {
			throw new CategoryException();
		}
	}

	public Category(Long l) throws CategoryException {
		this(l.longValue());
	}
	
	public Category(String s) throws CategoryException {
		this(Long.parseLong(s));	
	}

	public boolean isTopLevel() {
		try {
			return (_parentId == 0) ? true : false;
		} catch (Exception e) {
			return false;
		}
	}

	public long getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public long getParentid() {
		return _parentId;
	}
	
	public String getTemplate() {
		return _template;	
	}
}