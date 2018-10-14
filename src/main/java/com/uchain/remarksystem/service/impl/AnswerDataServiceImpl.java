package com.uchain.remarksystem.service.impl;

import com.uchain.remarksystem.DTO.AnswerDataDTO;
import com.uchain.remarksystem.DTO.AnswerDataUpdateDTO;
import com.uchain.remarksystem.dao.AnswerDataMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.exception.GlobalException;
import com.uchain.remarksystem.form.answer.AnswerDataForm;
import com.uchain.remarksystem.form.answer.AnswerDataUpdateForm;
import com.uchain.remarksystem.model.*;
import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class AnswerDataServiceImpl implements AnswerDataService {

    @Autowired
    private AnswerDataMapper answerDataMapper;
    @Autowired
    private FormService formService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserService userService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private DataService dataService;

    @Override
    public boolean insert(AnswerData answerData) {
        if (answerDataMapper.insert(answerData)==1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(AnswerData answerData) {
        if (answerDataMapper.updateByPrimaryKey(answerData)==1) {
            return true;
        }
        return false;
    }

    @Override
    public AnswerData selectByAnswerDataId(Long answerId){
      return answerDataMapper.selectByPrimaryKey(answerId);
    }

    @Override
    public List<AnswerData> allPackageAnswerData(Long packageId) {
        return answerDataMapper.selectByPackageId(packageId);
    }

    @Override
    public List<AnswerData> selectByProjectRowNum(Long projectId, Integer rowNum) {
        return answerDataMapper.selectByProjectAndRowNum(projectId,rowNum);
    }

    @Override
    public List<AnswerData> selectByPackageAndRowNum(Long packageId, Integer rowNum) {
        return answerDataMapper.selectByPackageAndRowNum(packageId,rowNum);
    }

    @Override
    public AnswerData selectByAnswerHeaderIdAndRowNum(Long answerHeaderId, Integer rowNum) {
        return answerDataMapper.selectByAnswerHeaderIdAndRowNum(answerHeaderId,rowNum);
    }

    @Transactional
    @Override
    public Result addAnswerData(AnswerDataDTO answerDataDTO) {
        Answer answer1 = answerService.selectByPackageAndRowNum(answerDataDTO.getPackageId(), answerDataDTO.getRowNum());
        if (answer1!=null){
            return Result.error(CodeMsg.ANSWER_ALREADY_EXIST);
        }

        Package aPackage = packageService.selectById(answerDataDTO.getPackageId());
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        List<AnswerData> answerDataList = selectByPackageAndRowNum(answerDataDTO.getPackageId(), answerDataDTO.getRowNum());
        if (answerDataList.size()!=0){
            return Result.error(CodeMsg.ANSWER_ALREADY_EXIST);
        }
        AnswerData answerData = new AnswerData();
        BeanUtils.copyProperties(answerDataDTO,answerData);
        for (AnswerDataForm answerDataForm : answerDataDTO.getAnswerDataForms()) {
            Header header = formService.selectByAnswerHeaderId(answerDataForm.getAnswerHeaderId());
            //若回答的header不存在则抛出异常
            if (header==null){
                throw new GlobalException(CodeMsg.ANSWER_HEADER_NOT_EXIST);
            }
            if (StringUtils.isEmpty(answerDataForm.getContent())){
//                throw new GlobalException(CodeMsg.CONTENT_CAN_NOT_NULL);
                answerDataForm.setContent("");
            }
            AnswerData answerData1 = selectByAnswerHeaderIdAndRowNum(answerDataForm.getAnswerHeaderId(), answerDataDTO.getRowNum());
            if (answerData1!=null){
                throw new GlobalException(CodeMsg.ANSWER_ALREADY_EXIST);
            }
            answerData.setId(null);
            answerData.setAnswerHeaderId(answerDataForm.getAnswerHeaderId());
            answerData.setContent(answerDataForm.getContent());
            answerData.setProjectId(aPackage.getProjectId());
            if (!insert(answerData)) {
                throw new GlobalException(CodeMsg.ADD_ERROR);
            }
        }
        //添加数据的回答状态
        Answer answer = new Answer();
        BeanUtils.copyProperties(answerDataDTO,answer);
        User currentUser = userService.getCurrentUser();
        answer.setUserId(currentUser.getId());
        if (!answerService.insert(answer)) {
            log.error("添加回答异常");
            throw new GlobalException(CodeMsg.ADD_ERROR);
        }
        List<Data> dataList = dataService.selectByProjectRowNum(aPackage.getProjectId(), answerDataDTO.getRowNum());
        for (Data data : dataList) {
            data.setAnswerId(answer.getId());
            if (!dataService.update(data)){
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
        }
        return Result.success();
    }

    @Transactional
    @Override
    public Result updateAnswerData(AnswerDataUpdateDTO answerDataUpdateDTO) {
        AnswerData temp = new AnswerData();
        for (AnswerDataUpdateForm answerDataUpdateForm : answerDataUpdateDTO.getAnswerDataUpdateForms()) {
            AnswerData answerData = selectByAnswerDataId(answerDataUpdateForm.getAnswerDataId());
            //判断当前的answerDataId是否存在
            if (answerData==null){
                throw new GlobalException(CodeMsg.ANSWER_DATA_NO_EXIST);
            }
            answerData.setContent(answerDataUpdateForm.getContent());
            if (!update(answerData)) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
            temp = answerData;
        }
        //TODO,更新数据后更改回答的状态
        Answer answer = answerService.selectByPackageAndRowNum(temp.getPackageId(), temp.getRowNum());
        //如果是标注员则将更新操作后的回答状态改成0:未审核
        if (userService.getCurrentUser().getRole()==1){
            answer.setStatus(0);
        }else {
            //如果是质检员或者是验收员则更新后将回答状态改为1:正确
            answer.setStatus(1);
        }
        if (!answerService.update(answer)) {
            throw new GlobalException(CodeMsg.UPDATE_ERROR);
        }
        return Result.success();
    }
}
