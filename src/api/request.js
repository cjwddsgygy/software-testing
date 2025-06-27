// 文件路径: src/api/request.js

import axios from 'axios';

// ✅ 只创建一个最干净、最简单的 axios 实例
// 不包含任何拦截器或 Pinia 依赖
const service = axios.create({
  baseURL: 'http://localhost:8080/api', // 确保 baseURL 正确
  timeout: 5000
});

// 导出这个干净的实例
export default service;