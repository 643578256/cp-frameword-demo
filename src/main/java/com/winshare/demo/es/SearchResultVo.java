package com.winshare.demo.es;


import java.io.Serializable;
import java.util.*;

public class SearchResultVo implements Serializable {
    protected static final long serialVersionUID = -2456700009882000477L;

    protected long totalSize;

    protected int startIndex;

    protected int pageSize;

    protected List<Map<String, Object>> docValues;

    protected String scrollId;
    protected Object[] afterSort;


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<Map<String, Object>> getDocValues() {
        return docValues;
    }

    public void setDocValues(List<Map<String, Object>> docValues) {
        this.docValues = docValues;
    }

    public String getScrollId() {
        return scrollId;
    }

    public void setScrollId(String scrollId) {
        this.scrollId = scrollId;
    }

    public Object[] getAfterSort() {
        return afterSort;
    }

    public void setAfterSort(Object[] afterSort) {
        this.afterSort = afterSort;
    }
}
