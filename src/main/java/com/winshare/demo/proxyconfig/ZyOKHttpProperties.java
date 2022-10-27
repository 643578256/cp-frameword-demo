package com.winshare.demo.proxyconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ZyOKHttpProperties {
    //请求保持时间
    private Long keepTime = 60L;
    //单个地址最大空闲链接线程
    private Integer maxIde = 50;
    //保持存活的时间
    private Long aliveTime = 60L;

    private String defaultRealm = "";

}
