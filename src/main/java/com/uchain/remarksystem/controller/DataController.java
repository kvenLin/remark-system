package com.uchain.remarksystem.controller;

import com.uchain.remarksystem.VO.PageInfoVO;
import com.uchain.remarksystem.VO.RowStatusVO;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.form.answer.AnswerForm;
import com.uchain.remarksystem.form.answer.AnswerStatusForm;
import com.uchain.remarksystem.form.answer.AnswerUpdateForm;
import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.model.Project;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.*;
import com.uchain.remarksystem.util.MyPageUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/data")
@RestController
@CrossOrigin
@Slf4j
public class DataController {

    @Autowired
    private DataService dataService;
    @Autowired
    private AnswerService answerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PackageService packageService;

    @GetMapping("/showOneData")
    @ApiOperation("展示一条数据进行标注")
    @RequireRole(role = RoleEnum.REMARK)
    public Object showOneData(Long packageId,Integer rowNum){
        User currentUser = userService.getCurrentUser();
        //判断当前数据包是否存在
        Package aPackage = packageService.selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }

        //检测权限和数据包状态是否匹配
        //0:未完成,1:审核状态,2:验收状态,3:审核打回,4:验收打回,5:通过状态
        Integer role = currentUser.getRole();
        Integer status = aPackage.getStatus();
        //验收员可以查看0,3,4
        if (role==1&&(status==1||status==2||status==5)){
            return Result.error(CodeMsg.STATUS_OVER_ROLE);
        }
        //质检员可以查看1
        if (role==2&&(status==0||status==2||status==3||status==4||status==5)){
            return Result.error(CodeMsg.STATUS_OVER_ROLE);
        }
        //验收员可以查看2
        if (role==3&&(status==1||status==3||status==4||status==5)){
            return Result.error(CodeMsg.STATUS_OVER_ROLE);
        }
        return dataService.showOneDataResultVO(packageId,rowNum);
    }

    //返回当前数据包的所有rowNum和每个答案的状态
    @GetMapping("/rowStatus")
    @ApiOperation("展示当前数据包的每条数据回答的状态")
    @RequireRole(role = RoleEnum.REMARK)
    public Object getAllRowNumStatus(Integer pageNum,Long packageId){
        if (pageNum==null||packageId==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        if (pageNum<1){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }

        User currentUser = userService.getCurrentUser();
        //判断当前数据包是否存在
        Package aPackage = packageService.selectById(packageId);
        if (aPackage==null){
            return Result.error(CodeMsg.PACKAGE_NOT_EXIST);
        }

        //检测权限和数据包状态是否匹配
        //0:未完成,1:审核状态,2:验收状态,3:审核打回,4:验收打回,5:通过状态
        Integer role = currentUser.getRole();
        Integer status = aPackage.getStatus();
        //验收员可以查看0,3,4
        if (role==1&&(status==1||status==2||status==5)){
            return Result.error(CodeMsg.STATUS_OVER_ROLE);
        }
        //质检员可以查看1
        if (role==2&&(status==0||status==2||status==3||status==4||status==5)){
            return Result.error(CodeMsg.STATUS_OVER_ROLE);
        }
        //验收员可以查看2
        if (role==3&&(status==1||status==3||status==4||status==5)){
            return Result.error(CodeMsg.STATUS_OVER_ROLE);
        }

        //判断是否是标注员,如果不是则按照项目的审核数量来获取数据状态
        Integer dataNum;
        if (role==1){
            dataNum = null;
        }else {
            Project project = projectService.selectOfPackage(packageId);
            dataNum = project.getCheckNum();
            log.info("checkNum:{}",dataNum);
        }
        List<RowStatusVO> packageRowStatus = dataService.getPackageRowStatus(packageId,dataNum);
        PageInfoVO myPageInfo = MyPageUtil.getMyPageInfo(packageRowStatus, pageNum);
        return Result.success(myPageInfo);
    }

    @PostMapping("/answerData")
    @ApiOperation("进行标注回答")
    @RequireRole(role = RoleEnum.REMARK)
    public Object answerData(@Valid @RequestBody AnswerForm answerForm){
        return dataService.answerOneData(answerForm);
    }

    @PostMapping("/updateAnswer")
    @ApiOperation("更新标注内容")
    @RequireRole(role = RoleEnum.REMARK)
    public Object updateAnswer(@Valid @RequestBody AnswerUpdateForm answerUpdateForm){
        return answerService.updateAnswer(answerUpdateForm);
    }

    @PostMapping("/changeAnswerStatus")
    @ApiOperation("更新回答的状态,1正确,2错误")
    @RequireRole(role = RoleEnum.INSPECTOR)
    public Object changeAnswerStatus(@Valid @RequestBody AnswerStatusForm answerStatusForm){
        return answerService.changeAnswerStatus(answerStatusForm);
    }
}
