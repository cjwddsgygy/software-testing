<!-- START OF FILE HomeView.vue -->
<template>
  <div class="home-view">
    <h2 class="view-title">系统概览</h2>
    <div class="stats-cards">
      <div class="card">
        <h3>在院老人总数</h3>
        <p class="stat-number">128</p>
      </div>
      <div class="card">
        <h3>空余床位</h3>
        <p class="stat-number">22</p>
      </div>
      <div class="card">
        <h3>今日待办事项</h3>
        <p class="stat-number">5</p>
      </div>
       <div class="card">
        <h3>一级护理人数</h3>
        <p class="stat-number">35</p>
      </div>
    </div>
  </div>
</template>

<script setup>
</script>

<style scoped>
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.stats-cards { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 20px; }
.card { background: #fff; padding: 25px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); text-align: center; }
.card h3 { margin: 0 0 10px 0; font-size: 16px; color: #666; }
.stat-number { margin: 0; font-size: 36px; font-weight: bold; color: #409eff; }
</style>
<!-- END OF FILE HomeView.vue -->