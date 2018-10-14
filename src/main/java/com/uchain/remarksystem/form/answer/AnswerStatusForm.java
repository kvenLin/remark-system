package com.uchain.remarksystem.form.answer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class AnswerStatusForm {
    @NotNull(message = "回答的id不能为空")
    @ApiModelProperty("回答id")
    private Long answerId;
    @NotNull(message = "状态不能为空")
    @ApiModelProperty("回答的状态,1正确,2错误")
    @Max(2)
    @Min(1)
    private Integer status;
}
