// 文件路径: src/main.js (最终带全局错误处理版)
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import { useAuthStore } from './stores/auth'; // 导入 auth store
import { ElMessage } from 'element-plus';

import './assets/base.css'; 
import App from './App.vue';
import router from './router';

const app = createApp(App);
const pinia = createPinia();

pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(router);

// ✅✅✅ 全局错误和 401 处理器 ✅✅✅
app.config.errorHandler = (err, instance, info) => {
  // 处理组件渲染错误
  console.error('Vue Error:', err, instance, info);
};

window.addEventListener('unhandledrejection', event => {
  // event.promise: a Promise that was rejected
  // event.reason: the rejection value
  const reason = event.reason;
  if (reason && reason.isAxiosError && reason.response?.status === 401) {
    // 这是我们专门要捕获的 401 错误
    console.error('Global unhandled rejection (401):', reason);
    ElMessage.error('认证已过期，请重新登录。');
    
    // 调用 authStore 来安全地登出
    const authStore = useAuthStore();
    authStore.logout();
    router.push('/login');
  } else {
    // 其他未捕获的 promise 错误
    console.error('Global unhandled rejection:', reason);
  }
});


app.mount('#app');