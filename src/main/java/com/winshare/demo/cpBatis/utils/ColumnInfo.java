package com.winshare.demo.cpBatis.utils;

import lombok.Data;

import java.lang.reflect.Field;

@Data
public class ColumnInfo {
    private String columnName;
    private String propertName;
    private Field field;
    private Class type;
}
