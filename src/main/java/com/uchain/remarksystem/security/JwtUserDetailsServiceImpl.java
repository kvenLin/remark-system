package com.uchain.remarksystem.security;

import com.uchain.remarksystem.enums.RoleEnum;
import com.uchain.remarksystem.model.User;
import com.uchain.remarksystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String empNum) throws UsernameNotFoundException {

        //从数据库查询username,如果不存在则抛出异常
        User user = userService.selectByEmpNum(empNum);
        if (user==null) {
            log.info("认证邮箱信息不存在");
            throw new UsernameNotFoundException(String.format(" user not exist with stuId ='%s'.", empNum));
        } else {
            //若存在则返回userDetails对象
            String role = RoleEnum.getRole(user.getRole());
            return new JwtUser(empNum,passwordEncoder.encode(user.getPassword()),role);
        }
    }
}
