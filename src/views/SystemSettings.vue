<!-- 文件路径: src/views/SystemSettings.vue -->
<template>
  <div class="system-settings-view">
    <h2 class="view-title">系统设置</h2>

    <div v-if="loading" class="loading-text">正在加载系统设置...</div>
    <div v-else class="settings-form-container">
      <form @submit.prevent="saveSettings" class="settings-form">
        <div class="form-item">
          <label for="systemName">系统名称</label>
          <input id="systemName" v-model="settings.systemName" type="text" placeholder="请输入系统名称" required>
        </div>
        <div class="form-item">
          <label for="bedWarningThreshold">床位预警阈值 (%)</label>
          <input id="bedWarningThreshold" v-model.number="settings.bedWarningThreshold" type="number" min="0" max="100" placeholder="请输入0-100的百分比" required>
        </div>
        <div class="form-item">
          <label for="sessionTimeout">会话超时时间(分钟)</label>
          <input id="sessionTimeout" v-model.number="settings.sessionTimeout" type="number" min="1" placeholder="请输入分钟数" required>
        </div>
        
        <div class="form-actions">
          <button type="submit" class="btn btn-primary">保存设置</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request';

const loading = ref(true);
const settings = reactive({
  systemName: '',
  bedWarningThreshold: null,
  sessionTimeout: null,
});

const fetchSettings = async () => {
  loading.value = true;
  try {
    // 模拟API请求
    const response = await new Promise(resolve => {
      setTimeout(() => {
        resolve({
          data: {
            code: 0,
            data: {
              systemName: '智慧养老院管理系统',
              bedWarningThreshold: 80, // 80% occupancy
              sessionTimeout: 30, // 30 minutes
            }
          }
        });
      }, 500);
    });

    if (response.data.code === 0 && response.data.data) {
      Object.assign(settings, response.data.data);
    } else {
      throw new Error(response.data.msg || '获取系统设置失败');
    }
  } catch (error) {
    ElMessage.error(error.message || '网络错误，无法加载系统设置');
  } finally {
    loading.value = false;
  }
};

const saveSettings = async () => {
  try {
    // 简单的前端校验
    if (!settings.systemName.trim()) {
      ElMessage.error('系统名称不能为空');
      return;
    }
    if (settings.bedWarningThreshold === null || settings.bedWarningThreshold < 0 || settings.bedWarningThreshold > 100) {
      ElMessage.error('床位预警阈值必须是0-100之间的数字');
      return;
    }
    if (settings.sessionTimeout === null || settings.sessionTimeout < 1) {
      ElMessage.error('会话超时时间必须是大于0的分钟数');
      return;
    }

    // 模拟API请求
    // await apiClient.post('/settings', settings); // 实际API调用
    await new Promise(resolve => setTimeout(resolve, 300));
    console.log('保存设置:', settings);
    ElMessage.success('系统设置保存成功！');
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '保存失败，请重试');
  }
};

onMounted(fetchSettings);
</script>

<style scoped>
.system-settings-view {
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

.settings-form-container {
  max-width: 600px;
  margin: 0 auto;
  background: #fff;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.08);
}

.settings-form .form-item {
  margin-bottom: 25px;
}

.settings-form label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
  color: #555;
  font-size: 16px;
}

.settings-form input[type="text"],
.settings-form input[type="number"] {
  width: 100%;
  padding: 12px 15px;
  border: 1px solid #ddd;
  border-radius: 6px;
  font-size: 16px;
  box-sizing: border-box;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.settings-form input[type="text"]:focus,
.settings-form input[type="number"]:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
  outline: none;
}

.form-actions {
  text-align: right;
  margin-top: 40px;
}

.btn {
  padding: 12px 25px;
  border: none;
  border-radius: 6px;
  color: #fff;
  cursor: pointer;
  font-size: 18px;
  transition: background-color 0.3s ease, transform 0.2s ease;
}

.btn-primary {
  background-color: #409eff;
}

.btn-primary:hover {
  background-color: #66b1ff;
  transform: translateY(-1px);
}
</style>