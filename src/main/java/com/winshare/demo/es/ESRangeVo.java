package com.winshare.demo.es;

public class ESRangeVo extends AbstactTermVo {

    private Object from;
    private Object to;

    public static ESRangeVo builder(String fieldName, Object from,Object to, FilterLogicType logicType, FilterTermType termType) {
        return new ESRangeVo(fieldName, from,to, logicType, termType);
    }
    public ESRangeVo(String fieldName, Object from,Object to, FilterLogicType logicType, FilterTermType termType) {
        super(fieldName, logicType, termType);
        this.from = from;
        this.to = to;
    }

    public Object getFrom() {
        return from;
    }

    public void setFrom(Object from) {
        this.from = from;
    }

    public Object getTo() {
        return to;
    }

    public void setTo(Object to) {
        this.to = to;
    }
}
