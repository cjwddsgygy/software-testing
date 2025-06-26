<!-- START OF FILE LoginView.vue -->
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
import apiClient from '../api'; // 引入我们配置好的api客户端

const router = useRouter();
const form = reactive({
  username: '',
  password: ''
});
const loading = ref(false);
const errorMessage = ref('');

const handleLogin = async () => {
  loading.value = true;
  errorMessage.value = '';
  try {
    // 调用后端的 /admins/login 接口，使用POST方法
    const response = await apiClient.post('/admins/login', {
      username: form.username,
      password: form.password
    });

    // 后端返回成功，我们进行跳转
    if (response.data.code === '200' && response.data.data) { // 根据您后端返回的格式判断
      console.log('登录成功:', response.data.data);
      // 您可以在这里存储token等用户信息
      // localStorage.setItem('user', JSON.stringify(response.data.data));
      router.push('/dashboard');
    } else {
      // 后端返回了业务错误，例如密码错误
      errorMessage.value = response.data.msg || '用户名或密码错误！';
    }
  } catch (error) {
    // 网络错误或服务器500错误
    errorMessage.value = '登录失败，请检查网络或联系管理员。';
    console.error('登录请求失败:', error);
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
/* 样式保持不变 */
.error-message { color: #f56c6c; margin-bottom: 15px; }
.login-container { display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
.login-box { width: 400px; padding: 40px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center; }
.title { font-size: 24px; color: #333; margin-bottom: 30px; }
.form-group { margin-bottom: 20px; text-align: left; }
.form-group label { display: block; margin-bottom: 8px; color: #555; font-weight: bold; }
.form-group input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.login-button { width: 100%; padding: 12px; background-color: #409eff; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; }
.login-button:disabled { background-color: #a0cfff; cursor: not-allowed; }
</style>
<!-- END OF FILE LoginView.vue -->