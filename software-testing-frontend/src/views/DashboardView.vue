<!-- 文件路径: src/views/DashboardView.vue (已重构) -->
<template>
  <div class="dashboard-view">
    <aside class="sidebar">
      <!-- ✅ 1. 使用来自 store 的动态系统名称 -->
      <h1 class="system-title">{{ settingsStore.systemName }}</h1>
      
      <!-- ✅ 2. 使用 v-for 渲染导航菜单，更易于维护 -->
      <nav class="nav-menu">
        <ul>
          <li v-for="item in navItems" :key="item.to">
            <router-link :to="item.to">
              <i :class="item.icon"></i> <!-- (可选) 添加图标 -->
              <span>{{ item.title }}</span>
            </router-link>
          </li>
        </ul>
      </nav>
      
      <button @click="logout" class="logout-btn">
        <i class="fas fa-sign-out-alt"></i>
        <span>退出登录</span>
      </button>
    </aside>
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { useAuthStore } from '@/stores/auth';
import { useSettingsStore } from '@/stores/settings'; // ✅ 3. 导入 settings store

const router = useRouter();
const authStore = useAuthStore();
const settingsStore = useSettingsStore(); // ✅ 3. 获取 settings store 实例

// ✅ 4. 将导航项定义为数据，便于管理
const navItems = ref([
  { to: '/dashboard/home', title: '首页', icon: 'fas fa-home' },
  { to: '/dashboard/careworkers', title: '护工信息管理', icon: 'fas fa-user-nurse' },
  { to: '/dashboard/elders', title: '老人信息管理', icon: 'fas fa-users' },
  { to: '/dashboard/beds', title: '床位信息管理', icon: 'fas fa-bed' },
  { to: '/dashboard/expenses', title: '消费记录管理', icon: 'fas fa-wallet' },
  { to: '/dashboard/health-records', title: '健康数据管理', icon: 'fas fa-heartbeat' },
  { to: '/dashboard/settings', title: '系统设置', icon: 'fas fa-cog' },
]);

// ✅ 5. 在主布局挂载时，初始化系统设置
onMounted(() => {
  settingsStore.initializeSettings();
});

const logout = () => {
  authStore.logout(); 
  ElMessage.success('您已成功退出登录！');
  router.push('/login');
};
</script>

<style scoped>
/* ✅ 6. 所有样式都使用 CSS 变量，以支持主题切换 */
.dashboard-view {
  display: flex;
  height: 100vh;
  background-color: var(--bg-page); /* 使用变量 */
}

.sidebar {
  width: 250px;
  background-color: var(--bg-card); /* 使用变量 */
  color: var(--text-primary); /* 使用变量 */
  padding: 20px;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 6px rgba(0, 0, 0, 0.1);
  border-right: 1px solid var(--border-color); /* 使用变量 */
  transition: background-color 0.3s, color 0.3s;
}

.system-title {
  font-size: 1.5rem; /* 调整大小以适应更长的名字 */
  text-align: center;
  margin-bottom: 30px;
  color: var(--primary-color); /* 使用变量 */
  font-weight: 600;
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
  margin-bottom: 8px;
}

.nav-menu a {
  display: flex; /* 改为 flex 布局以对齐图标和文字 */
  align-items: center;
  gap: 12px; /* 图标和文字的间距 */
  padding: 12px 15px;
  color: var(--text-regular); /* 使用变量 */
  text-decoration: none;
  border-radius: 6px;
  transition: background-color 0.3s, color 0.3s;
  font-size: 1rem;
  font-weight: 500;
}

.nav-menu a:hover {
  background-color: var(--primary-hover);
  color: #fff;
}
.nav-menu a.router-link-exact-active { /* 使用更精确的 active 类 */
  background-color: var(--primary-color);
  color: #fff;
}

.logout-btn {
  background-color: var(--danger-color); /* 使用变量 */
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s;
  width: 100%;
  margin-top: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.logout-btn:hover {
  opacity: 0.85;
}

.main-content {
  flex-grow: 1;
  padding: 25px; /* 增加内边距 */
  overflow-y: auto;
}
</style>