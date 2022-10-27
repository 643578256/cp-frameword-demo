package com.winshare.demo.cpBatis.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cp.datasource")
@Data
public class DataSourcePropertes {

    private String name;
    private String pwd;
    private String url;
    private String driver;
    private String validationQuery = "SELECT 1";
    private String typeAliasesPackage;
    private int initialSize = 1;
    private int maxActive = 5;
    private int minIdle = 0;
    private int maxWait = 60000;
}
