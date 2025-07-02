<template>
  <div class="change-password-form">
    <h4>修改密码</h4>
    <form @submit.prevent="handleSubmit">
      <div class="form-field">
        <label for="oldPassword">旧密码</label>
        <input id="oldPassword" type="password" v-model="passwords.oldPassword" required placeholder="请输入您的当前密码">
      </div>
      <div class="form-field">
        <label for="newPassword">新密码</label>
        <input id="newPassword" type="password" v-model="passwords.newPassword" required placeholder="请输入新密码">
      </div>
      <div class="form-field">
        <label for="confirmPassword">确认新密码</label>
        <input id="confirmPassword" type="password" v-model="passwords.confirmPassword" required placeholder="请再次输入新密码">
      </div>
      <button type="submit" class="btn btn-primary" :disabled="loading">
        {{ loading ? '提交中...' : '确认修改' }}
      </button>
    </form>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request';

const passwords = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});
const loading = ref(false);

const handleSubmit = async () => {
  if (passwords.newPassword !== passwords.confirmPassword) {
    ElMessage.error('两次输入的新密码不一致！');
    return;
  }
  if (!passwords.newPassword || passwords.newPassword.length < 6) {
    ElMessage.error('新密码长度不能少于6位！');
    return;
  }
  
  loading.value = true;
  try {
    await apiClient.put('/api/account/password', {
      oldPassword: passwords.oldPassword,
      newPassword: passwords.newPassword,
    });
    ElMessage.success('密码修改成功！下次登录请使用新密码。');
    // 清空表单
    passwords.oldPassword = '';
    passwords.newPassword = '';
    passwords.confirmPassword = '';
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '密码修改失败，请检查旧密码是否正确。');
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.change-password-form h4 {
  font-size: 1.2rem;
  margin-bottom: 20px;
}
.form-field { margin-bottom: 15px; }
.form-field label { display: block; margin-bottom: 8px; font-weight: 500; }
.form-field input { width: 100%; max-width: 400px; padding: 10px; border-radius: 6px; border: 1px solid var(--border-color); }
.btn { margin-top: 10px; }
</style>