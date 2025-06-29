// src/api/request.js (已优化)

import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '@/router';

// ... (创建 axios 实例 和 请求拦截器部分保持不变) ...
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
});

apiClient.interceptors.request.use(
  (config) => {
    const authDataStr = localStorage.getItem('auth');
    if (authDataStr) {
      try {
        const authData = JSON.parse(authDataStr);
        if (authData.token) {
          config.headers.Authorization = `Bearer ${authData.token}`;
        }
      } catch (e) {
        console.error("Error parsing 'auth' data from localStorage:", e);
        localStorage.removeItem('auth');
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);


// ✅ 响应拦截器优化
apiClient.interceptors.response.use(
  (response) => {
    // ... (成功响应部分保持不变) ...
    if (response.data && response.data.code !== 0) {
      ElMessage.error(response.data.msg || '操作失败');
      return Promise.reject(new Error(response.data.msg));
    }
    return response;
  },
  (error) => {
    console.error('Response Interceptor Error:', error.response);
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElMessage.error('认证失败或登录已过期，请重新登录');
          localStorage.removeItem('auth'); // 清除本地存储的凭证

          // ✅✅✅ 核心修复：强制页面刷新以解决无限重定向问题 ✅✅✅
          // router.push 会保留当前 Vue 应用的内存状态（包括 Pinia）。
          // 直接跳转会导致路由守卫认为用户仍 "在线"，从而重定向回主页，造成死循环。
          // 强制刷新可以彻底清空内存状态，让应用从空的 localStorage 重新启动。
          router.push('/login').then(() => {
            window.location.reload();
          });
          break;
        // ... (其他错误处理保持不变) ...
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