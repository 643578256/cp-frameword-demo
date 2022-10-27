package com.winshare.demo.es.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "es")
public class SearchProperties {

    //@ApiModelProperty (name = "type", value = "适配类型：OS OpenSearch   ES ElasticSearch")
    protected String type;
    //@ApiModelProperty (name = "host", value = "主机名或IP地址")
    protected String host;
    //@ApiModelProperty (name = "accessKey", value = "阿里Open Search访问Id或用户名")
    protected String accessKey;
    //@ApiModelProperty (name = "secret", value = "阿里Open Search访问私钥或密码")
    protected String secret;
    //@ApiModelProperty (name = "clusterName", value = "ElasticSearch 集群名称")
    protected String clusterName;
    /** @deprecated */
    @Deprecated
    //@ApiModelProperty (name = "nodeName",value = "ElasticSearch 节点名称，5.0之后无需配置")
    protected String nodeName;
    //@ApiModelProperty (name = "port", value = "ElasticSearch端口号")
    protected int port;
    protected Boolean sniff;
    //@ApiModelProperty (name = "availableProcessors", value = "可用CPU核数")
    protected Integer availableProcessors;
    //@ApiModelProperty (name = "addresses", value = "多个连接地址,host:port格式输入")
    protected String[] addresses;


}
