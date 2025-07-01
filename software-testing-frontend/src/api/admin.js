// 文件路径: src/api/auth.js (新建或修改)
import request from './request';

/**
 * 通用登录接口
 * @param {object} data - 包含 account, password, role 的对象
 */
export const loginAPI = (data) => {
    return request({
        url: '/api/login', // ✅ 调用统一接口
        method: 'POST',
        data
    });
};