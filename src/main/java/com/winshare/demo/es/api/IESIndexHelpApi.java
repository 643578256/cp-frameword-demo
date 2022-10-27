package com.winshare.demo.es.api;

import com.winshare.demo.es.ESBaseVO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IESIndexHelpApi {

    Map<String, Object> indexView(String indexName, String typeName);

    /**
     * es 的索引创建分为动态创建，和静态创建
     * 动态：就是根据你第一次写es数据，es会自动根据数据推断出mapping 中的类型。一般不用
     * 静态：就是通过mapping请求先创建index的mapping数据。能更精准的指定数据类型和分词器等信息，一般使用这种方式
     */
    void createIndex();

    Boolean addDoc(ESBaseVO baseES) throws IOException;

    /**
     * 批量最大 20
     * @param listEs
     * @return
     */
    Boolean batchAddDocs(List<ESBaseVO> listEs);

    Boolean updateDoc(ESBaseVO baseES);

    Boolean deleteDoc(ESBaseVO baseES);

    Boolean deleteDocByIds(ESBaseVO baseES);
}
