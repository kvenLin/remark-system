package com.uchain.remarksystem.service.impl;

import com.uchain.remarksystem.VO.AnswerDataVO;
import com.uchain.remarksystem.VO.DataResultVO;
import com.uchain.remarksystem.VO.DataVO;
import com.uchain.remarksystem.VO.RowStatusVO;
import com.uchain.remarksystem.dao.DataMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.exception.GlobalException;
import com.uchain.remarksystem.form.answer.AnswerForm;
import com.uchain.remarksystem.model.*;
import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.redis.RedisService;
import com.uchain.remarksystem.redis.RowStatusKey;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DataServiceImpl implements DataService {

    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private AnswerDataService answerDataService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private FormService formService;
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean insert(Data data) {
        if (dataMapper.insert(data)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Data data) {
        if (dataMapper.updateByPrimaryKey(data)==1) {
            return true;
        }
        return false;
    }

    @Override
    public List<Data> allProjectData(Long projectId) {
        return dataMapper.selectByProjectId(projectId);
    }

    @Override
    public List<Data> allPackageData(Long packageId) {
        return dataMapper.selectByPackageId(packageId);
    }

    @Override
    public List<Data> selectByProjectRowNum(Long projectId, Integer rowNum) {
        return dataMapper.selectByProjectAndRowNum(projectId,rowNum);
    }

    @Override
    public int grabData(Long projectId,Long packageId, Integer dataNum) {
        int re = 0;
        try {
            re = dataMapper.grabProjectDataRandom(projectId,packageId,dataNum);
        }catch (Exception e){
            throw new GlobalException(CodeMsg.DATA_GRAB_ERROR);
        }
        return re;
    }

    @Override
    public boolean checkPackageDataFinish(Long packageId) {
        List<Data> data = dataMapper.selectPackageNoAnswerData(packageId);
        if (data.size()==0){
            return true;
        }
        return false;
    }

    @Override
    public boolean checkProjectDataFinish(Long projectId){
        List<Data> data = dataMapper.selectProjectNoAnswerData(projectId);
        if (data.size()==0){
            return true;
        }
        return false;
    }

    @Override
    public Result showOneDataResultVO(Long packageId, Integer rowNum) {
        Package aPackage = packageService.selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        Project project = projectService.selectById(aPackage.getProjectId());

        Answer answer = answerService.selectByPackageAndRowNum(packageId,rowNum);

        List<Data> dataList = dataMapper.selectByPackageAndRowNum(packageId,rowNum);
        List<DataVO> dataVOS = new ArrayList<>();
        for (Data data : dataList) {
            Header header = formService.selectByHeaderId(data.getHeaderId());
            dataVOS.add(new DataVO(header,data));
        }
        //设置返回的dataVO数据格式
        DataResultVO dataResultVO = new DataResultVO();
        dataResultVO.setDataVOS(dataVOS);
        dataResultVO.setAnswer(answer);
        //1.有文本则说明是answerData的形式进行回答
        if (project.getHasText()==1){
            List<Header> headers = formService.selectAnswerHeaderByProjectId(project.getId());
            List<AnswerDataVO> answerDataVOS = new ArrayList<>();
            for ( Header header : headers) {
                AnswerData answerData = answerDataService.selectByAnswerHeaderIdAndRowNum(header.getId(), rowNum);
                answerDataVOS.add(new AnswerDataVO(header,answerData));
            }
            dataResultVO.setChoiceOrAnswerDataVOS(answerDataVOS);
        }else {
            //2.没有文本则表示是选择形式的回答
            List<Choice> choices = formService.selectChoiceByProjectId(project.getId());
            dataResultVO.setChoiceOrAnswerDataVOS(choices);
        }
        dataResultVO.setWordsNum(project.getWordsNum());
        dataResultVO.setHasText(project.getHasText());
        return Result.success(dataResultVO);
    }

    @Override
    public List<RowStatusVO> getPackageRowStatus(Long packageId,Integer dataNum) {
        List<RowStatusVO> rowStatusVOS;
        if (dataNum!=null){
             rowStatusVOS = redisService.get(RowStatusKey.getByPackageId, "" + packageId, List.class);
            if (rowStatusVOS!=null){
                return rowStatusVOS;
            }
            rowStatusVOS = dataMapper.selectRowStatus(packageId, dataNum);
            if (rowStatusVOS.size()!=0){
                redisService.set(RowStatusKey.getByPackageId,""+packageId,rowStatusVOS);
            }
            return rowStatusVOS;
        }else {
            rowStatusVOS = dataMapper.selectRowStatus(packageId,dataNum);
        }
        return rowStatusVOS;
    }


    @Transactional
    @Override
    public Result answerOneData(AnswerForm answerForm) {
        Answer answer1 = answerService.selectByPackageAndRowNum(answerForm.getPackageId(), answerForm.getRowNum());
        if (answer1!=null){
            return Result.error(CodeMsg.ANSWER_ALREADY_EXIST);
        }

        Package aPackage = packageService.selectById(answerForm.getPackageId());
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        Choice choice = formService.selectByChoiceId(answerForm.getChoiceId());
        if (choice==null){
            return Result.error(CodeMsg.CHOICE_NOT_EXIST);
        }
        if (choice.getProjectId()!=aPackage.getProjectId()){
            return Result.error(CodeMsg.CHOICE_NOT_IN_PROJECT);
        }
        List<Data> dataList = dataMapper.selectByPackageAndRowNum(aPackage.getId(), answerForm.getRowNum());
        if (dataList.size()==0){
            return Result.error(CodeMsg.DATA_NOT_EXIST);
        }
        Answer answer = new Answer();
        answer.setPackageId(answerForm.getPackageId());
        answer.setRowNum(answerForm.getRowNum());
        answer.setChoiceId(answerForm.getChoiceId());
        answer.setUserId(userService.getCurrentUser().getId());
        if (!answerService.insert(answer)) {
            throw new GlobalException(CodeMsg.ADD_ERROR);
        }
        for (Data data : dataList) {
            data.setAnswerId(answer.getId());
            if (!update(data)){
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
        }
        return Result.success(answer);
    }


}
