package com.uchain.remarksystem.service;

import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.form.UploadForm;
import com.uchain.remarksystem.form.choice.ChoiceForm;
import com.uchain.remarksystem.form.choice.ChoiceUpdateForm;
import com.uchain.remarksystem.form.header.HeaderForm;
import com.uchain.remarksystem.form.header.HeaderUpdateForm;
import com.uchain.remarksystem.model.Choice;
import com.uchain.remarksystem.model.Header;
import com.uchain.remarksystem.result.Result;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public interface FormService {
    boolean insertHeader(Header header);
    boolean insertAnswerHeader(Header header);
    boolean insertChoice(Choice choice);
    boolean updateHeader(Header header);
    boolean updateAnswerHeader(Header header);
    boolean updateChoice(Choice choice);
    void deleteHeader(Long id);
    void deleteAnswerHeader(Long id);
    void deleteChoice(Long id);
    Header selectHeaderByProjectIdAndColumnNum(Long projectId,Integer columnNum);

    Header selectAnswerHeaderByProjectIdAndColumnNum(Long projectId, Integer columnNum);

    Header selectByHeaderId(Long headerId);
    Header selectByAnswerHeaderId(Long answerHeaderId);
    Choice selectByChoiceId(Long choiceId);
    List<Header> selectHeaderByProjectId(Long projectId);
    List<Header> selectAnswerHeaderByProjectId(Long projectId);

    List<Choice> selectChoiceByProjectId(Long projectId);
    //headerType:1:导表的header,2:回答的header
    CodeMsg addHeader(HeaderForm headerForm,Integer headerType);
    Result addHeaders(HeaderForm[] headerForms,Integer headerType);

    CodeMsg addChoice(ChoiceForm choiceForm);
    Result addChoices(ChoiceForm[] choiceForms);

    CodeMsg changeChoice(ChoiceUpdateForm choiceUpdateForm);

    Result changeChoices(ChoiceUpdateForm[] choiceUpdateForms);

    CodeMsg changeHeader(HeaderUpdateForm headerUpdateForm,Integer headerType);

    Result changeHeaders(HeaderUpdateForm[] headerUpdateForms,Integer headerType);

    Result uploadFile(UploadForm uploadForm);

    void download(Long projectId, HttpServletResponse response) throws IOException;

    Result getDownloadPath(HttpServletRequest request);

    boolean checkPath(HttpServletRequest request, String path);
}
