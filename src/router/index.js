// src/router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '../views/LoginView.vue';
import DashboardView from '../views/DashboardView.vue';
import HomeView from '../views/HomeView.vue';
import OlderManagement from '../views/OlderManagement.vue';
import BedManagement from '../views/BedManagement.vue';
import SystemSettings from '../views/SystemSettings.vue';

const routes = [
  {
    path: '/',
    redirect: '/login' // 默认打开应用时，重定向到登录页
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
    // 这里是“子路由”，所有在仪表盘内部切换的页面都在这里定义
    children: [
      {
        path: '', // /dashboard 的默认子页面
        redirect: '/dashboard/home'
      },
      {
        path: 'home', // 对应的完整路径是 /dashboard/home
        name: 'Home',
        component: HomeView
      },
      {
        path: 'older-management',
        name: 'OlderManagement',
        component: OlderManagement
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
      }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;