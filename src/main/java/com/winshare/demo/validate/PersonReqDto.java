package com.winshare.demo.validate;

import com.winshare.demo.validate.annotation.AddrassLocal;
import com.winshare.demo.validate.annotation.MoneyRule;
import com.winshare.demo.validate.grop.DZGroup;
import com.winshare.demo.validate.grop.GZGroup;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class PersonReqDto implements Serializable {

    @AddrassLocal(groups = {DZGroup.class}, message = "地址信息不能为空")
    private String local;

    @MoneyRule(groups = {GZGroup.class}, message = "金额规则不对")
    private String countValue;

    @NotBlank
    private String countName;

    @Valid
    private PersonReqDto reqDto;
}
