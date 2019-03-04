package com.uchain.remarksystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uchain.remarksystem.DTO.UserProjectDTO;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.form.userproject.UserProjectForm;
import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.PackageService;
import com.uchain.remarksystem.service.UserProjectService;
import com.uchain.remarksystem.util.MyPageUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/remark/userProject")
@CrossOrigin
public class UserProjectController {

    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private PackageService packageService;

    @PostMapping("/add")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("指定某用户参与某项目")
    public Object add(@Valid @RequestBody UserProjectDTO userProjectDTO){
        return userProjectService.addUserProject(userProjectDTO);
    }
    @GetMapping("/delete")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("从项目中移除某用户")
    public Object delete(Long userId,Long projectId){
        if (userId==null||projectId==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        List<Package> packages = packageService.selectByUserAndProject(userId, projectId);
        //检测用户的参与项目是否有未完成的数据包
        if (packages.size()==0){
            userProjectService.deleteByUserProject(userId,projectId);
            return Result.success();
        }
        return Result.error(CodeMsg.PACKAGE_PROCESSING);
    }

    @GetMapping("/selectProject")
    @ApiOperation("查看指定用户的参与的项目")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object selectProject(Integer pageNum,Long userId){
        if (pageNum==null||pageNum<1){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        if (userId==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        PageHelper.startPage(pageNum,MyPageUtil.projectNum);
        List<Project> projects = userProjectService.selectProjectByUserId(userId);
        PageInfo<Project> projectPageInfo = new PageInfo<>(projects);
        return Result.success(projectPageInfo);
    }

    @GetMapping("/selectUser")
    @ApiOperation("查询指定项目的参与的所有用户")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object selectUser(Long projectId){
        return Result.success(userProjectService.selectUserByProjectId(projectId));
    }
}
