// src/api/request.js

import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router'; // 导入 router 用于跳转

// 创建 axios 实例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
});

// ✅✅✅ 核心修改：添加请求拦截器 ✅✅✅
apiClient.interceptors.request.use(
  (config) => {
    // 1. 在每个请求发送出去之前，从 localStorage 中获取 token
    const token = localStorage.getItem('token'); 
    
    // 2. 如果 token 存在，则为这个请求的请求头添加 Authorization 字段
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // 3. 必须返回 config 对象，否则请求会被中断
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    console.error('Request Interceptor Error:', error);
    return Promise.reject(error);
  }
);


// ✅ 同样重要的是：添加响应拦截器，统一处理错误
apiClient.interceptors.response.use(
  (response) => {
    // 成功响应 (HTTP 状态码为 2xx)
    // 如果后端有业务错误码，可以在这里判断
    if (response.data && response.data.code !== 0) {
      ElMessage.error(response.data.msg || '操作失败');
      return Promise.reject(new Error(response.data.msg));
    }
    return response;
  },
  (error) => {
    // 失败响应 (HTTP 状态码不是 2xx)
    console.error('Response Interceptor Error:', error.response);
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElMessage.error('认证失败或登录已过期，请重新登录');
          // 清理本地 token，并跳转到登录页
          localStorage.removeItem('token'); 
          router.push('/login');
          break;
        case 403:
          ElMessage.error('您没有权限执行此操作');
          break;
        default:
          ElMessage.error(error.response.data.msg || `服务器错误: ${error.response.status}`);
      }
    } else if (error.request) {
        ElMessage.error('网络连接错误，请检查后端服务是否开启');
    } else {
        ElMessage.error('请求发送失败');
    }

    return Promise.reject(error);
  }
);

export default apiClient;