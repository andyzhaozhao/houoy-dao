package org.citic.iiot.persist.gener;

import org.citic.iiot.persist.gener.metadata.Column;
import org.citic.iiot.persist.gener.metadata.Metadata;
import org.citic.iiot.persist.gener.metadata.MetadataFactory;
import org.citic.iiot.persist.gener.template.Domain;
import org.citic.iiot.persist.gener.template.Mapper;
import org.citic.iiot.core.util.FileUtil;
import org.citic.iiot.core.util.StringUtil;

import java.io.File;
import java.sql.SQLException;
import java.util.List;


public class Generator {

	private Metadata metadata;

	private GeneratorTemplate generatorTemplate;

	public Generator() {
		this.metadata = MetadataFactory.newInstance();
		this.generatorTemplate = new GeneratorTemplate();
	}

	public void generate() {
		try {
			List<String> tables = getTables();
			for (String table : tables) {
				List<Column> columnList = getMetadata().getColumns(table);
				GeneratorContext.setParameter("columnList", columnList);

				generateDomain(table);
				generateMapper(table);
				generateDao(table);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			getMetadata().closeConnection();
		}
	}

	private void generateDomain(String table) {
		Domain domain = new Domain(table);
		GeneratorContext.setParameter("domain", domain);

		getGeneratorTemplate().generateDomain(domain.className(), domainTemplate(),
				domainFile(domain));
	}

	private void generateMapper(String table) {
		Mapper mapper = new Mapper(table);
		GeneratorContext.setParameter("mapper", mapper);
		GeneratorContext.setParameter("mapperName", mapper.mapperName());
		GeneratorContext.setParameter("tableDomainName", mapper.tableDomainName());

		getGeneratorTemplate().generateMapper(mapperTemplate(), mapperFile(mapper));
	}

	private void generateDao(String table) {
		Domain domain = new Domain(table);
		GeneratorContext.setParameter("domain", domain);

		getGeneratorTemplate().generateDao(daoTemplate(), daoFile(domain));
	}

	private File mapperFile(Mapper mapper) {
		String mapperPath = GeneratorContext.getMapperPath();
		return FileUtil.createFile(mapperPath + File.separator + mapper.mapperName() + ".xml");
	}

	private String mapperTemplate() {
		StringBuffer mapperTemplate = new StringBuffer();
		mapperTemplate.append(GeneratorContext.getVmPath());
		mapperTemplate.append(getMetadata().getDatabase());
		mapperTemplate.append("/");
		mapperTemplate.append("mapper.vm");

		return mapperTemplate.toString();
	}

	private String domainTemplate() {
		StringBuffer domainTemplate = new StringBuffer();
		domainTemplate.append(GeneratorContext.getVmPath());
		domainTemplate.append(getMetadata().getDatabase());
		domainTemplate.append("/");
		domainTemplate.append("domain.vm");

		return domainTemplate.toString();
	}

	private String daoTemplate() {
		StringBuffer daoTemplate = new StringBuffer();
		daoTemplate.append(GeneratorContext.getVmPath());
		daoTemplate.append("dao");
		daoTemplate.append("/");
		daoTemplate.append("dao.vm");

		return daoTemplate.toString();
	}

	private File domainFile(Domain domain) {
		String domainPath = GeneratorContext.getDomainPath();
		return FileUtil.createFile(domainPath + File.separator + domain.className() + ".java");
	}

	private File daoFile(Domain domain) {
		String daoPath = GeneratorContext.getDaoPath();
		return FileUtil.createFile(daoPath + File.separator + domain.className() + "Dao.java");
	}

	private List<String> getTables() throws SQLException {
		List<String> tables = null;

		if (!StringUtil.isNull(GeneratorContext.getTableList())) {
			tables = StringUtil.splitToList(GeneratorContext.getTableList(), ",");
		} else if (!StringUtil.isNull(GeneratorContext.getTablePrefix())) {
			tables = getMetadata().getTables(GeneratorContext.getTablePrefix());
		} else {
			tables = getMetadata().getTables();
		}

		return tables;
	}

	private Metadata getMetadata() {
		return metadata;
	}

	private GeneratorTemplate getGeneratorTemplate() {
		return generatorTemplate;
	}

	public static void main(String[] args) {
		// 解析参数
		Parameter parameter = new Parameter(args);
		// 将参数存入上下文对象，供其他类引用
		GeneratorContext.setParameter(parameter);
		// 开始生成
		Generator generator = new Generator();
		generator.generate();
	}

}
