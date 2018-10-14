package com.uchain.remarksystem.form.answer;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerDataUpdateForm {
    @NotNull(message = "answerDataId不能为空")
    private Long answerDataId;
    @NotNull(message = "内容不能为空")
    private String content;
}
