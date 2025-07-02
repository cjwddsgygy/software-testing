<template>
  <div class="account-view">
    <div class="view-header">
      <h2 class="view-title">账户管理</h2>
      <p class="view-description">管理您的个人账户信息</p>
    </div>

    <div v-if="loading" class="loading-placeholder"><div class="loading-spinner"></div></div>
    <div v-else-if="error" class="error-panel"><p>加载用户信息失败：{{ error }}</p></div>

    <div v-else class="account-content" :class="panelClass">
      <h3>{{ panelTitle }}</h3>
      <p>欢迎，<strong>{{ localUser.name }}</strong>！在这里您可以更新您的个人资料。</p>
      
      <form @submit.prevent="handleUpdateSubmit" class="account-form">
        
        <!-- ==================== 管理员可编辑字段 ==================== -->
        <div v-if="authStore.isAdmin" class="form-section">
          <h4>基础信息</h4>
          <div class="form-grid">
            <div class="form-field"><label for="account">登录账号</label><input id="account" type="text" :value="localUser.account" disabled></div>
            <div class="form-field"><label for="name">姓名</label><input id="name" type="text" v-model="localUser.name"></div>
          </div>
        </div>

        <!-- ==================== 护工可编辑字段 ==================== -->
        <div v-if="authStore.isCareWorker" class="form-section">
          <h4>个人档案</h4>
          <div class="form-grid">
            <div class="form-field"><label>登录账号</label><input :value="localUser.account" disabled></div>
            <div class="form-field"><label>姓名</label><input v-model="localUser.name"></div>
            <div class="form-field"><label>年龄</label><input type="number" v-model.number="localUser.age"></div>
            <div class="form-field"><label>出生日期</label><input type="date" v-model="localUser.birthDate"></div>
            <div class="form-field"><label>民族</label><input v-model="localUser.ethnicity"></div>
            <div class="form-field"><label>学历</label><input v-model="localUser.education"></div>
            <div class="form-field full-span"><label>工作经验</label><textarea v-model="localUser.experience" rows="3"></textarea></div>
            <div class="form-field full-span"><label>个人特长</label><textarea v-model="localUser.specialties" rows="3"></textarea></div>
          </div>
        </div>
        
        <!-- ==================== 老人可编辑字段 ==================== -->
        <div v-if="authStore.isElder" class="form-section">
          <h4>个人档案</h4>
          <div class="form-grid">
            <div class="form-field"><label>登录账号</label><input :value="localUser.account" disabled></div>
            <div class="form-field"><label>姓名</label><input v-model="localUser.name"></div>
            <div class="form-field"><label>年龄</label><input type="number" v-model.number="localUser.age"></div>
            <div class="form-field"><label>出生日期</label><input type="date" v-model="localUser.birthDate"></div>
            <div class="form-field"><label>民族</label><input v-model="localUser.ethnicity"></div>
            <div class="form-field"><label>学历</label><input v-model="localUser.education"></div>
            <div class="form-field"><label>婚姻状况</label><input v-model="localUser.maritalStatus"></div>
            <div class="form-field"><label>亲属联系方式</label><input v-model="localUser.relativeContact"></div>
            <div class="form-field full-span"><label>个人爱好</label><textarea v-model="localUser.hobbies" rows="3"></textarea></div>
          </div>
        </div>

        <!-- ==================== 通用：修改密码 ==================== -->
        <div class="form-section">
          <h4>修改密码</h4>
          <div class="form-grid">
            <div class="form-field"><label>新密码</label><input type="password" v-model="passwordForm.newPassword" placeholder="留空则不修改"></div>
            <div class="form-field"><label>确认新密码</label><input type="password" v-model="passwordForm.confirmPassword"></div>
          </div>
        </div>

        <!-- ==================== 提交按钮 ==================== -->
        <div class="form-actions">
           <button type="submit" class="btn btn-primary" :disabled="isSubmitting">
            <span v-if="!isSubmitting"><i class="fas fa-save"></i> 保存更改</span>
            <span v-else>保存中...</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { useAuthStore } from '@/stores/auth';
import apiClient from '@/api/request';
import { ElMessage, ElNotification } from 'element-plus';

const authStore = useAuthStore();
const loading = ref(true);
const error = ref(null);
const isSubmitting = ref(false);
const localUser = reactive({});
const passwordForm = reactive({ newPassword: '', confirmPassword: '' });

onMounted(async () => {
  loading.value = true;
  error.value = null;
  try {
    const response = await apiClient.get('/api/account/me');
    if (response.data.code === 0 && response.data.data) {
      const userData = response.data.data;
      // 格式化日期字段，以适配 <input type="date">
      if (userData.birthDate) {
        userData.birthDate = userData.birthDate.split('T')[0];
      }
      Object.assign(localUser, userData);
    } else {
      throw new Error(response.data.msg || '无法获取您的个人信息');
    }
  } catch (err) {
    error.value = err.message;
    ElMessage.error('加载用户信息失败！');
  } finally {
    loading.value = false;
  }
});

const panelTitle = computed(() => {
  if (authStore.isAdmin) return '管理员个人资料';
  if (authStore.isCareWorker) return '护工个人资料';
  if (authStore.isElder) return '老人个人资料';
  return '账户信息';
});

const handleUpdateSubmit = async () => {
  if (passwordForm.newPassword !== passwordForm.confirmPassword) {
    return ElMessage.error('两次输入的新密码不一致！');
  }

  isSubmitting.value = true;
  
  // ✅✅✅ 核心修复：构建一个包含所有可编辑字段的 updates 对象 ✅✅✅
  const updates = { ...localUser }; // 先复制所有字段
  delete updates.id; // 不应提交 ID
  delete updates.account; // 账号不可修改
  delete updates.createdAt; // 不应提交
  delete updates.updatedAt; // 不应提交

  if (passwordForm.newPassword) {
    updates.password = passwordForm.newPassword;
  } else {
    delete updates.password; // 如果密码为空，则不提交该字段
  }

  try {
    const response = await apiClient.put('/api/account/me', updates);
    if (response.data.code === 0) {
      authStore.user = response.data.data;
      ElNotification.success('您的账户信息已更新！');
      passwordForm.newPassword = '';
      passwordForm.confirmPassword = '';
    } else {
      throw new Error(response.data.msg);
    }
  } catch (err) {
    ElMessage.error(`更新失败：${err.message}`);
  } finally {
    isSubmitting.value = false;
  }
};
</script>

<style scoped>
/* 您的样式可以保持不变，这里只提供一些基础和表单样式 */
.account-view { padding: 25px; background-color: var(--bg-page); }
.view-header { text-align: center; margin-bottom: 30px; }
.view-title { font-size: 1.75rem; font-weight: 600; color: var(--text-primary); }
.view-description { font-size: 1rem; color: var(--text-regular); margin-top: 8px; }

.account-content { max-width: 800px; margin: 0 auto; padding: 30px; background-color: var(--bg-card); border-radius: 12px; box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05); }
.account-content h3 { font-size: 1.5rem; margin-top: 0; margin-bottom: 20px; border-bottom: 1px solid var(--border-color); padding-bottom: 15px; }

.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
.form-field.full-span { grid-column: 1 / -1; }
.form-section { margin-top: 30px; padding-top: 20px; border-top: 1px dashed var(--border-color); }
.form-section h4 { margin-top: 0; margin-bottom: 20px; font-size: 1.2rem; }
.form-field { display: flex; flex-direction: column; margin-bottom: 10px; }
.form-field label { margin-bottom: 8px; font-weight: 500; }
.form-field input, .form-field textarea { width: 100%; padding: 10px; border-radius: 6px; border: 1px solid var(--border-color); background-color: var(--bg-page); box-sizing: border-box; }
.form-field input:disabled { background-color: #f5f5f5; cursor: not-allowed; opacity: 0.7; }
.form-actions { text-align: right; margin-top: 30px; }
.btn { padding: 10px 20px; border-radius: 6px; border: none; color: white; cursor: pointer; display: inline-flex; align-items: center; gap: 8px; }
.btn-primary { background-color: var(--primary-color); }
.loading-placeholder, .error-panel { text-align: center; padding: 50px; }
.loading-spinner { width: 40px; height: 40px; border: 4px solid #f0f2f5; border-top-color: var(--primary-color); border-radius: 50%; animation: spin 1s linear infinite; margin: 0 auto; }
@keyframes spin { to { transform: rotate(360deg); } }
</style>