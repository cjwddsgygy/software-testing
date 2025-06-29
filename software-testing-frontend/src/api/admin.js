// 文件路径: src/api/admin.js

import request from './request'; // 确保这里导入的是 src/api/request.js 的默认导出 apiClient

/**
 * 管理员登录接口
 * @param {object} data - 包含登录凭证的对象，例如 { account, password }
 */
export const loginAPI = (data) => {
    return request({
        // ✅ 核心修改：确保 URL 与后端 UserController 路径完全匹配
        url: '/api/adminLogin', 
        method: 'POST',
        data // POST 请求的数据放在 data 字段
    });
};

// 你可以继续在这里添加其他用户相关的API，例如获取用户信息、登出等...