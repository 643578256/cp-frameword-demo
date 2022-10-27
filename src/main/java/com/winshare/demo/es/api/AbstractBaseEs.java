package com.winshare.demo.es.api;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractBaseEs{

    protected RestHighLevelClient client;

    private static Map<String,Map<String,Object>> propertiesCache = new ConcurrentHashMap<>(16);

    public AbstractBaseEs(RestHighLevelClient client){
        this.client = client;
    }

    public Map<String, Object> findMapping(String indexName){
        if(StringUtils.isEmpty(indexName)){
            return null;
        }
        Map<String, Object> objectMap = propertiesCache.get(indexName);
        if(objectMap != null){
            return objectMap;
        }
        GetIndexRequest request = new GetIndexRequest(indexName);
        try {
            GetIndexResponse getIndexResponse = client.indices().get(request, RequestOptions.DEFAULT);
            Map<String, Object> sourceAsMap = getIndexResponse.getMappings().get(indexName).getSourceAsMap();
            Map<String, Object> properties = (Map<String, Object>) sourceAsMap.get("properties");
            return propertiesCache.putIfAbsent(indexName,properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
