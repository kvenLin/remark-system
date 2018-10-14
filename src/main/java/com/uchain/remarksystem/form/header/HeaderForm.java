package com.uchain.remarksystem.form.header;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class HeaderForm {
    @ApiModelProperty("表头名称")
    @NotNull(message = "表头名称不能为空")
    private String name;
    @ApiModelProperty("该表头所在列数")
    @NotNull(message = "指定表头的列数不能为空")
    @Min(value = 1,message = "表头所在列数不能小于1")
    private Integer columnNum;
    @ApiModelProperty("项目id")
    @NotNull(message = "项目的id不能为空")
    private Long projectId;
}
