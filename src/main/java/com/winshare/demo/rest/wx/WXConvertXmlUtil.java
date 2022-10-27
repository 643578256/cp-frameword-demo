package com.winshare.demo.rest.wx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.winshare.demo.rest.dto.WXAddCustomerEventRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class WXConvertXmlUtil {
    private static Logger log = LoggerFactory.getLogger(WXConvertXmlUtil.class);
    private final static String ADD = "add_external_contact"; //"添加企业客户事件"),



    public static WXAddCustomerEventRequestDto convertPojo(String xmlStr) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist1 = root.getElementsByTagName("ChangeType");
            String content = nodelist1.item(0).getTextContent();
           switch (content){
               case ADD:
                   XmlMapper xmlMapper = new XmlMapper();
                   return xmlMapper.readValue(xmlStr, WXAddCustomerEventRequestDto.class);
               default:
                   log.info("210126-1011#convertPojo不支持的企业微信回调操作={}",content);
           }
        } catch (Exception e) {
            log.error("210126-1015#convertPojo解析xml={}异常",xmlStr,e);
        }
        return null;
    }
}
