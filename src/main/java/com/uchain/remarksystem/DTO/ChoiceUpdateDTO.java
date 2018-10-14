package com.uchain.remarksystem.DTO;

import com.uchain.remarksystem.form.choice.ChoiceUpdateForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChoiceUpdateDTO {
    @NotNull(message = "参数不能为空")
    private ChoiceUpdateForm[] choiceUpdateForms;
}
