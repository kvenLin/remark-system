package com.uchain.remarksystem.form.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ProjectForm {
    @ApiModelProperty("项目名称")
    @NotNull(message = "项目名不能为空")
    private String name;
    @ApiModelProperty("指定标注界面是否需要文本区域,0:没有,1:有;默认为0没有")
    @Min(value = 0,message = "无效参数")
    @Max(value = 1,message = "无效参数")
    private Integer hasText = 0;
    @ApiModelProperty("文本区字数限制,默认为-1没有字数限制")
    private Integer wordsNum = -1;
    @ApiModelProperty("指定该项目数据包的条数,默认为200条为一个数据包")
    private Integer packageNum = 200;
    @ApiModelProperty("指定抽检数量,默认为30条")
    private Integer checkNum = 30;
}
