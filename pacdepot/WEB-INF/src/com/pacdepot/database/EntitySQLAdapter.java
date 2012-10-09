package com.pacdepot.database;



public interface EntitySQLAdapter {
	String getTableName();
	String getColumns();
	String getInsertSQL();
	String getDeleteSQL(long id);
	String getSelectSQL(long id);
}