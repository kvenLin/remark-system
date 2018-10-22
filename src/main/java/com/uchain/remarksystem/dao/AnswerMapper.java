package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.Answer;
import java.util.List;

public interface AnswerMapper {
    int deleteByPrimaryKey(Long id);

    void deleteByProject(Long id);

    int insert(Answer record);

    Answer selectByPrimaryKey(Long id);

    List<Answer> selectAll();

    int updateByPrimaryKey(Answer record);

    Answer selectByPackageAndRowNum(Long packageId, Integer rowNum);

    Answer selectByProjectAndRowNum(Long projectId, Integer rowNum);

    List<Answer> selectByPackageAndStatus(Long packageId, Integer status);
}