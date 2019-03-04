package com.uchain.remarksystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uchain.remarksystem.VO.PackageInfoVO;
import com.uchain.remarksystem.VO.UserPackageVO;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.dao.PackageMapper;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.model.Package;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.DataService;
import com.uchain.remarksystem.service.PackageService;
import com.uchain.remarksystem.service.ProjectService;
import com.uchain.remarksystem.service.UserService;
import com.uchain.remarksystem.util.MyPageUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/remark/package")
@CrossOrigin
public class PackageController {

    @Autowired
    private PackageService packageService;
    @Autowired
    private DataService dataService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private PackageMapper packageMapper;
    @Autowired
    private UserService userService;

    @GetMapping("/grab")
    @ApiOperation("抓取数据操作,标注员")
    @RequireRole(role = RoleEnum.REMARK)
    public Object grabProjectPackage(Long projectId){
        if (userService.getCurrentUser().getRole()!=1){
            return Result.error(CodeMsg.NOT_BID_USER);
        }
        return packageService.grabPackage(projectId);
    }

    @GetMapping("/commitToCheck")
    @ApiOperation("提交至审核,标注员")
    @RequireRole(role = RoleEnum.REMARK)
    public Object commitToCheck(Long packageId){
        return packageService.commitToCheck(packageId);
    }

    @GetMapping("/commitToExam")
    @ApiOperation("提交至验收,质检员权限")
    @RequireRole(role = RoleEnum.INSPECTOR)
    public Object commitToExam(Long packageId){
        return packageService.commitToExam(packageId);
    }

    @GetMapping("/passExam")
    @ApiOperation("验收通过,验收员权限")
    @RequireRole(role = RoleEnum.EXAMINER)
    public Object passExam(Long packageId){
        return packageService.passExam(packageId);
    }

    /**
     * 检测当前用户参与的项目id和身份role
     * 从而查询对应的项目和状态的数据包
     * @return
     */
    @GetMapping("/allOfUser")
    @ApiOperation("查询当前用户抓取的所有数据包信息")
    @RequireRole(role = RoleEnum.REMARK)
    public Object allOfUser(Integer pageNum){
        if (pageNum==null||pageNum<1){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        User currentUser = userService.getCurrentUser();
        PageHelper.startPage(pageNum,MyPageUtil.packageNum);
        List<UserPackageVO> userPackageVOS = null;
        //如果是审核角色则查询参与的项目的所有审核数据包
        if (currentUser.getRole()>1){
            userPackageVOS = packageService.checkAllPackage(currentUser.getId());
        }else {
            //如果是标注员则只能查看到当前自己的数据包的状态
            userPackageVOS = packageService.userAllPackage(currentUser.getId());
        }
        PageInfo<UserPackageVO> packagePageInfo = new PageInfo<>(userPackageVOS);
        return Result.success(packagePageInfo);
    }

    @RequireRole(role = RoleEnum.ADMIN)
    @GetMapping("/allOfProject")
    @ApiOperation("查看指定数据包的进度,info:按项目id或用户名或项目名称来搜索")
    public Object allOfProject(Integer pageNum,String info){
        if (pageNum==null||pageNum<1){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        PageHelper.startPage(pageNum,MyPageUtil.packageNum);
        List<PackageInfoVO> packageInfoVOS = packageService.showPackageInfoVO(info);
        PageInfo<PackageInfoVO> packagePageInfo = new PageInfo<>(packageInfoVOS);
        return Result.success(packagePageInfo);
    }

    @GetMapping("/releasePackage")
    @ApiOperation("释放当前数据包还未标注的数据,管理员权限")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object releasePackage(Long packageId){
        if (packageId==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        return packageService.releasePackage(packageId);
    }

    @ApiOperation("打回标注数据包")
    @GetMapping("/repulseToRemark")
    @RequireRole(role = RoleEnum.INSPECTOR)
    public Object repulseToRemark(Long packageId){
        return packageService.repulseToRemark(packageId);
    }

    @ApiOperation("打回审核数据包")
    @GetMapping("/repulseToCheck")
    @RequireRole(role = RoleEnum.EXAMINER)
    public Object repulseToCheck(Long packageId){
        return packageService.repulseToCheck(packageId);
    }

}
