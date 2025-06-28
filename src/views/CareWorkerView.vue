<template>
  <div class="CareWorker-View">
    <h2 class="view-title">护工信息管理</h2>

    <!-- Toolbar: 统一搜索框和新增按钮 -->
    <div class="toolbar">
      <input type="text" v-model="searchParams.searchTerm" @keyup.enter="fetchCareworkers" placeholder="按姓名或账户搜索..." class="search-input" />
      <button @click="fetchCareworkers" class="btn btn-primary">搜索</button>
      <button @click="openModal()" class="btn btn-success">新增护工</button>
    </div>
    
    <!-- 数据表格 -->
    <div v-if="loading" class="loading-text">正在加载护工列表...</div>
    <div v-else class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>姓名</th>
            <th>年龄</th>
            <th>出生日期</th>
            <th>民族</th>          
            <th>账户名</th>
            <th>学历</th>
            <th>经验</th>
            <th>特长</th>
            <th>负责老人数量</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="careworker in careworkers" :key="careworker.id">
            <td>{{ careworker.id }}</td>
            <td>{{ careworker.name }}</td>
            <td>{{ careworker.age }}</td>
            <td>{{ formatDisplayDate(careworker.birthDate) }}</td>
            <td>{{ careworker.ethnicity }}</td> 
            <td>{{ careworker.account }}</td>
            <td>{{ careworker.education }}</td> 
            <td>{{ careworker.experience }}</td> 
            <td>{{ careworker.specialties }}</td>
            <td>{{ careworker.elderCount || 0 }}</td>
            <td class="action-buttons">
              <button @click="openDetailModal(careworker)" class="action-btn detail-btn">详情</button>
              <button @click="openModal(careworker)" class="action-btn edit-btn">编辑</button>
              <button @click="confirmDelete(careworker.id)" class="action-btn delete-btn">删除</button>
            </td>
          </tr>
          <tr v-if="!careworkers || careworkers.length === 0">
            <td colspan="11" class="empty-text">当前没有护工信息或未搜索到结果。</td> 
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
      <div class="modal-content careworker-modal" @click.stop> 
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑护工信息' : '新增护工' }}</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <!-- 修复：将整个表单内容和按钮区域都包含在 <form> 标签内 -->
          <form @submit.prevent="handleSave">
            <div class="form-grid">
              <!-- 基础信息（必填项，优先显示） -->
              <div class="form-item"><label>姓名:</label><input v-model="currentCareworker.name" type="text" required></div>
              <div class="form-item"><label>年龄:</label><input v-model="currentCareworker.age" type="number" required></div>
              <div class="form-item"><label>出生日期:</label><input v-model="currentCareworker.birthDate" type="date" required></div>
              <div class="form-item"><label>民族:</label><input v-model="currentCareworker.ethnicity" type="text" required></div>
              <div class="form-item"><label>账户名:</label><input v-model="currentCareworker.account" type="text" required></div>
              
              <!-- 状态只在新增时显示且必填 (原密码框位置) -->
              <div class="form-item" v-if="!isEditing">
                <label>状态:</label><input v-model="currentCareworker.status" type="text" required>
              </div>
              <div class="form-item" v-else>
                <label> </label><div></div>
              </div>
              
              <!-- 编辑模式显示完整表单 -->
              <div v-if="isEditing">
                <div class="form-item"><label>学历:</label><input v-model="currentCareworker.education" type="text"></div>
                <div class="form-item"><label>模拟性别:</label><input v-model="currentCareworker.gender" type="text"></div>
                <div class="form-item"><label>模拟联系方式:</label><input v-model="currentCareworker.contact" type="text"></div>
                
                <!-- 跨两列的文本域 -->
                <div class="form-item full-width"><label>工作经验:</label><textarea v-model="currentCareworker.experience"></textarea></div>
                <div class="form-item full-width"><label>特长:</label><textarea v-model="currentCareworker.specialties"></textarea></div>
                <div class="form-item full-width"><label>负责老人列表:</label><textarea v-model="currentCareworker.assignedElders"></textarea></div>
              </div>
            </div>
            
            <!-- 修复：将按钮区域移到 <form> 标签内部 -->
            <div class="modal-footer">
              <button type="button" @click="closeModal" class="btn btn-secondary">取消</button>
              <button type="submit" class="btn btn-primary">
                {{ isEditing ? '保存更新' : '确认新增' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>

    <!-- 详情弹窗 -->
    <div v-if="isDetailModalVisible" class="modal-overlay" @click="closeDetailModal">
      <div class="modal-content detail-modal" @click.stop>
        <div class="modal-header">
          <h3>护工详细信息</h3>
          <button class="close-btn" @click="closeDetailModal">×</button>
        </div>
        <div class="modal-body">
          <div v-if="detailCareworker" class="detail-grid">
            <div class="detail-item"><strong>ID:</strong><span>{{ detailCareworker.id }}</span></div>
            <div class="detail-item"><strong>姓名:</strong><span>{{ detailCareworker.name }}</span></div>
            <div class="detail-item"><strong>账户名:</strong><span>{{ detailCareworker.account }}</span></div>
            <div class="detail-item"><strong>年龄:</strong><span>{{ detailCareworker.age }}</span></div>
            <div class="detail-item"><strong>民族:</strong><span>{{ detailCareworker.ethnicity }}</span></div>
            <div class="detail-item"><strong>出生日期:</strong><span>{{ formatDisplayDate(detailCareworker.birthDate) }}</span></div>
            <div class="detail-item"><strong>学历:</strong><span>{{ detailCareworker.education || '未填写' }}</span></div>
            <div class="detail-item"><strong>负责老人数量:</strong><span>{{ detailCareworker.elderCount || 0 }}</span></div>
            <div class="detail-item full-width"><strong>工作经验:</strong><span>{{ detailCareworker.experience || '无' }}</span></div>
            <div class="detail-item full-width"><strong>特长:</strong><span>{{ detailCareworker.specialties || '无' }}</span></div>
            <div class="detail-item full-width"><strong>负责老人列表:</strong><span>{{ detailCareworker.assignedElders || '无' }}</span></div>
            <div class="detail-item"><strong>创建时间:</strong><span>{{ formatDisplayDate(detailCareworker.createdAt, true) }}</span></div>
            <div class="detail-item"><strong>最后更新:</strong><span>{{ formatDisplayDate(detailCareworker.updatedAt, true) }}</span></div>
            <div class="detail-item"><strong>模拟性别:</strong><span>{{ detailCareworker.gender || '未填写' }}</span></div>
            <div class="detail-item"><strong>模拟联系方式:</strong><span>{{ detailCareworker.contact || '未填写' }}</span></div>
          </div>
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
          <p>您确定要删除这位护工的信息吗？</p>
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
const careworkers = ref([]);
const loading = ref(true);
const searchParams = reactive({
  searchTerm: ''
});
const isModalVisible = ref(false);
const currentCareworker = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailCareworker = ref(null);
const isDeleteDialogVisible = ref(false);
const deletingCareworkerId = ref(null);

// --- API 调用函数 ---
const fetchCareworkers = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/api/careWorkers', {
      params: { searchTerm: searchParams.searchTerm }
    });
    if (response.data.code === 0) {
      careworkers.value = response.data.data;
    } else {
      throw new Error(response.data.msg || '获取护工数据失败');
    }
  } catch (error) {
    ElMessage.error(error.message || '网络错误，获取护工信息失败');
  } finally {
    loading.value = false;
  }
};

const handleSave = async () => {
  try {
    if (isEditing.value) {
      // 编辑逻辑保持不变，但移除了 password
      const careworkerToUpdate = { ...currentCareworker.value };
      delete careworkerToUpdate.password; // 确保不发送 password 字段
      await apiClient.put('/api/careWorkers', careworkerToUpdate);
      ElMessage.success('护工信息更新成功！');
    } else {
      // 新增护工的逻辑
      // 1. 前端必填项校验（您指定的六个字段）
      if (
        !currentCareworker.value.name || 
        !currentCareworker.value.age || 
        !currentCareworker.value.birthDate || 
        !currentCareworker.value.ethnicity || 
        !currentCareworker.value.account ||
        !currentCareworker.value.status // 校验 status
      ) {
        ElMessage.error('请填写所有必填项 (姓名、年龄、出生日期、民族、账户、状态)');
        return;
      }

      // 2. 创建一个只包含您指定的六个字段的对象
      const dataToSend = {
        name: currentCareworker.value.name,
        age: currentCareworker.value.age,
        birthDate: currentCareworker.value.birthDate,
        ethnicity: currentCareworker.value.ethnicity,
        account: currentCareworker.value.account,
        status: currentCareworker.value.status,
      };

      // 3. 发送这个只包含六个字段的对象
      await apiClient.post('/api/careWorkers', dataToSend);
      ElMessage.success('新增护工成功！');
    }
    closeModal();
    await fetchCareworkers();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '操作失败，请检查输入');
  }
};

const confirmDelete = (id) => {
  deletingCareworkerId.value = id;
  isDeleteDialogVisible.value = true;
};

const handleConfirmDelete = async () => {
  if (!deletingCareworkerId.value) return;
  
  try {
    await apiClient.delete(`/api/careWorkers/${deletingCareworkerId.value}`);
    ElMessage.success('删除成功！');
    isDeleteDialogVisible.value = false;
    await fetchCareworkers();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '删除失败，请重试');
    isDeleteDialogVisible.value = false;
  } finally {
    deletingCareworkerId.value = null;
  }
};

// --- 弹窗控制 ---
const openModal = (careworker = null) => {
  if (careworker) {
    // 编辑模式
    isEditing.value = true;
    currentCareworker.value = { 
      ...careworker,
      birthDate: careworker.birthDate ? formatDisplayDate(careworker.birthDate) : '',
      ethnicity: careworker.ethnicity || '',       
      education: careworker.education || '',       
      experience: careworker.experience || '',     
      specialties: careworker.specialties || '',           
      assignedElders: careworker.assignedElders || '',
      gender: careworker.gender || '',       
      contact: careworker.contact || '',
    };
  } else {
    // 新增模式
    isEditing.value = false;
    currentCareworker.value = {
        name: '', 
        age: null,
        birthDate: '', 
        ethnicity: '', 
        account: '', 
        status: '', // 新增时 status 必填
        education: '',         
        experience: '',     
        specialties: '',           
        assignedElders: '',
        gender: '',       
        contact: ''    
    };
  }
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
  currentCareworker.value = {};
};

const openDetailModal = (careworker) => {
  detailCareworker.value = { ...careworker };
  isDetailModalVisible.value = true;
};

const closeDetailModal = () => {
  isDetailModalVisible.value = false;
  detailCareworker.value = null;
};

// --- 工具函数 ---
const formatDisplayDate = (dateString, showTime = false) => {
    if (!dateString) return '';
    try {
        if (!showTime && dateString.length >= 10 && dateString.charAt(4) === '-' && dateString.charAt(7) === '-') {
            return dateString.substring(0, 10);
        }
        
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
onMounted(fetchCareworkers);
</script>

<style scoped>
/* 基础样式保持与 ElderView 一致 */
.CareWorker-View { padding: 20px; }
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

/* 操作按钮样式与 ElderView 一致 */
.action-buttons {
  display: flex;
  gap: 6px;
}

.action-btn {
  padding: 6px 12px;
  border-radius: 4px;
  font-size: 13px;
  cursor: pointer;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid transparent;
}

.detail-btn {
  color: #409eff;
  border-color: #409eff;
  background-color: #ffffff;
}

.detail-btn:hover {
  background-color: #e6f4ff;
}

.edit-btn {
  color: #e6a23c;
  border-color: #e6a23c;
  background-color: #ffffff;
}

.edit-btn:hover {
  background-color: #fff7e6;
}

.delete-btn {
  color: #f56c6c;
  border-color: #f56c6c;
  background-color: #ffffff;
}

.delete-btn:hover {
  background-color: #ffe6e6;
}

/* 弹窗样式 */
.modal-overlay {
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
  backdrop-filter: blur(3px);
}

.modal-content {
  position: relative;
  border-radius: 12px;
  background: white;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  max-height: 90vh;
  display: flex;
  flex-direction: column;
}

.careworker-modal {
  width: 750px;
  max-width: 95%;
}

.detail-modal {
  width: 700px;
  max-width: 95%;
}

.modal-header {
  padding: 18px 25px;
  background-color: #f8fafc;
  border-bottom: 1px solid #e2e8f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: #334155;
}

.close-btn {
  background: none;
  border: none;
  font-size: 20px;
  color: #94a3b8;
  cursor: pointer;
  transition: color 0.2s;
}

.close-btn:hover {
  color: #334155;
}

.modal-body {
  padding: 25px;
  overflow-y: auto;
  flex-grow: 1;
}

.modal-footer {
  padding: 15px 25px;
  background-color: #f8fafc;
  border-top: 1px solid #e2e8f0;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 表单网格布局 */
.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 18px 25px;
}

.form-item {
  display: flex;
  flex-direction: column;
}

.form-item label {
  font-weight: 500;
  color: #475569;
  margin-bottom: 6px;
  font-size: 14px;
}

.form-item input,
.form-item textarea {
  padding: 9px 12px;
  border: 1px solid #cbd5e1;
  border-radius: 6px;
  font-size: 14px;
  color: #334155;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.form-item input:focus,
.form-item textarea:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.2);
}

.form-item textarea {
  min-height: 80px;
  resize: vertical;
}

.form-item.full-width {
  grid-column: 1 / -1;
}

/* 详情弹窗的网格布局 */
.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px 25px;
}

.detail-item {
  display: flex;
  padding-bottom: 10px;
  border-bottom: 1px solid #f1f5f9;
}

.detail-item strong {
  color: #64748b;
  min-width: 120px;
  font-weight: 500;
}

.detail-item span {
  color: #334155;
  flex: 1;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

/* 删除确认对话框 */
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
  border-radius: 12px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);
  width: 380px;
  max-width: 90%;
  overflow: hidden;
}

.modal-title {
  font-size: 18px;
  font-weight: 600;
  color: #334155;
  margin: 0;
}

.text-danger { color: #ef4444; }
.text-2xl { font-size: 24px; }
.text-gray-500 { color: #64748b; }
.text-sm { font-size: 12px; }
.btn-danger { background-color: #ef4444; }
.btn-danger:hover { background-color: #dc2626; }

/* 动画效果 */
.modal-content {
  animation: fadeIn 0.3s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; transform: scale(0.95); }
  to { opacity: 1; transform: scale(1); }
}

/* 美化提交按钮 */
.btn-primary {
  background-color: #409eff;
  color: white;
  font-weight: 500;
  padding: 9px 16px;
  border-radius: 6px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  border: none;
}

.btn-primary:hover {
  background-color: #368ee0;
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.btn-primary:active {
  transform: translateY(0);
  box-shadow: none;
}

.btn-secondary {
  background-color: #909399;
  color: white;
  font-weight: 500;
  padding: 9px 16px;
  border-radius: 6px;
  transition: all 0.2s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  border: none;
}

.btn-secondary:hover {
  background-color: #808389;
  transform: translateY(-1px);
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.btn-secondary:active {
  transform: translateY(0);
  box-shadow: none;
}
</style>