package com.winshare.demo.cpBatis.dao.filter;

import java.util.ArrayList;
import java.util.List;

public class SqlFilterBuilder {
    private SqlFilterBuilder() {
    }

    public List<SqlFilter> getSqlFilters() {
        return sqlFilters;
    }

    private final List<SqlFilter> sqlFilters = new ArrayList<>();

    public static SqlFilterBuilder Builder() {
        SqlFilterBuilder builder = new SqlFilterBuilder();
        return builder;
    }

    public SqlFilterBuilder addFilter(SqlFilter sqlFilter) {
        sqlFilters.add(sqlFilter);
        return this;
    }
}
