// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import DashboardView from '../views/DashboardView.vue';
import HomeView from '../views/HomeView.vue';
import ElderView from '../views/ElderView.vue';
import BedManagement from '../views/BedManagement.vue';
import SystemSettings from '../views/SystemSettings.vue';

const routes = [
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
    component: DashboardView, // 父路由只负责布局，不需要 name
    redirect: '/dashboard/home', // 进入 /dashboard 时，自动跳转到 /dashboard/home
    children: [
      // 子路由负责具体页面
      {
        path: 'home',
        name: 'DashboardHome', // 给首页一个明确的名字
        component: HomeView
      },
      {
        path: 'bed-management',
        name: 'BedManagement',
        component: BedManagement
      },
      {
        path: 'system-settings',
        name: 'SystemSettings',
        component: SystemSettings
      },
	  { path: 'elder', 
	  name: 'Elder', 
	  component: () => import('@/views/ElderView.vue'), 
	  meta: { title: '老人信息管理' } },
	  // --- 新增护工管理路由 ---
	  { path: 'care-worker',
	  name: 'CareWorker',
	  component: () => import('@/views/CareWorkerView.vue'),
	  meta: { title: '护工信息管理' } },
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;