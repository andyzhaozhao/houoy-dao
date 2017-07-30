package org.citic.iiot.persist.gener;

import org.citic.iiot.core.util.StringUtil;

import java.util.HashMap;
import java.util.Map;


public class Parameter {

	public static final String MetaClass = "metaClass";
	public static final String JDBCUrl = "jdbcUrl";
	public static final String JDBCUser = "jdbcUser";
	public static final String JDBCPassword = "jdbcPassword";
	public static final String JDBCDriver = "jdbcDriver";
	public static final String DomainPackage = "domainPackage";
	public static final String DomainPath = "domainPath";
	public static final String MapperPath = "mapperPath";
	public static final String DaoPackage = "daoPackage";
	public static final String DaoPath = "daoPath";
	public static final String TablePrefix = "tablePrefix";
	public static final String TableList = "tableList";
	public static final String BuildPath = "buildPath";
	public static final String SqlSessionTemplateName = "sqlSessionTemplateName";

	private Map<String, String> parameterMap = new HashMap<String, String>();

	public Parameter(String[] params) {
		parseParameter(params);
		validParameter();
	}

	protected void parseParameter(String[] params) {
		for (String param : params) {
			getParameter().putAll(StringUtil.splitToMap(param, "="));
		}
	}

	public Map<String, String> getParameter() {
		return this.parameterMap;
	}

	protected boolean isContains(String key) {
		return getParameter().containsKey(key);
	}

	protected void validParameter() {
		if (isContains(TableList) && isContains(TablePrefix)) {
			throw new RuntimeException("Can't both contains 'tableList' and 'tablePrefix'.");
		}
	}

}
