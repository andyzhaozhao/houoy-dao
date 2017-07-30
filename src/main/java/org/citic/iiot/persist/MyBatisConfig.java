package org.citic.iiot.persist;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by zhaohl on 2017/5/31.
 */
//@Configuration
//@MapperScan(basePackages="org.citic.iiot.persist.mapper")
public class MyBatisConfig {

    @Autowired
    private Environment env;


    @Bean
    public DataSource getDataSource() throws Exception{
       Properties props = new Properties();
       props.put("driverClassName", env.getProperty("spring.datasource.driver-class-name"));
       props.put("url", env.getProperty("spring.datasource.url"));
       props.put("username", env.getProperty("spring.datasource.username"));
       props.put("password", env.getProperty("spring.datasource.password"));
       return DruidDataSourceFactory.createDataSource(props);
      }

   /* @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(DataSource ds) throws Exception{
        SqlSessionFactoryBean ssfBean = new SqlSessionFactoryBean();
        ssfBean.setDataSource(ds);
        ssfBean.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));//指定基包
        ssfBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));//指定xml文件位
        return ssfBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }*/
}
