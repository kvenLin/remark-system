package com.uchain.remarksystem.form.choice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ChoiceForm {

    @ApiModelProperty("选项内容")
    @NotNull(message = "选项内容描述不能为空")
    private String content;
    @ApiModelProperty("项目id")
    @NotNull(message = "项目id不能为空")
    private Long projectId;
}
