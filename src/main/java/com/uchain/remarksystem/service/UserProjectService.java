package com.uchain.remarksystem.service;

import com.uchain.remarksystem.DTO.UserProjectDTO;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.form.userproject.UserProjectForm;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.model.UserProject;
import com.uchain.remarksystem.result.Result;

import java.util.List;

public interface UserProjectService {
    boolean insert(UserProject userProject);
    void deleteByUserProject(Long userId,Long projectId);
    UserProject select(Long userId,Long projectId);
    CodeMsg add(UserProjectForm userProjectForm);
    List<Project> selectProjectByUserId(Long userId);
    List<User> selectUserByProjectId(Long projectId);
    Result addUserProject(UserProjectDTO userProjectDTO);
}
