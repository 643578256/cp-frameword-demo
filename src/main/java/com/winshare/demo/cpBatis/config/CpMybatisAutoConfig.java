package com.winshare.demo.cpBatis.config;


import com.alibaba.druid.pool.DruidDataSource;
import lombok.SneakyThrows;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;


/*@Configuration
@EnableConfigurationProperties(value = DataSourcePropertes.class)
@EnableTransactionManagement*/
public class CpMybatisAutoConfig {


    @Autowired
    private DataSourcePropertes propertes;

    @Bean
    public PlatformTransactionManager transactionManager(@Autowired DruidDataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @SneakyThrows
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Autowired DruidDataSource dataSource) {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setConfiguration(mybatisConfiguration());
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setTypeAliasesPackage(propertes.getTypeAliasesPackage());
        // sqlSessionFactory.afterPropertiesSet();
        return sqlSessionFactory.getObject();
    }

    @Bean(destroyMethod = "close")
    public DruidDataSource dataSource() throws SQLException {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(propertes.getDriver());
        dataSource.setUrl(propertes.getUrl());
        dataSource.setUsername(propertes.getName());
        dataSource.setPassword("cp@0826");
        dataSource.setValidationQuery(propertes.getValidationQuery());
        dataSource.setInitialSize(propertes.getInitialSize());
        dataSource.setMaxActive(propertes.getMaxActive());
        dataSource.setMaxWait(propertes.getMaxWait());
        dataSource.setTestOnBorrow(false);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        dataSource.setTimeBetweenEvictionRunsMillis(60000L);
        dataSource.setMinEvictableIdleTimeMillis(25200000L);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(1800);
        dataSource.setLogAbandoned(true);
        dataSource.setFilters("mergeStat");
        dataSource.setConnectionInitSqls(Arrays.asList("set names utf8mb4;"));
        if (true) {
            Properties p = new Properties();
            p.setProperty("druid.stat.mergeSql", true + "");
            dataSource.setConnectProperties(p);
        }
        dataSource.init();
        return dataSource;
    }

    public org.apache.ibatis.session.Configuration mybatisConfiguration() {
        org.apache.ibatis.session.Configuration configuration = new org.apache.ibatis.session.Configuration();
        //configuration.addMappedStatement();
        return configuration;
    }
}
