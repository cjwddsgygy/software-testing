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
import apiClient from '@/api/request'; // 假设您的API请求工具

const stats = ref({});
const loading = ref(true);

const fetchStats = async () => {
  loading.value = true;
  try {
    // 模拟API请求
    const response = await new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: {
            code: 0,
            data: {
              totalOlders: 125,
              totalBeds: 150,
              availableBeds: 25,
              totalCharges: 12,
              // 可以添加更多统计数据
              occupiedBeds: 125,
              careworkerCount: 18,
            }
          }
        });
      }, 800); // 模拟网络延迟
    });

    if (response.data.code === 0) {
      stats.value = response.data.data;
    } else {
      throw new Error(response.data.msg || '获取统计数据失败');
    }
  } catch (error) {
    ElMessage.error(error.message || '网络错误，无法加载统计数据');
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