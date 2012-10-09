package com.pacdepot.database;

public class RegistrationBitmapSQLAdapter implements EntitySQLAdapter {
	static private String _tableName = "Registrationbitmaps";

		static private String _columns = "id, " //1 
		+"firstname, " //2
		+"lastname, " //3
		+"telephone, " //4
		+"address," //5
		+"city, " //6
		+"postalcode, " //7
		+"country, " //8
		+"province, " //9
		+"email, " //10
		+"sex, " //11
		+"image, " //12
		+"contentType, " //13
		+"description, " //14
	+"title  "; //15

	private static int _columnCount = 15;

	public String getColumns() {
		return _columns;
	}

	public String getTableName() {
		return _tableName;
	}

	public String getDeleteSQL(long id) {
		return "DELETE FROM " + getTableName() + " WHERE id = " + id;
	}

	public String getInsertSQL() {
		return "INSERT INTO " + getTableName() + "(" + getColumns() + ")" + getUnkownValues();
	}

	public String getSelectSQL(long id) {
		return "SELECT " + getColumns() + " FROM " + getTableName() + " WHERE id = " + id;
	}

	public static String getUnkownValues() {
		StringBuffer sb = new StringBuffer();
		sb.append("    VALUES (");
		for (int i = 0; i < _columnCount - 1; i++)
			sb.append("?,");
		sb.append("?)   ");
		return sb.toString();
	}
}