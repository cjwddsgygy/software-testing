// 文件路径: src/api/older.js

import request from './request';

/**
 * 获取老人列表 (可带分页和搜索条件)
 * @param {object} params - 查询参数，例如 { page: 1, pageSize: 10, name: '张三' }
 */
export const getOlderListAPI = (params) => {
    return request({
        // ✅ 修改 url，使其与 ElderController 的 @GetMapping 路径匹配
        url: '/elders', 
        method: 'GET',
        params // axios 会自动将 params 对象转换为 URL 查询字符串
    });
};

/**
 * 新增老人信息
 * @param {object} data - 老人信息对象
 */
export const addOlderAPI = (data) => {
    return request({
        url: '/elders',
        method: 'POST',
        data // POST 请求的数据放在 data 字段
    });
};

/**
 * 根据ID删除老人
 * @param {number|string} id - 老人的ID
 */
export const deleteOlderAPI = (id) => {
    return request({
        url: `/elders/${id}`, // 使用模板字符串拼接ID
        method: 'DELETE'
    });
};

/**
 * 更新老人信息
 * @param {object} data - 包含ID的老人信息对象
 */
export const updateOlderAPI = (data) => {
    return request({
        url: `/elders`, // 假设更新接口是 PUT /elders (根据RESTful风格)
        method: 'PUT',
        data
    });
};

// 你可以继续在这里添加其他与老人相关的API...