package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.Header;

import java.util.List;

public interface HeaderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Header record);

    Header selectByPrimaryKey(Long id);

    List<Header> selectAll();

    int updateByPrimaryKey(Header record);

    Header selectByProjectIdColumnNum(Long projectId, Integer columnNum);

    List<Header> selectByProjectId(Long projectId);

    List<Header> selectLaterHeader(Long projectId,Integer columnNum);

    int deleteHeaderByProject(Long projectId);
}