package com.uchain.remarksystem.form.answer;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerDataForm {
    @NotNull(message = "回答内容不能为空")
    private String content;
    @NotNull(message = "answerHeaderId不能为空")
    private Long answerHeaderId;
}
