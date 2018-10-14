package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ProjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Project record);

    Project selectByPrimaryKey(Long id);

    List<Project> selectAll();

    int updateByPrimaryKey(Project record);

    List<Project> selectByRandom(@Param("info") String info);

    Project selectByHeaderId(Long headerId);

    Project selectByChoiceId(Long choiceId);

    Project selectByPackageId(Long packageId);

    Project selectByAnswerHeaderId(Long answerHeaderId);
}