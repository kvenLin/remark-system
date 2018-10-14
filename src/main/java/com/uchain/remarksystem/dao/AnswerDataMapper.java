package com.uchain.remarksystem.dao;

import com.uchain.remarksystem.model.AnswerData;

import java.util.List;

public interface AnswerDataMapper {
    int deleteByPrimaryKey(Long id);

    void deleteByProject(Long id);

    int insert(AnswerData record);

    AnswerData selectByPrimaryKey(Long id);

    List<AnswerData> selectAll();

    int updateByPrimaryKey(AnswerData record);

    List<AnswerData> selectByProjectId(Long projectId);

    List<AnswerData> selectByPackageId(Long packageId);

    List<AnswerData> selectByPackageAndRowNum(Long packageId,Integer rowNum);
    //按照headerId升序来排列

    List<AnswerData> selectByProjectAndRowNum(Long projectId,Integer rowNum);

    AnswerData selectByAnswerHeaderIdAndRowNum(Long answerHeaderId, Integer rowNum);
}
