package com.winshare.demo.proxyconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;



@Getter
@Setter
@ToString
@Accessors(chain = true)
public class UrlSegment {
    private String segmentName;
    private boolean needReplace;
}
