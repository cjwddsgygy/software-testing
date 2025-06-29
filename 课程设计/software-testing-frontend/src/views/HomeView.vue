<template>
  <div class="home-view">
    <!-- 1. 动态欢迎语 -->
    <div class="welcome-header">
      <h1 class="view-title">系统数据驾驶舱</h1>
      <p class="view-description">实时洞察养老院核心运营指标</p>
    </div>

    <!-- 2. 骨架屏加载效果 -->
    <div v-if="loading" class="stats-grid">
      <div v-for="i in 5" :key="i" class="stat-card skeleton">
        <div class="skeleton-icon"></div>
        <div class="skeleton-text"></div>
        <div class="skeleton-value"></div>
      </div>
    </div>

    <!-- 3. 真实数据卡片 -->
    <div v-else class="stats-grid">
      <!-- 在院老人 -->
      <div class="stat-card card-elders">
        <div class="card-icon"><i class="fas fa-users"></i></div>
        <div class="stat-value">{{ stats.totalOlders ?? 0 }}</div>
        <div class="stat-label">在院老人总数</div>
      </div>
      <!-- 护工总数 -->
      <div class="stat-card card-careworkers">
        <div class="card-icon"><i class="fas fa-user-nurse"></i></div>
        <div class="stat-value">{{ stats.totalCareworkers ?? 0 }}</div>
        <div class="stat-label">在职护工总数</div>
      </div>
      <!-- 床位利用率 -->
      <div class="stat-card card-beds">
        <div class="card-icon"><i class="fas fa-bed-pulse"></i></div>
        <div class="stat-value">{{ bedUsageRate }}<span>%</span></div>
        <div class="stat-label">床位利用率</div>
        <div class="stat-sublabel">
          {{ stats.totalBeds - stats.availableBeds }} / {{ stats.totalBeds }}
        </div>
      </div>
      <!-- 空余床位 -->
      <div class="stat-card card-available-beds">
        <div class="card-icon"><i class="fas fa-door-open"></i></div>
        <div class="stat-value">{{ stats.availableBeds ?? 0 }}</div>
        <div class="stat-label">空余床位数</div>
      </div>
      <!-- 消费记录 -->
      <div class="stat-card card-expenses">
        <div class="card-icon"><i class="fas fa-wallet"></i></div>
        <div class="stat-value">{{ stats.totalExpenses ?? 0 }}</div>
        <div class="stat-label">本月消费记录</div>
      </div>
    </div>

    <!-- 可以在这里添加图表等其他模块 -->
    <!-- <div class="charts-section"> ... </div> -->
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import apiClient from '@/api/request';

const stats = ref({});
const loading = ref(true);

// 4. 新增一个计算属性，用于计算床位利用率
const bedUsageRate = computed(() => {
  if (!stats.value.totalBeds || stats.value.totalBeds === 0) {
    return '0.0';
  }
  const usedBeds = stats.value.totalBeds - stats.value.availableBeds;
  const rate = (usedBeds / stats.value.totalBeds) * 100;
  return rate.toFixed(1); // 保留一位小数
});

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
    // 失败时给默认值，防止页面因 undefined 报错
    stats.value = { totalOlders: 0, totalBeds: 0, availableBeds: 0, totalExpenses: 0, totalCareworkers: 0 };
  } finally {
    loading.value = false;
  }
};

onMounted(fetchStats);
</script>

<style scoped>
/* --- 整体布局与背景 --- */
.home-view {
  padding: 30px;
  min-height: calc(100vh - 60px); /* 60px 是假设的顶部导航栏高度 */
  background-color: var(--bg-page);
  position: relative;
  overflow: hidden;
}
/* 添加微妙的网格背景 */
.home-view::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image: 
    linear-gradient(rgba(var(--primary-rgb), 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(var(--primary-rgb), 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
  z-index: 0;
  pointer-events: none;
}

/* --- 欢迎语 --- */
.welcome-header {
  text-align: center;
  margin-bottom: 40px;
  position: relative;
  z-index: 1;
}
.view-title {
  font-size: 2.25rem;
  font-weight: 700;
  color: var(--text-primary);
  text-shadow: 0 0 15px rgba(var(--primary-rgb), 0.2);
}
.view-description {
  font-size: 1.1rem;
  color: var(--text-secondary);
  margin-top: 10px;
}

/* --- 卡片网格布局 --- */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
  gap: 30px;
  position: relative;
  z-index: 1;
}

/* --- 数据卡片通用样式 --- */
.stat-card {
  background: rgba(var(--bg-card-rgb), 0.7);
  border-radius: 16px;
  padding: 25px;
  border: 1px solid rgba(var(--primary-rgb), 0.15);
  backdrop-filter: blur(10px);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 左对齐 */
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  position: relative;
  overflow: hidden;
}
.stat-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 40px rgba(var(--primary-rgb), 0.2);
  border-color: rgba(var(--primary-rgb), 0.4);
}
/* 卡片右上角图标 */
.card-icon {
  position: absolute;
  top: 20px;
  right: 25px;
  font-size: 2rem;
  opacity: 0.1;
  transition: opacity 0.3s ease;
}
.stat-card:hover .card-icon {
  opacity: 0.2;
}

/* 卡片数据值 */
.stat-value {
  font-size: 3rem;
  font-weight: 700;
  line-height: 1;
  margin-bottom: 10px;
  display: flex;
  align-items: baseline; /* 让百分号对齐 */
  color: var(--text-primary);
}
.stat-value span {
  font-size: 1.5rem;
  font-weight: 600;
  margin-left: 5px;
  color: var(--text-secondary);
}

/* 卡片标签 */
.stat-label {
  font-size: 1rem;
  font-weight: 500;
  color: var(--text-secondary);
}
.stat-sublabel {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin-top: 5px;
  background-color: rgba(var(--primary-rgb), 0.05);
  padding: 2px 8px;
  border-radius: 4px;
}

/* --- 不同卡片的专属配色 --- */
.card-elders .stat-value { color: #3b82f6; }
.card-careworkers .stat-value { color: #10b981; }
.card-beds .stat-value { color: #f97316; }
.card-available-beds .stat-value { color: #8b5cf6; }
.card-expenses .stat-value { color: #ef4444; }

/* --- 骨架屏加载效果 --- */
.stat-card.skeleton {
  pointer-events: none;
}
.skeleton-icon, .skeleton-text, .skeleton-value {
  background: linear-gradient(90deg, 
    rgba(var(--primary-rgb), 0.05) 25%, 
    rgba(var(--primary-rgb), 0.1) 50%, 
    rgba(var(--primary-rgb), 0.05) 75%);
  background-size: 200% 100%;
  animation: skeleton-loading 1.5s infinite linear;
  border-radius: 8px;
}
.skeleton-icon {
  width: 32px;
  height: 32px;
  position: absolute;
  top: 20px;
  right: 25px;
}
.skeleton-value {
  width: 60%;
  height: 48px;
  margin-bottom: 15px;
}
.skeleton-text {
  width: 80%;
  height: 24px;
}
@keyframes skeleton-loading {
  0% { background-position: 200% 0; }
  100% { background-position: -200% 0; }
}

</style>