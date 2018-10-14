package com.uchain.remarksystem.DTO;

import com.uchain.remarksystem.form.header.HeaderForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HeaderDTO {
    @NotNull(message = "参数不能为空")
    private HeaderForm[] headerForms;
}
