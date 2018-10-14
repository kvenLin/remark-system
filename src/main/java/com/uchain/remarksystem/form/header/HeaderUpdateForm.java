package com.uchain.remarksystem.form.header;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class HeaderUpdateForm extends HeaderForm{
    @ApiModelProperty("表头的id")
    @NotNull(message = "表头的id不能为空")
    private Long id;
}
