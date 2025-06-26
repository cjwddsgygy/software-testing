<!-- START OF FILE BedManagement.vue -->
<template>
  <div class="bed-management-view">
    <h2 class="view-title">床位管理</h2>
    <div class="bed-grid">
      <div class="bed occupied">A-101 (张三)</div>
      <div class="bed available">A-102 (空)</div>
      <div class="bed occupied">A-103 (王五)</div>
      <div class="bed available">A-104 (空)</div>
      <div class="bed occupied">B-201 (赵六)</div>
      <div class="bed occupied">B-202 (孙七)</div>
      <div class="bed occupied">B-203 (李四)</div>
      <div class="bed available">B-204 (空)</div>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.bed-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(150px, 1fr)); gap: 15px; }
.bed { padding: 20px; border-radius: 6px; text-align: center; font-weight: bold; color: #fff; box-shadow: 0 1px 4px rgba(0,0,0,0.1); cursor: pointer; transition: transform 0.2s; }
.bed:hover { transform: translateY(-3px); }
.bed.occupied { background-color: #f56c6c; }
.bed.available { background-color: #67c23a; }
</style>
<!-- END OF FILE BedManagement.vue -->