package org.citic.iiot.persist.gener.metadata;


import org.citic.iiot.core.util.StringUtil;

public abstract class Column {

	protected static final String[][] mappings = { { "12", "VARCHAR", "String" },
			{ "-1", "LONGVARCHAR", "String" }, { "4", "INTEGER", "int" },
			{ "-5", "BIGINT", "long" }, { "1", "CHAR", "String" }, { "2", "NUMERIC", "double" },
			{ "3", "DECIMAL", "double" }, { "-7", "BIT", "int" }, { "-6", "TINYINT", "int" },
			{ "5", "SMALLINT", "int" }, { "7", "REAL", "float" }, { "6", "FLOAT", "double" },
			{ "8", "DOUBLE", "double" }, { "-2", "BINARY", "byte[]" },
			{ "-3", "VARBINARY", "byte[]" }, { "-4", "LONGVARBINARY", "byte[]" },
			{ "91", "DATE", "java.sql.Date" }, { "92", "TIME", "java.sql.Time" },
			{ "93", "TIMESTAMP", "java.sql.Timestamp" }, { "2005", "CLOB", "String" } };

	/* 表字段名称 */
	private String columnName;

	/* 表字段数据库类型名称，如：VARCHAR */
	private String columnTypeName;

	/* 表字段类型标识，如：TIMESTAMP 标识为 93 */
	private int columnType;

	private boolean PK;

	private boolean autoIncrement;

	private boolean FK;

	private ForeginKey foreginKey;

	// 指定列的指定列宽
	private int precision;

	// 指定列的小数点右边的位数
	private int scale;

	// 包装jdbcType
	protected abstract int jdbcTypeWrapper(int jdbcType);

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnTypeName() {
		return columnTypeName;
	}

	public void setColumnTypeName(String columnTypeName) {
		this.columnTypeName = columnTypeName;
	}

	public int getColumnType() {
		return columnType;
	}

	public void setColumnType(int columnType) {
		this.columnType = columnType;
	}

	public boolean isPK() {
		return PK;
	}

	public void setPK(boolean pK) {
		PK = pK;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}

	public boolean isFK() {
		return FK;
	}

	public void setFK(boolean fK) {
		FK = fK;
	}

	public ForeginKey getForeginKey() {
		return foreginKey;
	}

	public void setForeginKey(ForeginKey foreginKey) {
		this.foreginKey = foreginKey;
	}

	public int getPrecision() {
		return precision;
	}

	public void setPrecision(int precision) {
		this.precision = precision;
	}

	public int getScale() {
		return scale;
	}

	public void setScale(int scale) {
		this.scale = scale;
	}

	protected String jdbcType2javaType(int jdbcType) {
		jdbcType = jdbcTypeWrapper(jdbcType);
		String javaType = null;

		for (int i = 0; i < mappings.length; i++) {
			if (mappings[i][0].equals(String.valueOf(jdbcType))) {
				javaType = mappings[i][2];
				break;
			}
		}

		if (javaType == null)
			throw new RuntimeException("JdbcType " + jdbcType + " can't be matched!");

		return javaType;
	}

	/* POJO对象的属性类型 */
	public String getJavaType() {
		return jdbcType2javaType(getColumnType());
	}

	/* POJO对象的属性名称 */
	public String getPropertyName() {
		StringBuffer property = new StringBuffer();

		for (String str : StringUtil.splitToList(getColumnName(), "_")) {
			property.append(StringUtil.firstLetterUpper(str.toLowerCase()));
		}

		return StringUtil.firstLetterLower(property.toString());
	}

	/* POJO对象的方法名称 */
	public String getMethodName() {
		StringBuffer method = new StringBuffer();

		for (String str : StringUtil.splitToList(getColumnName(), "_")) {
			method.append(StringUtil.firstLetterUpper(str.toLowerCase()));
		}

		return method.toString();
	}

}
