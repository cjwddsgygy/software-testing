// src/api/user.js
import request from './request';

export const loginAPI = (data) => {
    return request({
        url: '/adminLogin',
        method: 'POST',
        data
    });
};