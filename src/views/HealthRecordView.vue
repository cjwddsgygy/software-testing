<!-- 文件路径: src/views/HealthRecordView.vue -->
<template>
  <div class="HealthRecord-View">
    <h2 class="view-title">健康管理</h2>

    <!-- Toolbar: 统一搜索框和新增按钮 -->
    <div class="toolbar">
      <input type="text" v-model="searchParams.searchTerm" @keyup.enter="fetchHealthRecords" placeholder="按老人姓名或记录类型搜索..." class="search-input" />
      <button @click="fetchHealthRecords" class="btn btn-primary">搜索</button>
      <button @click="openModal()" class="btn btn-success">新增健康记录</button>
    </div>
    
    <!-- 数据表格 -->
    <div v-if="loading" class="loading-text">正在加载健康记录...</div>
    <div v-else class="table-container">
      <table>
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
          <tr v-for="record in healthRecords" :key="record.id">
            <td>{{ record.id }}</td>
            <td>{{ record.elderName }}</td>
            <td>{{ record.recordType }}</td>
            <td>{{ record.value }}</td>
            <td>{{ formatDisplayDate(record.recordDate) }}</td>
            <td>{{ record.careworkerName }}</td>
            <td>{{ record.notes || '无' }}</td>
            <td>
              <button @click="openDetailModal(record)" class="btn-action detail">详情</button>
              <button @click="openModal(record)" class="btn-action edit">编辑</button>
              <button @click="confirmDelete(record.id)" class="btn-action delete">删除</button>
            </td>
          </tr>
          <tr v-if="!healthRecords || healthRecords.length === 0">
            <td colspan="8" class="empty-text">当前没有健康记录或未搜索到结果。</td> 
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑健康记录' : '新增健康记录' }}</h3>
        <form @submit.prevent="handleSave">
          <div class="form-item"><label>老人姓名:</label><input v-model="currentRecord.elderName" type="text" required></div>
          <div class="form-item">
            <label>记录类型:</label>
            <select v-model="currentRecord.recordType" required>
              <option value="">请选择</option>
              <option value="血压">血压</option>
              <option value="体温">体温</option>
              <option value="血糖">血糖</option>
              <option value="心率">心率</option>
              <option value="体重">体重</option>
              <option value="其他">其他</option>
            </select>
          </div>
          <div class="form-item"><label>测量值:</label><input v-model="currentRecord.value" type="text" placeholder="例如: 120/80 mmHg, 36.5 °C" required></div>
          <div class="form-item"><label>记录日期:</label><input v-model="currentRecord.recordDate" type="date" required></div>
          <div class="form-item"><label>记录护工:</label><input v-model="currentRecord.careworkerName" type="text" required></div>
          <div class="form-item"><label>备注:</label><textarea v-model="currentRecord.notes"></textarea></div>
          
          <div class="modal-footer">
            <button type="button" @click="closeModal" class="btn btn-secondary">取消</button>
            <button type="submit" class="btn btn-primary">{{ isEditing ? '保存更新' : '确认新增' }}</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="isDetailModalVisible" class="modal-overlay" @click="closeDetailModal">
      <div class="modal-content detail-modal" @click.stop>
        <h3>健康记录详情</h3>
        <div v-if="detailRecord" class="detail-grid">
          <div class="detail-item"><strong>ID:</strong><span>{{ detailRecord.id }}</span></div>
          <div class="detail-item"><strong>老人姓名:</strong><span>{{ detailRecord.elderName }}</span></div>
          <div class="detail-item"><strong>记录类型:</strong><span>{{ detailRecord.recordType }}</span></div>
          <div class="detail-item"><strong>测量值:</strong><span>{{ detailRecord.value }}</span></div>
          <div class="detail-item"><strong>记录日期:</strong><span>{{ formatDisplayDate(detailRecord.recordDate) }}</span></div>
          <div class="detail-item"><strong>记录护工:</strong><span>{{ detailRecord.careworkerName }}</span></div>
          <div class="detail-item full-width"><strong>备注:</strong><span>{{ detailRecord.notes || '无' }}</span></div>
          <div class="detail-item"><strong>创建时间:</strong><span>{{ formatDisplayDate(detailRecord.createdAt, true) }}</span></div>
          <div class="detail-item"><strong>最后更新:</strong><span>{{ formatDisplayDate(detailRecord.updatedAt, true) }}</span></div>
        </div>
        <div class="modal-footer">
          <button type="button" @click="closeDetailModal" class="btn btn-primary">关闭</button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="isDeleteDialogVisible" class="custom-modal-overlay" @click="isDeleteDialogVisible = false">
      <div class="custom-modal-content" @click.stop>
        <div class="modal-header">
          <h3 class="modal-title">确认删除</h3>
        </div>
        <div class="modal-body text-center">
          <i class="el-icon-warning-outline text-danger text-2xl mb-4"></i>
          <p>您确定要删除这条健康记录吗？</p>
          <p class="text-sm text-gray-500 mt-2">此操作将永久删除该记录，且无法恢复。</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="isDeleteDialogVisible = false">取消</button>
          <button type="button" class="btn btn-danger" @click="handleConfirmDelete">确认删除</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request';

// --- 响应式状态 ---
const healthRecords = ref([]);
const loading = ref(true);
const searchParams = reactive({
  searchTerm: ''
});
const isModalVisible = ref(false);
const currentRecord = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailRecord = ref(null);
const isDeleteDialogVisible = ref(false);
const deletingRecordId = ref(null);

// --- API 调用函数 ---
const fetchHealthRecords = async () => {
  loading.value = true;
  try {
    // 模拟API请求
    const response = await new Promise(resolve => {
      setTimeout(() => {
        const mockData = [
          { id: 1, elderName: '张三', recordType: '血压', value: '120/80 mmHg', recordDate: '2024-06-25', careworkerName: '王阿姨', notes: '血压正常', createdAt: '2024-06-25T08:00:00Z', updatedAt: '2024-06-25T08:00:00Z' },
          { id: 2, elderName: '李四', recordType: '体温', value: '36.8 °C', recordDate: '2024-06-25', careworkerName: '李师傅', notes: '体温正常', createdAt: '2024-06-25T09:15:00Z', updatedAt: '2024-06-25T09:15:00Z' },
          { id: 3, elderName: '张三', recordType: '血糖', value: '5.5 mmol/L', recordDate: '2024-06-24', careworkerName: '王阿姨', notes: '饭前血糖', createdAt: '2024-06-24T07:30:00Z', updatedAt: '2024-06-24T07:30:00Z' },
          { id: 4, elderName: '王五', recordType: '心率', value: '75 bpm', recordDate: '2024-06-23', careworkerName: '张小丽', notes: '心率平稳', createdAt: '2024-06-23T10:00:00Z', updatedAt: '2024-06-23T10:00:00Z' },
        ];
        const filteredData = mockData.filter(rec =>
          (rec.elderName && rec.elderName.includes(searchParams.searchTerm)) ||
          (rec.recordType && rec.recordType.includes(searchParams.searchTerm))
        );
        resolve({ data: { code: 0, data: filteredData } });
      }, 500);
    });

    if (response.data.code === 0) {
      healthRecords.value = response.data.data;
    } else {
      throw new Error(response.data.msg || '获取数据失败');
    }
  } catch (error) {
    ElMessage.error(error.message || '网络错误');
  } finally {
    loading.value = false;
  }
};

const handleSave = async () => {
  try {
    if (!currentRecord.value.elderName || !currentRecord.value.recordType || !currentRecord.value.value || !currentRecord.value.recordDate || !currentRecord.value.careworkerName) {
      ElMessage.error('请填写所有必填项 (老人姓名, 记录类型, 测量值, 记录日期, 记录护工)');
      return;
    }

    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 300));

    if (isEditing.value) {
      const index = healthRecords.value.findIndex(rec => rec.id === currentRecord.value.id);
      if (index !== -1) {
        healthRecords.value[index] = { ...currentRecord.value, updatedAt: new Date().toISOString() };
      }
      ElMessage.success('健康记录更新成功！');
    } else {
      const newId = Math.max(...healthRecords.value.map(rec => rec.id), 0) + 1;
      healthRecords.value.push({ 
        id: newId, 
        ...currentRecord.value, 
        createdAt: new Date().toISOString(), 
        updatedAt: new Date().toISOString(),
        notes: currentRecord.value.notes || ''
      });
      ElMessage.success('新增健康记录成功！');
    }
    closeModal();
    await fetchHealthRecords();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '操作失败，请检查输入');
  }
};

const confirmDelete = (id) => {
  deletingRecordId.value = id;
  isDeleteDialogVisible.value = true;
};

const handleConfirmDelete = async () => {
  if (!deletingRecordId.value) return;
  
  try {
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 300));
    
    healthRecords.value = healthRecords.value.filter(rec => rec.id !== deletingRecordId.value);
    ElMessage.success('删除成功！');
    isDeleteDialogVisible.value = false;
    await fetchHealthRecords();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '删除失败，请重试');
    isDeleteDialogVisible.value = false;
  } finally {
    deletingRecordId.value = null;
  }
};

// --- 弹窗控制 ---
const openModal = (record = null) => {
  if (record) {
    isEditing.value = true;
    currentRecord.value = { 
      ...record,
      recordDate: record.recordDate ? formatDisplayDate(record.recordDate) : ''
    };
  } else {
    isEditing.value = false;
    currentRecord.value = {
        elderName: '', 
        recordType: '',
        value: '',
        recordDate: formatDisplayDate(new Date()), 
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

// --- 工具函数 ---
const formatDisplayDate = (dateString, showTime = false) => {
    if (!dateString) return '';
    try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return dateString; 

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const datePart = `${year}-${month}-${day}`;

        if (showTime) {
            const hours = String(date.getHours()).padStart(2, '0');
            const minutes = String(date.getMinutes()).padStart(2, '0');
            const seconds = String(date.getSeconds()).padStart(2, '0');
            const timePart = `${hours}:${minutes}:${seconds}`;
            return `${datePart} ${timePart}`;
        }
        return datePart;
    } catch (e) {
        return dateString;
    }
};

// --- 生命周期钩子 ---
onMounted(fetchHealthRecords);
</script>

<style scoped>
/* 详情按钮颜色 */
.btn-action.detail { color: #409eff; }

.detail-modal {
  width: 700px;
  max-width: 95%;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px 25px;
  text-align: left;
}

.detail-item {
  display: flex;
  align-items: flex-start;
  border-bottom: 1px solid #f0f2f5;
  padding-bottom: 10px;
  font-size: 14px;
}

.detail-item strong {
  color: #555;
  min-width: 90px;
  font-weight: bold;
}

.detail-item span {
  color: #333;
  word-break: break-all;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

/* --- 以下是通用样式 (与ElderView保持一致，或根据需要微调) --- */
.HealthRecord-View { padding: 20px; }
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.loading-text, .empty-text { font-size: 16px; color: #888; text-align: center; padding: 40px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 20px; }
.search-input { padding: 8px 12px; border: 1px solid #ccc; border-radius: 4px; flex-grow: 1; max-width: 300px; }
.btn { padding: 8px 15px; border: none; border-radius: 4px; color: #fff; cursor: pointer; transition: background-color 0.3s; }
.btn-primary { background-color: #409eff; }
.btn-success { background-color: #67c23a; }
.btn-secondary { background-color: #909399; }
.table-container { background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eef1f6; }
th { background-color: #fafafa; font-weight: bold; color: #666; }
.btn-action { background: none; border: none; cursor: pointer; padding: 5px 8px; margin-right: 5px; font-size: 14px; }
.btn-action:hover { opacity: 0.7; }
.btn-action.edit { color: #e6a23c; }
.btn-action.delete { color: #f56c6c; }

.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.6); display: flex; justify-content: center; align-items: center; z-index: 1000; }
.modal-content { background: #fff; padding: 25px 30px; border-radius: 8px; width: 500px; max-width: 90%; }
.modal-content h3 { margin-top: 0; margin-bottom: 25px; }
.form-item { margin-bottom: 15px; }
.form-item label { display: block; margin-bottom: 5px; font-weight: bold; }
.form-item input, .form-item select, .form-item textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
.form-item textarea { min-height: 80px; resize: vertical; }
.modal-footer { text-align: right; margin-top: 30px; }
.modal-footer button { margin-left: 10px; }

.text-danger { color: #f56c6c; }
.text-2xl { font-size: 24px; }
.text-gray-500 { color: #909399; }
.text-sm { font-size: 12px; }

/* 自定义确认对话框样式 */
.custom-modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.custom-modal-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 350px;
  max-width: 90%;
  display: flex;
  flex-direction: column;
  animation: fadeIn 0.3s ease-out;
}

.modal-header {
  padding: 20px 20px 10px;
  border-bottom: 1px solid #eee;
}

.modal-title {
  font-size: 18px;
  color: #333;
  margin: 0;
}

.modal-body {
  padding: 25px 20px;
}

.modal-footer {
  padding: 15px 20px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.btn-danger {
  background-color: #f56c6c;
}

.btn-danger:hover {
  background-color: #f78989;
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}
</style>