package com.pacdepot.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Entity {
	protected Map _attributes = new HashMap(); // For strings, primitives
	protected Map _elements = new HashMap(); // For sub-entities
	protected Set _errors = new HashSet();
	protected Set _requiredAttributes = new HashSet();
	
	public Entity() {}

	public Entity(long id) {
		setAttribute("id", id);	
	}

	public Entity(Map attributes, Set errors) {
		_attributes = attributes;
		_errors = errors;
	}

	public Map getAttributes() {
		return _attributes;
	}
	
	public void setAttribute(Object key) {
		setAttribute(key, new String());
	}

	public void setAttribute(Object key, Object value) {
		if (key != null && value != null)
			_attributes.put(key, value);
	}

	public void setAttribute(String key, int value) {
		setAttribute(key, new Integer(value));
	}

	public void setAttribute(String key, long value) {
		setAttribute(key, new Long(value));
	}

	public void setAttribute(String key, double value) {
		setAttribute(key, new Double(value));
	}

	public void setAttribute(String key, boolean value) {
		setAttribute(key, new Boolean(value));
	}

	public void setAttribute(Object key, String value) {
		if (value != null)
			setAttribute(key, (Object) value);
	}
	
	public boolean hasAttribute(Object key) {
		return _attributes.containsKey(key);
	}
	
	public void removeAttribute(Object key) {
		_attributes.remove(key);	
	}

	public Set getErrors() {
		return _errors;
	}
	
	public boolean hasErrors() {
		return !_errors.isEmpty();
	}	
	
	public boolean hasError(Object key) {
		return _errors.contains(key);
	}
	
	public void setError(Object key) {
		_errors.add(key);
	}
	
	public void removeError(Object key) {
		_errors.remove(key);
	}
	
	public void clearErrors() {
		_errors.clear();
	}

	public static Set getRequiredAttributes() {
		return null;
	}

	public Set getAttributeEntrySet() {
		return _attributes.entrySet();
	}

	public Object getAttribute(Object key) {
		return _attributes.get(key);
	}

	public double getAttributeAsDouble(Object key) {
		return ((Double) _attributes.get(key)).doubleValue();
	}

	public int getAttributeAsInt(Object key) {
		return ((Integer) _attributes.get(key)).intValue();
	}

	public boolean getAttributeAsBoolean(Object key) {
		try {
			return ((Boolean) _attributes.get(key)).booleanValue();
		} catch (Exception e) { 
			return false;
		}	
	}

	public long getAttributeAsLong(Object key) {
		return ((Long) _attributes.get(key)).longValue();
	}	
	
	public long getId() {
		try {
			return getAttributeAsLong("id");
		} catch (Exception e) {
			return Long.parseLong(getAttribute("id").toString());
		}
	}

	public void setLabel(String label) {
		setAttribute("label", label);
	}
	
	public String getLabel() {
		return (String) getAttribute("label");
	}
	
	public String toString() {
		return getLabel() + ":" + getId();
	}
}