package com.winshare.demo.es.api;

import com.winshare.demo.es.AbstactTermVo;
import com.winshare.demo.es.CommFieldVo;
import com.winshare.demo.es.ESRangeVo;
import org.elasticsearch.index.query.QueryBuilder;

public abstract class AbstactTermBuilder{

    public QueryBuilder builder(AbstactTermVo termVo){
        excute(termVo);

        return null;
    }
    public abstract QueryBuilder excute(AbstactTermVo termVo);

}
