package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(User record);

    User selectByPrimaryKey(Long id);

    List<User> selectAll();

    int updateByPrimaryKey(User record);

    User selectByEmpNum(String empNum);

    List<User> selectByRandom(@Param("info") String info);

    List<User> selectByRole(Integer role);

    List<User> selectUserByProjectId(Long projectId);
}