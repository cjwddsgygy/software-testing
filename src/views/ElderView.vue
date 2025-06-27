<!-- START OF FILE OlderManagement.vue -->
<template>
  <div class="older-management-view">
    <h2 class="view-title">老人信息管理</h2>

    <!-- Toolbar: 搜索和新增按钮 -->
    <div class="toolbar">
      <input type="text" v-model="searchName" @keyup.enter="searchSeniors" placeholder="按姓名搜索..." class="search-input" />
      <button @click="searchSeniors" class="btn btn-primary">搜索</button>
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
          <tr v-for="senior in seniors" :key="senior.id">
            <td>{{ senior.id }}</td>
            <td>{{ senior.olderName }}</td>
            <td>{{ senior.olderAge }}</td>
            <td>{{ senior.careLevel }}</td>
            <td>{{ senior.admissionTime }}</td>
            <td>
              <button @click="openModal(senior)" class="btn-action edit">编辑</button>
              <button @click="deleteSenior(senior.id)" class="btn-action delete">删除</button>
            </td>
          </tr>
          <tr v-if="!seniors || seniors.length === 0">
            <td colspan="6" class="empty-text">当前没有在院老人信息或未搜索到结果。</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑老人信息' : '新增老人' }}</h3>
        <form @submit.prevent="saveSenior">
          <div class="form-item">
            <label>姓名:</label>
            <input v-model="currentSenior.olderName" type="text" required>
          </div>
          <div class="form-item">
            <label>年龄:</label>
            <input v-model="currentSenior.olderAge" type="number" required>
          </div>
          <div class="form-item">
            <label>护理级别:</label>
            <input v-model="currentSenior.careLevel" type="text">
          </div>
          <div class="form-item">
            <label>入院时间:</label>
            <input v-model="currentSenior.admissionTime" type="date">
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
import { ref, reactive, onMounted, computed } from 'vue';
import apiClient from '../api';

const seniors = ref([]);
const loading = ref(true);
const searchName = ref('');

// 弹窗相关状态
const isModalVisible = ref(false);
const currentSenior = ref({});
const isEditing = computed(() => !!currentSenior.value.id);

const defaultSenior = {
  olderName: '',
  olderAge: null,
  careLevel: '',
  admissionTime: new Date().toISOString().split('T')[0] // 默认为今天
};

// --- API 调用函数 ---

// 1. 获取老人列表 (Read)
const fetchSeniors = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/elders/');
    seniors.value = response.data.data; // 根据您后端返回的格式，数据在 .data 里面
  } catch (error) { console.error('获取老人列表失败:', error); } 
  finally { loading.value = false; }
};

// 2. 搜索老人
const searchSeniors = async () => {
  if (!searchName.value.trim()) {
    fetchSeniors();
    return;
  }
  loading.value = true;
  try {
    const response = await apiClient.get(`/elders/search?name=${searchName.value}`);
    seniors.value = response.data.data;
  } catch (error) { console.error('搜索老人失败:', error); } 
  finally { loading.value = false; }
};

// 3. 保存老人 (Create/Update)
const saveSenior = async () => {
  try {
    if (isEditing.value) {
      // 更新 (PUT)
      await apiClient.put('/elders/', currentSenior.value);
    } else {
      // 新增 (POST)
      await apiClient.post('/elders/', currentSenior.value);
    }
    alert(isEditing.value ? '更新成功！' : '新增成功！');
    closeModal();
    fetchSeniors(); // 刷新列表
  } catch (error) {
    console.error('保存老人信息失败:', error);
    alert('操作失败，请稍后重试。');
  }
};

// 4. 删除老人 (Delete)
const deleteSenior = async (id) => {
  if (!confirm(`确定要删除ID为 ${id} 的老人信息吗？`)) return;
  try {
    await apiClient.delete(`/elders/${id}`);
    alert('删除成功！');
    fetchSeniors(); // 刷新列表
  } catch (error) {
    console.error('删除老人失败:', error);
    alert('删除失败，请稍后重试。');
  }
};

// --- 弹窗控制 ---

const openModal = (senior = null) => {
  if (senior) {
    // 编辑：深拷贝一个对象，避免直接修改列表中的数据
    currentSenior.value = { ...senior };
  } else {
    // 新增：使用默认空对象
    currentSenior.value = { ...defaultSenior };
  }
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
  currentSenior.value = {};
};

// 组件加载后，自动获取一次老人列表
onMounted(fetchSeniors);
</script>

<style scoped>
/* 表格和工具栏样式保持不变 */
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

/* 弹窗样式 */
.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.6); display: flex; justify-content: center; align-items: center; z-index: 1000; }
.modal-content { background: #fff; padding: 25px 30px; border-radius: 8px; width: 500px; max-width: 90%; }
.modal-content h3 { margin-top: 0; margin-bottom: 25px; }
.form-item { margin-bottom: 15px; }
.form-item label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-item input { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.modal-footer { text-align: right; margin-top: 30px; }
.modal-footer button { margin-left: 10px; }
</style>
<!-- END OF FILE OlderManagement.vue -->