// 文件路径: backend/src/main/java/com/yusheng/service/impl/UserDetailsServiceImpl.java
package com.yusheng.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这个方法在我们的新流程中不会被调用，因为JWT过滤器已经处理了认证。
        // 我们保留它只是为了满足Spring Security的配置要求。
        throw new UsernameNotFoundException("此方法不应被调用，认证由JWT过滤器处理。");
    }
}