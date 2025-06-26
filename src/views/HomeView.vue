<!-- START OF FILE HomeView.vue -->
<template>
  <div class="home-view">
    <h2 class="view-title">系统概览</h2>
    <div v-if="loading" class="loading-text">正在从服务器加载统计数据...</div>
    <div v-else class="stats-cards">
      <div class="card">
        <h3>在院老人总数</h3>
        <p class="stat-number">{{ stats.totalOlders || 0 }}</p>
      </div>
      <div class="card">
        <h3>床位总数</h3>
        <p class="stat-number">{{ stats.totalBeds || 0 }}</p>
      </div>
      <div class="card">
        <h3>空余床位</h3>
        <p class="stat-number">{{ stats.availableBeds || 0 }}</p>
      </div>
       <div class="card">
        <h3>收费项目数</h3>
        <p class="stat-number">{{ stats.totalCharges || 0 }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import apiClient from '../api'; // 使用全局配置

const stats = ref({});
const loading = ref(true);

onMounted(async () => {
  try {
    const response = await apiClient.get('/admins/statistics');
    // 注意：这里的键名需要和你后端 AdminServiceImpl.java 中 getStatistics() 方法返回的Map的键名完全一致
    stats.value = response.data.data;
  } catch (error) {
    console.error('获取统计数据失败:', error);
  } finally {
    loading.value = false;
  }
});
</script>

<style scoped>
/* 样式保持不变 */
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.loading-text { font-size: 18px; color: #888; text-align: center; padding: 40px; }
.stats-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; }
.card { background: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); text-align: center; }
.card h3 { margin: 0 0 10px 0; font-size: 16px; color: #666; }
.stat-number { margin: 0; font-size: 36px; font-weight: bold; color: #409eff; }
</style>
<!-- END OF FILE HomeView.vue -->