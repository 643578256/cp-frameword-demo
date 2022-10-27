package com.winshare.demo.cpBatis.dao.filter;

import com.winshare.demo.cpBatis.dao.exeception.SqlBuilException;
import lombok.Data;

import java.util.List;

@Data
public class SqlFilter {
    private String column;
    private Object values;
    private Operator op;

    public static SqlFilter addFilter(Operator op, String column, Object obj) {
        if (op == Operator.between) {
            if (!(obj instanceof List)) {
                throw new SqlBuilException("值不支持非list集合");
            }
        }

        SqlFilter sqlFilter = new SqlFilter();
        sqlFilter.column = column;
        sqlFilter.values = obj;
        sqlFilter.op = op;
        return sqlFilter;
    }

    public static enum Operator {
        eq("=", "等于"),
        ne("<>", "不等于"),
        gt(">", "大于"),
        lt(">", "小于"),
        le("<=", "小于等于"),
        ge(">=", "大于等于"),
        like("LIKE", "like"),
        between("BETWEEN", "在两值之间"),
        in("IN", "在集合中"),
        notIn("NOT IN", "不在集合中"),
        isNull("IS NULL", "为空"),
        isNotNull("IS NOT NULL", "不为空"),
        or("OR", "或"),
        and("AND", "与"),
        desc("desc", "倒序"),
        asc("asc", "升序"),
        ;

        private String remark;
        private String value;

        public String getValue() {
            return value;
        }

        public String getRemark() {
            return remark;
        }

        Operator(String value, String remark) {
            this.remark = remark;
            this.value = value;
        }

    }

}
