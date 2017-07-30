package org.citic.iiot.persist.dao;

import org.apache.commons.beanutils.PropertyUtils;
import org.citic.iiot.persist.domain.PageEntity;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


@SuppressWarnings("unchecked")
public class MapperDaoTemplate<T extends PageEntity>{
	@Autowired
	private SqlSessionTemplate sqlSession;

	public MapperDaoTemplate(SqlSessionTemplate sqlSession) {
		this.sqlSession = sqlSession;
	}

	public T selectOne(T t) {
		return selectOne(t, Query.FIND.getQuery());
	}

	/* 合并对象属性和参数Map */
	private Map<String, Object> mergeMaps(T t, Map<String, Object> paramsMap) {
		try {
			Map<String, Object> objectMap = PropertyUtils.describe(t);

			if (null != paramsMap) {
				objectMap.putAll(paramsMap);
			}
			return objectMap;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public T selectOne(T t, Map<String, Object> paramsMap) {
		return (T) this.getSqlSession().selectOne(
				Query.FIND.getQuery(t.getClass().getSimpleName()), mergeMaps(t, paramsMap));
	}

	public T selectOne(T t, String queryName) {
		return (T) this.getSqlSession().selectOne(
				Query.getQuery(t.getClass().getSimpleName(), queryName), t);
	}

	public T selectOne(T t, String queryName, Map<String, Object> paramsMap) {
		return (T) this.getSqlSession().selectOne(
				Query.getQuery(t.getClass().getSimpleName(), queryName), mergeMaps(t, paramsMap));
	}

	public Map<String, Object> selectMap(T t, String queryName, Map<String, Object> paramsMap) {
		return (Map<String, Object>) this.getSqlSession().selectOne(
				Query.getQuery(t.getClass().getSimpleName(), queryName), mergeMaps(t, paramsMap));
	}

	public Map<String, Object> selectMap(T t, String queryName) {
		return selectMap(t, queryName, null);
	}

	public List<T> selectList(T t) {
		return selectList(t, Query.FIND.getQuery());
	}

	public List<T> selectList(T t, String queryName) {
		return this.getSqlSession().selectList(
				Query.getQuery(t.getClass().getSimpleName(), queryName), t);
	}

	public List<T> selectList(T t, Map<String, Object> paramsMap) {
		return this.getSqlSession().selectList(Query.FIND.getQuery(t.getClass().getSimpleName()),
				mergeMaps(t, paramsMap));
	}

	public List<T> selectList(T t, String queryName, Map<String, Object> paramsMap) {
		return this.getSqlSession().selectList(
				Query.getQuery(t.getClass().getSimpleName(), queryName), mergeMaps(t, paramsMap));
	}

	public List<Map<String, Object>> selectMaps(T t, String queryName, Map<String, Object> paramsMap) {
		return this.getSqlSession().selectList(
				Query.getQuery(t.getClass().getSimpleName(), queryName), mergeMaps(t, paramsMap));
	}

	public List<Map<String, Object>> selectMaps(T t, String queryName) {
		return selectMaps(t, queryName, null);
	}

	public int selectCount(T t, String queryName, Map<String, Object> paramsMap) {
		return (Integer) this.getSqlSession().selectOne(
				Query.getQuery(t.getClass().getSimpleName(), queryName), mergeMaps(t, paramsMap));
	}

	public int selectCount(T t, String queryName) {
		return (Integer) this.getSqlSession().selectOne(
				Query.getQuery(t.getClass().getSimpleName(), queryName), t);
	}

	public int selectCount(T t, Map<String, Object> paramsMap) {
		return (Integer) this.getSqlSession().selectOne(
				Query.COUNT.getQuery(t.getClass().getSimpleName()), mergeMaps(t, paramsMap));
	}

	public int selectCount(T t) {
		return selectCount(t, Query.COUNT.getQuery());
	}

	public List<T> selectPage(T t, String pageQueryName, String countQueryName, int curPage,
			int linePerPage) {
		int count = selectCount(t, countQueryName);
		int totalPage = (count % linePerPage == 0) ? (count / linePerPage) : (Math.abs(count
				/ linePerPage) + 1);

		t.setCurPage(curPage);
		t.setLinePerPage(linePerPage);
		t.setTotalPage(totalPage);
		t.setTotalLine(count);
		t.setStartLine((curPage - 1) * linePerPage);
		t.setLimitLine(linePerPage);

		return selectList(t, pageQueryName);
	}

	public List<T> selectPage(T t, String pageQueryName, String countQueryName, int curPage,
			int linePerPage, Map<String, Object> paramsMap) {
		t.setCurPage(curPage);
		t.setLinePerPage(linePerPage);
		t.setStartLine((curPage - 1) * linePerPage);
		t.setLimitLine(linePerPage);

		Map<String, Object> objectMap = mergeMaps(t, paramsMap);

		int count = selectCount(t, countQueryName, objectMap);
		int totalPage = (count % linePerPage == 0) ? (count / linePerPage) : (Math.abs(count
				/ linePerPage) + 1);
		t.setTotalLine(count);
		t.setTotalPage(totalPage);

		return (List<T>) selectList(t, pageQueryName, objectMap);
	}

	public List<Map<String, Object>> selectMaps(T t, String pageQueryName, String countQueryName,
			int curPage, int linePerPage, Map<String, Object> paramsMap) {
		t.setCurPage(curPage);
		t.setLinePerPage(linePerPage);
		t.setStartLine((curPage - 1) * linePerPage);
		t.setLimitLine(linePerPage);

		Map<String, Object> objectMap = mergeMaps(t, paramsMap);

		int count = selectCount(t, countQueryName, objectMap);
		int totalPage = (count % linePerPage == 0) ? (count / linePerPage) : (Math.abs(count
				/ linePerPage) + 1);
		t.setTotalLine(count);
		t.setTotalPage(totalPage);

		return selectMaps(t, pageQueryName, objectMap);
	}

	public List<Map<String, Object>> selectMaps(T t, String pageQueryName, String countQueryName,
			int curPage, int linePerPage) {
		return selectMaps(t, pageQueryName, countQueryName, curPage, linePerPage, null);
	}

	public List<T> selectPage(T t, int curPage, int linePerPage, Map<String, Object> paramsMap) {
		return selectPage(t, Query.PAGE.getQuery(), Query.COUNT.getQuery(), curPage, linePerPage,
				paramsMap);
	}

	public List<T> selectPage(T t, int curPage, int linePerPage) {
		return selectPage(t, Query.PAGE.getQuery(), Query.COUNT.getQuery(), curPage, linePerPage);
	}

	/**
	 * 以startLine开始，取limitLine条记录，支持排序操作
	 * 
	 * @param startLine
	 *            开始的行号，以0开始计数
	 * @param limitLine
	 *            取多少条记录
	 * @return
	 */
	public List<T> selectLimit(T t, int startLine, int limitLine) {
		return selectLimit(t, Query.LIMIT.getQuery(), startLine, limitLine);
	}

	public List<T> selectLimit(T t, Map<String, Object> paramsMap, int startLine, int limitLine) {
		return selectLimit(t, Query.LIMIT.getQuery(), paramsMap, startLine, limitLine);
	}

	public List<T> selectLimit(T t, String limitQueryName, int startLine, int limitLine) {
		t.setStartLine(startLine);
		t.setLimitLine(limitLine);
		return selectList(t, limitQueryName);
	}

	public List<T> selectLimit(T t, String limitQueryName, Map<String, Object> paramsMap,
			int startLine, int limitLine) {
		t.setStartLine(startLine);
		t.setLimitLine(limitLine);
		return selectList(t, limitQueryName, paramsMap);
	}

	public int save(T t) {
		return this.getSqlSession().insert(Query.SAVE.getQuery(t.getClass().getSimpleName()), t);
	}

	public void saveBatch(List<T> list) {
		for (T t : list) {
			this.getSqlSession().insert(Query.SAVE.getQuery(t.getClass().getSimpleName()), t);
		}
	}

	public int update(T t) {
		return this.getSqlSession().update(Query.UPDATE.getQuery(t.getClass().getSimpleName()), t);
	}

	public int update(T t, String queryName) {
		return this.getSqlSession().update(Query.getQuery(t.getClass().getSimpleName(), queryName),
				t);
	}

	public void updateBatch(List<T> list) {
		for (T t : list) {
			this.getSqlSession().update(Query.UPDATE.getQuery(t.getClass().getSimpleName()), t);
		}
	}

	public int delete(T t) {
		return this.getSqlSession().delete(Query.DELETE.getQuery(t.getClass().getSimpleName()), t);
	}

	public int delete(T t, String queryName) {
		return this.getSqlSession().delete(Query.getQuery(t.getClass().getSimpleName(), queryName),
				t);
	}

	public int delete(T t, String queryName, Map<String, Object> paramsMap) {
		return this.getSqlSession().delete(Query.getQuery(t.getClass().getSimpleName(), queryName),
				paramsMap);
	}

	public void deleteBatch(List<T> list) {
		for (T t : list) {
			this.getSqlSession().delete(Query.DELETE.getQuery(t.getClass().getSimpleName()), t);
		}
	}

	public SqlSessionTemplate getSqlSession(){
		return this.sqlSession;
	}

}
