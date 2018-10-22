package com.uchain.remarksystem.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.uchain.remarksystem.VO.RowStatusVO;
import com.uchain.remarksystem.dao.AnswerMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.form.answer.AnswerStatusForm;
import com.uchain.remarksystem.form.answer.AnswerUpdateForm;
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

import java.util.List;

@Service
@Slf4j
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerMapper answerMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private DataService dataService;

    @Override
    public Answer selectByPackageAndRowNum(Long packageId, Integer rowNum) {
        return answerMapper.selectByPackageAndRowNum(packageId,rowNum);
    }

    @Override
    public boolean insert(Answer answer) {
        if (answerMapper.insert(answer)==1){
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Answer answer) {
        if (answerMapper.updateByPrimaryKey(answer)==1){
            //更新缓存的状态
            List rowStatusVOS = redisService.get(RowStatusKey.getByPackageId, "" + answer.getPackageId(), List.class);
            if (rowStatusVOS!=null){
                for (int i = 0; i < rowStatusVOS.size(); i++) {
                    RowStatusVO rowStatusVO = JSONObject.parseObject(rowStatusVOS.get(i).toString(), RowStatusVO.class);
                    if (rowStatusVO.getRowNum()==answer.getRowNum()) {
                        rowStatusVO.setStatus(answer.getStatus());
                        rowStatusVOS.set(i,JSONObject.parseObject(rowStatusVO.toString()));
                        break;
                    }
                }
                redisService.set(RowStatusKey.getByPackageId,""+answer.getPackageId(),rowStatusVOS);
            }
            return true;
        }
        return false;
    }

    @Override
    public Answer selectById(Long answerId) {
        return answerMapper.selectByPrimaryKey(answerId);
    }

    @Override
    public Answer selectByProjectAndRowNum(Long projectId, Integer rowNum) {
        return answerMapper.selectByProjectAndRowNum(projectId,rowNum);
    }

    @Override
    public Result updateAnswer(AnswerUpdateForm answerUpdateForm) {
        User currentUser = userService.getCurrentUser();
        //检测当前的answer是否是存在
        Answer answer = selectById(answerUpdateForm.getAnswerId());
        if (answer==null){
            return Result.error(CodeMsg.ANSWER_NO_EXIST);
        }
        //判断当前的数据包是否是可以进行修改
        Package aPackage = packageService.selectByAnswerId(answerUpdateForm.getAnswerId());
        //标注员不能修改数据包为审核状态,验收状态或完成状态的数据
        if (currentUser.getRole()==1&&(aPackage.getStatus()==1
                ||aPackage.getStatus()==2||aPackage.getStatus()==5)){
            return Result.error(CodeMsg.PACKAGE_COMMIT_TO_CHECK);
        }
        answer.setChoiceId(answerUpdateForm.getChoiceId());
        //根据当前修改用户的角色来更新回答的状态
        if (currentUser.getRole()>1){
            answer.setStatus(1);
        }else {
            answer.setStatus(0);
        }
        if (update(answer)) {
            return Result.success(answer);
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    @Transactional
    public Result changeAnswerStatus(AnswerStatusForm answerStatusForm) {
        Answer answer = selectById(answerStatusForm.getAnswerId());
        if (answer==null){
            return Result.error(CodeMsg.ANSWER_NO_EXIST);
        }
        Package aPackage = packageService.selectById(answer.getPackageId());
        User currentUser = userService.getCurrentUser();
        UserProject userProject = userProjectService.select(currentUser.getId(), aPackage.getProjectId());
        if (userProject==null){
            return Result.error(CodeMsg.PERMISSION_DENNY);
        }
        answer.setStatus(answerStatusForm.getStatus());
        if (update(answer)) {
            return Result.success(answer);
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public List<Answer> selectByPackageAndStatus(Long packageId, Integer status) {
        return answerMapper.selectByPackageAndStatus(packageId,status);
    }

}

