package com.winshare.demo.proxyconfig;

import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 掉中心
 */
public class HttpRestTest {
    private static Logger logger = LoggerFactory.getLogger(HttpRestTest.class);
    private static void writeLog(Object obj) {
        System.out.println(String.valueOf(obj));
    }

    // 配置信息
    static String applicationKey = "e1fce770a0abb2a831d7058d8251e211";
    static String secretKey = "9dcbb3305c991fe6581c43058efae380";
    static String
            GATEWAY_URL ="http://zyapi-dev.k8s.dtyunxi.cn/api-out/member";

    public static void main(String[] args) throws IOException {
        logger.info("aaaa");
        //String api = "/api-out/member/v1/points-trade/inc";
        String api = "/v1/member/info/page";
        // 测试数据 封装请求信息
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNum", 1);
        jsonObject.put("pageSize", 10);
        jsonObject.put("phone", "156");
/*        jsonObject.put("memberId", 1273173699994121678L);
        jsonObject.put("pointsAccountCode", 1273173699994121678L);
        jsonObject.put("points", 5);
        jsonObject.put("operationCode", "Z_LP_MOA");
        jsonObject.put("remark", "enen");*/

        writeLog("<<<<<<<<<<<<<<<<<<<<<请求信息:" + jsonObject.toJSONString());

        // 生成签名
        String nonce = UUID.randomUUID().toString();
        String timestamp = "" + System.currentTimeMillis();
        String signSrc =
                "Application-Key=" + applicationKey
                        + "&X-Yx-Nonce=" + nonce
                        + "&X-Yx-Timestamp=" + timestamp;
        // 签名,签名算法会对字串进行排序
        HmacUtils hm1 = new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secretKey);
        String sign = hm1.hmacHex(signSrc);

        // 封装公共参数
        Map<String, String> headers1 = new HashMap<>();
        headers1.put("Application-Key", applicationKey);
        headers1.put("X-Yx-Nonce", nonce);
        headers1.put("X-Yx-Timestamp", timestamp);
        headers1.put("X-Yx-Signature", sign);
        headers1.put("Content-Type", "application/json");

        headers1.forEach((k, v) -> System.out.println(k + "=" + v));


        Request request = new Request.Builder().post( RequestBody.create(MediaType.parse( "application/json"), jsonObject.toJSONString())).headers(Headers.of(headers1)).url(GATEWAY_URL + api).build();

        OkHttpClient build = new OkHttpClient().newBuilder().callTimeout(3000, TimeUnit.SECONDS).
                build();

        ZyApiInvoker invoker = new ZyApiInvoker();
        Response execute = build.newCall(request).execute();
        String resp = null;
        if (Objects.nonNull(execute.body())) {
            resp = execute.body().string();
        }
        //String responseContent1 = HttpUtil.get(GATEWAY_URL + api, headers1);

        writeLog("<<<<<<<<<<<<<<<<<<<<<新中台返回结果:" + resp);

        //client.execute();
    }
}

