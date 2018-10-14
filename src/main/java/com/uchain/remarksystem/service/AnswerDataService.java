package com.uchain.remarksystem.service;

import com.uchain.remarksystem.DTO.AnswerDataDTO;
import com.uchain.remarksystem.DTO.AnswerDataUpdateDTO;
import com.uchain.remarksystem.model.AnswerData;
import com.uchain.remarksystem.result.Result;

import java.util.List;

public interface AnswerDataService {
    boolean insert(AnswerData answerData);
    boolean update(AnswerData answerData);
    AnswerData selectByAnswerDataId(Long answerId);
    List<AnswerData> allPackageAnswerData(Long packageId);
    List<AnswerData> selectByProjectRowNum(Long projectId, Integer rowNum);
    List<AnswerData> selectByPackageAndRowNum(Long packageId,Integer rowNum);
    AnswerData selectByAnswerHeaderIdAndRowNum(Long answerHeaderId,Integer rowNum);
    Result addAnswerData(AnswerDataDTO answerDataDTO);
    Result updateAnswerData(AnswerDataUpdateDTO answerDataUpdateDTO);
}
