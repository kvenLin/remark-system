package com.uchain.remarksystem.form.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserUpdateForm extends UserAddForm{
    @ApiModelProperty("用户id")
    @NotNull(message = "用户id不能为空")
    private Long id;
}
