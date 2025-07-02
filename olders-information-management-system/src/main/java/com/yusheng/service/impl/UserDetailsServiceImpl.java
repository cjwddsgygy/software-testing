// 文件路径: src/main/java/com/yusheng/service/impl/UserDetailsServiceImpl.java
package com.yusheng.service.impl;

// ✅ 1. 导入新的 JPA Repository
import com.yusheng.repository.AdminRepository;
import com.yusheng.repository.CareWorkersRepository;
import com.yusheng.repository.ElderRepository;

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
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    // ✅ 2. 注入 JPA Repositories，而不是 MyBatis Mappers
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CareWorkersRepository careWorkersRepository;
    @Autowired
    private ElderRepository elderRepository;

    /**
     * Spring Security 调用此方法来获取用户信息。
     * @param username 在我们的系统中，这实际上是 account
     * @return UserDetails 对象
     * @throws UsernameNotFoundException 如果在所有表中都找不到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // 1. 查找管理员表
        Optional<Admin> adminOptional = adminRepository.findByAccount(username);
        if (adminOptional.isPresent()) {
            Admin admin = adminOptional.get();
            // 找到了，封装成 Spring Security 的 User 对象并返回
            // 我们在 JwtAuthenticationTokenFilter 中创建权限，这里可以给一个默认的
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            return new User(admin.getAccount(), admin.getPassword(), authorities);
        }

        // 2. 查找护工表
        Optional<CareWorker> careWorkerOptional = careWorkersRepository.findByAccount(username);
        if (careWorkerOptional.isPresent()) {
            CareWorker careWorker = careWorkerOptional.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_CAREWORKER"));
            return new User(careWorker.getAccount(), careWorker.getPassword(), authorities);
        }

        // 3. 查找老人表
        Optional<Elder> elderOptional = elderRepository.findByAccount(username);
        if (elderOptional.isPresent()) {
            Elder elder = elderOptional.get();
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ELDER"));
            return new User(elder.getAccount(), elder.getPassword(), authorities);
        }

        // 4. 如果三张表都找不到，抛出标准异常
        throw new UsernameNotFoundException("User not found with account: " + username);
    }
}