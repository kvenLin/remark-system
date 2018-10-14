package com.uchain.remarksystem.form.answer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerForm {
    @ApiModelProperty("数据包的id")
    @NotNull(message = "数据包的id不能为空")
    private Long packageId;
    @NotNull(message = "第几条数据不能为空")
    private Integer rowNum;
    @ApiModelProperty("对应项目的选项的id")
    private Long choiceId;
}
