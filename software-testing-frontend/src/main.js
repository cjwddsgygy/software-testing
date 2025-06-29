// src/main.js (已优化)

import { createApp } from 'vue';
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';

import './assets/base.css';
import App from './App.vue';
import router from './router';
import apiClient from '@/api/request';

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(router);

// ✅✅✅ 核心修改：统一并修复启动时的 Token 初始化逻辑 ✅✅✅
// 这一步确保了在页面刷新后，即使 Pinia 尚未完全 rehydrate，
// 第一个发出的网络请求也能携带正确的 Token。
// 我们从 'auth' (Pinia持久化的键) 中解析 token，保持与拦截器逻辑一致。
try {
  const authDataStr = localStorage.getItem('auth'); // Pinia持久化authStore的默认键
  if (authDataStr) {
    const authData = JSON.parse(authDataStr);
    const token = authData.token;
    if (token) {
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
  }
} catch (e) {
  console.error('Failed to initialize Axios headers from localStorage:', e);
  // 如果解析失败，可能是数据损坏，可以考虑清理
  localStorage.removeItem('auth');
}

app.mount('#app');