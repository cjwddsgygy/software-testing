<template>
  <div class="system-settings-view">
    <div class="view-header">
      <h2 class="view-title">系统设置</h2>
      <p class="view-description">配置系统的基本参数和全局选项</p>
    </div>

    <div class="settings-card">
      <div v-if="loading" class="loading-placeholder">
        <div class="loading-spinner"></div>
        <p>正在加载系统设置...</p>
      </div>
      <div v-else class="settings-form">
        <form @submit.prevent="saveSettings">
          <div class="form-section">
            <h3 class="section-title">基本信息</h3>
            <div class="form-grid">
              <div class="form-field">
                <label for="systemName">系统名称</label>
                <div class="input-wrapper">
                  <!-- 添加 @input 事件监听来实现实时预览 -->
                  <input id="systemName" v-model="settings.systemName" type="text" placeholder="请输入系统名称" required @input="onSystemNameChange">
                  <span class="field-icon"><i class="fa fa-building"></i></span>
                </div>
                <p class="field-hint">显示在浏览器标签和顶部导航栏的系统名称</p>
              </div>

              <!-- 新增：系统图标上传 -->
              <div class="form-field">
                <label for="favicon">系统图标 (Favicon)</label>
                <div class="favicon-uploader">
                  <img :src="settings.favicon" alt="图标预览" class="favicon-preview" v-if="settings.favicon" />
                   <div v-else class="favicon-placeholder">
                      <i class="fa fa-picture-o"></i>
                   </div>
                  <button type="button" class="btn btn-secondary" @click="triggerFileUpload">
                    <i class="fa fa-upload mr-2"></i>选择图标
                  </button>
                  <!-- 隐藏的文件选择框 -->
                  <input ref="fileInput" type="file" @change="handleFileSelect" accept="image/png, image/jpeg, image/x-icon, image/svg+xml" style="display: none;" />
                </div>
                <p class="field-hint">建议使用 .ico 或 32x32 的 .png 文件</p>
              </div>
            </div>
          </div>

          <!-- 其他设置项保持不变 -->
          <div class="form-section">
            <h3 class="section-title">床位管理</h3>
             <!-- ... 省略 ... -->
          </div>
          
          <div class="form-section">
            <h3 class="section-title">会话设置</h3>
             <!-- ... 省略 ... -->
          </div>

          <div class="form-actions">
            <button type="submit" class="btn btn-primary">
              <i class="fa fa-save mr-2"></i>保存设置
            </button>
            <button type="button" class="btn btn-secondary" @click="resetSettings">
              <i class="fa fa-refresh mr-2"></i>重置
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request';
import { useSettingsStore } from '@/stores/settings'; // 引入我们创建的 Store

const settingsStore = useSettingsStore();
const loading = ref(true);
const fileInput = ref(null); // 用于引用文件输入框DOM元素

// 组件本地的表单数据
const settings = reactive({
  systemName: '',
  bedWarningThreshold: 80,
  sessionTimeout: 30,
  favicon: '',
});

// 用于“重置”和“是否有修改”的判断
let originalSettings = {};

// 从后端获取设置
const fetchSettings = async () => {
  loading.value = true;
  try {
    // 调用 Store 的 action
    const data = await settingsStore.fetchAndApplySettings();
    Object.assign(settings, data); // 更新本地表单
    originalSettings = { ...data }; // 保存一份原始副本
  } catch (error) {
    ElMessage.error('无法加载系统设置，将使用默认值');
    Object.assign(settings, settingsStore.$state);
    originalSettings = { ...settingsStore.$state };
  } finally {
    loading.value = false;
  }
};

// 保存设置到后端
const saveSettings = async () => {
  // ... (前端校验保持不变)
  const hasChanges = Object.keys(settings).some(key => settings[key] !== originalSettings[key]);
  if (!hasChanges) {
    ElMessage.info('没有检测到任何修改');
    return;
  }

  try {
    // 调用真实的后端 API
    await apiClient.post('/settings', settings);
    
    // 成功后，重新从后端获取最新数据，确保状态一致
    await settingsStore.fetchAndApplySettings();
    originalSettings = { ...settings }; // 更新原始副本为刚保存的状态
    
    ElMessage.success('系统设置保存成功！');
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '保存失败，请重试');
  }
};

// 当系统名称输入框内容变化时，实时更新浏览器标题
const onSystemNameChange = (event) => {
    settingsStore.applySetting('systemName', event.target.value);
};

// 点击“选择图标”按钮，触发隐藏的文件输入框
const triggerFileUpload = () => {
  fileInput.value.click();
};

// 处理用户选择的文件
const handleFileSelect = (event) => {
  const file = event.target.files[0];
  if (!file) return;

  // 使用 FileReader 将图片文件转换为 Base64 字符串
  const reader = new FileReader();
  reader.onload = (e) => {
    const base64String = e.target.result;
    settings.favicon = base64String; // 更新本地表单的图标（用于预览）
    settingsStore.applySetting('favicon', base64String); // 实时应用到浏览器标签页
  };
  reader.readAsDataURL(file);
};

// 重置表单
const resetSettings = () => {
  Object.assign(settings, originalSettings);
  // 同时也要重置实时预览的部分
  settingsStore.applySetting('systemName', originalSettings.systemName);
  settingsStore.applySetting('favicon', originalSettings.favicon);
};

// 组件挂载时获取设置
onMounted(fetchSettings);
</script>

<style scoped>
/* ... (您已有的样式都很好，无需修改) ... */

/* 新增：图标上传器样式 */
.favicon-uploader {
  display: flex;
  align-items: center;
  gap: 15px;
}

.favicon-preview {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  border: 1px solid #e5e7eb;
  object-fit: contain;
}

.favicon-placeholder {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  border: 1px dashed #d1d5db;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
  font-size: 16px;
}
</style>