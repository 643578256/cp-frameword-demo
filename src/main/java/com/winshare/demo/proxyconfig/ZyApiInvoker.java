package com.winshare.demo.proxyconfig;

import com.alibaba.fastjson.JSON;
import com.winshare.demo.rest.Person;
import okhttp3.*;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
public class ZyApiInvoker {

    private static final Logger logger = LoggerFactory.getLogger(ZyApiInvoker.class);

    private static final String CONTENT_TYPE = "application/json";

    private static final MediaType APPLICATION_JSON = MediaType.parse(CONTENT_TYPE);

    private OkHttpClient okHttpClient;
    private InvokerConfiguration invokerConfiguration;


    public void setIntercepters(List<RemoteInvokerIntercepter> intercepters) {
        Collections.sort(intercepters, (o1, o2) -> o1.getSort() - o2.getSort());
        this.intercepters = intercepters;
    }

    private List<RemoteInvokerIntercepter> intercepters;

    public ZyApiInvoker() {

    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public void setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    public InvokerConfiguration getInvokerConfiguration() {
        return invokerConfiguration;
    }

    public void setInvokerConfiguration(InvokerConfiguration invokerConfiguration) {
        this.invokerConfiguration = invokerConfiguration;
    }

    public String httpRequest(HttpRequestType httpRequestType, String url, Object requestParam) {
        String responseStr = null;
        try {
            HttpRequestDomain requestDomain = new HttpRequestDomain(url,requestParam);
            if (CollectionUtils.isNotEmpty(intercepters)) {
                Collections.sort(intercepters,(o1,o2) -> o1.getSort() - o2.getSort());
                for (RemoteInvokerIntercepter intercepter : intercepters) {
                    intercepter.intercepter(requestDomain);
                }
            }
            switch (httpRequestType) {
                case GET:
                    responseStr = httpGet(url, requestDomain);
                    break;
                case POST:
                    responseStr = httpPost(url, requestParam, requestDomain);
                    break;
                case PUT:
                    responseStr = httpPut(url, requestParam, requestDomain);
                    break;
                case DELETE:
                    responseStr = httpDelete(url, requestParam, requestDomain);
                    break;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return responseStr;
    }

    /**
     * get请求
     *
     * @param url 路径
     * @return
     * @throws IOException
     */
    public String httpGet(String url, HttpRequestDomain requestDomain) throws IOException {
        checkUrl(url);
        Request.Builder builder = new Request.Builder();
        builHeard(requestDomain,builder);
        Request request = builder.addHeader("Accept", CONTENT_TYPE).url(url).get().build();
        return invoke(request);
    }


    /**
     * post请求
     *
     * @param url   路径
     * @param param 参数
     * @return
     * @throws IOException
     */
    public String httpPost(String url, Object param, HttpRequestDomain requestDomain) throws IOException {
        checkUrl(url);
        Request.Builder builder = new Request.Builder();
        builHeard(requestDomain,builder);
        Request request = builder.post(requestBody(param)).addHeader("Accept", CONTENT_TYPE).url(url).build();
        return invoke(request);
    }

    public String invoke(Request request) throws IOException {
        Response response = okHttpClient.newCall(request).execute();
        return unWrapResponse(response);
    }

    /**
     * put请求
     *
     * @param url   路径
     * @param param 参数
     * @return
     * @throws IOException
     */
    public String httpPut(String url, Object param, HttpRequestDomain requestDomain) throws IOException {
        checkUrl(url);
        Request.Builder builder = new Request.Builder();
        builHeard(requestDomain,builder);
        Request request = builder.put(requestBody(param)).addHeader("Accept", CONTENT_TYPE).url(url).build();
        return invoke(request);
    }

    /**
     * 生成requestBody
     *
     * @param param
     * @return
     */
    private RequestBody requestBody(Object param) {
        String json = JSON.toJSONString(param);
        return RequestBody.create(APPLICATION_JSON, json);
    }


    /**
     * delete请求
     *
     * @param url
     * @param param
     * @return
     * @throws IOException
     */
    public String httpDelete(String url, Object param, HttpRequestDomain requestDomain) throws IOException {
        checkUrl(url);
        Request request;
        Request.Builder builder = new Request.Builder();
        builHeard(requestDomain, builder);
        if (Objects.nonNull(param)) {
            request = builder.delete(requestBody(param)).url(url).build();
        } else {
            request = builder.delete().url(url).build();
        }
        return invoke(request);
    }

    private void builHeard(HttpRequestDomain requestDomain, Request.Builder builder) {
        Map<String, String> heards = requestDomain.heards();
        if (heards != null && heards.size() > 0) {
            heards.keySet().forEach(name -> {
                builder.header(name, heards.get(name));
            });
        }
    }

    private void checkUrl(String url) {
        Assert.isTrue(StringUtils.hasLength(url), "url请求参数不能为空");
    }


    private String unWrapResponse(Response response) throws IOException {
        logger.info("响应的内容是:{}", response);
        if (!response.isSuccessful()) {
            throw new RemoteInvocationException("远程调用发生错误，响应信息是" + response);
        }
        String resp = null;
        if (Objects.nonNull(response.body())) {
            resp = response.body().string();
        }
        return resp;
    }

    public static void main(String[] args) throws IOException, InterruptedException, NoSuchMethodException {
        /*String st="http://localhost:8080/user/login?username=su ming&password=123456";
        OkHttpClient httpClient = new OkHttpClient().newBuilder().callTimeout(10L, TimeUnit.SECONDS).connectionPool(new ConnectionPool()).build();
        Request request = new Request.Builder().url("http://localhost:8080/user/1/").get().build();
        Response response = httpClient.newCall(request).execute();
        System.out.println(response);
        System.out.println(response.body());
        System.out.println(response.body().string());
        ZyApiInvoker zyApiInvoker = new ZyApiInvoker(httpClient);*/
       /* Method[] methods = IUserService.class.getDeclaredMethods();
        for (Method method : methods) {
            //Class<?> returnType = method.getReturnType();

            Type type = method.getGenericReturnType();
            if (type instanceof ParameterizedType) {
                Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
                System.out.println((Class<?>) actualTypeArguments[0]);
                // String typeName = actualTypeArguments[0].getTypeName();
                //System.out.println(typeName);
            }
        }*/
        //Method method = IUserService.class.getMethod("selectUserById", Long.class);
        OkHttpClient httpClient = new OkHttpClient().newBuilder().callTimeout(100L, TimeUnit.SECONDS).connectionPool(new ConnectionPool()).build();

        Person userVo = new Person("aaa",12);
        String json = JSON.toJSONString(userVo);
        Request request = new Request.Builder().url("http://localhost:8080/user/insert/").post(RequestBody.create(APPLICATION_JSON, json)).build();
        Response response = httpClient.newCall(request).execute();
        System.out.println(response);
    }

}
