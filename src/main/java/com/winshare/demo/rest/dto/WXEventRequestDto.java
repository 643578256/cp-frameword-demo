package com.winshare.demo.rest.dto;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@JacksonXmlRootElement(localName ="xml")
@Data
@ToString
public class WXEventRequestDto implements Serializable {

    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;

    @JacksonXmlProperty(localName = "AgentID")
    private String agentID;

    @JacksonXmlProperty(localName = "Encrypt")
    private String encrypt;


}
