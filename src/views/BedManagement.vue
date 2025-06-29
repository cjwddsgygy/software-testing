<template>
  <div class="bed-management-view">
    <h2 class="view-title">床位信息管理</h2>

    <!-- Toolbar: 统一搜索框和新增按钮 -->
    <div class="toolbar">
      <input type="text" v-model="searchParams.searchTerm" @keyup.enter="fetchBeds" placeholder="按床位号或入住人搜索..." class="search-input" />
      <button @click="fetchBeds" class="btn btn-primary">搜索</button>
      <button @click="openAddEditModal()" class="btn btn-success">新增床位</button>
    </div>

    <!-- 数据列表 -->
    <div v-if="loading" class="loading-text">正在加载床位信息...</div>
    <div v-else class="bed-list">
      <div v-if="!beds || beds.length === 0" class="empty-text">没有找到符合条件的床位。</div>
      <div v-else class="bed-grid">
        <div v-for="bed in beds" :key="bed.id" class="bed-card" @click="openDetailModal(bed)">
          <div class="bed-number" :class="{ 'status-occupied': bed.status === '占用', 'status-vacant': bed.status === '空闲' }">{{ bed.bedNumber }}</div>
          <div class="elder-name">{{ bed.status === '占用' ? bed.name : '(空闲)' }}</div>
        </div>
      </div>
    </div>

    <!-- 新增/编辑床位弹窗 -->
    <div v-if="isAddEditModalVisible" class="modal-overlay" @click="closeAddEditModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3 class="modal-title">{{ isEditing ? '编辑床位信息' : '新增床位' }}</h3>
          <button type="button" class="close-btn" @click="closeAddEditModal">×</button>
        </div>
        <form @submit.prevent="handleSaveBed">
          <div class="form-content">
            <div class="form-grid">
              <div class="form-item">
                <label>床位号:</label>
                <input v-model="currentBed.bedNumber" type="text" required />
              </div>
              <div class="form-item">
                <label>房间号:</label>
                <input v-model="currentBed.roomNumber" type="text" required/>
              </div>
              <div class="form-item">
                <label>状态:</label>
                <select v-model="currentBed.status" required>
                  <option value="空闲">空闲</option>
                  <option value="占用">占用</option>
                </select>
              </div>
            </div>
            <div v-if="currentBed.status === '占用'" class="required-section">
              <div class="form-grid">
                <div class="form-item">
                  <label>入住老人ID:</label>
                  <input v-model="currentBed.eldersId" type="number" placeholder="请输入入住老人ID" required/>
                </div>
                <div class="form-item">
                  <label>入住老人姓名:</label>
                  <input v-model="currentBed.name" type="text" placeholder="请输入入住老人姓名" required/>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label>描述/备注:</label>
              <textarea v-model="currentBed.description" rows="3" placeholder="可输入床位描述或备注信息"></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" @click="closeAddEditModal" class="btn btn-secondary">取消</button>
            <button type="submit" class="btn btn-primary">{{ isEditing ? '保存更新' : '确认新增' }}</button>
          </div>
        </form>
      </div>
    </div>

    <!-- 床位详情弹窗 -->
    <div v-if="isDetailModalVisible" class="modal-overlay" @click="closeDetailModal">
      <div class="modal-content detail-modal" @click.stop>
        <div class="modal-header">
          <h3 class="modal-title">床位详情 ({{ detailBed.bedNumber }})</h3>
          <button type="button" class="close-btn" @click="closeDetailModal">×</button>
        </div>
        <div class="detail-content">
          <div class="detail-grid">
            <div class="detail-item"><strong>床位ID:</strong><span>{{ detailBed.id }}</span></div>
            <div class="detail-item"><strong>床位号:</strong><span>{{ detailBed.bedNumber }}</span></div>
            <div class="detail-item"><strong>房间号:</strong><span>{{ detailBed.roomNumber || 'N/A' }}</span></div>
            <div class="detail-item"><strong>当前状态:</strong><span>{{ detailBed.status }}</span></div>
            <div class="detail-item" v-if="detailBed.status === '占用'">
              <strong>当前入住人:</strong><span>{{ detailBed.name }}</span>
            </div>
            <div class="detail-item" v-if="detailBed.status === '占用'">
              <strong>入住人ID:</strong><span>{{ detailBed.eldersId }}</span>
            </div>
            <div class="detail-item full-width"><strong>创建时间:</strong><span>{{ formatDate(detailBed.createdAt) }}</span></div>
            <div class="detail-item full-width"><strong>最后更新:</strong><span>{{ formatDate(detailBed.updatedAt) }}</span></div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" @click="handleEditFromDetail(detailBed)" class="btn btn-primary">编辑此床位</button>
          <button type="button" @click="confirmCheckout(detailBed)" v-if="detailBed.status === '占用'" class="btn btn-info">办理退住</button>
          <button type="button" @click="confirmDeleteBed(detailBed)" class="btn btn-danger">删除此床位</button>
          <button type="button" @click="closeDetailModal" class="btn btn-secondary">关闭</button>
        </div>
      </div>
    </div>

    <!-- 确认操作弹窗 (通用) -->
    <div v-if="isConfirmModalVisible" class="custom-modal-overlay" @click="closeConfirmModal">
      <div class="custom-modal-content" @click.stop>
        <div class="modal-header">
          <h3 class="modal-title">{{ confirmModal.title }}</h3>
        </div>
        <div class="modal-body text-center">
          <p v-html="confirmModal.message"></p>
          <p class="text-sm text-gray-500 mt-2">{{ confirmModal.note }}</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="closeConfirmModal">取消</button>
          <button type="button" :class="confirmModal.confirmButtonClass" @click="confirmModal.onConfirm">确认</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request';

// --- 响应式状态 ---
const beds = ref([]);
const loading = ref(true);
const searchParams = reactive({ searchTerm: '' });
const isAddEditModalVisible = ref(false);
const currentBed = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailBed = ref({}); // 专门用于详情展示的数据，避免污染

// --- 通用确认弹窗状态 ---
const isConfirmModalVisible = ref(false);
const confirmModal = reactive({
  title: '',
  message: '',
  note: '',
  confirmButtonClass: 'btn btn-primary',
  onConfirm: () => {}
});

// --- 工具函数 ---
const formatDate = (dateStr) => {
  if (!dateStr) return 'N/A';
  const date = new Date(dateStr);
  return date.toLocaleString();
};

// --- API 调用 ---
const fetchBeds = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/api/beds', {
      params: { searchTerm: searchParams.searchTerm }
    });
    if (response.data.code === 0) {
      beds.value = response.data.data;
    } else {
      throw new Error(response.data.msg || '获取床位信息失败');
    }
  } catch (error) {
    ElMessage.error(error.message || '网络错误，请检查后端服务');
  } finally {
    loading.value = false;
  }
};

const handleSaveBed = async () => {
  try {
    if (!currentBed.value.bedNumber || !currentBed.value.roomNumber || !currentBed.value.status) {
      ElMessage.error('请填写床位号、房间号和状态');
      return;
    }
    
    // 确保状态与入住信息一致
    if (currentBed.value.status === '占用') {
      if (!currentBed.value.eldersId || !currentBed.value.name) {
        ElMessage.error('状态为“占用”时，必须填写入住老人ID和姓名');
        return;
      }
    } else {
      // 空闲状态清除入住信息
      currentBed.value.name = null;
      currentBed.value.eldersId = null;
    }

    // 构建提交数据结构
    const bedData = {
      id: currentBed.value.id,
      bedNumber: currentBed.value.bedNumber,
      roomNumber: currentBed.value.roomNumber,
      status: currentBed.value.status,
      eldersId: currentBed.value.status === '占用' ? currentBed.value.eldersId : null,
      name: currentBed.value.status === '占用' ? currentBed.value.name : null,
      description: currentBed.value.description
    };

    const apiCall = isEditing.value 
      ? apiClient.put(`/api/beds/${currentBed.value.id}`, bedData)
      : apiClient.post('/api/beds', bedData);
    
    await apiCall;
    ElMessage.success(isEditing.value ? '床位信息更新成功！' : '新增床位成功！');
    closeAddEditModal();
    await fetchBeds();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '操作失败，请检查输入或联系管理员');
  }
};

// --- 弹窗与确认逻辑 ---
const openAddEditModal = (bed = null) => {
  if (bed && bed.id) {
    isEditing.value = true;
    // 复制床位数据，避免直接修改原对象
    currentBed.value = { ...bed };
  } else {
    isEditing.value = false;
    // 初始化新床位数据
    currentBed.value = { 
      bedNumber: '', 
      roomNumber: '', 
      status: '空闲', 
      name: null, 
      eldersId: null,
      description: ''
    };
  }
  isAddEditModalVisible.value = true;
};

const closeAddEditModal = () => {
  isAddEditModalVisible.value = false;
  currentBed.value = {};
};

const openDetailModal = (bed) => {
  detailBed.value = { ...bed };
  isDetailModalVisible.value = true;
};

const closeDetailModal = () => {
  isDetailModalVisible.value = false;
  detailBed.value = {};
};

const closeConfirmModal = () => {
  isConfirmModalVisible.value = false;
};

const handleEditFromDetail = (bed) => {
  closeDetailModal();
  setTimeout(() => openAddEditModal(bed), 150);
};

const confirmCheckout = (bed) => {
  confirmModal.title = '确认办理退住';
  confirmModal.message = `您要为床位 <strong>${bed.bedNumber}</strong> 上的老人办理退住吗？`;
  confirmModal.note = '此操作会将床位状态设置为空闲，并清空入住人信息。';
  confirmModal.confirmButtonClass = 'btn btn-info';
  confirmModal.onConfirm = async () => {
    try {
      await apiClient.put(`/api/beds/${bed.id}/checkout`, { status: '空闲' });
      ElMessage.success(`床位 ${bed.bedNumber} 办理退住成功！`);
      closeConfirmModal();
      await fetchBeds();
    } catch (error) {
      ElMessage.error(error.response?.data?.msg || '办理退住失败，请重试');
    }
  };
  isConfirmModalVisible.value = true;
};

const confirmDeleteBed = (bed) => {
  confirmModal.title = '确认删除床位';
  confirmModal.message = `您确定要删除床位 <strong>${bed.bedNumber}</strong> 吗？`;
  confirmModal.note = '此操作将永久删除该床位信息，且无法恢复。';
  confirmModal.confirmButtonClass = 'btn btn-danger';
  confirmModal.onConfirm = async () => {
    try {
      await apiClient.delete(`/api/beds/${bed.id}`);
      ElMessage.success('床位删除成功！');
      closeConfirmModal();
      await fetchBeds();
    } catch (error) {
      ElMessage.error(error.response?.data?.msg || '删除失败，请重试');
    }
  };
  isConfirmModalVisible.value = true;
};

onMounted(fetchBeds);
</script>

<style scoped>
/* 视图标题和通用容器 */
.bed-management-view {
  padding: 20px;
  font-family: 'Arial', sans-serif;
  background-color: #f8f9fa;
  min-height: calc(100vh - 60px);
}

.view-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 25px;
  text-align: center;
  font-weight: 600;
  letter-spacing: -0.5px;
}

/* 工具栏 */
.toolbar {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  justify-content: center;
}

.search-input {
  padding: 10px 15px;
  border: 1px solid #ccc;
  border-radius: 6px;
  flex-grow: 1;
  max-width: 400px;
  font-size: 16px;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.search-input:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  color: #fff;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  white-space: nowrap;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.btn-primary { background-color: #409eff; }
.btn-primary:hover { background-color: #66b1ff; }
.btn-success { background-color: #67c23a; }
.btn-success:hover { background-color: #85ce61; }
.btn-secondary { background-color: #909399; }
.btn-secondary:hover { background-color: #a6a9ad; }
.btn-info { background-color: #17a2b8; }
.btn-info:hover { background-color: #138496; }
.btn-danger { background-color: #f56c6c; }
.btn-danger:hover { background-color: #f78989; }

/* 列表加载和空状态文本 */
.loading-text, .empty-text {
  font-size: 18px;
  color: #888;
  text-align: center;
  padding: 60px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

/* 床位网格布局 */
.bed-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(180px, 1fr));
  gap: 25px;
  padding: 20px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 4px 15px rgba(0,0,0,0.05);
}

/* 床位卡片 */
.bed-card {
  background: #fff;
  border: 1px solid #eef1f6;
  border-radius: 10px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 150px;
}

.bed-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.1);
  border-color: #409eff;
}

.bed-card .bed-number {
  border-radius: 50%;
  width: 60px;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  margin-bottom: 15px;
  transition: background-color 0.3s, transform 0.3s;
  box-shadow: 0 2px 5px rgba(0,0,0,0.1);
}

.bed-card:hover .bed-number {
  transform: scale(1.05);
}

.bed-card .status-occupied { background-color: #f56c6c; /* 占用 - 红色 */ }
.bed-card .status-vacant { background-color: #67c23a; /* 空闲 - 绿色 */ }
.bed-card:hover .status-occupied { background-color: #f78989; }
.bed-card:hover .status-vacant { background-color: #85ce61; }

.elder-name {
  font-size: 18px;
  color: #555;
  font-weight: 500;
}

/* 模态框通用样式 */
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
  animation: fadeIn 0.3s ease-out;
}

.modal-content {
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  width: 500px; /* 调整弹窗宽度 */
  max-width: 90%;
  overflow: hidden;
  animation: slideIn 0.3s ease-out;
  display: flex;
  flex-direction: column;
}

.modal-header {
  padding: 18px 24px;
  border-bottom: 1px solid #f0f2f5;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fafafa;
}

.modal-title {
  font-size: 20px;
  color: #333;
  margin: 0;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 24px;
  color: #909399;
  cursor: pointer;
  transition: color 0.3s;
  padding: 0 5px;
}

.close-btn:hover {
  color: #606266;
}

.form-content {
  padding: 24px;
  max-height: 70vh;
  overflow-y: auto;
}

.form-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.form-item {
  margin-bottom: 0;
}

.form-item label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #606266;
  font-size: 15px;
}

.form-item input,
.form-item select,
.form-item textarea {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 15px;
  transition: border-color 0.3s, box-shadow 0.3s;
}

.form-item input:focus,
.form-item select:focus,
.form-item textarea:focus {
  outline: none;
  border-color: #409eff;
  box-shadow: 0 0 0 2px rgba(64, 158, 255, 0.2);
}

.modal-footer {
  padding: 15px 24px;
  border-top: 1px solid #f0f2f5;
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  background-color: #fafafa;
}

/* 详情模态框特有样式 */
.detail-modal {
  width: 600px; /* 调整详情弹窗宽度 */
}

.detail-content {
  padding: 24px;
  max-height: 70vh;
  overflow-y: auto;
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
  font-size: 16px;
  line-height: 1.5;
}

.detail-item strong {
  color: #555;
  min-width: 100px;
  font-weight: 500;
  margin-right: 10px;
}

.detail-item span {
  color: #333;
  word-break: break-all;
  flex: 1;
}

.detail-item.full-width {
  grid-column: 1 / -1;
}

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
  z-index: 1001;
  backdrop-filter: blur(3px);
}

.custom-modal-content {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  width: 380px;
  max-width: 90%;
  display: flex;
  flex-direction: column;
  animation: fadeIn 0.3s ease-out;
  overflow: hidden;
}

/* 新增/编辑表单中必须填写部分的样式 */
.required-section {
  background-color: #fafafa;
  padding: 15px;
  border-radius: 6px;
  border: 1px solid #eee;
  margin-bottom: 20px;
}

/* 动画 */
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

@keyframes slideIn {
  from { transform: translateY(-20px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>