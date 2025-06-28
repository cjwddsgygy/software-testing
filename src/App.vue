<template>
  <!-- router-view 会根据当前路由动态渲染对应的页面组件 -->
  <router-view />
</template>

<script setup>
import { onMounted } from 'vue';
import { useSettingsStore } from '@/stores/settings'; // 1. 引入我们创建的 settings store

// 2. 获取 settings store 的实例
const settingsStore = useSettingsStore();

// 3. 在组件挂载到 DOM 后，执行初始化操作
onMounted(() => {
  // 调用 store 中的 action 来从后端获取最新的系统设置，并应用到全局
  // 这个操作会在应用启动时执行一次，确保系统名称和图标是最新的。
  settingsStore.fetchAndApplySettings();
});
</script>

<style>
/* 这里可以放一些全局样式，重置浏览器默认边距 */
body, html {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB',
    'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  /* 也可以在这里设置一些全局的背景色等 */
  background-color: #f0f2f5;
}
</style>