package com.winshare.demo.cpBatis.utils;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class EntityMapTableInfo {
    private List<ColumnInfo> columnInfos;
    private Map<String, ColumnInfo> propertesInfos;
    private Class eoType;
}
