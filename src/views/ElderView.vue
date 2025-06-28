<template>
  <div class="Elder-View">
    <h2 class="view-title">老人信息管理</h2>

    <!-- Toolbar: 统一搜索框和新增按钮 -->
    <div class="toolbar">
      <input type="text" v-model="searchParams.searchTerm" @keyup.enter="fetchElders" placeholder="按姓名或ID搜索..." class="search-input" />
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
            <th>出生日期</th>
            <th>民族</th>          
            <th>教育程度</th>      
            <th>账户名</th>
            <th>床位号</th>
            <th>护理级别</th>
            <th>家属联系方式</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="elder in elders" :key="elder.id">
            <td>{{ elder.id }}</td>
            <td>{{ elder.name }}</td>
            <td>{{ elder.age }}</td>
            <td>{{ formatDisplayDate(elder.birthDate) }}</td>
            <td>{{ elder.ethnicity }}</td> 
            <td>{{ elder.education }}</td> 
            <td>{{ elder.account }}</td>
            <td>{{ elder.bedNumber }}</td>
            <td>{{ elder.careLevel }}</td>
            <td>{{ elder.relativeContact }}</td>
            <td class="action-buttons">
              <button @click="openDetailModal(elder)" class="action-btn detail-btn">详情</button>
              <button @click="openModal(elder)" class="action-btn edit-btn">编辑</button>
              <button @click="confirmDelete(elder.id)" class="action-btn delete-btn">删除</button>
            </td>
          </tr>
          <tr v-if="!elders || elders.length === 0">
            <td colspan="11" class="empty-text">当前没有在院老人信息或未搜索到结果。</td> 
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
      <div class="modal-content elder-modal" @click.stop> 
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑老人信息' : '新增老人' }}</h3>
          <button class="close-btn" @click="closeModal">×</button>
        </div>
        <div class="modal-body">
          <!-- 修复：将整个表单内容和按钮区域都包含在 <form> 标签内 -->
          <form @submit.prevent="handleSave">
            <div class="form-grid">
              <!-- 基础信息（必填项，优先显示） -->
              <div class="form-item"><label>姓名:</label><input v-model="currentElder.name" type="text" required></div>
              <div class="form-item"><label>年龄:</label><input v-model="currentElder.age" type="number" required></div>
              <div class="form-item"><label>出生日期:</label><input v-model="currentElder.birthDate" type="date" required></div>
              <div class="form-item"><label>民族:</label><input v-model="currentElder.ethnicity" type="text" required></div>
              <div class="form-item"><label>账户名:</label><input v-model="currentElder.account" type="text" required></div>
              <!-- 密码只在新增时显示且必填 -->
              <div class="form-item" v-if="!isEditing">
                <label>密码:</label><input v-model="currentElder.password" type="password" required>
              </div>
              <div class="form-item" v-else>
                <label> </label><div></div>
              </div>
              
              <!-- 编辑模式显示完整表单 -->
              <div v-if="isEditing">
                <div class="form-item"><label>教育程度:</label><input v-model="currentElder.education" type="text"></div>
                <div class="form-item"><label>婚姻状况:</label><input v-model="currentElder.maritalStatus" type="text"></div>
                <div class="form-item"><label>床位号:</label><input v-model="currentElder.bedNumber" type="text"></div>
                <div class="form-item"><label>护理级别:</label><input v-model="currentElder.careLevel" type="text"></div>
                <div class="form-item"><label>费用类型:</label><input v-model="currentElder.feeType" type="text"></div>
                <div class="form-item"><label>费用(月):</label><input v-model="currentElder.expenses" type="number"></div>
                
                <!-- 跨两列的文本域 -->
                <div class="form-item full-width"><label>家属联系方式:</label><textarea v-model="currentElder.relativeContact"></textarea></div>
                <div class="form-item full-width"><label>兴趣爱好:</label><textarea v-model="currentElder.hobbies"></textarea></div>
                <div class="form-item full-width"><label>医疗状况:</label><textarea v-model="currentElder.medicalCare"></textarea></div>
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
          <h3>老人详细信息</h3>
          <button class="close-btn" @click="closeDetailModal">×</button>
        </div>
        <div class="modal-body">
          <div v-if="detailElder" class="detail-grid">
            <div class="detail-item"><strong>ID:</strong><span>{{ detailElder.id }}</span></div>
            <div class="detail-item"><strong>姓名:</strong><span>{{ detailElder.name }}</span></div>
            <div class="detail-item"><strong>账户名:</strong><span>{{ detailElder.account }}</span></div>
            <div class="detail-item"><strong>年龄:</strong><span>{{ detailElder.age }}</span></div>
            <div class="detail-item"><strong>民族:</strong><span>{{ detailElder.ethnicity }}</span></div>
            <div class="detail-item"><strong>出生日期:</strong><span>{{ formatDisplayDate(detailElder.birthDate) }}</span></div>
            <div class="detail-item"><strong>教育程度:</strong><span>{{ detailElder.education }}</span></div>
            <div class="detail-item"><strong>婚姻状况:</strong><span>{{ detailElder.maritalStatus }}</span></div>
            <div class="detail-item"><strong>床位号:</strong><span>{{ detailElder.bedNumber }}</span></div>
            <div class="detail-item"><strong>护理级别:</strong><span>{{ detailElder.careLevel }}</span></div>
            <div class="detail-item"><strong>费用类型:</strong><span>{{ detailElder.feeType }}</span></div>
            <div class="detail-item"><strong>费用(月):</strong><span>{{ detailElder.expenses }} 元</span></div>
            <div class="detail-item full-width"><strong>家属联系方式:</strong><span>{{ detailElder.relativeContact }}</span></div>
            <div class="detail-item full-width"><strong>兴趣爱好:</strong><span>{{ detailElder.hobbies }}</span></div>
            <div class="detail-item full-width"><strong>医疗状况:</strong><span>{{ detailElder.medicalCare }}</span></div>
            <div class="detail-item"><strong>入院时间:</strong><span>{{ formatDisplayDate(detailElder.checkInDate, true) }}</span></div>
            <div class="detail-item"><strong>出院时间:</strong><span>{{ formatDisplayDate(detailElder.checkOutDate, true) || '未出院' }}</span></div>
            <div class="detail-item"><strong>信息创建时间:</strong><span>{{ formatDisplayDate(detailElder.createdAt, true) }}</span></div>
            <div class="detail-item"><strong>最后更新时间:</strong><span>{{ formatDisplayDate(detailElder.updatedAt, true) }}</span></div>
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
          <p>您确定要删除这位老人的信息吗？</p>
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
const elders = ref([]);
const loading = ref(true);
const searchParams = reactive({
  searchTerm: ''
});
const isModalVisible = ref(false);
const currentElder = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailElder = ref(null);
const isDeleteDialogVisible = ref(false);
const deletingElderId = ref(null);

// --- API 调用函数 ---
const fetchElders = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/api/elders', {
      params: { searchTerm: searchParams.searchTerm }
    });
    if (response.data.code === 0) {
      elders.value = response.data.data;
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
    if (!currentElder.value.name || !currentElder.value.age || !currentElder.value.birthDate || !currentElder.value.ethnicity || !currentElder.value.account) {
      ElMessage.error('请填写所有必填项 (姓名, 年龄, 出生日期, 民族, 账户名)');
      return;
    }
    if (!isEditing.value && !currentElder.value.password) {
      ElMessage.error('新增老人必须设置密码');
      return;
    }

    if (isEditing.value) {
      await apiClient.put('/api/elders', currentElder.value);
      ElMessage.success('老人信息更新成功！');
    } else {
      // 新增时只发送必要字段
      const minimalElderData = {
        name: currentElder.value.name,
        age: currentElder.value.age,
        birthDate: currentElder.value.birthDate,
        ethnicity: currentElder.value.ethnicity,
        account: currentElder.value.account,
        password: currentElder.value.password
      };
      await apiClient.post('/api/elders', minimalElderData);
      ElMessage.success('新增老人成功！');
    }
    closeModal();
    await fetchElders();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '操作失败，请检查输入');
  }
};

const confirmDelete = (id) => {
  deletingElderId.value = id;
  isDeleteDialogVisible.value = true;
};

const handleConfirmDelete = async () => {
  if (!deletingElderId.value) return;
  
  try {
    await apiClient.delete(`/api/elders/${deletingElderId.value}`);
    ElMessage.success('删除成功！');
    isDeleteDialogVisible.value = false;
    await fetchElders();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '删除失败，请重试');
    isDeleteDialogVisible.value = false;
  } finally {
    deletingElderId.value = null;
  }
};

// --- 弹窗控制 ---
const openModal = (elder = null) => {
  if (elder) {
    isEditing.value = true;
    currentElder.value = { 
      ...elder,
      birthDate: elder.birthDate ? formatDisplayDate(elder.birthDate) : '',
      ethnicity: elder.ethnicity || '',       
      education: elder.education || '',       
      maritalStatus: elder.maritalStatus || '', 
      hobbies: elder.hobbies || '',           
      medicalCare: elder.medicalCare || '',   
      feeType: elder.feeType || '月结',       
      expenses: elder.expenses || null,
      bedNumber: elder.bedNumber || '',       
      careLevel: elder.careLevel || '',       
      relativeContact: elder.relativeContact || '',
      password: elder.password || '',
    };
  } else {
    isEditing.value = false;
    currentElder.value = {
        name: '', 
        age: null,
        birthDate: '', 
        ethnicity: '', 
        account: '', 
        password: '',
        
        education: '',         
        maritalStatus: '',     
        hobbies: '',           
        medicalCare: '',   
        feeType: '月结',       
        expenses: null,        
        bedNumber: '',         
        careLevel: '',         
        relativeContact: ''    
    };
  }
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
  currentElder.value = {};
};

const openDetailModal = (elder) => {
  detailElder.value = { ...elder };
  isDetailModalVisible.value = true;
};

const closeDetailModal = () => {
  isDetailModalVisible.value = false;
  detailElder.value = null;
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
onMounted(fetchElders);
</script>

<style scoped>
/* 基础样式保持不变 */
.Elder-View { padding: 20px; }
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

/* 美化操作按钮 */
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
}

.detail-btn {
  color: #409eff;
  border: 1px solid #409eff;
  background-color: #ffffff;
}

.detail-btn:hover {
  background-color: #e6f4ff;
}

.edit-btn {
  color: #e6a23c;
  border: 1px solid #e6a23c;
  background-color: #ffffff;
}

.edit-btn:hover {
  background-color: #fff7e6;
}

.delete-btn {
  color: #f56c6c;
  border: 1px solid #f56c6c;
  background-color: #ffffff;
}

.delete-btn:hover {
  background-color: #ffe6e6;
}

/* 弹窗样式优化 */
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

/* 新增/编辑老人弹窗 */
.elder-modal {
  width: 800px;
  max-width: 95%;
}

/* 详情弹窗 */
.detail-modal {
  width: 750px;
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