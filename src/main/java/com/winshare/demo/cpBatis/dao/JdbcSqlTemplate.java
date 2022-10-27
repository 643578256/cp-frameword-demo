package com.winshare.demo.cpBatis.dao;

import com.winshare.demo.cpBatis.dao.base.BaseEntity;
import com.winshare.demo.cpBatis.dao.exeception.SqlBuilException;
import com.winshare.demo.cpBatis.dao.filter.SqlFilter;
import com.winshare.demo.cpBatis.dao.filter.SqlFilterBuilder;
import com.winshare.demo.cpBatis.utils.ColumnInfo;
import com.winshare.demo.cpBatis.utils.EntityMapTableInfo;
import com.winshare.demo.cpBatis.utils.TableUtils;
import lombok.extern.log4j.Log4j;
import org.apache.ibatis.jdbc.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Table;
import java.util.Collection;
import java.util.List;

public class JdbcSqlTemplate<T extends BaseEntity> {

    private static Logger log = LoggerFactory.getLogger(JdbcSqlTemplate.class);

    public String insert(T t) {
        SQL insert = new SQL();
        Table table = t.getClass().getAnnotation(Table.class);
        insert.INSERT_INTO(table.name());
        EntityMapTableInfo mapperTableInfo = TableUtils.getMapperTableInfo(t.getClass());
        List<ColumnInfo> columnInfos = mapperTableInfo.getColumnInfos();
        for (ColumnInfo columnInfo : columnInfos) {
            insert.VALUES(columnInfo.getColumnName(), "#{" + columnInfo.getPropertName() + "}");
        }
        return insert.toString();
    }

    public String logicDeleteById(T t) {
        SQL insert = new SQL();
        Table table = t.getClass().getAnnotation(Table.class);
        insert.UPDATE(table.name());
        try {
            insert.SET("dr=0");
            insert.WHERE("id=#{id}");
        } catch (Exception e) {
            log.info("构建表" + table.name() + "sql异常：{}", e);
            e.printStackTrace();
        }
        return insert.toString();
    }

    public String logicDeleteBySelect(T t) {
        SQL insert = new SQL();
        Table table = t.getClass().getAnnotation(Table.class);
        insert.UPDATE(table.name());
        EntityMapTableInfo mapperTableInfo = TableUtils.getMapperTableInfo(t.getClass());
        try {
            insert.SET("dr=0");
            insert.WHERE(buildWhereFilterSql(mapperTableInfo, t));
        } catch (Exception e) {
            log.info("构建表" + table.name() + "sql异常：{}", e);
            e.printStackTrace();
        }
        return insert.toString();
    }

    public String updateSelectColumnById(T t) {
        SQL insert = new SQL();
        Table table = t.getClass().getAnnotation(Table.class);
        insert.UPDATE(table.name());
        EntityMapTableInfo mapperTableInfo = TableUtils.getMapperTableInfo(t.getClass());
        try {
            insert.SET(buidColumnSqlNotNull(mapperTableInfo, t));
            insert.WHERE("id=#{id}");
        } catch (Exception e) {
            log.info("构建表" + table.name() + "sql异常：{}", e);
            e.printStackTrace();
        }
        return insert.toString();
    }

    public String updateBySqlFilter(T t) {
        SQL update = new SQL();
        Table table = t.getClass().getAnnotation(Table.class);
        update.UPDATE(table.name());
        EntityMapTableInfo tableInfo = TableUtils.getMapperTableInfo(t.getClass());
        update.SET(buidColumnSqlNotNull(tableInfo, t));
        update.WHERE(buildWhereFilterSql(tableInfo, t));
        return update.toString();
    }

    public String selectById(T t) {
        SQL select = new SQL();
        EntityMapTableInfo mapperTableInfo = TableUtils.getMapperTableInfo(t.getClass());
        List<ColumnInfo> columnInfos = mapperTableInfo.getColumnInfos();
        select.SELECT(buildSelectColumns(columnInfos));
        Table table = t.getClass().getAnnotation(Table.class);
        select.FROM(table.name());
        select.WHERE("id = #{id}");
        return select.toString();
    }

    public String selectBySqlFilter(T t) {
        SQL select = new SQL();
        EntityMapTableInfo mapperTableInfo = TableUtils.getMapperTableInfo(t.getClass());
        List<ColumnInfo> columnInfos = mapperTableInfo.getColumnInfos();
        select.SELECT(buildSelectColumns(columnInfos));
        Table table = t.getClass().getAnnotation(Table.class);
        select.FROM(table.name());
        select.WHERE(buildWhereFilterSql(mapperTableInfo, t));
        return select.toString();
    }

    private String buildSelectColumns(List<ColumnInfo> columnInfos) {
        StringBuilder builder = new StringBuilder();
        for (ColumnInfo columnInfo : columnInfos) {
            builder.append(columnInfo.getColumnName() + ",");
        }
        String sql = builder.toString();
        return sql.substring(0, sql.length() - 1);
    }

    private String buildWhereFilterSql(EntityMapTableInfo tableInfo, T t) {
        StringBuilder builder = new StringBuilder();
        SqlFilterBuilder sqlFilterBuilder = t.getBuilder();
        if (sqlFilterBuilder != null && sqlFilterBuilder.getSqlFilters().size() > 0) {
            List<SqlFilter> sqlFilters = sqlFilterBuilder.getSqlFilters();
            for (SqlFilter sqlFilter : sqlFilters) {
                SqlFilter.Operator operator = sqlFilter.getOp();
                if (operator != SqlFilter.Operator.isNotNull || operator != SqlFilter.Operator.isNull) {
                    throw new SqlBuilException(sqlFilter.getColumn() + "值不能为null");
                }
                switch (operator) {
                    case between:
                        builder.append("AND " + sqlFilter.getColumn() + " ");
                        builder.append(operator.getValue() + " ");
                        if (sqlFilter.getValues() instanceof List) {
                            builder.append(((List) sqlFilter.getValues()).get(0));
                            builder.append("AND " + ((List) sqlFilter.getValues()).get(1));
                        }
                        break;
                    case in:
                        if (sqlFilter.getValues() instanceof Collection) {
                            Collection<Object> values = (Collection) sqlFilter.getValues();
                            buidInOrNotIn(builder, sqlFilter, operator, values);
                        }
                        break;
                    case notIn:
                        if (sqlFilter.getValues() instanceof Collection) {
                            Collection<Object> values = (Collection) sqlFilter.getValues();
                            buidInOrNotIn(builder, sqlFilter, operator, values);
                        }
                        break;
                    case or:
                        builder.append("OR " + sqlFilter.getColumn() + "=" + sqlFilter.getValues());
                        break;
                    case like:
                        builder.append(sqlFilter.getColumn() + "LIKE '" + operator.getValue() + "'");
                        break;
                    default:
                        builder.append("AND " + sqlFilter.getColumn() + operator.getValue() + sqlFilter.getValues());
                        //                        throw new SqlBuilException("根据实体类："+tableInfo.getEoType().getSimpleName()+",不支持的sql格式构建异常operator："+operator.name()+" SQL:"+builder.toString());
                }
            }
            String sql = builder.toString();
            sql = sql.substring(3, sql.length());
            return sql;
        }
        return "";
    }

    private StringBuilder buidInOrNotIn(StringBuilder builder, SqlFilter sqlFilter, SqlFilter.Operator operator, Collection values) {
        builder.append("AND " + sqlFilter.getColumn() + " " + operator.getValue() + " (");
        int index = 1;
        for (Object o : values) {
            if (index == values.size()) {
                builder.append(o + " ");
            } else {
                builder.append(o + " ,");
            }
            index++;
        }
        builder.append(")");
        return builder;
    }

    private String buidColumnSqlNotNull(EntityMapTableInfo tableInfo, T t) {
        StringBuilder builder = new StringBuilder();
        List<ColumnInfo> columnInfos = tableInfo.getColumnInfos();
        try {
            for (ColumnInfo columnInfo : columnInfos) {
                if (columnInfo.getField().get(t) != null) {
                    builder.append(columnInfo.getColumnName() + "=");
                    builder.append("#{");
                    builder.append(columnInfo.getPropertName());
                    builder.append("},");
                }
            }
        } catch (Exception e) {
            throw new SqlBuilException(e.getMessage());
        }
        String s = builder.toString();
        String sql = s.substring(0, s.length() - 1);
        return sql;
    }

}
