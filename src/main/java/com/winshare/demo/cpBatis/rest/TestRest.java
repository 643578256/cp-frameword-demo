package com.winshare.demo.cpBatis.rest;

import com.winshare.demo.cpBatis.dao.eo.OrderPayDetailInfoEo;
import com.winshare.demo.cpBatis.dao.filter.SqlFilter;
import com.winshare.demo.cpBatis.dao.filter.SqlFilterBuilder;
import com.winshare.demo.cpBatis.dao.mapper.OrderPayDetailInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*@RestController*/
public class TestRest {

    @Autowired
    private OrderPayDetailInfoMapper mapper;

    @RequestMapping("/test/batis")
    public void test() {
        OrderPayDetailInfoEo eo = new OrderPayDetailInfoEo();
        eo.setOmsOrderId("123");
        eo.setPayType("01");
        eo.setId(100L);
        mapper.insert(eo);
        int a = 0;
    }

    @RequestMapping("/test/batis/findByTerm")
    public void findByTerm() {
        OrderPayDetailInfoEo eo = new OrderPayDetailInfoEo();
        eo.setOmsOrderId("123");
        eo.setPayType("01");
        eo.setId(100L);
        SqlFilterBuilder builder = SqlFilterBuilder.Builder()
                .addFilter(SqlFilter.addFilter(SqlFilter.Operator.and, "pay_type", "01"))
                .addFilter(SqlFilter.addFilter(SqlFilter.Operator.and, "pay_type", "01"));

        eo.setBuilder(builder);
        mapper.insert(eo);
        int a = 0;
    }

    @RequestMapping("/test/batis/delete")
    public void delete() {
        OrderPayDetailInfoEo eo = new OrderPayDetailInfoEo();
        eo.setOmsOrderId("123");
        eo.setPayType("01");
        eo.setId(100L);
        mapper.insert(eo);
        int a = 0;
    }

    @RequestMapping("/test/batis/update")
    public void update() {
        OrderPayDetailInfoEo eo = new OrderPayDetailInfoEo();
        eo.setOmsOrderId("123");
        eo.setPayType("01");
        eo.setId(100L);
        mapper.insert(eo);
        int a = 0;
    }

    @RequestMapping("/test/batis/find")
    public void find() {
        OrderPayDetailInfoEo eo = new OrderPayDetailInfoEo();
        eo.setOmsOrderId("123");
        eo.setPayType("01");
        eo.setId(100L);
        mapper.insert(eo);
        int a = 0;
    }
}
