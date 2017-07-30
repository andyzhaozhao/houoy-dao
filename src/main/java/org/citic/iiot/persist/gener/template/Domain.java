package org.citic.iiot.persist.gener.template;

import org.citic.iiot.core.util.StringUtil;

public class Domain {

	private String table;

	public Domain(String table) {
		this.table = table;
	}

	public String getTable() {
		return this.table;
	}

	public String className() {
		StringBuffer className = new StringBuffer();

		for (String str : StringUtil.splitToList(getTable(), "_")) {
			className.append(StringUtil.firstLetterUpper(str.toLowerCase()));
		}

		return className.toString();
	}

}
