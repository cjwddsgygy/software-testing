// src/api/user.js
import request from './request';

export const loginAPI = (data) => {
    return request({
        url: '/admin/login',
        method: 'POST',
        data
    });
};