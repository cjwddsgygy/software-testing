// src/api.js
import axios from 'axios';

const apiClient = axios.create({
  baseURL: 'http://localhost:8080', // 您的后端API根地址
  headers: {
    'Content-Type': 'application/json'
  }
});

// 您可以在这里添加拦截器等高级功能，例如自动附加token

export default apiClient;