package com.winshare.demo.es;


public abstract class AbstactTermVo {

    private String fieldName;
    private Object fieldValue;
    private Float boost;
    private FilterLogicType logicType;
    private FilterTermType termType;

    public AbstactTermVo(String fieldName, FilterLogicType logicType, FilterTermType termType) {
        this.fieldName = fieldName;
        this.logicType = logicType;
        this.termType = termType;
    }

    public AbstactTermVo(String fieldName, Object fieldValue, FilterLogicType logicType, FilterTermType termType){
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.logicType = logicType;
        this.termType = termType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(Object fieldValue) {
        this.fieldValue = fieldValue;
    }

    public Float getBoost() {
        return boost;
    }

    public void setBoost(Float boost) {
        this.boost = boost;
    }

    public FilterLogicType getLogicType() {
        return logicType;
    }

    public void setLogicType(FilterLogicType logicType) {
        this.logicType = logicType;
    }

    public FilterTermType getTermType() {
        return termType;
    }

    public void setTermType(FilterTermType termType) {
        this.termType = termType;
    }




}
