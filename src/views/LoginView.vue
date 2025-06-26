<!-- START OF FILE LoginView.vue -->
<template>
  <div class="login-container">
    <div class="login-box">
      <h1 class="title">养老院信息管理系统</h1>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">账号</label>
          <input type="text" id="username" v-model="username" placeholder="请输入账号" required />
        </div>
        <div class="form-group">
          <label for="password">密码</label>
          <input type="password" id="password" v-model="password" placeholder="请输入密码" required />
        </div>
        <button type="submit" class="login-button">登录</button>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'; // 引入 useRouter

const router = useRouter(); // 获取 router 实例

const username = ref('admin');
const password = ref('123456');

const handleLogin = () => {
  // 这里是您的登录验证逻辑
  // 假设验证成功
  console.log('登录成功:', username.value);
  
  // 使用 router.push() 跳转到仪表盘的主页
  router.push('/dashboard/home'); 
};
</script>

<style scoped>
/* 样式与上次提供的相同，这里省略以保持简洁 */
.login-container { display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5; }
.login-box { width: 400px; padding: 40px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1); text-align: center; }
.title { font-size: 24px; color: #333; margin-bottom: 30px; }
.form-group { margin-bottom: 20px; text-align: left; }
.form-group label { display: block; margin-bottom: 8px; color: #555; font-weight: bold; }
.form-group input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.login-button { width: 100%; padding: 12px; background-color: #409eff; color: white; border: none; border-radius: 4px; font-size: 16px; cursor: pointer; transition: background-color 0.3s; }
.login-button:hover { background-color: #66b1ff; }
</style>
<!-- END OF FILE LoginView.vue -->