package com.uchain.remarksystem.service.impl;

import com.uchain.remarksystem.VO.PackageInfoVO;
import com.uchain.remarksystem.VO.UserPackageVO;
import com.uchain.remarksystem.dao.DataMapper;
import com.uchain.remarksystem.dao.PackageMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.exception.GlobalException;
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

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PackageServiceImpl implements PackageService {

    @Autowired
    private UserService userService;
    @Autowired
    private PackageMapper packageMapper;
    @Autowired
    private DataService dataService;
    @Autowired
    private DataMapper dataMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserProjectService userProjectService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private AnswerService answerService;


    @Override
    public boolean insert(Package aPackage) {
        if (packageMapper.insert(aPackage)==1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Package aPackage) {
        if (packageMapper.updateByPrimaryKey(aPackage)==1){
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long packageId) {
        packageMapper.deleteByPrimaryKey(packageId);
    }

    @Override
    public Package selectById(Long packageId) {
        return packageMapper.selectByPrimaryKey(packageId);
    }

    @Override
    public List<Package> selectByUserAndProject(Long userId, Long projectId) {
        return packageMapper.selectByUserIdAndProjectId(userId,projectId);
    }

    @Override
    public Package selectByAnswerId(Long answerId) {
        return packageMapper.selectByAnswerId(answerId);
    }

    @Override
    public List<Package> projectAllPackage(Long projectId) {
        return packageMapper.selectByProjectId(projectId);
    }

    @Override
    public List<UserPackageVO> userAllPackage(Long userId) {
        return packageMapper.selectByUserId(userId);
    }

    @Override
    public List<UserPackageVO> checkAllPackage(Long userId) {
        return packageMapper.selectByUserJoin(userId);
    }

    @Override
    public List<PackageInfoVO> showPackageInfoVO(String info) {
        return packageMapper.selectPackageVOByProjectId(info);
    }

    @Override
    public List<Package> selectFinishByProject(Long projectId) {
        return packageMapper.selectByStatusAndProject(5,projectId);
    }

    /**
     * 根据项目的设置抓取指定的数量的数据包
     * @param projectId
     * @return
     */
    @Override
    @Transactional
    public Result grabPackage(Long projectId) {
        //判断当前用户是否参与了该项目
        User currentUser = userService.getCurrentUser();
        if (userProjectService.select(currentUser.getId(), projectId)==null) {
            return Result.error(CodeMsg.GRAB_PERMISSION_DENY);
        }
        //判断当前是否有对应项目未完成的数据包,有则无法进行抓取数据
        List<Package> packages = selectUnFinishPackage(currentUser.getId(), projectId);
        if (packages.size()!=0){
            return Result.error(CodeMsg.ONE_PACKAGE_UN_FINISH);
        }
        //判断当前项目状态,若未启动则进行启动,若已完成则返回无法进行抓取
        Project project = projectService.selectById(projectId);
        if (project.getStatus()==2){
            return Result.error(CodeMsg.PROJECT_FINISHED);
        }
        //若项目为开始则设置状态为开始,默认从抓取第一次数据项目进行启动
        if (project.getStatus()==0){
            project.setStatus(1);
            projectService.update(project);
        }
        //判断数据包抓取的结果
        Package aPackage = new Package();
        aPackage.setProjectId(projectId);
        aPackage.setStartTime(new Date());
        aPackage.setUpdateTime(new Date());
        aPackage.setUserId(currentUser.getId());
        if (insert(aPackage)) {
            Integer dataNum = project.getPackageNum();
            int grabDataNum = dataService.grabData(projectId, aPackage.getId(), dataNum);
            if (grabDataNum==0) {
                throw new GlobalException(CodeMsg.DATA_CLEAN);
            }
            return Result.success(aPackage);
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public List<Package> selectUnFinishPackage(Long userId, Long projectId) {
        return packageMapper.selectUnFinishPackage(userId,projectId);
    }

    /**
     * 操作数据包状态,
     * 0:未完成,
     * 1:审核状态,
     * 2:验收状态,
     * 3:审核打回,
     * 4:验收打回,
     * 5:通过状态
     * @param packageId
     * @param status
     * @return
     */
    @Override
    public Result changeStatus(Long packageId, Integer status) {
        //检测当前package是否存在
        Package aPackage = selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        aPackage.setStatus(status);
        if (update(aPackage)) {
            redisService.delete(RowStatusKey.getByPackageId,""+packageId);
            return Result.success(aPackage);
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result commitToCheck(Long packageId) {
        Package aPackage = selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        //检测当前数据包是否标注完成
        if (dataService.checkPackageDataFinish(packageId)) {
            //判断当前的数据包中是否有错误的回答,若有则无标识没有进行修改则不能进行提交至验收员
            List<Answer> answers = answerService.selectByPackageAndStatus(packageId,2);
            log.info("answers:{}",answers.toString());
            if (answers.size()!=0){
                return Result.error(CodeMsg.HAS_WRONG_ANSWER_TO_CHANGE);
            }
            return changeStatus(packageId,1);
        }
        return Result.error(CodeMsg.DATA_ANSWER_NOT_FINISH);
    }

    @Override
    public Result commitToExam(Long packageId) {
        Package aPackage = selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        //检测当前数据包是否是1审核状态
        if (aPackage.getStatus()!=1&&aPackage.getStatus()!=4){
            return Result.error(CodeMsg.PACKAGE_STATUS_ERROR);
        }
        //判断当前的数据包中是否有错误的回答,若有则不能提交至验收员
        List<Answer> answers = answerService.selectByPackageAndStatus(packageId, 2);
        if (answers.size()!=0){
            return Result.error(CodeMsg.HAS_WRONG_ANSWER_TO_CHANGE);
        }
        return changeStatus(packageId,2);
    }

    @Transactional
    @Override
    public Result passExam(Long packageId) {
        Package aPackage = selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        Project project = projectService.selectById(aPackage.getProjectId());
        //检测当前数据包的状态是否是2审核验收状态
        if (aPackage.getStatus()!=2){
            return Result.error(CodeMsg.PACKAGE_STATUS_ERROR);
        }
        //判断当前的数据包中是否有错误的回答,若有则不能验收通过
        List<Answer> answers = answerService.selectByPackageAndStatus(packageId, 2);
        if (answers.size()!=0){
            return Result.error(CodeMsg.HAS_WRONG_ANSWER_TO_CHANGE);
        }
        Result result = changeStatus(packageId, 5);
        if (result.getCode()!=0){
            return result;
        }

        //判断是否还有未完成的数据,若无则更改项目状态为结束
        if (dataService.checkProjectDataFinish(project.getId())){
            project.setStatus(2);
            if (!projectService.update(project)) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
        }

        return Result.success(aPackage);
    }

    @Override
    public Result repulseToRemark(Long packageId) {
        Package aPackage = selectById(packageId);

        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        if (aPackage.getStatus()!=1&&aPackage.getStatus()!=4) {
            return Result.error(CodeMsg.PACKAGE_STATUS_ERROR);
        }
        return changeStatus(packageId,3);
    }

    @Override
    public Result repulseToCheck(Long packageId) {
        Package aPackage = selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        if (aPackage.getStatus()!=2){
            return Result.error(CodeMsg.PACKAGE_STATUS_ERROR);
        }
        return changeStatus(packageId,4);
    }

    @Transactional
    @Override
    public Result releasePackage(Long packageId) {
        Package aPackage = selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }
        if (aPackage.getStatus()!=0){
            return Result.error(CodeMsg.RELEASE_PACKAGE_STATUS_ERROR);
        }
        List<Data> dataList = dataMapper.selectPackageNoAnswerData(packageId);
        for (Data data : dataList) {
            data.setPackageId(null);
            if (!dataService.update(data)){
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
        }
        List<Data> dataList1 = dataMapper.selectPackageAnswerData(packageId);
        if (dataList1.size()==0){
            delete(packageId);
            return Result.success();
        }
        if (aPackage.getStatus()==0){
            aPackage.setStatus(1);
            if (!update(aPackage)) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
        }
        return Result.success();
    }
}
