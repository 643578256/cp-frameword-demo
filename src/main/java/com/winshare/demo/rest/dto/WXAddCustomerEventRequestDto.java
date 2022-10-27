package com.winshare.demo.rest.dto;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@JacksonXmlRootElement(localName ="xml")
@Data
@ToString
public class WXAddCustomerEventRequestDto implements Serializable {

    @JacksonXmlProperty(localName = "ToUserName")
    private String toUserName;

    @JacksonXmlProperty(localName = "FromUserName")
    private String fromUserName;

    @JacksonXmlProperty(localName = "CreateTime")
    private Long createTime;

    @JacksonXmlProperty(localName = "MsgType")
    private String msgType;

    @JacksonXmlProperty(localName = "Event")
    private String event;

    @JacksonXmlProperty(localName = "ChangeType")
    private String changeType;

    @JacksonXmlProperty(localName = "UserID")
    private String userID;

    @JacksonXmlProperty(localName = "ExternalUserID")
    private String externalUserID;

    /**
     * 与WXCreateCustomerRequestDto state 对应
     */
    @JacksonXmlProperty(localName = "State")
    private String state;


    @JacksonXmlProperty(localName = "WelcomeCode")
    private String welcomeCode;


}
