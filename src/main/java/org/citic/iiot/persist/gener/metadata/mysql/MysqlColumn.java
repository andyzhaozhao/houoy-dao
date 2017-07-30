package org.citic.iiot.persist.gener.metadata.mysql;


import org.citic.iiot.persist.gener.metadata.Column;

public class MysqlColumn extends Column {

	@Override
	protected int jdbcTypeWrapper(int jdbcType) {
		return jdbcType;
	}

}
