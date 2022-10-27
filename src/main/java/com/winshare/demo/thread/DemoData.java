package com.winshare.demo.thread;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class DemoData {
    
    @ExcelProperty(
            value = "会员编号（必填）"
    )
    private String memberNo;
   
    @ExcelProperty(
            value = "宝宝名称"
    )
    private String value;
    
    @ExcelProperty(
            value = "宝宝昵称"
    )
    private String nickvalue;
   
    @ExcelProperty(
            value = "宝宝生日（必填）"
    )
    private Date birthday;
    
    @ExcelProperty(
            value = "宝宝性别"
    )
    private String sex;
   
    @ExcelProperty(
            value = "宝宝年龄"
    )
    private Integer age;
   
    @ExcelProperty(
            value = "备注"
    )
    private String remark;
    
    @ExcelProperty(
            value = "错误原因"
    )
    private String errorReason;

    private Long memberId;

  
   
}