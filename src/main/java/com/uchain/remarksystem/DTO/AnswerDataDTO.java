package com.uchain.remarksystem.DTO;

import com.uchain.remarksystem.form.answer.AnswerDataForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AnswerDataDTO {
    @NotNull(message = "数据的行数不能为空")
    private Integer rowNum;
    @NotNull(message = "数据包id不能为空")
    private Long packageId;
    @NotNull(message = "回答数据表单不能为空")
    private AnswerDataForm[] answerDataForms;
}
