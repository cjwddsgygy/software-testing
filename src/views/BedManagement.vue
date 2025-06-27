<!-- 文件路径: src/views/BedManagement.vue -->
<template>
  <div class="bed-management-view">
    <h2 class="view-title">床位管理</h2>

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
          <div class="bed-number">{{ bed.bedNumber }}</div>
          <div class="elder-name">{{ bed.elderName || '(空闲)' }}</div>
        </div>
      </div>
    </div>

    <!-- 新增/编辑床位弹窗 -->
    <div v-if="isAddEditModalVisible" class="modal-overlay" @click="closeAddEditModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑床位信息' : '新增床位' }}</h3>
        <form @submit.prevent="handleSaveBed">
          <div class="form-item">
            <label>床位号:</label>
            <input v-model="currentBed.bedNumber" type="text" required />
          </div>
          <div class="form-item">
            <label>楼层:</label>
            <input v-model="currentBed.floor" type="text" />
          </div>
          <div class="form-item">
            <label>房间号:</label>
            <input v-model="currentBed.roomNumber" type="text" />
          </div>
          <div class="form-item">
            <label>状态:</label>
            <select v-model="currentBed.status" required>
              <option value="空闲">空闲</option>
              <option value="已入住">已入住</option>
              <option value="维护中">维护中</option>
            </select>
          </div>
          <div class="form-item" v-if="currentBed.status === '已入住'">
            <label>入住老人姓名:</label>
            <input v-model="currentBed.elderName" type="text" placeholder="若已入住，请输入老人姓名" />
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
        <h3>床位详情 ({{ currentBed.bedNumber }})</h3>
        <div v-if="currentBed" class="detail-grid">
          <div class="detail-item"><strong>床位ID:</strong><span>{{ currentBed.id }}</span></div>
          <div class="detail-item"><strong>床位号:</strong><span>{{ currentBed.bedNumber }}</span></div>
          <div class="detail-item"><strong>楼层:</strong><span>{{ currentBed.floor || 'N/A' }}</span></div>
          <div class="detail-item"><strong>房间号:</strong><span>{{ currentBed.roomNumber || 'N/A' }}</span></div>
          <div class="detail-item"><strong>当前状态:</strong><span>{{ currentBed.status }}</span></div>
          <div class="detail-item full-width" v-if="currentBed.status === '已入住'">
            <strong>当前入住人:</strong><span>{{ currentBed.elderName }}</span>
          </div>
          <div class="detail-item full-width">
            <strong>描述/备注:</strong><span>{{ currentBed.description || '无' }}</span>
          </div>
        </div>

        <div class="modal-footer mt-4">
          <button type="button" @click="openAddEditModal(currentBed)" class="btn btn-primary">编辑此床位</button>
          <button type="button" @click="confirmCheckout(currentBed.id)" v-if="currentBed.status === '已入住'" class="btn btn-info">办理退住</button>
          <button type="button" @click="confirmDeleteBed(currentBed.id)" class="btn btn-danger">删除此床位</button>
          <button type="button" @click="closeDetailModal" class="btn btn-secondary">关闭</button>
        </div>
      </div>
    </div>

    <!-- 办理退住确认弹窗 -->
    <div v-if="isConfirmCheckoutVisible" class="custom-modal-overlay" @click="isConfirmCheckoutVisible = false">
      <div class="custom-modal-content" @click.stop>
        <div class="modal-header">
          <h3 class="modal-title">确认办理退住</h3>
        </div>
        <div class="modal-body text-center">
          <i class="el-icon-warning-outline text-info text-2xl mb-4"></i>
          <p>您要为床位 <strong>{{ currentBed.bedNumber }}</strong> 上的老人办理退住吗？</p>
          <p class="text-sm text-gray-500 mt-2">此操作会将床位状态设置为空闲，并清空入住人信息。</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="isConfirmCheckoutVisible = false">取消</button>
          <button type="button" class="btn btn-primary" @click="handleCheckout">确认退住</button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="isDeleteBedVisible" class="custom-modal-overlay" @click="isDeleteBedVisible = false">
      <div class="custom-modal-content" @click.stop>
        <div class="modal-header">
          <h3 class="modal-title">确认删除床位</h3>
        </div>
        <div class="modal-body text-center">
          <i class="el-icon-warning-outline text-danger text-2xl mb-4"></i>
          <p>您确定要删除床位 <strong>{{ currentBed.bedNumber }}</strong> 吗？</p>
          <p class="text-sm text-gray-500 mt-2">此操作将永久删除该床位信息，且无法恢复。</p>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="isDeleteBedVisible = false">取消</button>
          <button type="button" class="btn btn-danger" @click="handleDeleteBed">确认删除</button>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import apiClient from '@/api/request'; // 假设您的API请求工具

// --- 响应式状态 ---
const beds = ref([]);
const loading = ref(true);
const searchParams = reactive({
  searchTerm: ''
});
const isAddEditModalVisible = ref(false);
const currentBed = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const isConfirmCheckoutVisible = ref(false);
const isDeleteBedVisible = ref(false);

// --- API 调用函数 ---
const fetchBeds = async () => {
  loading.value = true;
  try {
    // 模拟API请求
    const response = await new Promise(resolve => {
      setTimeout(() => {
        const mockData = [
          { id: 1, bedNumber: 'A101', floor: '1F', roomNumber: '101', status: '已入住', elderName: '张三丰', description: '靠窗，带独立卫生间' },
          { id: 2, bedNumber: 'A102', floor: '1F', roomNumber: '101', status: '空闲', elderName: null, description: '靠窗' },
          { id: 3, bedNumber: 'B201', floor: '2F', roomNumber: '201', status: '已入住', elderName: '李莫愁', description: '朝南' },
          { id: 4, bedNumber: 'B202', floor: '2F', roomNumber: '201', status: '维护中', elderName: null, description: '空调维修中' },
          { id: 5, bedNumber: 'C301', floor: '3F', roomNumber: '301', status: '空闲', elderName: null, description: '视野好' },
        ];
        const filteredData = mockData.filter(bed =>
          (bed.bedNumber && bed.bedNumber.includes(searchParams.searchTerm)) ||
          (bed.elderName && bed.elderName.includes(searchParams.searchTerm))
        );
        resolve({ data: { code: 0, data: filteredData } });
      }, 500);
    });

    if (response.data.code === 0) {
      beds.value = response.data.data;
    } else {
      throw new Error(response.data.msg || '获取床位信息失败');
    }
  } catch (error) {
    ElMessage.error(error.message || '网络错误，获取床位信息失败');
  } finally {
    loading.value = false;
  }
};

const handleSaveBed = async () => {
  try {
    if (!currentBed.value.bedNumber || !currentBed.value.status) {
      ElMessage.error('请填写床位号和状态');
      return;
    }

    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 300));

    if (isEditing.value) {
      // 模拟更新操作
      const index = beds.value.findIndex(b => b.id === currentBed.value.id);
      if (index !== -1) {
        beds.value[index] = { ...currentBed.value };
      }
      ElMessage.success('床位信息更新成功！');
    } else {
      // 模拟新增操作
      const newId = Math.max(...beds.value.map(b => b.id), 0) + 1;
      beds.value.push({ id: newId, ...currentBed.value });
      ElMessage.success('新增床位成功！');
    }
    closeAddEditModal();
    await fetchBeds(); // 刷新列表
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '操作失败，请检查输入');
  }
};

const confirmCheckout = (bedId) => {
  currentBed.value = beds.value.find(b => b.id === bedId);
  isDetailModalVisible.value = false; // 关闭详情弹窗
  isConfirmCheckoutVisible.value = true;
};

const handleCheckout = async () => {
  try {
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 300));

    const bedIdToCheckout = currentBed.value.id;
    const bed = beds.value.find(b => b.id === bedIdToCheckout);
    if (bed) {
      bed.status = '空闲';
      bed.elderName = null;
      ElMessage.success(`床位 ${bed.bedNumber} 办理退住成功，已设为空闲！`);
    } else {
      throw new Error('未找到该床位');
    }
    isConfirmCheckoutVisible.value = false;
    await fetchBeds();
  } catch (error) {
    ElMessage.error(error.message || '办理退住失败，请重试');
    isConfirmCheckoutVisible.value = false;
  } finally {
    currentBed.value = {};
  }
};

const confirmDeleteBed = (bedId) => {
  currentBed.value = beds.value.find(b => b.id === bedId);
  isDetailModalVisible.value = false; // 关闭详情弹窗
  isDeleteBedVisible.value = true;
};

const handleDeleteBed = async () => {
  try {
    // 模拟API请求
    await new Promise(resolve => setTimeout(resolve, 300));

    const bedIdToDelete = currentBed.value.id;
    beds.value = beds.value.filter(b => b.id !== bedIdToDelete);
    ElMessage.success('床位删除成功！');
    isDeleteBedVisible.value = false;
    await fetchBeds();
  } catch (error) {
    ElMessage.error(error.response?.data?.msg || '删除失败，请重试');
    isDeleteBedVisible.value = false;
  } finally {
    currentBed.value = {};
  }
};

// --- 弹窗控制 ---
const openAddEditModal = (bed = null) => {
  if (bed) {
    isEditing.value = true;
    currentBed.value = { ...bed };
  } else {
    isEditing.value = false;
    currentBed.value = { bedNumber: '', floor: '', roomNumber: '', status: '空闲', elderName: null, description: '' };
  }
  isAddEditModalVisible.value = true;
};

const closeAddEditModal = () => {
  isAddEditModalVisible.value = false;
  currentBed.value = {};
};

const openDetailModal = (bed) => {
  currentBed.value = { ...bed };
  isDetailModalVisible.value = true;
};

const closeDetailModal = () => {
  isDetailModalVisible.value = false;
  currentBed.value = {};
};

// --- 生命周期钩子 ---
onMounted(fetchBeds);
</script>

<style scoped>
/* 视图标题和通用容器 */
.bed-management-view {
  padding: 20px;
  font-family: 'Arial', sans-serif;
  background-color: #f8f9fa;
  min-height: calc(100vh - 60px); /* Adjust based on your header height */
}

.view-title {
  font-size: 28px;
  color: #333;
  margin-bottom: 25px;
  text-align: center;
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
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  color: #fff;
  cursor: pointer;
  transition: background-color 0.3s ease, transform 0.2s ease;
  font-size: 16px;
  white-space: nowrap;
}

.btn:hover {
  transform: translateY(-1px);
}

.btn-primary { background-color: #409eff; }
.btn-primary:hover { background-color: #66b1ff; }
.btn-success { background-color: #67c23a; }
.btn-success:hover { background-color: #85ce61; }
.btn-secondary { background-color: #909399; }
.btn-secondary:hover { background-color: #a6a9ad; }
.btn-info { background-color: #17a2b8; } /* For check-out */
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
  background: #f0f2f5;
  border-radius: 10px;
  padding: 20px;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 120px;
}

.bed-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 6px 16px rgba(0,0,0,0.15);
  background-color: #e9ecef;
}

.bed-number {
  font-size: 28px;
  font-weight: bold;
  color: #333;
  margin-bottom: 10px;
}

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
  background: rgba(0, 0, 0, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  animation: fadeIn 0.3s ease-out;
}

.modal-content {
  background: #fff;
  padding: 30px;
  border-radius: 10px;
  width: 550px;
  max-width: 90%;
  box-shadow: 0 5px 15px rgba(0,0,0,0.3);
  animation: slideIn 0.3s ease-out;
}

.modal-content h3 {
  margin-top: 0;
  margin-bottom: 25px;
  font-size: 24px;
  color: #333;
  text-align: center;
}

.form-item {
  margin-bottom: 20px;
}

.form-item label {
  display: block;
  margin-bottom: 8px;
  font-weight: bold;
  color: #555;
}

.form-item input,
.form-item select {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 5px;
  box-sizing: border-box;
  font-size: 16px;
}

.modal-footer {
  text-align: right;
  margin-top: 30px;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

/* 详情模态框特有样式 */
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
  font-size: 16px;
  line-height: 1.5;
}

.detail-item strong {
  color: #555;
  min-width: 100px;
  font-weight: bold;
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

/* 自定义确认对话框样式 (与 ElderView 保持一致) */
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
  z-index: 1001; /* Higher than normal modal */
}

.custom-modal-content {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  width: 380px;
  max-width: 90%;
  display: flex;
  flex-direction: column;
  animation: fadeIn 0.3s ease-out;
}

.modal-header {
  padding: 20px 20px 10px;
  border-bottom: 1px solid #eee;
  text-align: center;
}

.modal-title {
  font-size: 20px;
  color: #333;
  margin: 0;
}

.modal-body {
  padding: 25px 20px;
  text-align: center;
}

.modal-body .text-info { color: #17a2b8; }
.modal-body .text-danger { color: #f56c6c; }
.modal-body .text-2xl { font-size: 28px; display: block; margin-bottom: 15px; } /* Adjust font-size for icon */
.modal-body .mb-4 { margin-bottom: 1rem; } /* Add margin for icon */
.modal-body .text-sm { font-size: 14px; }
.modal-body .text-gray-500 { color: #909399; }

.modal-footer {
  padding: 15px 20px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
  gap: 10px;
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