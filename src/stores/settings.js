// 文件路径: src/stores/settings.js (请确保你的文件是这个版本)

import { defineStore } from 'pinia';
import apiClient from '@/api/request';

export const useSettingsStore = defineStore('settings', {
  state: () => ({
    // isInitialized 反映当前会话状态，初始值永远是 false，且不应被持久化
    isInitialized: false, 
    
    // 这些是需要持久化的默认值，当后端请求失败时会使用
    systemName: '养老院信息管理系统', 
    theme: 'light',
    copyright: '© 2025',
    logo: '',
    favicon: '',
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
      document.title = this.systemName;
      const root = document.documentElement;
      if (this.theme === 'dark') {
        root.setAttribute('data-theme', 'dark');
      } else {
        root.removeAttribute('data-theme');
      }
       // 3. ✅ 动态更新 Favicon
      // 查找 head 中现有的 favicon link 标签
      let faviconLink = document.querySelector("link[rel~='icon']");
      
      // 如果不存在，就创建一个新的 link 标签
      if (!faviconLink) {
        faviconLink = document.createElement('link');
        faviconLink.rel = 'icon';
        document.head.appendChild(faviconLink);
      }
      
      // 如果 store 中有 favicon 的 URL，就更新 href，否则使用默认的
      faviconLink.href = this.favicon || '/favicon.ico'; // 假设你的 public 目录下有一个默认的 favicon.ico
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