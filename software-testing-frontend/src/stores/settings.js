// 文件路径: src/stores/settings.js (请确保你的文件是这个版本)

import { defineStore } from 'pinia';
import apiClient from '@/api/request';

export const useSettingsStore = defineStore('settings', {
  state: () => ({
    // isInitialized 反映当前会话状态，初始值永远是 false，且不应被持久化
    isInitialized: false, 
    systemName: '养老院信息管理系统', 
    theme: 'light',
    copyright: '© 2025',
    logo: '',
    favicon: '', // ✅ state 中必须有这个字段来存储 favicon 的 URL
    sessionTimeout: 30,
  }),

  // ✅✅✅ 核心修复：精细化控制持久化策略 ✅✅✅
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'settings', // localStorage 中的键名
        storage: localStorage,
        // ✅ 明确指定只保存这些字段，'isInitialized' 被排除在外了
        paths: [
          'systemName', 
          'theme', 
          'copyright', 
          'logo', 
          'favicon', 
          'sessionTimeout'
        ],
      },
    ],
  },

  actions: {
    // 这个初始化函数现在是可靠的了
    async initializeSettings() {
      // 每次页面刷新后，isInitialized 都会是 false，所以这个 if 判断不会在首次加载时为 true
      if (this.isInitialized) {
        this.applyDisplaySettings();
        return;
      }
      // 因此，下面的网络请求必然会执行
      return this.fetchAndApplySettings();
    },

    async fetchAndApplySettings() {
      console.log("[Store] 正在从服务器获取最新的系统设置...");
      try {
        const response = await apiClient.get('/api/settings');
        // 将从服务器获取的最新数据更新到 store
        this.applyAllSettings(response.data.data); // 确保你的后端返回的数据结构是 { "code": 0, "data": { ... } }
        this.isInitialized = true; // 标记本次会话已初始化
        return response.data.data;
      } catch (error) {
        console.error("获取系统设置失败:", error);
        // 即使获取失败，也应用本地持久化的设置（作为备用方案）
        this.applyDisplaySettings();
        throw error;
      }
    },
    
    applyAllSettings(settings) {
      if (!settings) return;
      this.$patch(settings);
      this.applyDisplaySettings();
    },

    /**
     * ✅ 3. 将视觉设置应用到 DOM
     */
    applyDisplaySettings() {
      // 1. 更新页面标题
      if (this.systemName) {
        document.title = this.systemName;
      }

      // 2. 更新主题
      const root = document.documentElement;
      if (this.theme === 'dark') {
        root.setAttribute('data-theme', 'dark');
      } else {
        root.removeAttribute('data-theme');
      }

      // 3. ✅ 动态更新浏览器标签页图标 (Favicon)
      
      // 首先，尝试找到页面 <head> 中已存在的 favicon 链接
      let faviconLink = document.querySelector("link[rel~='icon']");
      
      // 如果页面上没有 <link rel="icon"> 标签，就创建一个新的
      if (!faviconLink) {
        faviconLink = document.createElement('link');
        faviconLink.rel = 'icon';
        // 将新创建的 link 标签添加到 <head> 的末尾
        document.head.appendChild(faviconLink);
      }
      
      // 现在，更新这个 link 标签的 href 属性
      if (this.favicon && this.favicon.trim() !== '') {
        // 如果 store 中存储了有效的 favicon URL，就使用它
        // 关键：确保 URL 是完整的，如果后端返回的是相对路径，需要拼接
        // 假设 apiClient.defaults.baseURL 是 'http://localhost:8080'
        // 并且后端返回的 this.favicon 是 '/uploads/xxx.png'
        const baseURL = apiClient.defaults.baseURL || window.location.origin;
        // 如果 this.favicon 已经是完整的 http 地址，则无需拼接
        faviconLink.href = this.favicon.startsWith('http') ? this.favicon : `${baseURL}${this.favicon}`;
      } else {
        // 如果没有，就使用 public 目录下的默认 favicon.ico
        // Vite 会自动处理 /favicon.ico 路径
        faviconLink.href = '/favicon.ico'; 
      }
    },

   
    /**
     * ✅ 6. 仅预览主题，不修改 state 中的 theme 值
     * 用于设置页面中主题切换的即时反馈
     */
    previewTheme(theme) {
      const root = document.documentElement;
      if (theme === 'dark') {
        root.setAttribute('data-theme', 'dark');
      } else {
        root.removeAttribute('data-theme');
      }
    },
  },
});