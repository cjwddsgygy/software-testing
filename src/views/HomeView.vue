<!-- 文件路径: src/views/HomeView.vue -->
<template>
  <div class="home-view">
    <h2 class="view-title">系统概览</h2>

    <div v-if="loading" class="loading-text">正在从服务器加载统计数据...</div>
    <div v-else class="stats-grid">
      <div class="stat-card">
        <div class="stat-label">在院老人总数</div>
        <div class="stat-value">{{ stats.totalOlders || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">床位总数</div>
        <div class="stat-value">{{ stats.totalBeds || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">空余床位</div>
        <div class="stat-value">{{ stats.availableBeds || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">收费记录总数</div>
        <div class="stat-value">{{ stats.totalExpenses || 0 }}</div>
      </div>
      <!-- ✅ 新增：护工总数卡片 -->
      <div class="stat-card">
        <div class="stat-label">护工总数</div>
        <div class="stat-value">{{ stats.totalCareworkers || 0 }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request'; // 您的API请求工具

const stats = ref({});
const loading = ref(true);

const fetchStats = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/api/stats/overview'); 
    
    if (response.data.code === 0) {
      stats.value = response.data.data;
    } else {
      throw new Error(response.data.msg || '获取统计数据失败'); 
    }
  } catch (error) {
    console.error("HomeView 获取统计数据失败:", error);
    stats.value = {
      totalOlders: 'N/A',
      totalBeds: 'N/A',
      availableBeds: 'N/A',
      totalExpenses: 'N/A',
      totalCareworkers: 'N/A', // ✅ 新增：失败时也显示 N/A
    };
  } finally {
    loading.value = false;
  }
};

onMounted(fetchStats);
</script>

<style scoped>
/* 样式保持不变 */
.home-view {
  padding: 20px;
  font-family: 'Arial', sans-serif;
  background-color: #f8f9fa;
  min-height: calc(100vh - 60px);
}

.view-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 30px;
  text-align: center;
}

.loading-text {
  font-size: 18px;
  color: #888;
  text-align: center;
  padding: 50px;
}

.stats-grid {
  /* 调整列数，以适应新增的卡片 */
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); /* 略微缩小 minmax 宽度，确保能容纳5列 */
  gap: 30px;
}

.stat-card {
  background: #fff;
  border-radius: 10px;
  padding: 25px 30px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
  text-align: center;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  min-height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(0,0,0,0.12);
}

.stat-label {
  font-size: 18px;
  color: #666;
  margin-bottom: 15px;
  font-weight: bold;
}

.stat-value {
  font-size: 48px;
  font-weight: bold;
  color: #409eff; /* Primary color */
  line-height: 1;
}

/* Specific colors for different stats if needed */
.stat-card:nth-child(1) .stat-value { color: #409eff; } /* Blue for total elders */
.stat-card:nth-child(2) .stat-value { color: #67c23a; } /* Green for total beds */
.stat-card:nth-child(3) .stat-value { color: #e6a23c; } /* Orange for available beds */
.stat-card:nth-child(4) .stat-value { color: #f56c6c; } /* Red for total Expenses */
.stat-card:nth-child(5) .stat-value { color: #8a2be2; } /* 紫色 for total Careworkers (新增) */


/* Responsive adjustments */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); /* 调整移动端布局 */
  }
  .stat-value {
    font-size: 40px;
  }
}
</style>