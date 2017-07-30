package org.citic.iiot.persist;

import org.citic.iiot.persist.gener.Generator;
import org.citic.iiot.persist.gener.GeneratorContext;
import org.citic.iiot.persist.gener.Parameter;

/**
 * Created by zhaohl on 2017/5/31.
 */
public class MyBatisGenerator {

	public static void main(String args[]){
		String[] parameters = {"buildPath=build",
				"metaClass=org.citic.iiot.persist.gener.metadata.mysql.MysqlMetadata",
				"jdbcUrl=jdbc:mysql://10.247.33.42:3306/iotapp",
				"jdbcUser=mysql",
				"jdbcPassword=mysql123",
				"jdbcDriver=com.mysql.jdbc.Driver",
				"sqlSessionTemplateName=sqlSessionTemplate",
				"domainPackage=org.citic.iiot.device.operation.domain",
				"domainPath=E:\\dev\\workSpace\\device-operation\\src\\main\\java\\org\\citic\\iiot\\device\\operation\\domain",
				"mapperPath=E:\\dev\\workSpace\\device-operation\\src\\main\\resources\\mapper",
				"daoPackage=org.citic.iiot.device.operation.dao",
				"daoPath=E:\\dev\\workSpace\\device-operation\\src\\main\\java\\org\\citic\\iiot\\device\\operation\\dao",
				"tableList=operationlog"};
		// 解析参数
		Parameter parameter = new Parameter(parameters);
		// 将参数存入上下文对象，供其他类引用
		GeneratorContext.setParameter(parameter);
		// 开始生成
		Generator generator = new Generator();
		generator.generate();
	}
}
