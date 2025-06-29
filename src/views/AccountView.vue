<template>
  <div class="account-view">
    <div class="view-header">
      <h2 class="view-title">账户管理</h2>
      <p class="view-description">管理您的个人账户信息</p>
    </div>

    <!-- 使用 v-if, v-else-if, v-else 来根据角色渲染不同的组件或内容 -->
    
    <!-- 情况一：如果是管理员 (Admin) -->
    <div v-if="authStore.isAdmin" class="account-content admin-panel">
      <h3>管理员面板</h3>
      <p>欢迎，管理员 **{{ authStore.user?.name }}**！</p>
      <p>在这里，您可以管理整个系统的用户、角色和权限。</p>
      <!-- TODO: 在这里放入管理员专属的管理组件，例如用户列表、权限分配等 -->
      <div class="feature-placeholder">系统用户管理模块 (开发中...)</div>
    </div>

    <!-- 情况二：如果是护工 (CareWorker) -->
    <div v-else-if="authStore.isCareWorker" class="account-content careworker-panel">
      <h3>护工个人中心</h3>
      <p>欢迎，护工 **{{ authStore.user?.name }}**！</p>
      <p>在这里，您可以查看您的排班、负责的老人列表，并修改个人密码。</p>
      <div class="form-field">
        <label for="change-password">修改密码</label>
        <input id="change-password" type="password" placeholder="输入新密码">
      </div>
       <button class="btn btn-primary">确认修改</button>
    </div>

    <!-- 情况三：如果是老人 (Elder) -->
    <div v-else-if="authStore.isElder" class="account-content elder-panel">
      <h3>老人个人中心</h3>
      <p>欢迎您，**{{ authStore.user?.name }}**！</p>
      <p>在这里，您可以查看您的亲属联系方式，并修改登录密码。</p>
      <div class="info-item">
        <strong>亲属联系方式:</strong>
        <span>{{ authStore.user?.relativeContact || '未设置' }}</span>
      </div>
    </div>
    
    <!-- 默认情况或加载中 -->
    <div v-else class="account-content">
      <p>正在加载账户信息...</p>
    </div>

  </div>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';

// 获取 auth store 实例，模板中可以直接使用
const authStore = useAuthStore();
</script>

<style scoped>
/* 为 AccountView 添加一些基础样式 */
.account-view {
  padding: 25px;
  background-color: var(--bg-page);
}
.view-header {
  text-align: center;
  margin-bottom: 30px;
}
.view-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: var(--text-primary);
}
.view-description {
  font-size: 1rem;
  color: var(--text-regular);
  margin-top: 8px;
}

.account-content {
  max-width: 800px;
  margin: 0 auto;
  padding: 30px;
  background-color: var(--bg-card);
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.account-content h3 {
  font-size: 1.5rem;
  margin-top: 0;
  margin-bottom: 20px;
  border-bottom: 1px solid var(--border-color);
  padding-bottom: 15px;
}
.form-field, .info-item {
  margin-bottom: 20px;
}
.form-field label, .info-item strong {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
}
.form-field input {
  width: 100%;
  max-width: 400px;
  padding: 10px;
  border-radius: 6px;
  border: 1px solid var(--border-color);
}
.feature-placeholder {
  padding: 50px;
  text-align: center;
  background-color: var(--bg-page);
  border-radius: 8px;
  color: var(--text-secondary);
  border: 2px dashed var(--border-color);
}

/* 可以为不同角色的面板添加特定颜色以作区分 */
.admin-panel h3 { color: var(--danger-color); }
.careworker-panel h3 { color: var(--primary-color); }
.elder-panel h3 { color: var(--success-color); }
</style>