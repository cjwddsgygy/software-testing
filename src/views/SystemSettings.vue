<!-- START OF FILE SystemSettings.vue -->
<template>
  <div class="settings-view">
    <h2 class="view-title">系统设置</h2>
    <div class="settings-form">
      <div class="form-item">
        <label for="sys-name">系统名称</label>
        <input type="text" id="sys-name" value="养老院信息管理系统">
      </div>
      <div class="form-item">
        <label for="alert-threshold">床位预警阈值</label>
        <input type="number" id="alert-threshold" value="10">
      </div>
       <div class="form-item">
        <label for="session-timeout">会话超时时间(分钟)</label>
        <input type="number" id="session-timeout" value="30">
      </div>
      <button class="btn btn-primary">保存设置</button>
    </div>
  </div>
</template>

<script setup></script>

<style scoped>
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.settings-form { max-width: 600px; background: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
.form-item { margin-bottom: 20px; }
.form-item label { display: block; margin-bottom: 8px; font-weight: bold; color: #555; }
.form-item input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.btn { padding: 10px 20px; border: none; border-radius: 4px; color: #fff; cursor: pointer; }
.btn-primary { background-color: #409eff; }
</style>
<!-- END OF FILE SystemSettings.vue -->