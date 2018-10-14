package com.uchain.remarksystem.form.choice;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChoiceUpdateForm extends ChoiceForm{
    @ApiModelProperty("选项的id号")
    @NotNull(message = "选项的id号不能为空")
    private Long id;

}
