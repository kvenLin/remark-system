package com.uchain.remarksystem.DTO;

import com.uchain.remarksystem.form.answer.AnswerDataUpdateForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerDataUpdateDTO {
    @NotNull(message = "回答的信息更新的表单不能为空")
    private AnswerDataUpdateForm[] answerDataUpdateForms;
}
