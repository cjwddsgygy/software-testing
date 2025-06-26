// src/api/request.js
import axios from 'axios';
import { useUserStore } from '@/stores/user'; // 引入 Pinia store
import { ElMessage } from 'element-plus'; // 引入 Element Plus 的消息提示

const baseURL = 'http://localhost:8080'; // 你的后端地址
const service = axios.create({
    baseURL,
    timeout: 5000
});

// 请求拦截器
service.interceptors.request.use(
    config => {
        const userStore = useUserStore();
        if (userStore.token) {
            // 如果存在 token，则在请求头中添加 Authorization
            config.headers['Authorization'] = userStore.token;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器
service.interceptors.response.use(
    response => {
        const res = response.data;
        // 你的后端返回格式是 { code, msg, data }
        if (res.code === 0) { // 假设 0 是成功
            return res.data; // 直接返回 data 部分
        } else {
            // 业务失败，显示错误信息
            ElMessage({
                message: res.msg || 'Error',
                type: 'error',
                duration: 5 * 1000
            });
            return Promise.reject(new Error(res.msg || 'Error'));
        }
    },
    error => {
        ElMessage({
            message: error.message,
            type: 'error',
            duration: 5 * 1000
        });
        return Promise.reject(error);
    }
);

export default service;