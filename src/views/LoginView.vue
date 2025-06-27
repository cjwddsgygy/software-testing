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
import axios from 'axios'; // 引入 axios

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
    // 发送 POST 请求到你的后端 API
    const response = await axios.post('http://localhost:8080/admin/login', {
      account: form.username, // 注意：后端需要的字段是 account 和 password
      password: form.password
    });


  // --- 关键调试代码 ---
  console.log('后端返回的完整数据:', response.data); 
  // --------------------
    // 假设后端成功时返回的数据结构包含一个 code 字段
    // 请根据你后端实际返回的数据结构来判断是否登录成功
    if (response.data.code === 1) { //  code '1' 表示成功
      console.log('登录成功:', response.data);
      // 可以在此保存 token 等用户信息
      // localStorage.setItem('token', response.data.data.token);
      
      router.push('/dashboard'); // 跳转到仪表盘
    } else {
      // 后端返回了业务错误，例如“用户名或密码错误”
      errorMessage.value = response.data.msg || '用户名或密码错误';
    }
  } catch (error) {
    console.error('登录请求失败:', error);
    if (error.code === 'ERR_NETWORK') {
        errorMessage.value = '网络错误，请检查后端服务是否开启，或是否存在跨域问题。';
    } else if (error.response) {
        // 后端返回了 HTTP 错误状态码 (如 401, 500)
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
/* 样式与之前相同 */
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