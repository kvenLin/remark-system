package com.uchain.remarksystem.service;

import com.uchain.remarksystem.form.user.*;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.result.Result;

import java.io.IOException;
import java.util.List;

public interface UserService {
    boolean insert(User user);
    boolean update(User user);
    void delete(Long id);
    User selectByEmpNum(String empNum);
    User selectById(Long id);
    //按用户名或qq号或电话号码来查找用户
    List<User> selectByRandom(String info);
    List<User> selectByRole(Integer role);
    List<User> allUser();
    List<User> selectUserByProjectId(Long projectId);

    Result addUser(UserAddForm userAddForm);
    Result deleteUser(Long userId);

    Result updateUser(UserUpdateForm userUpdateForm);
    String sendVerifyCode(String clientIp) throws IOException;
    boolean checkVerifyCode(String clientIp,String code);

    Result register(String clientIp,UserRegisterForm userRegisterForm);
    Result login(String clientIp,LoginForm loginForm);

    User getCurrentUser();

    Result changePassword(ChangeForm changeForm,String clientIp);

    Result resetPassword(Long userId);
}
