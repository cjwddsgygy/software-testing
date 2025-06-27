package com.yusheng.service.impl;

        import com.yusheng.mapper.AdminMapper;
        import com.yusheng.pojo.Admin;
        import com.yusheng.pojo.AdminInfo;
        import com.yusheng.service.AdminService;
        import com.yusheng.utils.JwtUtils;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Service;

        import java.util.HashMap;
        import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    //    管理员登录
    @Override
    public AdminInfo login(Admin admin) {
//        1.调用mapper接口，根据用户名和密码查询管理员信息
        Admin adminInfo = adminMapper.selectByUsernameAndPassword(admin);

//        2.判断：判断是否存在这个管理员，如果存在，组装登陆成功信息
        if (adminInfo != null) {
//            生成JWT令牌
            Map<String, Object> payload = new HashMap<>();
            payload.put("id", adminInfo.getId());
            payload.put("account", adminInfo.getAccount());
            String jwt = JwtUtils.generateToken(payload);

            return new AdminInfo(adminInfo.getId(), adminInfo.getName(),
                    adminInfo.getAccount(), jwt);
        }

//        3.不存在，返回null
        return null;
    }
}