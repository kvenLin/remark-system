package com.uchain.remarksystem.service.impl;

import com.sun.org.apache.regexp.internal.RE;
import com.uchain.remarksystem.DTO.UserProjectDTO;
import com.uchain.remarksystem.dao.UserProjectMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.exception.GlobalException;
import com.uchain.remarksystem.form.userproject.UserProjectForm;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.model.UserProject;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.ProjectService;
import com.uchain.remarksystem.service.UserProjectService;
import com.uchain.remarksystem.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserProjectServiceImpl implements UserProjectService {

    @Autowired
    private UserProjectMapper userProjectMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;

    @Override
    public boolean insert(UserProject userProject) {
        if (userProjectMapper.insert(userProject)==1){
            return true;
        }
        return false;
    }

    @Override
    public void deleteByUserProject(Long userId,Long projectId) {
        //若不存在则直接返回
        UserProject userProject = select(userId, projectId);
        if (userProject==null){
            return;
        }
        userProjectMapper.deleteByPrimaryKey(userProject.getId());
    }

    @Override
    public UserProject select(Long userId, Long projectId) {
        return userProjectMapper.selectByUserIdAndProjectId(userId, projectId);
    }

    @Override
    public CodeMsg add(UserProjectForm userProjectForm) {
        UserProject userProject = select(userProjectForm.getUserId(), userProjectForm.getProjectId());
        if (userProject!=null){
            return CodeMsg.USER_PROJECT_EXIST;
        }
        User user = userService.selectById(userProjectForm.getUserId());
        if (user==null){
            return CodeMsg.USER_NOT_EXIST;
        }
        if (user.getRole()==4||user.getRole()==0){
            return CodeMsg.ADD_ADMIN_UNCHECK_ERROR;
        }
        if (projectService.selectById(userProjectForm.getProjectId())==null){
            return CodeMsg.PROJECT_NOT_EXIST;
        }
        userProject = new UserProject();
        BeanUtils.copyProperties(userProjectForm,userProject);
        if (insert(userProject)) {
            return null;
        }
        return CodeMsg.ADD_ERROR;
    }

    @Override
    public List<Project> selectProjectByUserId(Long userId) {
        return userProjectMapper.selectProjectByUserId(userId);
    }

    @Override
    public List<User> selectUserByProjectId(Long projectId) {
        return userService.selectUserByProjectId(projectId);
    }

    @Override
    @Transactional
    public Result addUserProject(UserProjectDTO userProjectDTO) {
        if (userProjectDTO.getUserProjectForms().length==0){
            return Result.error(CodeMsg.CHOOSE_USER_TO_ADD);
        }
        for (UserProjectForm userProjectForm : userProjectDTO.getUserProjectForms()) {
            CodeMsg codeMsg = add(userProjectForm);
            if (codeMsg!=null){
                throw new GlobalException(codeMsg);
            }
        }
        return Result.success();
    }

}
