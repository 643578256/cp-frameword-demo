package com.winshare.demo.es.api;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winshare.demo.es.ESBaseVO;
import com.winshare.demo.es.EsIndexDocument;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


public class ESIndexHelpService extends AbstractBaseEs implements IESIndexHelpApi {

    private static final Logger log = LoggerFactory.getLogger(ESIndexHelpService.class);
    private static final ConcurrentHashMap<Class, EsIndexDocument> indexMap = new ConcurrentHashMap<>(16);
    private static final ConcurrentHashMap<Class, List<Field>> classMap = new ConcurrentHashMap<>(16);

    public ESIndexHelpService(RestHighLevelClient client) {
        super(client);
    }

    @Override
    public Map<String, Object> indexView(String indexName, String typeName) {
        return findMapping(indexName);
    }

    @Override
    public void createIndex() {

    }

    @Override
    public Boolean addDoc(ESBaseVO baseES) {
        IndexRequest request = new IndexRequest();
        request.index(getIndex(baseES)).id(baseES.getId());
        try {
            ObjectMapper mappers = new ObjectMapper();
            String json = mappers.writeValueAsString(baseES);
            request.source(json, XContentType.JSON);
            request.timeout(new TimeValue(1, TimeUnit.SECONDS));
            IndexResponse index = client.index(request, RequestOptions.DEFAULT);
            if (index.status() == RestStatus.CREATED) {
                return true;
            }
            log.error("写入Es错误,错误信息={}，写入信息={}", index.getResult(), json);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean batchAddDocs(List<ESBaseVO> listEs) {
        if (CollectionUtils.isEmpty(listEs)) {
            return true;
        }
        if (listEs.size() > 20) {
            throw new RuntimeException("批量写入索引数量过大，不能超过 20");
        }
        try {
            BulkRequest bulkRequest = new BulkRequest();
            ObjectMapper mappers = new ObjectMapper();
            for (ESBaseVO e : listEs) {
                String json = mappers.writeValueAsString(e);
                bulkRequest.add(new IndexRequest().index(getIndex(e)).id(e.getId()).source(json, XContentType.JSON));
            }
            bulkRequest.timeout(new TimeValue(1, TimeUnit.SECONDS));
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.status() == RestStatus.OK) {
                return true;
            }
            log.error("批量写入Es错误,错误状态={}", bulk.status());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean updateDoc(ESBaseVO baseES) {
        UpdateRequest request = new UpdateRequest();
        request.index(getIndex(baseES)).id(baseES.getId());
        request.doc(notNullField(baseES));
        try {
            request.timeout(new TimeValue(1, TimeUnit.SECONDS));
            UpdateResponse update = client.update(request, RequestOptions.DEFAULT);
            if (update.status() == RestStatus.OK) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteDoc(ESBaseVO baseES) {
        DeleteRequest request = new DeleteRequest();
        request.index(getIndex(baseES)).id(baseES.getId());
        try {
            request.timeout(new TimeValue(1, TimeUnit.SECONDS));
            DeleteResponse delete = client.delete(request, RequestOptions.DEFAULT);
            if (delete.status() == RestStatus.OK) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Boolean deleteDocByIds(ESBaseVO baseES) {
        if (CollectionUtils.isEmpty(baseES.getIds())) {
            return true;
        }
        if (baseES.getIds().size() > 20) {
            throw new RuntimeException("批量写入索引数量过大，不能超过 20");
        }
        BulkRequest bulkRequest = new BulkRequest();
        String index = getIndex(baseES);
        for (String id :baseES.getIds()) {
            bulkRequest.add(new DeleteRequest().index(index).id(id));
        }
        try {
            bulkRequest.timeout(new TimeValue(1, TimeUnit.SECONDS));
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            if (bulk.status() == RestStatus.OK) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getIndex(ESBaseVO baseES) {
        EsIndexDocument esIndexDocument = indexMap.get(baseES.getClass());
        if (esIndexDocument == null) {
            esIndexDocument = baseES.getClass().getAnnotation(EsIndexDocument.class);
            indexMap.put(baseES.getClass(), esIndexDocument);
        }
        return esIndexDocument.indexName();
    }

    private Map<String, Object> notNullField(ESBaseVO baseES) {
        Map<String, Object> updateMap = new HashMap<>(16);
        List<Field> fields = classMap.get(baseES.getClass());
        if (fields == null) {
            Field[] declaredFields = baseES.getClass().getDeclaredFields();
            fields = new ArrayList<>(declaredFields.length);
            for (int i = 0; i < declaredFields.length; i++) {
                declaredFields[i].setAccessible(true);
                fields.add(declaredFields[i]);
            }
            classMap.put(baseES.getClass(), fields);
        }
        try {
            for (Field f : fields) {
                Object o = f.get(baseES);
                if (o != null) {
                    updateMap.put(f.getName(), o);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return updateMap;
    }
}
