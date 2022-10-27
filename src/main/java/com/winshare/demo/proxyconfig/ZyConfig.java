package com.winshare.demo.proxyconfig;

import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*@Configuration
@RemoteServiceScanner(basePackages = {
        "com.youzan.cloud.zyyy.custom.biz.inter",
        "com.youzan.cloud.zyyy.custom.biz.member",
        "com.youzan.cloud.zyyy.custom.biz.trade"
})*/
public class ZyConfig {

    @ConfigurationProperties(prefix = "zyyy.okhttp")
    @Bean
    public ZyOKHttpProperties zyOkHttpProperties() {
        return new ZyOKHttpProperties();
    }


    /**
     * 后期配置个通过配置类导入okhttp
     * @param zyOKHttpProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(OkHttpClient.class)
    public OkHttpClient okHttpClient(ZyOKHttpProperties zyOKHttpProperties) {
        return new OkHttpClient().newBuilder().callTimeout(zyOKHttpProperties.getKeepTime(), TimeUnit.SECONDS).
                connectionPool(new ConnectionPool(zyOKHttpProperties.getMaxIde(), zyOKHttpProperties.getAliveTime(), TimeUnit.SECONDS)).
                build();
    }

    @Bean
    public ZyApiInvoker zyApiInvoker(OkHttpClient okHttpClient, ZyOKHttpProperties zyOKHttpProperties, List<RemoteInvokerIntercepter> intercepters) {
        ZyApiInvoker zyApiInvoker = new ZyApiInvoker();
        InvokerConfiguration configuration = new InvokerConfiguration();
        configuration.setDefaultRealm(zyOKHttpProperties.getDefaultRealm());
        zyApiInvoker.setInvokerConfiguration(configuration);
        zyApiInvoker.setOkHttpClient(okHttpClient);
        zyApiInvoker.setIntercepters(intercepters);
        return zyApiInvoker;
    }

}
