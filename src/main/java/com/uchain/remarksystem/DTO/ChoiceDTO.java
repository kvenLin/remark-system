package com.uchain.remarksystem.DTO;

import com.uchain.remarksystem.form.choice.ChoiceForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChoiceDTO {
    @NotNull(message = "参数不能为空")
    private ChoiceForm[] choiceForms;
}
