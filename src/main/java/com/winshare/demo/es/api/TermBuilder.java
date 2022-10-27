package com.winshare.demo.es.api;

import com.winshare.demo.es.AbstactTermVo;
import org.elasticsearch.index.query.QueryBuilder;

public interface TermBuilder {

    QueryBuilder builderQuery(AbstactTermVo termVo);
}
