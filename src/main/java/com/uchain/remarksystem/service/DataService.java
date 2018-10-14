package com.uchain.remarksystem.service;

import com.uchain.remarksystem.VO.RowStatusVO;
import com.uchain.remarksystem.form.answer.AnswerForm;
import com.uchain.remarksystem.form.answer.AnswerUpdateForm;
import com.uchain.remarksystem.model.Data;
import com.uchain.remarksystem.result.Result;

import java.util.List;

public interface DataService {
    boolean insert(Data data);
    boolean update(Data data);
    List<Data> allProjectData(Long projectId);
    List<Data> allPackageData(Long packageId);
    List<Data> selectByProjectRowNum(Long projectId, Integer rowNum);

    int grabData(Long projectId,Long packageId, Integer dataNum);

    boolean checkPackageDataFinish(Long packageId);

    boolean checkProjectDataFinish(Long projectId);
    Result showOneDataResultVO(Long packageId, Integer rowNum);
    List<RowStatusVO> getPackageRowStatus(Long packageId,Integer dataNum);

    Result answerOneData(AnswerForm answerForm);
}
