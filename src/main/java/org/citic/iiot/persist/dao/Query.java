package org.citic.iiot.persist.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum Query {

	FIND("findDao"), COUNT("countDao"), LIMIT("limitDao"), PAGE("pageDao"), SAVE("saveDao"), DELETE(
			"deleteDao"), UPDATE("updateDao");

	private static final Logger logger = LoggerFactory.getLogger(Query.class);

	private final String name;

	private Query(String name) {
		this.name = name;
	}

	private static String nameFilter(String clazz) {
		StringBuffer name = new StringBuffer();
		char[] chars = clazz.toCharArray();

		for (char ch : chars) {
			if (Character.isLetter(ch) || Character.isDigit(ch)) {
				name.append(ch);
			} else {
				break;
			}
		}

		return name.toString();
	}

	private static String mapperName(String clazz) {
		StringBuffer name = new StringBuffer(clazz);
		name.append("Mapper");

		return name.toString();
	}

	public String getQuery() {
		return this.name;
	}

	public String getQuery(String clazz) {
		String clazzName = nameFilter(clazz);
		String clazzMapper = mapperName(clazzName);

		StringBuffer mapperQueryName = new StringBuffer(clazzMapper);
		mapperQueryName.append(".");
		mapperQueryName.append(getQuery());

		logger.debug("#MapperQueryName: {}", mapperQueryName.toString());

		return mapperQueryName.toString();
	}

	public static String getQuery(String clazz, String query) {
		String clazzName = nameFilter(clazz);
		String clazzMapper = mapperName(clazzName);

		StringBuffer mapperQueryName = new StringBuffer(clazzMapper);
		mapperQueryName.append(".");
		mapperQueryName.append(query);

		logger.debug("#MapperQueryName: {}", mapperQueryName.toString());

		return mapperQueryName.toString();
	}

}
