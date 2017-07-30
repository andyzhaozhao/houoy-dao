package org.citic.iiot.persist.gener.metadata;


import java.sql.Connection;
import java.sql.DatabaseMetaData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Metadata {

	private static final String TABLE = "TABLE";
	private static final String VIEW = "VIEW";
	private static final String SELECT_FROM = "SELECT * FROM ";
	private static final String WHERE = " WHERE 1=2";

	private Connection connection;
	private DatabaseMetaData databaseMetaData;

	public abstract org.citic.iiot.persist.gener.metadata.Column getColumn();

	public abstract String getDatabase();

	public Metadata(Connection connection) {
		try {
			this.connection = connection;
			this.databaseMetaData = connection.getMetaData();
		} catch (SQLException e) {
			throw new RuntimeException("Initialize Metadata the getMetaData error.", e);
		}
	}

	public Connection getConnection() {
		return connection;
	}

	public DatabaseMetaData getDatabaseMetaData() {
		return databaseMetaData;
	}

	public List<String> getTables() throws SQLException {
		List<String> tables = new ArrayList<String>();

		ResultSet resultSet = getDatabaseMetaData().getTables(null, null, null,
				new String[] { TABLE, VIEW });
		while (resultSet.next()) {
			tables.add(resultSet.getString(3));
		}

		this.closeResultSet(resultSet);

		return tables;
	}

	public List<String> getTables(String startsWith) throws SQLException {
		List<String> tables = new ArrayList<String>();

		for (String table : this.getTables()) {
			if (table.startsWith(startsWith)) {
				tables.add(table);
			}
		}

		return tables;
	}

	public List<org.citic.iiot.persist.gener.metadata.Column> getColumns(String table) throws SQLException {
		PreparedStatement pstmt = null;
		ResultSet resultSet = null;

		StringBuffer sql = new StringBuffer(SELECT_FROM);
		sql.append(table);
		sql.append(WHERE);

		pstmt = getConnection().prepareStatement(sql.toString());
		resultSet = pstmt.executeQuery();

		return getColumns(resultSet.getMetaData(), table);
	}

	public List<org.citic.iiot.persist.gener.metadata.Column> getColumns(ResultSetMetaData rsmd, String table) throws SQLException {
		List<org.citic.iiot.persist.gener.metadata.Column> columnList = new ArrayList<org.citic.iiot.persist.gener.metadata.Column>();
		List<ForeginKey> ForeginKeys = this.getForeginKeys(table);

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			org.citic.iiot.persist.gener.metadata.Column column = getColumn();
			column.setColumnName(rsmd.getColumnName(i));
			column.setColumnTypeName(rsmd.getColumnTypeName(i));
			column.setColumnType(rsmd.getColumnType(i));

			column.setAutoIncrement(rsmd.isAutoIncrement(i));
			column.setPK(this.isPrimaryKey(table, rsmd.getColumnName(i)));

			column.setPrecision(rsmd.getPrecision(i));
			column.setScale(rsmd.getScale(i));

			for (ForeginKey foreginKey : ForeginKeys) {
				if (foreginKey.getFKColumn().equals(column.getColumnName())) {
					column.setFK(true);
					column.setForeginKey(foreginKey);
					break;
				}
			}

			columnList.add(column);
		}

		return columnList;
	}

	public List<String> getPrimaryKeys(String table) throws SQLException {
		List<String> primaryKeys = new ArrayList<String>();

		ResultSet resultSet = getDatabaseMetaData().getPrimaryKeys(null, null, table);
		while (resultSet.next()) {
			primaryKeys.add(resultSet.getString(4));
		}

		this.closeResultSet(resultSet);

		return primaryKeys;
	}

	public boolean isPrimaryKey(String table, String column) throws SQLException {
		boolean isPk = false;

		List<String> primaryKeys = this.getPrimaryKeys(table);
		for (String primaryKey : primaryKeys) {
			if (primaryKey.toUpperCase().equals(column.toUpperCase())) {
				isPk = true;
				break;
			}
		}

		return isPk;
	}

	public List<ForeginKey> getForeginKeys(String table) throws SQLException {
		List<ForeginKey> foreginKeys = new ArrayList<ForeginKey>();

		ResultSet resultSet = getDatabaseMetaData().getImportedKeys(null, null, table);
		while (resultSet.next()) {
			ForeginKey foreginKey = new ForeginKey();
			foreginKey.setFKTable(resultSet.getString(3));
			foreginKey.setPKColumn(resultSet.getString(4));
			foreginKey.setPKTable(resultSet.getString(7));
			foreginKey.setFKColumn(resultSet.getString(8));

			foreginKeys.add(foreginKey);
		}

		return foreginKeys;
	}

	private void closeResultSet(ResultSet resultSet) {
		try {
			if (resultSet != null)
				resultSet.close();
		} catch (SQLException e) {
			throw new RuntimeException("CloseResultSet error.", e);
		}
	}

	public void closeConnection() {
		try {
			if (getConnection() != null)
				getConnection().close();
		} catch (SQLException e) {
			throw new RuntimeException("CloseConnection error.", e);
		}
	}

}
