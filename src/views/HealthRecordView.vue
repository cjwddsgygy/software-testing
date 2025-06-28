<template>
  <div class="health-record-view">
    <div class="view-header">
      <h2 class="view-title">健康记录管理</h2>
      <p class="view-description">查看、添加和管理老人的健康记录</p>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="search-container">
        <div class="search-input-wrapper">
          <input 
            type="text" 
            v-model="searchParams.searchTerm" 
            @keyup.enter="fetchHealthRecords" 
            placeholder="按老人姓名或记录类型搜索..." 
          />
          <button @click="fetchHealthRecords" class="search-button">
            <i class="fa fa-search"></i>
          </button>
        </div>
      </div>
      <div class="button-group">
        <button @click="openModal()" class="btn btn-primary">
          <i class="fa fa-plus mr-2"></i>新增健康记录
        </button>
      </div>
    </div>
    
    <!-- 数据表格 -->
    <div class="data-card">
      <div v-if="loading" class="loading-placeholder">
        <div class="loading-spinner"></div>
        <p>正在加载健康记录...</p>
      </div>
      <div v-else>
        <div class="table-responsive">
          <table class="data-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>老人姓名</th>
                <th>记录类型</th>
                <th>测量值</th>
                <th>记录日期</th>
                <th>记录护工</th>
                <th>备注</th>
                <th>操作</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="record in healthRecords" :key="record.id" :class="{ 'table-row-hover': !loading }">
                <td>{{ record.id }}</td>
                <td>{{ record.elderName }}</td>
                <td>{{ record.recordType }}</td>
                <td>{{ record.value }}</td>
                <td>{{ formatDate(record.recordDate) }}</td>
                <td>{{ record.careworkerName }}</td>
                <td>{{ record.notes || '无' }}</td>
                <td class="actions-cell">
                  <button @click="openDetailModal(record)" class="action-btn detail-btn">
                    <i class="fa fa-eye"></i>
                  </button>
                  <button @click="openModal(record)" class="action-btn edit-btn">
                    <i class="fa fa-pencil"></i>
                  </button>
                  <button @click="confirmDelete(record.id)" class="action-btn delete-btn">
                    <i class="fa fa-trash"></i>
                  </button>
                </td>
              </tr>
              <tr v-if="!healthRecords || healthRecords.length === 0">
                <td colspan="8" class="empty-row">
                  <div class="empty-content">
                    <i class="fa fa-inbox text-gray-400 text-4xl mb-3"></i>
                    <p class="text-gray-500">当前没有健康记录或未搜索到结果</p>
                  </div>
                </td> 
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-backdrop" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑健康记录' : '新增健康记录' }}</h3>
          <button @click="closeModal" class="close-btn">
            <i class="fa fa-times"></i>
          </button>
        </div>
        <form @submit.prevent="handleSave">
          <div class="form-grid">
            <div class="form-field">
              <label for="elderName">老人姓名</label>
              <input 
                id="elderName" 
                v-model="currentRecord.elderName" 
                type="text" 
                required 
                placeholder="请输入老人姓名"
              />
            </div>
            <div class="form-field">
              <label for="recordType">记录类型</label>
              <select id="recordType" v-model="currentRecord.recordType" required>
                <option value="">请选择</option>
                <option value="血压">血压</option>
                <option value="体温">体温</option>
                <option value="血糖">血糖</option>
                <option value="心率">心率</option>
                <option value="体重">体重</option>
                <option value="其他">其他</option>
              </select>
            </div>
            <div class="form-field">
              <label for="value">测量值</label>
              <input 
                id="value" 
                v-model="currentRecord.value" 
                type="text" 
                placeholder="例如: 120/80 mmHg, 36.5 °C" 
                required 
              />
            </div>
            <div class="form-field">
              <label for="recordDate">记录日期</label>
              <input 
                id="recordDate" 
                v-model="currentRecord.recordDate" 
                type="date" 
                required 
              />
            </div>
            <div class="form-field">
              <label for="careworkerName">记录护工</label>
              <input 
                id="careworkerName" 
                v-model="currentRecord.careworkerName" 
                type="text" 
                required 
                placeholder="请输入记录护工姓名"
              />
            </div>
            <div class="form-field full-width">
              <label for="notes">备注</label>
              <textarea 
                id="notes" 
                v-model="currentRecord.notes" 
                placeholder="请输入备注信息（可选）"
              ></textarea>
            </div>
          </div>
          
          <div class="modal-footer">
            <button type="button" @click="closeModal" class="btn btn-secondary">
              <i class="fa fa-times mr-2"></i>取消
            </button>
            <button type="submit" class="btn btn-primary">
              <i class="fa fa-check mr-2"></i>{{ isEditing ? '保存更新' : '确认新增' }}
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="isDetailModalVisible" class="modal-backdrop" @click="closeDetailModal">
      <div class="modal-content detail-modal" @click.stop>
        <div class="modal-header">
          <h3>健康记录详情</h3>
          <button @click="closeDetailModal" class="close-btn">
            <i class="fa fa-times"></i>
          </button>
        </div>
        <div v-if="detailRecord" class="detail-grid">
          <div class="detail-item">
            <div class="item-label">ID</div>
            <div class="item-value">{{ detailRecord.id }}</div>
          </div>
          <div class="detail-item">
            <div class="item-label">老人姓名</div>
            <div class="item-value">{{ detailRecord.elderName }}</div>
          </div>
          <div class="detail-item">
            <div class="item-label">记录类型</div>
            <div class="item-value">{{ detailRecord.recordType }}</div>
          </div>
          <div class="detail-item">
            <div class="item-label">测量值</div>
            <div class="item-value">{{ detailRecord.value }}</div>
          </div>
          <div class="detail-item">
            <div class="item-label">记录日期</div>
            <div class="item-value">{{ formatDate(detailRecord.recordDate) }}</div>
          </div>
          <div class="detail-item">
            <div class="item-label">记录护工</div>
            <div class="item-value">{{ detailRecord.careworkerName }}</div>
          </div>
          <div class="detail-item full-width">
            <div class="item-label">备注</div>
            <div class="item-value">{{ detailRecord.notes || '无' }}</div>
          </div>
          <div class="detail-item">
            <div class="item-label">创建时间</div>
            <div class="item-value">{{ formatDateTime(detailRecord.createdAt) }}</div>
          </div>
          <div class="detail-item">
            <div class="item-label">最后更新</div>
            <div class="item-value">{{ formatDateTime(detailRecord.updatedAt) }}</div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" @click="closeDetailModal" class="btn btn-primary">
            <i class="fa fa-check mr-2"></i>关闭
          </button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="isDeleteDialogVisible" class="modal-backdrop" @click="isDeleteDialogVisible = false">
      <div class="confirm-modal" @click.stop>
        <div class="confirm-icon">
          <i class="fa fa-exclamation-triangle"></i>
        </div>
        <h3 class="confirm-title">确认删除</h3>
        <p class="confirm-message">您确定要删除这条健康记录吗？</p>
        <p class="confirm-submessage">此操作将永久删除该记录，且无法恢复。</p>
        <div class="confirm-actions">
          <button type="button" class="btn btn-secondary" @click="isDeleteDialogVisible = false">
            <i class="fa fa-times mr-2"></i>取消
          </button>
          <button type="button" class="btn btn-danger" @click="handleConfirmDelete">
            <i class="fa fa-trash mr-2"></i>确认删除
          </button>
        </div>
      </div>
    </div>

    <!-- 全局通知 -->
    <div v-if="notification.show" class="notification" :class="notification.type">
      <div class="notification-content">
        <i :class="notification.icon"></i>
        <span>{{ notification.message }}</span>
      </div>
      <button class="close-notification" @click="closeNotification">
        <i class="fa fa-times"></i>
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request';

// --- 响应式状态 ---
const healthRecords = ref([]);
const loading = ref(true);
const searchParams = reactive({
  searchTerm: '',
  page: 1,
  pageSize: 10,
  total: 0
});
const isModalVisible = ref(false);
const currentRecord = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailRecord = ref(null);
const isDeleteDialogVisible = ref(false);
const deletingRecordId = ref(null);
const notification = ref({
  show: false,
  type: '',
  message: '',
  icon: ''
});

// --- API 调用函数 ---
const fetchHealthRecords = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/api/healthRecords', {
      params: {
        search: searchParams.searchTerm,
        page: searchParams.page,
        pageSize: searchParams.pageSize
      }
    });

    if (response.data.code === 0) {
      healthRecords.value = response.data.data.list || [];
      searchParams.total = response.data.data.total || 0;
    } else {
      showNotification('error', response.data.msg || '获取健康记录失败');
    }
  } catch (error) {
    console.error('获取健康记录错误:', error);
    showNotification('error', '网络错误，无法加载健康记录');
  } finally {
    loading.value = false;
  }
};

const createHealthRecord = async () => {
  try {
    const response = await apiClient.post('/health-records', currentRecord.value);
    
    if (response.data.code === 0) {
      showNotification('success', '新增健康记录成功！');
      return response.data.data;
    } else {
      showNotification('error', response.data.msg || '新增失败');
      return null;
    }
  } catch (error) {
    console.error('新增健康记录错误:', error);
    showNotification('error', '网络错误，新增失败');
    return null;
  }
};

const updateHealthRecord = async () => {
  try {
    const response = await apiClient.put(`/health-records/${currentRecord.value.id}`, currentRecord.value);
    
    if (response.data.code === 0) {
      showNotification('success', '健康记录更新成功！');
      return response.data.data;
    } else {
      showNotification('error', response.data.msg || '更新失败');
      return null;
    }
  } catch (error) {
    console.error('更新健康记录错误:', error);
    showNotification('error', '网络错误，更新失败');
    return null;
  }
};

const deleteHealthRecord = async (id) => {
  try {
    const response = await apiClient.delete(`/health-records/${id}`);
    
    if (response.data.code === 0) {
      showNotification('success', '删除成功！');
      return true;
    } else {
      showNotification('error', response.data.msg || '删除失败');
      return false;
    }
  } catch (error) {
    console.error('删除健康记录错误:', error);
    showNotification('error', '网络错误，删除失败');
    return false;
  }
};

// --- 弹窗控制 ---
const openModal = (record = null) => {
  if (record) {
    isEditing.value = true;
    currentRecord.value = { 
      ...record,
      recordDate: formatDate(record.recordDate)
    };
  } else {
    isEditing.value = false;
    currentRecord.value = {
      elderName: '', 
      recordType: '',
      value: '',
      recordDate: formatDate(new Date()), 
      careworkerName: '',
      notes: ''
    };
  }
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
  currentRecord.value = {};
};

const openDetailModal = (record) => {
  detailRecord.value = record;
  isDetailModalVisible.value = true;
};

const closeDetailModal = () => {
  isDetailModalVisible.value = false;
  detailRecord.value = null;
};

const confirmDelete = (id) => {
  deletingRecordId.value = id;
  isDeleteDialogVisible.value = true;
};

// --- 表单处理 ---
const handleSave = async () => {
  try {
    // 前端校验
    if (!currentRecord.value.elderName.trim()) {
      showNotification('warning', '请输入老人姓名');
      return;
    }
    if (!currentRecord.value.recordType) {
      showNotification('warning', '请选择记录类型');
      return;
    }
    if (!currentRecord.value.value.trim()) {
      showNotification('warning', '请输入测量值');
      return;
    }
    if (!currentRecord.value.recordDate) {
      showNotification('warning', '请选择记录日期');
      return;
    }
    if (!currentRecord.value.careworkerName.trim()) {
      showNotification('warning', '请输入记录护工姓名');
      return;
    }

    let result;
    if (isEditing.value) {
      result = await updateHealthRecord();
    } else {
      result = await createHealthRecord();
    }

    if (result) {
      closeModal();
      await fetchHealthRecords();
    }
  } catch (error) {
    console.error('保存健康记录错误:', error);
    showNotification('error', '操作失败，请重试');
  }
};

const handleConfirmDelete = async () => {
  if (!deletingRecordId.value) return;
  
  const success = await deleteHealthRecord(deletingRecordId.value);
  
  if (success) {
    isDeleteDialogVisible.value = false;
    await fetchHealthRecords();
  }
};

// --- 工具函数 ---
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toISOString().split('T')[0];
};

const formatDateTime = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
};

const showNotification = (type, message) => {
  const icons = {
    success: 'fa fa-check-circle',
    error: 'fa fa-times-circle',
    warning: 'fa fa-exclamation-circle',
    info: 'fa fa-info-circle'
  };
  
  notification.value = {
    show: true,
    type,
    message,
    icon: icons[type] || 'fa fa-info-circle'
  };
  
  // 3秒后自动关闭
  setTimeout(() => {
    notification.value.show = false;
  }, 3000);
};

const closeNotification = () => {
  notification.value.show = false;
};

// --- 生命周期钩子 ---
onMounted(fetchHealthRecords);

// 监听搜索参数变化，自动刷新数据
watch(searchParams, () => {
  fetchHealthRecords();
}, { deep: true });
</script>

<style scoped>
.health-record-view {
  padding: 30px;
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  background-color: #f9fafb;
  min-height: calc(100vh - 60px);
}

.view-header {
  text-align: center;
  margin-bottom: 40px;
}

.view-title {
  font-size: 28px;
  color: #1f2937;
  margin-bottom: 10px;
  font-weight: 600;
}

.view-description {
  color: #6b7280;
  font-size: 16px;
  margin: 0;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  gap: 15px;
}

.search-container {
  flex-grow: 1;
  max-width: 400px;
}

.search-input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}

.search-input-wrapper input {
  width: 100%;
  padding: 12px 40px 12px 16px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 16px;
  box-sizing: border-box;
  transition: all 0.2s ease;
}

.search-input-wrapper input:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
  outline: none;
}

.search-button {
  position: absolute;
  right: 10px;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  font-size: 16px;
}

.button-group {
  display: flex;
  gap: 10px;
}

.btn {
  padding: 12px 24px;
  border: none;
  border-radius: 8px;
  color: #fff;
  cursor: pointer;
  font-size: 16px;
  font-weight: 500;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
}

.btn-primary {
  background-color: #409eff;
  box-shadow: 0 4px 6px -1px rgba(64, 158, 255, 0.1), 0 2px 4px -1px rgba(64, 158, 255, 0.06);
}

.btn-primary:hover {
  background-color: #368ee0;
  transform: translateY(-1px);
  box-shadow: 0 10px 15px -3px rgba(64, 158, 255, 0.1), 0 4px 6px -2px rgba(64, 158, 255, 0.05);
}

.btn-secondary {
  background-color: #f3f4f6;
  color: #4b5563;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.05), 0 2px 4px -1px rgba(0, 0, 0, 0.03);
}

.btn-secondary:hover {
  background-color: #e5e7eb;
  transform: translateY(-1px);
  box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.05), 0 4px 6px -2px rgba(0, 0, 0, 0.03);
}

.btn-danger {
  background-color: #f56c6c;
  box-shadow: 0 4px 6px -1px rgba(245, 108, 108, 0.1), 0 2px 4px -1px rgba(245, 108, 108, 0.06);
}

.btn-danger:hover {
  background-color: #f78989;
  transform: translateY(-1px);
  box-shadow: 0 10px 15px -3px rgba(245, 108, 108, 0.1), 0 4px 6px -2px rgba(245, 108, 108, 0.05);
}

.mr-2 {
  margin-right: 8px;
}

.data-card {
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.loading-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 0;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid #e5e7eb;
  border-radius: 50%;
  border-top-color: #409eff;
  animation: spin 1s linear infinite;
  margin-bottom: 20px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.table-responsive {
  overflow-x: auto;
}

.data-table {
  width: 100%;
  border-collapse: collapse;
}

.data-table th,
.data-table td {
  padding: 15px 20px;
  text-align: left;
  border-bottom: 1px solid #f3f4f6;
}

.data-table th {
  background-color: #f9fafb;
  font-weight: 500;
  color: #4b5563;
  text-transform: capitalize;
}

.data-table tbody tr:last-child td {
  border-bottom: none;
}

.data-table .table-row-hover:hover {
  background-color: #f9fafb;
  transition: background-color 0.2s ease;
}

.text-right {
  text-align: right;
}

.font-medium {
  font-weight: 500;
}

.actions-cell {
  display: flex;
  gap: 5px;
}

.action-btn {
  width: 32px;
  height: 32px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: none;
  border-radius: 4px;
  background-color: transparent;
  cursor: pointer;
  transition: all 0.2s ease;
}

.action-btn:hover {
  background-color: #f3f4f6;
}

.detail-btn {
  color: #409eff;
}

.edit-btn {
  color: #e6a23c;
}

.delete-btn {
  color: #f56c6c;
}

.empty-row {
  padding: 60px 0 !important;
}

.empty-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #9ca3af;
}

.text-gray-400 {
  color: #9ca3af;
}

.text-gray-500 {
  color: #6b7280;
}

.text-4xl {
  font-size: 2rem;
}

.mb-3 {
  margin-bottom: 12px;
}

/* 弹窗样式 */
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(2px);
}

.modal-content {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  width: 550px;
  max-width: 90%;
  max-height: 90vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  animation: fadeIn 0.3s ease-out;
}

.modal-header {
  padding: 20px 25px;
  border-bottom: 1px solid #f3f4f6;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
  color: #1f2937;
}

.close-btn {
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  font-size: 18px;
  transition: color 0.2s ease;
}

.close-btn:hover {
  color: #4b5563;
}

.modal-body {
  padding: 25px;
  overflow-y: auto;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.form-field {
  display: flex;
  flex-direction: column;
}

.form-field.full-width {
  grid-column: 1 / -1;
}

.form-field label {
  margin-bottom: 8px;
  font-weight: 500;
  color: #4b5563;
  font-size: 15px;
}

.form-field input,
.form-field select,
.form-field textarea {
  padding: 12px 16px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  font-size: 16px;
  box-sizing: border-box;
  transition: all 0.2s ease;
}

.form-field input:focus,
.form-field select:focus,
.form-field textarea:focus {
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
  outline: none;
}

.form-field textarea {
  min-height: 100px;
  resize: vertical;
}

.modal-footer {
  padding: 20px 25px;
  border-top: 1px solid #f3f4f6;
  display: flex;
  justify-content: flex-end;
  gap: 15px;
}

.detail-modal {
  width: 650px;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px 25px;
  margin-bottom: 20px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  padding-bottom: 15px;
  border-bottom: 1px solid #f3f4f6;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

.item-label {
  font-size: 14px;
  color: #6b7280;
  margin-bottom: 5px;
}

.item-value {
  font-size: 16px;
  color: #1f2937;
  word-break: break-all;
}

.confirm-modal {
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  width: 400px;
  max-width: 90%;
  padding: 40px 30px;
  text-align: center;
  animation: fadeIn 0.3s ease-out;
}

.confirm-icon {
  width: 60px;
  height: 60px;
  background-color: #fef0f0;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #f56c6c;
  font-size: 24px;
  margin: 0 auto 20px;
}

.confirm-title {
  font-size: 20px;
  font-weight: 500;
  color: #1f2937;
  margin-bottom: 15px;
}

.confirm-message {
  color: #4b5563;
  margin-bottom: 10px;
}

.confirm-submessage {
  color: #6b7280;
  font-size: 14px;
  margin-bottom: 30px;
}

.confirm-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}

/* 通知样式 */
.notification {
  position: fixed;
  top: 20px;
  right: 20px;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  display: flex;
  align-items: center;
  z-index: 10000;
  transform: translateX(120%);
  opacity: 0;
  transition: all 0.3s ease-out;
  max-width: 350px;
}

.notification.show {
  transform: translateX(0);
  opacity: 1;
}

.notification.success {
  background-color: #f0f9eb;
  color: #67c23a;
}

.notification.error {
  background-color: #fef0f0;
  color: #f56c6c;
}

.notification.warning {
  background-color: #fdf6ec;
  color: #e6a23c;
}

.notification.info {
  background-color: #f4f4f5;
  color: #909399;
}

.notification-content {
  display: flex;
  align-items: center;
}

.notification .fa {
  margin-right: 10px;
  font-size: 18px;
}

.close-notification {
  background: none;
  border: none;
  color: #9ca3af;
  cursor: pointer;
  margin-left: 15px;
  font-size: 14px;
}
</style>