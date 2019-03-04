package com.uchain.remarksystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.form.project.ProjectForm;
import com.uchain.remarksystem.form.project.ProjectUpdateForm;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.ProjectService;
import com.uchain.remarksystem.service.UserProjectService;
import com.uchain.remarksystem.service.UserService;
import com.uchain.remarksystem.util.MyPageUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/remark/project")
@CrossOrigin
@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("创建项目")
    public Object create(@Valid @RequestBody ProjectForm projectForm){
        return projectService.addProject(projectForm);
    }

    @GetMapping("/delete")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("删除项目 0:未启动, 2:已完成, 1:进行中")
    public Object delete(Long id){
        if (id==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        Project project = projectService.selectById(id);
        if (project==null){
            return Result.success();
        }
        projectService.delete(id);
        return Result.success();
//        return Result.error(CodeMsg.PROJECT_CAN_NOT_DELETE);
    }

    @GetMapping("/all")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("查看所有项目信息")
    public Object all(Integer pageNum){
        if (pageNum==null||pageNum<=0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        PageHelper.startPage(pageNum,MyPageUtil.projectNum);
        List<Project> projects = projectService.allProjects();
        PageInfo<Project> projectPageInfo = new PageInfo<>(projects);
        return Result.success(projectPageInfo);
    }

    @PostMapping("/update")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("更改项目信息")
    public Object update(@Valid @RequestBody ProjectUpdateForm projectUpdateForm){
        return projectService.updatedProject(projectUpdateForm);
    }

    @GetMapping("/select")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("按项目id或项目名进行搜索接口")
    public Object select(Integer pageNum,String info){
        if (pageNum==null||pageNum<1){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        PageHelper.startPage(pageNum,MyPageUtil.projectNum);
        List<Project> projects = projectService.selectRandom(info);
        PageInfo<Project> projectPageInfo = new PageInfo<>(projects);
        return Result.success(projectPageInfo);
    }

    @GetMapping("/allOfUser")
    @ApiOperation("当前用户的参与的所有项目的信息")
    @RequireRole(role = RoleEnum.REMARK)
    public Object allOfUser(Integer pageNum){
        if (pageNum==null||pageNum<1){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        Long id = userService.getCurrentUser().getId();
        PageHelper.startPage(pageNum,MyPageUtil.projectNum);
        List<Project> projects = userProjectService.selectProjectByUserId(id);
        PageInfo<Project> projectPageInfo = new PageInfo<>(projects);
        return Result.success(projectPageInfo);
    }


}
