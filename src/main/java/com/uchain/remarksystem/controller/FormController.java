package com.uchain.remarksystem.controller;

import com.uchain.remarksystem.DTO.ChoiceDTO;
import com.uchain.remarksystem.DTO.ChoiceUpdateDTO;
import com.uchain.remarksystem.DTO.HeaderDTO;
import com.uchain.remarksystem.DTO.HeaderUpdateDTO;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.exception.GlobalException;
import com.uchain.remarksystem.form.UploadForm;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.FormService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormService formService;

    @PostMapping("/addHeaders")
    @ApiOperation("添加表头指定导入的格式")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object addHeaders(@Valid @RequestBody HeaderDTO headerDTO){
        return formService.addHeaders(headerDTO.getHeaderForms(),0);
    }

    @PostMapping("/addAnswerHeaders")
    @ApiOperation("添加回答的表头")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object addAnswerHeaders(@Valid @RequestBody HeaderDTO headerDTO){
        return formService.addHeaders(headerDTO.getHeaderForms(),1);
    }


    @PostMapping("/addChoices")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object addChoice(@Valid @RequestBody ChoiceDTO choiceDTO){
        return formService.addChoices(choiceDTO.getChoiceForms());
    }

    @GetMapping("/deleteHeader")
    @ApiOperation("删除表头")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object deleteHeader(Long id){
        formService.deleteHeader(id);
        return Result.success();
    }

    @GetMapping("/deleteAnswerHeader")
    @ApiOperation("删除回答的表头")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object deleteAnswerHeader(Long id){
        formService.deleteAnswerHeader(id);
        return Result.success();
    }

    @GetMapping("/deleteChoice")
    @ApiOperation("删除选项")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object deleteChoice(Long id) {
        formService.deleteChoice(id);
        return Result.success();
    }


    @GetMapping("/allChoices")
    @ApiOperation("某项目的全部选项")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object allChoice(Long projectId){
        if (projectId==null){
            Result.error(CodeMsg.PARAM_IS_NULL);
        }
        return Result.success(formService.selectChoiceByProjectId(projectId));
    }

    @GetMapping("/allHeaders")
    @ApiOperation("某项目的全部表头,作为标注界面的提示字段")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object allHeader(Long projectId){
        if (projectId==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        return Result.success(formService.selectHeaderByProjectId(projectId));
    }

    @GetMapping("/allAnswerHeaders")
    @ApiOperation("获取某项目的所有回答表头")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object allAnswerHeaders(Long projectId){
        if (projectId==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        return Result.success(formService.selectAnswerHeaderByProjectId(projectId));
    }

    @PostMapping("/updateChoices")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("更新选项内容")
    public Object updateChoice(@Valid @RequestBody ChoiceUpdateDTO choiceUpdateDTO){
        return formService.changeChoices(choiceUpdateDTO.getChoiceUpdateForms());
    }



    @PostMapping("/updateHeaders")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("更新原始表头信息")
    public Object updateHeaders(@Valid @RequestBody HeaderUpdateDTO headerUpdateDTO){
        return formService.changeHeaders(headerUpdateDTO.getHeaderUpdateForms(),0);
    }

    @PostMapping("/updateAnswerHeaders")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("更新回答表头信息")
    public Object updateAnswerHeaders(@Valid @RequestBody HeaderUpdateDTO headerUpdateDTO){
        return formService.changeHeaders(headerUpdateDTO.getHeaderUpdateForms(),1);
    }

    @PostMapping("/uploadForm")
    @ApiOperation("上传excel表文件")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object uploadForm(Long projectId, MultipartFile file){
        return formService.uploadFile(new UploadForm(projectId,file));
    }

    @GetMapping("/{path}/downloadFile")
    @ApiOperation("导出指定项目的excel文件")
    @RequireRole(role = RoleEnum.EXAMINER)
    public void downloadFile(Long projectId,@PathVariable("path") String path
            ,HttpServletRequest request, HttpServletResponse response){
        if (projectId==null){
            throw new GlobalException(CodeMsg.PARAM_IS_NULL);
        }
        if (StringUtils.isEmpty(path)){
            throw new GlobalException(CodeMsg.NOT_FOUND);
        }
        if (!formService.checkPath(request,path)){
            throw new GlobalException(CodeMsg.NOT_FOUND);
        }
        try {
            formService.download(projectId,response);
        } catch (IOException e) {
            throw new GlobalException(CodeMsg.DOWNLOAD_FILE_ERROR);
        }
    }

    @GetMapping("/getPath")
    @RequireRole(role = RoleEnum.EXAMINER)
    public Object getDownloadPath(HttpServletRequest request){
        return formService.getDownloadPath(request);
    }

}
