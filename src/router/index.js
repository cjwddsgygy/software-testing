// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth'; // 导入你的 Pinia auth store

// 导入你的视图组件
import LoginView from '../views/LoginView.vue';
import DashboardView from '../views/DashboardView.vue';
import HomeView from '../views/HomeView.vue';
import ElderView from '../views/ElderView.vue';
// ... 导入其他视图组件，例如 CareWorkerView, BedManagementView 等

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login' // 根路径重定向到登录页
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
      // 对于 Dashboard 及其所有子路由，我们都要求进行认证
      meta: { requiresAuth: true }, 
      redirect: '/dashboard/home', // 访问 /dashboard 时，默认显示 home 子路由
      children: [
        {
          path: 'home',
          name: 'Home',
          component: HomeView
        },
        {
          path: 'elder',
          name: 'ElderManagement',
          component: ElderView
        },
        // 在这里添加其他子路由的定义
        // {
        //   path: 'care-worker',
        //   name: 'CareWorkerManagement',
        //   component: CareWorkerView
        // },
        // {
        //   path: 'bed-management',
        //   name: 'BedManagement',
        //   component: BedManagementView
        // }
      ]
    },
    // (可选) 添加一个 404 页面
    // { 
    //   path: '/:pathMatch(.*)*', 
    //   name: 'NotFound', 
    //   component: NotFoundView 
    // }
  ]
});

/**
 * 全局路由守卫 (Navigation Guard)
 * 每次路由跳转之前都会执行这个函数
 */
router.beforeEach((to, from, next) => {
  // 在守卫函数内部获取 Pinia store 实例
  const authStore = useAuthStore();
  const isLoggedIn = authStore.isLoggedIn; // 从 store 获取响应式的登录状态

  // 检查目标路由是否需要认证
  // 我们在路由配置中通过 meta: { requiresAuth: true } 来标记
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth);

  // --- 核心判断逻辑 ---

  // 场景1：目标路由需要认证，但用户未登录
  if (requiresAuth && !isLoggedIn) {
    // 提示用户需要登录（可选）
    console.log('Access denied. User not logged in. Redirecting to /login.');
    // 重定向到登录页
    next({ name: 'Login' });
  } 
  // 场景2：用户已登录，但试图访问登录页
  else if (to.name === 'Login' && isLoggedIn) {
    // 不允许访问，直接将他重定向到主页
    next({ path: '/dashboard/home' });
  } 
  // 场景3：其他所有合法情况
  // (例如：未登录访问登录页，或已登录访问需要认证的页面)
  else {
    // 一切正常，直接放行
    next();
  }
});

export default router;