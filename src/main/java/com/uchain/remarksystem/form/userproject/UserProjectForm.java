package com.uchain.remarksystem.form.userproject;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserProjectForm {
    @ApiModelProperty("所选择的用户的id")
    @NotNull(message = "用户的id不能为空")
    private Long userId;
    @ApiModelProperty("所选择的项目的id")
    @NotNull(message = "项目id不能为空")
    private Long projectId;
}
