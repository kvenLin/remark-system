package com.uchain.remarksystem.DTO;

import com.uchain.remarksystem.form.header.HeaderUpdateForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HeaderUpdateDTO {
    @NotNull(message = "参数不能为空")
    private HeaderUpdateForm[] headerUpdateForms;
}
