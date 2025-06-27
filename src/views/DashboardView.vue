<!-- START OF FILE DashboardView.vue -->
<template>
  <div class="dashboard-layout">
    <!-- Sidebar -->
    <aside class="sidebar">
      <div class="sidebar-header">
        <h2>管理系统</h2>
      </div>
      <nav class="sidebar-nav">
        <ul>
          <!-- 使用 <router-link> 进行导航 -->
          <li><router-link to="/dashboard/home" class="nav-link">首页</router-link></li>
		  <li><router-link to="/dashboard/care-worker" class="nav-link">护工信息管理</router-link></li>
          <li><router-link to="/dashboard/elder" class="nav-link">老人信息管理</router-link></li>
          <li><router-link to="/dashboard/bed-management" class="nav-link">床位管理</router-link></li>
          <li><router-link to="/dashboard/system-settings" class="nav-link">系统设置</router-link></li>
        </ul>
      </nav>
      <div class="sidebar-footer">
        <!-- 添加点击事件处理退出登录 -->
        <a href="#" @click.prevent="handleLogout" class="nav-link logout">退出登录</a>
      </div>
    </aside>

    <!-- Main Content Area -->
    <main class="main-content">
      <!-- 子路由对应的组件将在这里被渲染 -->
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

const handleLogout = () => {
  console.log('用户退出登录');
  // 可以在这里清除 token 或用户会话
  // 然后跳转回登录页
  router.push('/login');
};
</script>

<style scoped>
.dashboard-layout { display: flex; height: 100vh; width: 100vw; background-color: #f5f7fa; }
.sidebar { width: 220px; background-color: #304156; color: #bfcbd9; display: flex; flex-direction: column; flex-shrink: 0; position: fixed; top: 0; left: 0; height: 100%; }
.sidebar-header { padding: 20px; text-align: center; border-bottom: 1px solid #4a5e73; }
.sidebar-header h2 { margin: 0; font-size: 20px; color: #ffffff; }
.sidebar-nav { flex-grow: 1; overflow-y: auto; }
.sidebar-nav ul { list-style: none; padding: 0; margin: 20px 0; }
.nav-link { display: block; padding: 15px 20px; color: #bfcbd9; text-decoration: none; transition: background-color 0.3s, color 0.3s; }
.nav-link:hover { background-color: #263445; }
/* Vue Router 会自动为当前激活的链接添加 'router-link-active' 和 'router-link-exact-active' class */
.nav-link.router-link-active { background-color: #409eff; color: #ffffff; }
.sidebar-footer { padding: 15px; border-top: 1px solid #4a5e73; }
.logout { text-align: center; }
.main-content { flex-grow: 1; padding: 30px; overflow-y: auto; margin-left: 220px; }
</style>
<!-- END OF FILE DashboardView.vue -->