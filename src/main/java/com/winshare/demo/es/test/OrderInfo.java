package com.winshare.demo.es.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.winshare.demo.es.ESBaseVO;
import com.winshare.demo.es.EsIndexDocument;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@EsIndexDocument(indexName = "order_info")
public class OrderInfo extends ESBaseVO {

    private String buyer;
    private String address;
    private BigDecimal price;
    //private List<String> items;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-mm-dd hh:MM:ss")
    private Date createDate;

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

   /* public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }*/

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
