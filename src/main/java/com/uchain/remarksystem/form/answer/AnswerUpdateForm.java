package com.uchain.remarksystem.form.answer;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerUpdateForm {
    @ApiModelProperty("回答的id")
    @NotNull(message = "回答的id不能为空")
    private Long answerId;
    @ApiModelProperty("选项的id")
    private Long choiceId;
}
