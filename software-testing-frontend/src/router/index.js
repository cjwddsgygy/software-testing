// 文件路径: src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import apiClient from '@/api/request';

// --- 导入所有视图组件 ---
import LoginView from '../views/LoginView.vue';
import DashboardView from '../views/DashboardView.vue';
import HomeView from '../views/HomeView.vue';
import ElderView from '../views/ElderView.vue';
import CareWorkerView from '../views/CareWorkerView.vue';
import BedManagement from '../views/BedManagement.vue';
import SystemSettings from '../views/SystemSettings.vue';
import ExpansesView from '../views/ExpansesView.vue'; 
import HealthRecordView from '../views/HealthRecordView.vue';
import AccountView from '../views/AccountView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginView
    },
    {
      path: '/dashboard',
      name: 'Dashboard',
      component: DashboardView,
      meta: { requiresAuth: true }, 
      redirect: '/dashboard/home',
      children: [
        { path: 'home', name: 'Home', component: HomeView },
        { path: 'elders', name: 'ElderManagement', component: ElderView },
        { path: 'careworkers', name: 'CareWorkerManagement', component: CareWorkerView },
        { path: 'beds', name: 'BedManagement', component: BedManagement },
        { path: 'expenses', name: 'ExpensesManagement', component: ExpansesView },
        { path: 'health-records', name: 'HealthRecordManagement', component: HealthRecordView },
		    { path: 'account', name: 'AccountManagement', component: AccountView },
        { path: 'settings', name: 'SystemSettings', component: SystemSettings },
      ]
    }
  ]
});

// --- 全局路由守卫与智能初始化 ---

let appInitialized = false;

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();

  // 1. 应用首次加载/刷新页面时的初始化逻辑
  if (!appInitialized) {
    const token = authStore.token;
    if (token) {
      // 如果 Pinia 从 localStorage 恢复了 token，立即设置 apiClient 的请求头
      apiClient.defaults.headers.common['Authorization'] = `Bearer ${token}`;
    }
    appInitialized = true;
  }

  // 2. 路由守卫的核心判断逻辑
  const isLoggedIn = authStore.isLoggedIn;
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

  if (requiresAuth && !isLoggedIn) {
    authStore.logout(); // 跳转前清空状态
    next({ name: 'Login' });
  } 
  else if (to.name === 'Login' && isLoggedIn) {
    next({ path: '/dashboard/home' });
  } 
  else {
    next();
  }
});

export default router;