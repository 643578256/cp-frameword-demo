package com.winshare.demo.rest;

import com.winshare.demo.es.CommFieldListVo;
import com.winshare.demo.es.SearchResultVo;
import com.winshare.demo.es.api.ISearchHelpApi;
import com.winshare.demo.rest.dto.WXAddCustomerEventRequestDto;
import com.winshare.demo.rest.dto.WXEventRequestDto;
import com.winshare.demo.rest.wx.AesException;
import com.winshare.demo.rest.wx.WXBizMsgCrypt;
import com.winshare.demo.rest.wx.WXConvertXmlUtil;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

@RestController
public class WXAddCustomerEventController {

    @Autowired
    private  ATest test;

    @Autowired
    private  ISearchHelpApi searchHelpApi;

    @RequestMapping(value = "/v1/testaop")
    public void testaop(){
        Object o = test.testAop();
        int a = 0;
    }

    @RequestMapping(value = "/v1/es")
    public void searchHelpApi(){
        CommFieldListVo vo = new CommFieldListVo();
        vo.setIndexName("imarketing_uat_order");
        vo.setType("order");
        vo.addAndEqualtField("dr", "0");
        vo.setSort( "createTime", SortOrder.DESC);
        SearchResultVo search = searchHelpApi.search(vo);
        int  a = 0;
    }

    @RequestMapping(value = "/v1/member/customer/event",method = RequestMethod.POST)
    public void invockerEvent(@RequestParam("msg_signature") String msg_signature,
                              @RequestParam("timestamp") String timestamp,
                              @RequestParam("nonce") String nonce,
                              HttpServletRequest request, HttpServletResponse response) throws AesException {
        System.out.println("\n\n\n");
        WXBizMsgCrypt wxBizMsgCrypt = getWXBizMsgCrypt();
        try {
            String postData = readContent(request);
            String sMsg = wxBizMsgCrypt.DecryptMsg(msg_signature, timestamp, nonce,postData );
            System.out.println("after decrypt msg: " + sMsg);
            // TODO: 解析出明文xml标签的内容进行处理
            // For example:
            WXAddCustomerEventRequestDto requestDto = WXConvertXmlUtil.convertPojo(sMsg);
            System.out.println("\n\n\n");
            System.out.println("after decrypt requestDto: " + requestDto);
        } catch (Exception e) {
            // TODO
            // 解密失败，失败原因请查看异常
            e.printStackTrace();
        }
        System.out.println("\n\n\n");
    }

    /**
     * msg_signature	String	企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体。签名计算方法参考 消息体签名检验
     * timestamp	Integer	时间戳。与nonce结合使用，用于防止请求重放攻击。
     * nonce	String	随机数。与timestamp结合使用，用于防止请求重放攻击。
     * echostr	String	加密的字符串。需要解密得到消息内容明文，解密后有random、msg_len、msg、receiveid四个字段，其中msg即为消息内容明文
     */
    @RequestMapping(value = "/v1/member/customer/event",method = RequestMethod.GET)
    public void invockerEvents(@RequestParam("msg_signature") String msg_signature,
                               @RequestParam("timestamp") String timestamp,
                               @RequestParam("nonce") String nonce,
                               @RequestParam("echostr") String echostr, HttpServletResponse response) throws AesException {
        System.out.println("\n\n\n");
        System.out.println("msg_signature="+msg_signature);
        System.out.println("timestamp="+timestamp);
        System.out.println("nonce="+nonce);
        System.out.println("echostr="+echostr);
        System.out.println("\n\n\n");
        WXBizMsgCrypt wxcpt = getWXBizMsgCrypt();
        // 解析出url上的参数值如下：
        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(msg_signature, timestamp,
                    nonce, echostr);
            System.out.println("verifyurl echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            // HttpUtils.SetResponse(sEchoStr);
            PrintWriter writer = response.getWriter();
            writer.write(sEchoStr);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
    }

    private WXBizMsgCrypt getWXBizMsgCrypt() throws AesException {
        String sToken = "zyyytoken";
        String sCorpID = "ww5a24a0ad95f24fd2";
        String sEncodingAESKey = "fG5wAGAAVl2Mx1YMSBSpV5dhqTo1dhearXucWRPsPyT";
        return new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
    }


    private String readContent(HttpServletRequest request)  {
        int len = request.getContentLength();
        byte[] buffer = new byte[len];
        ServletInputStream in = null;

        try
        {
            in = request.getInputStream();
            in.read(buffer, 0, len);
            in.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if (null != in)
            {
                try
                {
                    in.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return new String(buffer);
    }

}
