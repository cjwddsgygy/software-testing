<!-- 文件路径: src/views/ElderView.vue -->
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
            <td>
              <button @click="openDetailModal(elder)" class="btn-action detail">详情</button>
              <button @click="openModal(elder)" class="btn-action edit">编辑</button>
              <button @click="confirmDelete(elder.id)" class="btn-action delete">删除</button>
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
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑老人信息' : '新增老人' }}</h3>
        <form @submit.prevent="handleSave">
          <!-- 姓名 (必填) -->
          <div class="form-item"><label>姓名:</label><input v-model="currentElder.name" type="text" required></div>
          <!-- 年龄 (必填) -->
          <div class="form-item"><label>年龄:</label><input v-model="currentElder.age" type="number" required></div>
          <!-- 出生日期 (必填) -->
          <div class="form-item"><label>出生日期:</label><input v-model="currentElder.birthDate" type="date" required></div>
          <!-- 民族 (必填，解决非空约束) -->
          <div class="form-item"><label>民族:</label><input v-model="currentElder.ethnicity" type="text" required></div>
          <!-- 账户名 (必填) -->
          <div class="form-item"><label>账户名:</label><input v-model="currentElder.account" type="text" required></div>
          
          <!-- 以下是可选字段 -->
          <div class="form-item"><label>床位号:</label><input v-model="currentElder.bedNumber" type="text"></div>
          <div class="form-item"><label>护理级别:</label><input v-model="currentElder.careLevel" type="text"></div>
          
          <!-- 默认不展示的字段，但在 currentElder 初始化时需要赋值以避免后端报错 -->
          
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
        <h3>老人详细信息</h3>
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
const isDeleteDialogVisible = ref(false); // 控制删除确认对话框的显示
const deletingElderId = ref(null); // 存储当前要删除的老人ID

// --- API 调用函数 ---
const fetchElders = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/elders', {
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
    // 前端必填项校验
    if (!currentElder.value.name || !currentElder.value.age || !currentElder.value.birthDate || !currentElder.value.ethnicity || !currentElder.value.account) {
      ElMessage.error('请填写所有必填项 (姓名, 年龄, 出生日期, 民族, 账户名)');
      return;
    }

    if (isEditing.value) {
      await apiClient.put('/elders', currentElder.value);
      ElMessage.success('老人信息更新成功！');
    } else {
      await apiClient.post('/elders', currentElder.value);
      ElMessage.success('新增老人成功！');
    }
    closeModal();
    await fetchElders();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '操作失败，请检查输入');
  }
};

// 确认删除，显示对话框
const confirmDelete = (id) => {
  deletingElderId.value = id;
  isDeleteDialogVisible.value = true;
};

// 处理确认删除操作
const handleConfirmDelete = async () => {
  if (!deletingElderId.value) return;
  
  try {
    await apiClient.delete(`/elders/${deletingElderId.value}`);
    ElMessage.success('删除成功！');
    isDeleteDialogVisible.value = false;
    await fetchElders(); // 刷新列表
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
      relativeContact: elder.relativeContact || '' 
    };
  } else {
    isEditing.value = false;
    currentElder.value = {
        name: '', 
        age: null,
        birthDate: '', 
        ethnicity: '', 
        account: '', 
        
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
  detailElder.value = elder;
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
/* 详情按钮颜色 */
.btn-action.detail { color: #409eff; }

.detail-modal {
  width: 800px;
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
  align-items: center;
  border-bottom: 1px solid #f0f2f5;
  padding-bottom: 10px;
  font-size: 14px;
}

.detail-item strong {
  color: #555;
  min-width: 120px;
  font-weight: bold;
}

.detail-item span {
  color: #333;
  word-break: break-all;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

/* --- 以下是通用样式 --- */
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
.btn-action { background: none; border: none; cursor: pointer; padding: 5px 8px; margin-right: 5px; font-size: 14px; }
.btn-action:hover { opacity: 0.7; }
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