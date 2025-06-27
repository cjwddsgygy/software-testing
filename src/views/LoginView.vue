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
import { useAuthStore } from '@/stores/auth'; // ✅ 1. 导入你的 auth store

const router = useRouter();
const authStore = useAuthStore(); // ✅ 2. 获取 store 实例

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
    // ✅ 3. 调用 store 的 login action
    // login action 内部会处理 API 调用、状态更新和持久化
    await authStore.login({ 
      username: form.username, 
      password: form.password 
    });

    // 如果 login action 没有抛出错误，就说明登录成功
    ElMessage.success('登录成功！'); // (可选) 给出成功提示
    router.push('/dashboard/home'); // 跳转到主页

  } catch (error) {
    // login action 内部如果登录失败会抛出错误，我们在这里捕获
    console.error('登录失败:', error);
    errorMessage.value = error.message || '用户名或密码错误';
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