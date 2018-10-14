package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.Header;

import java.util.List;


public interface AnswerHeaderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Header record);

    Header selectByPrimaryKey(Long id);

    List<Header> selectAll();

    int updateByPrimaryKey(Header record);

    Header selectByProjectIdColumnNum(Long projectId,Integer columnNum);

    List<Header> selectByProjectId(Long projectId);

    List<Header> selectLaterAnswerHeader(Long projectId,Integer columnNum);

    int deleteAnswerHeaderByProject(Long projectId);

}
