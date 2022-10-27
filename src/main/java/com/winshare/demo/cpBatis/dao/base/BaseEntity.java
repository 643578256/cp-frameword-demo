package com.winshare.demo.cpBatis.dao.base;

import com.winshare.demo.cpBatis.dao.filter.SqlFilterBuilder;

import javax.persistence.Transient;

public abstract class BaseEntity {

    public SqlFilterBuilder getBuilder() {
        return builder;
    }

    public void setBuilder(SqlFilterBuilder builder) {
        this.builder = builder;
    }

    @Transient
    private SqlFilterBuilder builder;
}
