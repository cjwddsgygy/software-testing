// src/main.js

import { createApp } from 'vue';
import { createPinia } from 'pinia'; // 1. 从 pinia 库中导入 createPinia 函数
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'; // 2. 导入 Pinia 的持久化插件

import App from './App.vue';
import router from './router';
import apiClient from '@/api/request'; // ✅ 确保这里导入了你的 Axios 实例

// --- 创建和配置应用 ---

// 创建 Vue 应用实例
const app = createApp(App);

// 创建 Pinia 实例
const pinia = createPinia();

// 3. 让 Pinia 实例使用持久化插件
// 这个插件的作用是，当你配置了 store 的 persist: true 选项后，
// 它会自动将 store 的状态保存到 localStorage 中。
pinia.use(piniaPluginPersistedstate);

// 4. 将 Pinia 实例注册到 Vue 应用中
// 这样，在你的任何组件中，都可以通过 useAuthStore() 等方式访问到 store。
app.use(pinia);

// 5. 将路由实例注册到 Vue 应用中
app.use(router);

// ✅✅✅ 核心修改：在应用挂载前，根据 localStorage 初始化 Axios 默认请求头 ✅✅✅
// 这一步确保了在页面刷新或重新加载后，即使 Pinia 状态还没完全恢复，
// 第一个发出的网络请求也能携带正确的 Token。
const token = localStorage.getItem('token');
if (token) {
  apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
}

// 6. 将应用挂载到 HTML 页面上 id 为 'app' 的元素上
app.mount('#app');