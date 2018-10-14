package com.uchain.remarksystem.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserRegisterForm extends UserBaseForm{
    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    private String password;
    @ApiModelProperty("确认密码")
    @NotNull(message = "确认密码不能为空")
    private String checkPass;
    @ApiModelProperty("验证码")
    @NotNull(message = "验证码不能为空")
    private String verifyCode;
}
