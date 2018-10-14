package com.uchain.remarksystem.DTO;

import com.uchain.remarksystem.form.userproject.UserProjectForm;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserProjectDTO {
    @NotNull(message = "添加项目用户的表单不能为空")
    private UserProjectForm[] userProjectForms;
}
