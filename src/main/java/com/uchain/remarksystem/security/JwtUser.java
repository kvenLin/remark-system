package com.uchain.remarksystem.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 * 默认密码为空字符串  , 并且设置为启用,没有锁定 . 没有过期.
 */
public class JwtUser implements UserDetails {

    private String empNum;
    private String password;
    private String role;

    public JwtUser(String empNum, String password, String role) {
            this.empNum = empNum;
            this.password = password;
            this.role = role;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<>();
        auths.add(new SimpleGrantedAuthority(role));
        return auths;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return empNum;
    }

    //默认有效账户
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //默认账户没有被锁
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //默认凭证有效
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //默认账户可用
    @Override
    public boolean isEnabled() {
        return true;
    }


}
