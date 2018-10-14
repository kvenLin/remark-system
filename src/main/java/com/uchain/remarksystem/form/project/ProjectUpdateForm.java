package com.uchain.remarksystem.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ProjectUpdateForm extends ProjectForm{
    @ApiModelProperty("项目id")
    @NotNull(message = "项目的id不能为空")
    private Long id;
}
