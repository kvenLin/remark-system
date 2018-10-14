package com.uchain.remarksystem.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class UserAddForm extends UserBaseForm{
    @ApiModelProperty("0:待审核,1:remark,2:inspector,3:examiner,4:admin")
    @NotNull(message = "用户角色不能为空")
    @Max(value = 4,message = "角色最大值为4")
    @Min(value = 0,message = "角色最小值为0")
    private Integer role = 0;
}
