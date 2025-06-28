import { defineStore } from 'pinia';
import apiClient from '@/api/request';

// 这是一个辅助函数，用于更新浏览器标签页的图标
const updateFavicon = (faviconUrl) => {
  if (!faviconUrl) return;
  // 查找 head 中已存在的 icon link 标签
  let link = document.querySelector("link[rel~='icon']");
  if (!link) {
    // 如果不存在，则创建一个新的
    link = document.createElement('link');
    link.rel = 'icon';
    document.head.appendChild(link);
  }
  // 更新其 href 属性
  link.href = faviconUrl;
};

export const useSettingsStore = defineStore('settings', {
  // 1. 定义 state，提供默认值
  state: () => ({
    systemName: '智慧养老院管理系统',
    bedWarningThreshold: 80,
    sessionTimeout: 30,
    favicon: '', // 默认图标的URL或Base64字符串
  }),

  // 2. 定义 actions，用于处理业务逻辑和异步请求
  actions: {
    // 从后端获取设置并应用到全局
    async fetchAndApplySettings() {
      try {
        const response = await apiClient.get('/api/settings');
        if (response.data.code === 0 && response.data.data) {
          const settings = response.data.data;

          // 更新 Store 的 state
          this.systemName = settings.systemName || this.systemName;
          this.bedWarningThreshold = Number(settings.bedWarningThreshold) || this.bedWarningThreshold;
          this.sessionTimeout = Number(settings.sessionTimeout) || this.sessionTimeout;
          this.favicon = settings.favicon || this.favicon;

          // 将设置应用到 DOM
          document.title = this.systemName;
          updateFavicon(this.favicon);
          
          return settings; // 将数据返回给调用方（组件）
        }
      } catch (error) {
        console.error("获取系统设置失败:", error);
        // 如果失败，应用默认值
        document.title = this.systemName;
        updateFavicon(this.favicon);
      }
      return this.$state;
    },
    
    // 实时应用单个设置的更改（用于预览）
    applySetting(key, value) {
        if (this.hasOwnProperty(key)) {
            this[key] = value;
        }
        if (key === 'systemName') {
            document.title = value;
        }
        if (key === 'favicon') {
            updateFavicon(value);
        }
    }
  },
});