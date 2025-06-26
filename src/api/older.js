// src/api/older.js
import request from './request';

// 获取老人列表 (带分页和搜索)
export const getOlderListAPI = (params) => {
    return request({
        url: '/older',
        method: 'GET',
        params // GET 请求的参数用 params
    });
};

// ... 在这里可以继续添加增、删、改、查详情的 API