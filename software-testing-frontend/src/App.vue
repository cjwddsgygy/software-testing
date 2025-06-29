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

