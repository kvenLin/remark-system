package com.uchain.remarksystem.service.impl;

import com.uchain.remarksystem.dao.*;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.form.project.ProjectForm;
import com.uchain.remarksystem.form.project.ProjectUpdateForm;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private HeaderMapper headerMapper;
    @Autowired
    private ChoiceMapper choiceMapper;
    @Autowired
    private UserProjectMapper userProjectMapper;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private AnswerDataMapper answerDataMapper;
    @Autowired
    private AnswerHeaderMapper answerHeaderMapper;
    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private PackageMapper packageMapper;

    @Override
    public boolean insert(Project project) {
        if (projectMapper.insert(project)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Project project) {
        if (projectMapper.updateByPrimaryKey(project)==1){
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        //删除项目的所有选项
        choiceMapper.deleteChoiceByProject(id);
        //删除原始表头
        headerMapper.deleteHeaderByProject(id);
        //删除原始数据
        dataMapper.deleteByProject(id);
        //删除回答表头
        answerHeaderMapper.deleteAnswerHeaderByProject(id);
        //删除回答的数据
        answerDataMapper.deleteByProject(id);
        //删除用户和项目的关系
        userProjectMapper.deleteByProject(id);
        //删除项目的回答信息
        answerMapper.deleteByProject(id);
        //删除数据包
        packageMapper.deleteByProject(id);
        //删除项目信息
        projectMapper.deleteByPrimaryKey(id);
    }

    @Override
    public List<Project> allProjects() {
        return projectMapper.selectAll();
    }

    @Override
    public List<Project> selectRandom(String info) {
        return projectMapper.selectByRandom(info);
    }

    @Override
    public List<Project> UserAllProjects() {
        return userProjectService.selectProjectByUserId(userService.getCurrentUser().getId());
    }

    @Override
    public Project selectById(Long id) {
        return projectMapper.selectByPrimaryKey(id);
    }

    @Override
    public Project selectProjectByHeader(Long headerId) {
        return projectMapper.selectByHeaderId(headerId);
    }

    @Override
    public Project selectProjectByAnswerHeader(Long answerHeaderId) {
        return projectMapper.selectByAnswerHeaderId(answerHeaderId);
    }

    @Override
    public Project selectProjectByChoice(Long choiceId) {
        return projectMapper.selectByChoiceId(choiceId);
    }

    @Override
    public Result addProject(ProjectForm projectForm) {
        Project project = new Project();
        BeanUtils.copyProperties(projectForm,project);
        log.info("project:{}",project);
        User currentUser = userService.getCurrentUser();
        project.setCreatedBy(currentUser.getId());
        project.setUpdatedBy(currentUser.getId());
        if (insert(project)){
            return Result.success(project);
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public Result updatedProject(ProjectUpdateForm projectUpdateForm) {
        Project project = selectById(projectUpdateForm.getId());
        if (project==null){
            return Result.error(CodeMsg.PROJECT_NOT_EXIST);
        }
        if (project.getStatus()==1){
            return Result.error(CodeMsg.PROJECT_PROCESSING);
        }
        BeanUtils.copyProperties(projectUpdateForm,project);
        project.setUpdatedBy(userService.getCurrentUser().getId());
        if (update(project)){
            return Result.success(project);
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Project selectOfPackage(Long packageId) {
        return projectMapper.selectByPackageId(packageId);
    }
}
