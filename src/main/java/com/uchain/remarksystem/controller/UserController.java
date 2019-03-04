package com.uchain.remarksystem.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.form.user.UserAddForm;
import com.uchain.remarksystem.form.user.UserUpdateForm;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.UserService;
import com.uchain.remarksystem.util.MyPageUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/remark/user")
@CrossOrigin
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/add")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object add(@Valid @RequestBody UserAddForm userAddForm){
        return userService.addUser(userAddForm);
    }

    @GetMapping("/delete")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object delete(Long id){
        if (id==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        return userService.deleteUser(id);
    }

    @RequireRole(role = RoleEnum.ADMIN)
    @GetMapping("/all")
    @ApiOperation("查询所有用户信息")
    public Object all(Integer pageNum){
        if (pageNum==null||pageNum<=0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        PageHelper.startPage(pageNum,MyPageUtil.userNum);
        List<User> users = userService.allUser();
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return Result.success(userPageInfo);
    }

    @PostMapping("/update")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("更新用户角色信息")
    public Object update(@Valid @RequestBody UserUpdateForm userUpdateForm){
        return userService.updateUser(userUpdateForm);
    }

    @ApiOperation("按照用户姓名或者qq号或者电话号码和工号来搜索用户")
    @GetMapping("/selectByRandom")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object selectByRandom(Integer pageNum,String info){
        if (pageNum==null||pageNum<=0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        if (StringUtils.isEmpty(info)){
            return Result.success();
        }
        PageHelper.startPage(pageNum,MyPageUtil.userNum);
        List<User> users = userService.selectByRandom(info);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return Result.success(userPageInfo);
    }


    @ApiOperation("添加项目人员的搜索")
    @GetMapping("/selectAddByRandom")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object selectAddByRandom(Integer pageNum,String info){
        if (pageNum==null||pageNum<=0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        if (StringUtils.isEmpty(info)){
            return Result.success();
        }
        PageHelper.startPage(pageNum,MyPageUtil.selectAddUserNum);
        List<User> users = userService.selectByRandom(info);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return Result.success(userPageInfo);
    }


    @ApiOperation("按员工号进行搜索")
    @GetMapping("/selectByEmpNum")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object selectByEmpNum(String emp_num){
        //改成随机搜索是否需要分页,如果需要随机搜索前端处理
        User user = userService.selectByEmpNum(emp_num);
        if (user==null){
            return Result.error(CodeMsg.USER_NOT_EXIST);
        }
        return Result.success(user);
    }

    @ApiOperation("按角色查看用户,在查看未授权用户是可以用该接口")
    @GetMapping("/selectByRole")
    @RequireRole(role = RoleEnum.ADMIN)
    public Object selectByRole(Integer pageNum,Integer role){
        if (pageNum==null||pageNum<=0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        if (role==null||role<0||role>4){
            return Result.error(CodeMsg.ROLE_NOT_EXIST);
        }
        PageHelper.startPage(pageNum,MyPageUtil.userNum);
        List<User> users = userService.selectByRole(role);
        PageInfo<User> userPageInfo = new PageInfo<>(users);
        return Result.success(userPageInfo);
    }

    @GetMapping("/resetPassword")
    @RequireRole(role = RoleEnum.ADMIN)
    @ApiOperation("重置用户密码")
    public Object resetPassword(Long userId){
        if (userId==null){
            return Result.error(CodeMsg.PARAM_IS_NULL);
        }
        return userService.resetPassword(userId);
    }

}
