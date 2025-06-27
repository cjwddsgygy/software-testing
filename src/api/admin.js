// 文件路径: src/api/user.js

import request from './request';

/**
 * 管理员登录接口
 * @param {object} data - 包含登录凭证的对象，例如 { account, password }
 */
export const loginAPI = (data) => {
    return request({
        // ✅ 修改 url，使其与后端的登录接口路径完全匹配
        url: '/admin/login',
        method: 'POST',
        data // POST 请求的数据放在 data 字段
    });
};

// 你可以继续在这里添加其他用户相关的API，例如获取用户信息、登出等...