package com.winshare.demo.es;

public enum FilterTermType {
        //EQUALS, NOT_EQUALS, IN, NOT_IN, FORM_TO, GT 大于, LT 小于, GTE 大于等于, LTE 小于等于;
        MATCH,TERM, NOT_TERM, IN, NOT_IN, FORM_TO, GT, LT, GTE, LTE,PREFIX,FUZZY;
}
