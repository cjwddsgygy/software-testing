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
        <div class="stat-label">收费项目数</div>
        <div class="stat-value">{{ stats.totalCharges || 0 }}</div>
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
    // 关键：调用后端统计接口
    const response = await apiClient.get('/stats/overview'); 
    
    // 根据您后端 Result 类的 code 字段判断
    if (response.data.code === 0) {
      stats.value = response.data.data;
    } else {
      // 这里的错误信息通常会通过 apiClient 的响应拦截器处理，并弹出 ElMessage
      throw new Error(response.data.message || '获取统计数据失败'); 
    }
  } catch (error) {
    console.error("HomeView 获取统计数据失败:", error);
    // ElMessage.error(error.message || '网络错误，无法加载统计数据'); // 这行通常会被拦截器处理掉
    
    // 当请求失败时，为 stats 设置默认值，避免页面显示空
    stats.value = {
      totalOlders: 'N/A',
      totalBeds: 'N/A',
      availableBeds: 'N/A',
      totalCharges: 'N/A',
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
  min-height: calc(100vh - 60px); /* Adjust based on your header height */
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
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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
.stat-card:nth-child(4) .stat-value { color: #f56c6c; } /* Red for total charges */

/* Responsive adjustments */
@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  .stat-value {
    font-size: 40px;
  }
}
</style>