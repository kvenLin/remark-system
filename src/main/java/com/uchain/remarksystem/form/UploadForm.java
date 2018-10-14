package com.uchain.remarksystem.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UploadForm {
    @ApiModelProperty("项目的id")
    @NotNull(message = "项目id不能为空")
    private Long projectId;
    @ApiModelProperty("数据excel表文件")
    @NotNull(message = "文件不能为空")
    private MultipartFile file;
}
