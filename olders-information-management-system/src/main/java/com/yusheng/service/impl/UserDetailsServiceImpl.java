// 文件路径: src/main/java/com/yusheng/service/UserDetailsServiceImpl.java
package com.yusheng.service.impl;

import com.yusheng.mapper.AdminMapper;
import com.yusheng.mapper.CareWorkersSelfMapper;
import com.yusheng.mapper.ElderSelfMapper;
import com.yusheng.pojo.Admin;
import com.yusheng.pojo.CareWorker;
import com.yusheng.pojo.Elder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private CareWorkersSelfMapper careWorkersSelfMapper;
    @Autowired
    private ElderSelfMapper elderSelfMapper;

    /**
     * Spring Security 调用此方法来获取用户信息进行认证和授权。
     * @param username 在我们的系统中，这实际上是 account
     * @return UserDetails 对象，包含了用户名、密码（虽然不用）、权限等信息
     * @throws UsernameNotFoundException 如果找不到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 策略：依次在三张表中查找该账户

        // 1. 查找管理员表
        Admin queryAdmin = new Admin();
        queryAdmin.setAccount(username);
        Admin admin = adminMapper.selectByUsernameAndPassword(queryAdmin); // 这里密码用null或空字符串，因为我们只根据账号查
        if (admin != null) {
            // 找到了，封装成 UserDetails 返回
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_admin"));
            return new User(admin.getAccount(), admin.getPassword(), authorities);
        }

        // 2. 查找护工表
        CareWorker queryCareWorker = new CareWorker();
        queryCareWorker.setAccount(username);
        CareWorker careWorker = careWorkersSelfMapper.findFullByCredentials(queryCareWorker); // 假设密码验证在Service层
        if (careWorker != null) {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_careworker"));
            return new User(careWorker.getAccount(), careWorker.getPassword(), authorities);
        }

        // 3. 查找老人表
        Elder queryElder = new Elder();
        queryElder.setAccount(username);
        Elder elder = elderSelfMapper.findFullByCredentials(queryElder);
        if (elder != null) {
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_elder"));
            return new User(elder.getAccount(), elder.getPassword(), authorities);
        }

        // 如果三张表都找不到，抛出异常
        throw new UsernameNotFoundException("User not found with account: " + username);
    }
}