package com.uchain.remarksystem.security;

import com.alibaba.fastjson.JSON;
import com.uchain.remarksystem.enums.CodeMsg;
import com.uchain.remarksystem.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    private static final long serialVersionUID = -8970718410437077606L;

    /**
     * 拦截身份认证异常
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
//        if (authException instanceof BadCredentialsException){
//            response.sendError(401,"密码错误");
//        }
        //当访问时没有有效凭证时进行拦截返回401未认证
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=utf-8");
        Result result = Result.error(CodeMsg.AUTHENTICATION_ERROR);
        log.info("需要省份认证:{}",result);
        response.getWriter().append(JSON.toJSONString(result));
    }
}