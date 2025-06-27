<!-- frontend/src/views/LoginView.vue -->
<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="title">养老院信息管理系统</h1>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">账号</label>
          <input type="text" id="username" v-model="form.username" placeholder="请输入账号" required />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="form.password" placeholder="请输入密码" required />
        </div>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <button type="submit" class="login-button" :disabled="loading">
          {{ loading ? '登录中...' : '登录' }}
        </button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
// --- 关键修改 1: 导入我们配置好的 apiClient ---
import apiClient from '@/api'; // 不再使用原始的 axios

const router = useRouter();

const form = reactive({
  username: '',
  password: '',
});
const loading = ref(false);
const errorMessage = ref('');

const handleLogin = async () => {
  loading.value = true;
  errorMessage.value = '';

  try {
    // --- 关键修改 2: 使用 apiClient 发送请求 ---
    const response = await apiClient.post('/admin/login', { // 确认API路径为 /admin/login
      account: form.username,
      password: form.password
    });

    console.log('后端登录响应:', response.data);

    // 判断登录是否成功 (code === 1)
    if (response.data.code === 1) {
      
      // --- 关键修改 3: 保存 Token ---
      const token = response.data.data.token;
      
      if (token) {
        // 1. 将 Token 存储到浏览器的 localStorage 中
        localStorage.setItem('authToken', token);
        console.log('Token 已成功保存到 localStorage');

        // 2. 立即为 apiClient 的后续请求设置默认头部
        apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        
        // 3. 跳转到主页 (根据您的路由配置，主页是 /dashboard)
        router.push('/dashboard');

      } else {
        errorMessage.value = '登录凭证获取失败，请联系管理员。';
        console.error('登录成功但响应中缺少 token');
      }

    } else {
      // 处理业务错误，例如密码错误
      errorMessage.value = response.data.msg || '用户名或密码错误';
    }
  } catch (error) {
    // 处理网络或服务器错误
    console.error('登录请求失败:', error);
    if (error.code === 'ERR_NETWORK') {
        errorMessage.value = '网络错误，请检查后端服务是否开启。';
    } else if (error.response) {
        errorMessage.value = `登录失败: ${error.response.data.msg || '服务器错误'}`;
    } else {
        errorMessage.value = '登录时发生未知错误。';
    }
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* 样式保持不变 */
.login-container { display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
.login-box { width: 400px; padding: 40px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center; }
.title { font-size: 24px; color: #333; margin-bottom: 30px; }
.form-group { margin-bottom: 20px; text-align: left; }
.form-group label { display: block; margin-bottom: 8px; color: #555; font-weight: bold; }
.form-group input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.login-button { width: 100%; padding: 12px; background-color: #409eff; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; transition: background-color 0.3s; }
.login-button:hover { background-color: #66b1ff; }
.login-button:disabled { background-color: #a0cfff; cursor: not-allowed; }
.error-message { color: #f56c6c; margin-bottom: 15px; }
</style>