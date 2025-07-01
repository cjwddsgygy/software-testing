// 文件路径: src/api/request.js (最终单例版)
import axios from 'axios';
import { ElMessage } from 'element-plus';

// 1. 创建并立即导出 axios 实例，确保它是单例
const apiClient = axios.create({
  baseURL: 'http://localhost:8080',
  timeout: 10000,
});

// 2. 请求拦截器：它不再从 localStorage 读取，因为 Token 的设置将由外部完成
apiClient.interceptors.request.use(
  (config) => {
    // 这个拦截器现在可以很简单，甚至可以为空，
    // 因为 Token 的设置将在 auth.js 和 router.js 中动态完成。
    // 我们保留它，以备将来需要添加通用逻辑（如 loading 状态）。
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 3. 响应拦截器：它也不再直接处理 401，而是将错误原样抛出
apiClient.interceptors.response.use(
  (response) => {
    if (response.data && response.data.code !== 0) {
      ElMessage.error(response.data.msg || '操作失败');
      return Promise.reject(new Error(response.data.msg));
    }
    return response;
  },
  (error) => {
    // 只处理通用错误，将 401 等特定错误留给调用方或全局错误处理器
    if (error.response) {
        // 我们不再在这里处理 401 跳转，以避免循环依赖
        if (error.response.status !== 401) {
            ElMessage.error(error.response.data.msg || `服务器错误: ${error.response.status}`);
        }
    } else if (error.request) {
        ElMessage.error('网络连接错误，请检查后端服务是否开启');
    } else {
        ElMessage.error('请求发送失败');
    }
    // 将原始错误继续抛出
    return Promise.reject(error);
  }
);

export default apiClient;