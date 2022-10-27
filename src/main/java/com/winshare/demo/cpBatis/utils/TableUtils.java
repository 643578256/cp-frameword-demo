package com.winshare.demo.cpBatis.utils;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.*;

public class TableUtils {

    public static final Map<String, EntityMapTableInfo> mapperTable = new HashMap<>(56);

    public static EntityMapTableInfo getMapperTableInfo(Class tableClass) {
        Table table = (Table) tableClass.getDeclaredAnnotation(Table.class);
        Assert.isNull(table, "table is null");
        EntityMapTableInfo tableInfo = mapperTable.get(table.name());
        if (tableInfo != null) {
            return tableInfo;
        }
        EntityMapTableInfo mapperTableInfo = createMapperTableInfo(tableClass);
        mapperTable.put(table.name(), mapperTableInfo);
        return mapperTableInfo;
    }

    private static EntityMapTableInfo createMapperTableInfo(Class tableClass) {
        Table table = (Table) tableClass.getDeclaredAnnotation(Table.class);
        Assert.isNull(table, "table is null");
        Field[] declaredFields = tableClass.getDeclaredFields();
        List<Field> fields = Arrays.asList(declaredFields);
        EntityMapTableInfo tableInfo = new EntityMapTableInfo();
        List<ColumnInfo> columnInfoList = new ArrayList<>();
        for (Field fied : fields) {
            Column annotation = fied.getAnnotation(Column.class);
            if (annotation != null) {
                fied.setAccessible(true);
                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setColumnName(annotation.name());
                columnInfo.setField(fied);
                columnInfo.setPropertName(fied.getName());
                columnInfoList.add(columnInfo);
            }
        }
        tableInfo.setColumnInfos(columnInfoList);
        tableInfo.setEoType(tableClass);
        return tableInfo;
    }
}
