package com.winshare.demo.cpBatis.dao.eo;

import com.winshare.demo.cpBatis.dao.base.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Table(name = "order_pay_detail_info")
public class OrderPayDetailInfoEo extends BaseEntity {


    @Id
    @Column(name = "id")
    protected Long id;

    /**
     * oms订单号
     */
    @Column(name = "oms_order_id")
    private String omsOrderId;

    /**
     * 支付方式(枚举)
     */
    @Column(name = "pay_type")
    private String payType;
    /**
     * 支付金额
     */
    @Column(name = "pay_amount")
    private BigDecimal payAmount;

}
