package com.winshare.demo.es;

import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.sort.SortOrder;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class CommFieldListVo {
    private List<AbstactTermVo> fieldVoList = new ArrayList<>();


    protected boolean fetchSource = true;
    private String indexName;
    private String type;// es 6.0 后type被废弃
    private String routing;
    private TimeValue scroll;
    private boolean useScorll; //
    private String scorllId; //游标
    private SortOrder sortOrder;
    private String sortFiledName;

    private int from = 0;
    private int size = 10;

    public void setFieldVoList(List<AbstactTermVo> fieldVoList) {
        this.fieldVoList = fieldVoList;
    }

    public List<AbstactTermVo> getFieldVoList() {
        return fieldVoList;
    }

    public CommFieldListVo addAndEqualtField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.MUST_ADN,  FilterTermType.TERM));
        return this;
    }

    public CommFieldListVo addAndNotEqualtField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.MUST_ADN,  FilterTermType.NOT_TERM));
        return this;
    }

    public CommFieldListVo addAndInField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.MUST_ADN,  FilterTermType.IN));
        return this;
    }

    public CommFieldListVo addAndNotInField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.MUST_ADN,  FilterTermType.NOT_IN));
        return this;
    }

    public CommFieldListVo addAndFormToField(String fieldName, Object from,Object to) {
        fieldVoList.add(ESRangeVo.builder(fieldName, from,to,  FilterLogicType.MUST_ADN,  FilterTermType.FORM_TO));
        return this;
    }


    public CommFieldListVo addOrEqualtField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.SHOUT_OR,  FilterTermType.TERM));
        return this;
    }

    public CommFieldListVo addOrNotEqualtField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.SHOUT_OR,  FilterTermType.NOT_TERM));
        return this;
    }

    public CommFieldListVo addOrInField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.SHOUT_OR,  FilterTermType.IN));
        return this;
    }

    public CommFieldListVo addOrNotInField(String fieldName, Object fieldValue) {
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.SHOUT_OR,  FilterTermType.NOT_IN));
        return this;
    }
    public CommFieldListVo addOrFormToField(String fieldName, Object from,Object to) {
        fieldVoList.add(ESRangeVo.builder(fieldName, from,to,  FilterLogicType.SHOUT_OR,  FilterTermType.FORM_TO));
        return this;
    }

    ////////////////////////////
    public CommFieldListVo addNestedEqualtField(String fieldName, Object fieldValue) {
        checkNested(fieldName);
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.NESTED,  FilterTermType.TERM));
        return this;
    }

    public CommFieldListVo addNestedNotEqualtField(String fieldName, Object fieldValue) {
        checkNested(fieldName);
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.NESTED,  FilterTermType.NOT_TERM));
        return this;
    }

    public CommFieldListVo addNestedInField(String fieldName, Object fieldValue) {
        checkNested(fieldName);
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.NESTED,  FilterTermType.IN));
        return this;
    }

    public CommFieldListVo addNestedNotInField(String fieldName, Object fieldValue) {
        checkNested(fieldName);
        fieldVoList.add(CommFieldVo.builder(fieldName, fieldValue,  FilterLogicType.NESTED,  FilterTermType.NOT_IN));
        return this;
    }
    public CommFieldListVo addNestedFormToField(String fieldName, Object from,Object to) {
        checkNested(fieldName);
        fieldVoList.add(ESRangeVo.builder(fieldName, from,to,  FilterLogicType.NESTED,  FilterTermType.FORM_TO));
        return this;
    }

    private void checkNested(String fieldName){
        String[] split = fieldName.split("\\.");
        if (split.length != 2) {
            throw new InvalidParameterException("嵌套类型字段不正确");
        }
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public boolean isFetchSource() {
        return fetchSource;
    }

    public void setFetchSource(boolean fetchSource) {
        this.fetchSource = fetchSource;
    }

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRouting() {
        return routing;
    }

    public void setRouting(String routing) {
        this.routing = routing;
    }

    public TimeValue getScroll() {
        return scroll;
    }

    public void setScroll(TimeValue scroll) {
        this.scroll = scroll;
    }

    public boolean isUseScorll() {
        return useScorll;
    }

    public void setUseScorll(boolean useScorll) {
        this.useScorll = useScorll;
    }

    public String getScorllId() {
        return scorllId;
    }

    public void setScorllId(String scorllId) {
        this.scorllId = scorllId;
    }



    public void setSort(String sortFiledName,SortOrder sortOrder) {
        this.sortFiledName = sortFiledName;
        this.sortOrder = sortOrder;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public String getSortFiledName() {
        return sortFiledName;
    }
}
