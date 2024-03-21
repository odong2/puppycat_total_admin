package com.architecture.admin.libraries.db;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/*****************************************************
 * MAIN DB 접속 설정
 ****************************************************/
@Configuration
@MapperScan(basePackages = {"com.architecture.admin.models.commondao"}, sqlSessionFactoryRef = "dbcommonmainSqlSessionFactory")
@EnableTransactionManagement
public class ConfigurationCommonDBMainLibrary {
    // application.properties 설정 prefix 값 확인
    @Bean(name = "dbcommonmainDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.common.main")
    public DataSource dbcommonmainDataSource() {
        return DataSourceBuilder.create().build();

    }

    @Bean(name = "dbcommonmainSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dbcommonmainDataSource") DataSource dbcommonmainDataSource, ApplicationContext applicationContext) throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dbcommonmainDataSource);
        sessionFactory.setMapperLocations(applicationContext.getResources("classpath:/mybatis/commonmapper/*/*.xml"));

        sessionFactory.setConfigLocation(applicationContext.getResource("classpath:/mybatis/mybatis-config.xml"));
        return sessionFactory.getObject();
    }

    @Bean(name = "dbcommonmainSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory dbmainsqlSessionFactory) {
        return new SqlSessionTemplate(dbmainsqlSessionFactory);
    }

    @Bean(name = "dbcommonmaintransactionManager")
    public PlatformTransactionManager transactionManager(@Qualifier("dbcommonmainDataSource") DataSource dbcommonmainDataSource) {
        return new DataSourceTransactionManager(dbcommonmainDataSource);
    }
}