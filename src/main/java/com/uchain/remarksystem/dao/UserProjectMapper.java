package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.model.UserProject;
import java.util.List;

public interface UserProjectMapper {
    int deleteByPrimaryKey(Long id);

    void deleteByProject(Long id);

    int insert(UserProject record);

    UserProject selectByPrimaryKey(Long id);

    List<UserProject> selectAll();

    int updateByPrimaryKey(UserProject record);

    UserProject selectByUserIdAndProjectId(Long userId, Long projectId);

    List<Project> selectProjectByUserId(Long userId);
}