package org.citic.iiot.persist.gener.metadata.mysql;

import org.citic.iiot.persist.gener.metadata.Column;
import org.citic.iiot.persist.gener.metadata.Metadata;
import org.citic.iiot.persist.gener.metadata.mysql.*;

import java.sql.Connection;
public class MysqlMetadata extends Metadata {

	public MysqlMetadata(Connection connection) {
		super(connection);
	}

	@Override
	public String getDatabase() {
		return "mysql";
	}

	@Override
	public Column getColumn() {
		return new org.citic.iiot.persist.gener.metadata.mysql.MysqlColumn();
	}

}
