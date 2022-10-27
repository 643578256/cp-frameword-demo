package com.winshare.demo.cpBatis.dao.mapper;

import com.winshare.demo.cpBatis.dao.JdbcSqlTemplate;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.util.List;

public interface BaseMapper<T> {

    @InsertProvider(type = JdbcSqlTemplate.class, method = "insert")
    void insert(T t);

    @UpdateProvider(type = JdbcSqlTemplate.class, method = "logicDeleteById")
    public Integer logicDeleteById(T t);

    @UpdateProvider(type = JdbcSqlTemplate.class, method = "logicDeleteBySelect")
    public Integer logicDeleteBySelect(T t);

    @UpdateProvider(type = JdbcSqlTemplate.class, method = "updateSelectColumnById")
    public Integer updateSelectColumnById(T t);

    @UpdateProvider(type = JdbcSqlTemplate.class, method = "updateBySqlFilter")
    public Integer updateBySqlFilter(T t);

    @SelectProvider(type = JdbcSqlTemplate.class, method = "selectById")
    public T selectById(T t);

    @SelectProvider(type = JdbcSqlTemplate.class, method = "selectBySqlFilter")
    public List<T> selectBySqlFilter(T t);
}
