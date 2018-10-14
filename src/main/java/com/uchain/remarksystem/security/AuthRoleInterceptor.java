package com.uchain.remarksystem.security;


import com.alibaba.fastjson.JSON;
import com.uchain.remarksystem.annotation.RequireRole;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.result.Result;
import com.uchain.remarksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class AuthRoleInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private UserService userService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        String json = JSON.toJSONString(Result.error(CodeMsg.AUTHENTICATION_ERROR));
        User user = userService.getCurrentUser();
        //若当前用户为未认证用户则跳过权限验证,交给security做身份认证
        if (user==null) {
            return true;
        }
        log.info("...........执行权限验证........");
        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
            if (requireRole==null){
                return true;
            }
            //获取当前的需要的角色和用户拥有的角色
            Integer requireValue = requireRole.role().getValue();
            Integer userValue = user.getRole();
            log.info("requireValue:{},userValue:{}",requireValue,userValue);
            if (userValue>=requireValue){
                return true;
            }else {
                json = JSON.toJSONString(Result.error(CodeMsg.PERMISSION_DENNY));
                log.error("............权限不足...........");
            }
        }
        response.getWriter().append(json);
        return false;
    }

}
