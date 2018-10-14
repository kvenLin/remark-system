package com.uchain.remarksystem.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserBaseForm {
    @ApiModelProperty("姓名")
    @NotNull(message = "姓名不能为空")
    private String name;
    @ApiModelProperty("员工号")
    @NotNull(message = "员工号不能为空")
    private String empNum;
    @ApiModelProperty("QQ号")
    @NotNull(message = "QQ号不能为空")
    private String qqNum;
    @ApiModelProperty("电话号码")
    @NotNull(message = "电话号码不能为空")
    private String phoneNum;
}
