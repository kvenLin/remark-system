package com.uchain.remarksystem.controller;

import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.form.user.LoginForm;
import com.uchain.remarksystem.form.user.ChangeForm;
import com.uchain.remarksystem.form.user.UserRegisterForm;
import com.uchain.remarksystem.redis.RedisService;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.UserService;
import com.uchain.remarksystem.util.ClientUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/remark/anon")
@CrossOrigin
public class AnonController implements InitializingBean {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisService redisService;

    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginForm loginForm,HttpServletRequest request){
        String ip = ClientUtil.getClientIpAddress(request);
        return userService.login(ip,loginForm);
    }

    @PostMapping("/register")
    public Object register(@Valid @RequestBody UserRegisterForm userRegisterForm,HttpServletRequest request){
        String ipAddress = ClientUtil.getClientIpAddress(request);
        return userService.register(ipAddress,userRegisterForm);
    }

    @GetMapping("/sendVerifyCode")
    public Object sendVerifyCode(HttpServletRequest request){
        String ipAddress = ClientUtil.getClientIpAddress(request);
        String verifyCode = null;
        try {
            verifyCode = userService.sendVerifyCode(ipAddress);
        } catch (IOException e) {
            return Result.error(CodeMsg.SEND_CODE_ERROR);
        }

        return Result.success(verifyCode);
    }

    @PostMapping("/changePassword")
    @ApiOperation("更改密码")
    public Object changePassword(@Valid @RequestBody ChangeForm changeForm,HttpServletRequest request){
        return userService.changePassword(changeForm,ClientUtil.getClientIpAddress(request));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        redisService.flush();
        userService.sendVerifyCode("creationForFirst");

    }
}
