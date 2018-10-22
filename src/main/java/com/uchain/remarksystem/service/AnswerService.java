package com.uchain.remarksystem.service;


import com.uchain.remarksystem.form.answer.AnswerStatusForm;
import com.uchain.remarksystem.form.answer.AnswerUpdateForm;
import com.uchain.remarksystem.model.Answer;
import com.uchain.remarksystem.result.Result;

import java.util.List;

public interface AnswerService {
    boolean insert(Answer answer);
    boolean update(Answer answer);
    Answer selectById(Long answerId);
    Answer selectByPackageAndRowNum(Long packageId, Integer rowNum);
    Answer selectByProjectAndRowNum(Long projectId, Integer rowNum);
    Result updateAnswer(AnswerUpdateForm answerUpdateForm);
    Result changeAnswerStatus(AnswerStatusForm answerStatusForm);
    List<Answer> selectByPackageAndStatus(Long packageId, Integer status);
}
