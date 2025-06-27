// 文件路径: src/api/request.js

import axios from 'axios';
import { ElMessage } from 'element-plus';

// 创建 axios 实例
const service = axios.create({
  // 注意：这里的 baseURL 应该指向你的后端服务地址
  // 如果你用了 Vite 的 proxy，这里通常是 /api
  baseURL: 'http://localhost:8080', // 或者 '/api' 
  timeout: 10000, // 请求超时时间
});

// 请求拦截器 (Request Interceptor)
service.interceptors.request.use(
  config => {
    // 1. 从 localStorage 获取 token
    const token = localStorage.getItem('token'); 
    
    // 2. 如果 token 存在，则添加到请求头
    if (token) {
      // 这里的键名 'Authorization' 和值的格式 'Bearer ' + token
      // 必须与后端 TokenInterceptor 的期望完全一致
      config.headers['Authorization'] = 'Bearer ' + token;
    }
    
    return config;
  },
  error => {
    // 对请求错误做些什么
    console.error('Request Interceptor Error:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器 (Response Interceptor)
service.interceptors.response.use(
  response => {
    // 对响应数据做点什么
    // 例如，如果后端返回的数据结构是 { code, msg, data }
    const res = response.data;
    if (res.code !== 1 && res.code !== 200 && res.code !== 0) { // 假设1, 200, 0都代表成功
      ElMessage.error(res.msg || 'Error');
      
      // 如果是 token 失效 (例如 code 是 401)
      if (res.code === 401) {
        // 在这里可以做跳转到登录页的操作
        // router.push('/login');
      }
      return Promise.reject(new Error(res.msg || 'Error'));
    } else {
      // 直接返回后端数据中的 data 部分或整个响应体
      return response; // 返回整个response，让组件自己处理 response.data
    }
  },
  error => {
    // 对响应错误做点什么
    console.error('Response Interceptor Error:', error);
    ElMessage.error(error.message);
    return Promise.reject(error);
  }
);

export default service;