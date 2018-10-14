package com.uchain.remarksystem.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginForm {
    @ApiModelProperty("员工号")
    @NotNull(message = "员工号不能为空")
    private String empNum;
    @ApiModelProperty("密码")
    @NotNull(message = "密码不能为空")
    private String password;
    @ApiModelProperty("验证码")
    @NotNull(message = "验证码不能为空")
    private String verifyCode;
}
