<!-- 文件路径: src/views/DashboardView.vue -->
<template>
  <div class="dashboard-view">
    <aside class="sidebar">
      <h1 class="system-title">管理系统</h1>
      <nav class="nav-menu">
        <ul>
          <li><router-link to="/dashboard/home">首页</router-link></li>
          <li><router-link to="/dashboard/careworkers">护工信息管理</router-link></li>
          <li><router-link to="/dashboard/elders">老人信息管理</router-link></li>
          <li><router-link to="/dashboard/beds">床位管理</router-link></li>
          <li><router-link to="/dashboard/expenses">消费记录管理</router-link></li> <!-- 新增 -->
          <li><router-link to="/dashboard/health-records">健康管理</router-link></li> <!-- 新增 -->
          <li><router-link to="/dashboard/settings">系统设置</router-link></li>
        </ul>
      </nav>
      <button @click="logout" class="logout-btn">退出登录</button>
    </aside>
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus'; // 假设需要Element Plus的提示

const router = useRouter();

const logout = () => {
  // 模拟退出登录逻辑
  console.log('用户退出登录');
  ElMessage.success('您已成功退出登录！');
  // 实际项目中可能需要清除token，并重定向到登录页
  localStorage.removeItem('token'); // 清除token
  router.push('/login'); // 跳转到登录页
};
</script>

<style scoped>
.dashboard-view {
  display: flex;
  height: 100vh;
  background-color: #f0f2f5;
}

.sidebar {
  width: 250px;
  background-color: #2c3e50; /* 深色背景 */
  color: #ecf0f1; /* 浅色文字 */
  padding: 20px;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
}

.system-title {
  font-size: 28px;
  text-align: center;
  margin-bottom: 30px;
  color: #42b983; /* Vue 绿色 */
  font-weight: bold;
}

.nav-menu {
  flex-grow: 1;
}

.nav-menu ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.nav-menu li {
  margin-bottom: 10px;
}

.nav-menu a {
  display: block;
  padding: 12px 15px;
  color: #ecf0f1;
  text-decoration: none;
  border-radius: 6px;
  transition: background-color 0.3s, color 0.3s;
  font-size: 16px;
}

.nav-menu a:hover,
.nav-menu a.router-link-active {
  background-color: #42b983; /* Vue 绿色 */
  color: #fff;
}

.logout-btn {
  background-color: #e74c3c; /* 红色 */
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
  width: 100%;
  margin-top: 20px;
}

.logout-btn:hover {
  background-color: #c0392b;
}

.main-content {
  flex-grow: 1;
  padding: 20px;
  overflow-y: auto; /* Allow scrolling for content */
}
</style>