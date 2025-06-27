<!-- 文件路径: src/views/ElderView.vue -->
<template>
  <div class="Elder-View">
    <h2 class="view-title">老人信息管理</h2>

    <!-- Toolbar: 搜索和新增按钮 -->
    <div class="toolbar">
      <input type="text" v-model="searchParams.name" @keyup.enter="fetchElders" placeholder="按姓名搜索..." class="search-input" />
      <button @click="fetchElders" class="btn btn-primary">搜索</button>
      <button @click="openModal()" class="btn btn-success">新增老人</button>
    </div>
    
    <!-- 数据表格 -->
    <div v-if="loading" class="loading-text">正在加载老人列表...</div>
    <div v-else class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>护理级别</th>
            <th>入院时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <!-- ✅ 使用后端真实字段名: id, name, age, careLevel, checkInDate -->
          <tr v-for="elder in elders" :key="elder.id">
            <td>{{ elder.id }}</td>
            <td>{{ elder.name }}</td>
            <td>{{ elder.age }}</td>
            <td>{{ elder.careLevel }}</td>
            <td>{{ formatDisplayDate(elder.checkInDate) }}</td>
            <td>
              <button @click="openModal(elder)" class="btn-action edit">编辑</button>
              <button @click="handleDelete(elder.id)" class="btn-action delete">删除</button>
            </td>
          </tr>
          <tr v-if="!elders || elders.length === 0">
            <td colspan="6" class="empty-text">当前没有在院老人信息或未搜索到结果。</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑老人信息' : '新增老人' }}</h3>
        <form @submit.prevent="handleSave">
          <!-- ✅ v-model 绑定到与后端一致的字段名 -->
          <div class="form-item">
            <label>姓名:</label>
            <input v-model="currentElder.name" type="text" required>
          </div>
          <div class="form-item">
            <label>年龄:</label>
            <input v-model="currentElder.age" type="number" required>
          </div>
          <div class="form-item">
            <label>护理级别:</label>
            <input v-model="currentElder.careLevel" type="text">
          </div>
          <div class="form-item">
            <label>入院时间:</label>
            <input v-model="currentElder.checkInDate" type="date">
          </div>
          <!-- 其他字段可以按需添加 -->
          <div class="modal-footer">
            <button type="button" @click="closeModal" class="btn btn-secondary">取消</button>
            <button type="submit" class="btn btn-primary">{{ isEditing ? '保存更新' : '确认新增' }}</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
// 文件路径: src/views/ElderView.vue
// 只替换 <script setup> 部分

import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import request from '@/api/request'; // ✅ 导入我们刚刚清理过的 request.js

// --- 响应式状态 ---
const elders = ref([]);
const loading = ref(true);
const searchParams = reactive({ name: '' });

// --- 弹窗相关状态 ---
const isModalVisible = ref(false);
const currentElder = ref({});
const isEditing = computed(() => !!currentElder.value.id);

// 默认空对象
const defaultElder = {
  name: '',
  age: null,
  careLevel: '',
  checkInDate: new Date().toISOString().split('T')[0]
};

// --- API 调用函数 (使用干净的 axios 实例) ---

// 1. 获取/搜索老人列表
const fetchElders = async () => {
  loading.value = true;
  try {
    // ✅ 关键测试点：使用干净的、不带拦截器的 axios 实例
    const token = localStorage.getItem('token');
    const response = await request.get('/elders', {
        params: searchParams,
        headers: {
            'Authorization': token ? `Bearer ${token}` : ''
        }
    });

    // ✅ 直接使用 axios 返回的 data
    // 假设后端返回 { code: 200, data: [...] }
    const result = response.data; 
    if (result.code === 200 || result.code === 0) {
        elders.value = result.data;
    } else {
        throw new Error(result.msg || '获取数据失败');
    }

  } catch (error) {
    console.error('获取老人列表失败:', error);
    // 如果 error.response 存在，说明是HTTP错误
    if (error.response) {
      ElMessage.error(`请求失败: ${error.response.status} - ${error.response.statusText}`);
    } else {
      ElMessage.error(error.message);
    }
  } finally {
    loading.value = false;
  }
};

// --- 以下函数暂时保留，但本次测试不关注它们 ---
const handleSave = () => alert('保存功能已在测试中禁用');
const handleDelete = () => alert('删除功能已在测试中禁用');
const openModal = (elder = null) => {
    if (elder) { currentElder.value = { ...elder }; } 
    else { currentElder.value = { ...defaultElder }; }
    isModalVisible.value = true;
};
const closeModal = () => { isModalVisible.value = false; };
const formatDisplayDate = (dateString) => {
  if (!dateString) return '未记录';
  return dateString.split('T')[0];
};

onMounted(fetchElders);
</script>

<style scoped>
/* 样式无需修改，保持原样 */
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.loading-text, .empty-text { font-size: 16px; color: #888; text-align: center; padding: 40px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 20px; }
.search-input { padding: 8px 12px; border: 1px solid #ccc; border-radius: 4px; flex-grow: 1; max-width: 300px; }
.btn { padding: 8px 15px; border: none; border-radius: 4px; color: #fff; cursor: pointer; }
.btn-primary { background-color: #409eff; }
.btn-success { background-color: #67c23a; }
.btn-secondary { background-color: #909399; }
.table-container { background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eef1f6; }
th { background-color: #fafafa; font-weight: bold; color: #666; }
.btn-action { background: none; border: none; cursor: pointer; padding: 5px 8px; margin-right: 5px; }
.btn-action.edit { color: #e6a23c; }
.btn-action.delete { color: #f56c6c; }

.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.6); display: flex; justify-content: center; align-items: center; z-index: 1000; }
.modal-content { background: #fff; padding: 25px 30px; border-radius: 8px; width: 500px; max-width: 90%; }
.modal-content h3 { margin-top: 0; margin-bottom: 25px; }
.form-item { margin-bottom: 15px; }
.form-item label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-item input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.modal-footer { text-align: right; margin-top: 30px; }
.modal-footer button { margin-left: 10px; }
</style>