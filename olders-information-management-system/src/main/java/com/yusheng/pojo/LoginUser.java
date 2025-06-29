// 新建文件: src/main/java/com/yusheng/pojo/LoginUser.java
package com.yusheng.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class LoginUser implements UserDetails {

    // 使用一个泛型来存储真实的用户对象 (Admin, CareWorker, Elder)
    private Object user;

    // 存储用户的权限信息
    private List<String> permissions;

    // 存储转换后的GrantedAuthority对象
    @JsonIgnore // 这个字段不需要序列化
    private List<GrantedAuthority> authorities;

    public LoginUser(Object user, List<String> permissions) {
        this.user = user;
        this.permissions = permissions;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 如果 authorities 尚未计算，则进行计算
        if (authorities == null) {
            authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return authorities;
    }

    // 从真实的用户对象中获取密码 (如果需要的话)
    @Override
    public String getPassword() {
        if (user instanceof Admin) {
            return ((Admin) user).getPassword();
        }
        // 为其他用户类型添加类似逻辑
        // if (user instanceof CareWorker) { ... }
        return null;
    }

    // 从真实的用户对象中获取用户名
    @Override
    public String getUsername() {
        if (user instanceof Admin) {
            return ((Admin) user).getAccount();
        }
        // 为其他用户类型添加类似逻辑
        // if (user instanceof CareWorker) { ... }
        return null;
    }

    // 提供一个获取真实用户对象的方法
    public Object getPrincipalUser() {
        return user;
    }


    // 以下方法可以根据需要实现，通常保持默认的 true 即可
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}