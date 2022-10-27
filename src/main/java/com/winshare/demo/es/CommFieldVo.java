package com.winshare.demo.es;

public class CommFieldVo extends AbstactTermVo  {


    public static CommFieldVo builder(String fieldName, Object fieldValue, FilterLogicType logicType, FilterTermType termType) {
        return new CommFieldVo(fieldName, fieldValue, logicType, termType);
    }

    public CommFieldVo(String fieldName, Object fieldValue, FilterLogicType logicType, FilterTermType termType) {
        super(fieldName,fieldValue,logicType,termType);
    }


}
