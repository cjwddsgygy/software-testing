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
          <i class="fas fa-search search-icon"></i>
          <input 
            type="text" 
            v-model="searchParams.searchTerm" 
            @keyup.enter="fetchHealthRecords" 
            placeholder="按老人姓名或记录类型搜索..."
          />
          <button @click="fetchHealthRecords" class="action-btn" title="搜索"><i class="fas fa-search"></i></button>
          <button v-if="searchParams.searchTerm" @click="resetSearch" class="clear-button" title="清空">×</button>
        </div>
      </div>
      <div class="button-group">
        <button @click="openModal()" class="btn btn-primary add-btn">
          <i class="fas fa-plus"></i>
          <span>新增记录</span>
        </button>
      </div>
    </div>
    
    <!-- 数据表格 -->
    <div class="data-card">
      <div class="table-responsive">
        <table class="data-table">
          <thead class="table-header-rounded">
            <tr>
              <th>ID</th>
              <th>老人姓名</th>
              <th>记录类型</th>
              <th>测量值</th>
              <th>记录日期</th>
              <th>记录护工</th>
              <th class="notes-cell">健康描述</th>
              <th class="notes-cell">医生建议</th>
              <th class="notes-cell">备注</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody v-if="!loading && healthRecords.length > 0">
            <tr v-for="record in healthRecords" :key="record.id" class="table-row-hover">
              <td>{{ record.id }}</td>
              <td>{{ record.elderName }}</td>
              <td><span class="tag" :class="getRecordTypeClass(record.recordType)">{{ record.recordType }}</span></td>
              <td class="font-medium">{{ record.value }}</td>
              <td>{{ formatDate(record.recordDate) }}</td>
              <td>{{ record.careworkerName }}</td>
              <td class="notes-cell" :title="record.description">{{ record.description || '无' }}</td>
              <td class="notes-cell" :title="record.doctorNotes">{{ record.doctorNotes || '无' }}</td>
              <td class="notes-cell" :title="record.notes">{{ record.notes || '无' }}</td>
              <td class="actions-cell">
                <button @click="openDetailModal(record)" class="action-btn" title="查看详情"><i class="fas fa-eye"></i></button>
                <button @click="openModal(record)" class="action-btn" title="编辑"><i class="fas fa-pencil-alt"></i></button>
                <button @click="confirmDelete(record.id)" class="action-btn" title="删除"><i class="fas fa-trash"></i></button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="loading" class="loading-placeholder"><div class="loading-spinner"></div><p>正在加载...</p></div>
        <div v-if="!loading && healthRecords.length === 0" class="empty-content"><i class="fas fa-inbox"></i><p>暂无健康记录</p></div>
              <div class="pagination-container" v-if="searchParams.total > 0">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="searchParams.total"
          v-model:current-page="searchParams.page"
          v-model:page-size="searchParams.pageSize"
          :page-sizes="[10, 20, 50, 100]"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-backdrop" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑健康记录' : '新增健康记录' }}</h3>
          <button @click="closeModal" class="close-btn" title="关闭">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSave">
            <div class="form-section">
              <h4 class="section-title">核心健康信息 (必填)</h4>
              <div class="form-grid">
                <div class="form-field">
                  <label for="elderName">老人姓名</label>
                  <div class="input-wrapper"><i class="fas fa-user-tie"></i><input id="elderName" v-model="currentRecord.elderName" type="text" required placeholder="请输入老人姓名"/></div>
                </div>
                <div class="form-field">
                  <label for="recordType">记录类型</label>
                  <div class="input-wrapper">
                    <i class="fas fa-heartbeat"></i>
                    <select id="recordType" v-model="currentRecord.recordType" required>
                      <option disabled value="">请选择记录类型</option>
                      <option>血压</option>
                      <option>体温</option>
                      <option>血糖</option>
                      <option>心率</option>
                      <option>体重</option>
                      <option>其他</option>
                    </select>
                  </div>
                </div>
                <div class="form-field">
                  <label for="value">测量值</label>
                  <div class="input-wrapper"><i class="fas fa-ruler-combined"></i><input id="value" v-model="currentRecord.value" type="text" required placeholder="例如: 120/80 mmHg, 36.5 °C"/></div>
                </div>
                <div class="form-field">
                  <label for="recordDate">记录日期</label>
                  <div class="input-wrapper"><i class="fas fa-calendar-alt"></i><input id="recordDate" v-model="currentRecord.recordDate" type="date" required/></div>
                </div>
              </div>
            </div>
            <div class="form-section">
              <h4 class="section-title">附加信息 (可选)</h4>
              <div class="form-grid">
                <div class="form-field">
                  <label for="careworkerName">记录护工</label>
                  <div class="input-wrapper"><i class="fas fa-user-md"></i><input id="careworkerName" v-model="currentRecord.careworkerName" type="text" placeholder="请输入记录护工姓名"/></div>
                </div>
                <div class="form-field">
                  <label for="elder">关联老人</label>
                  <input id="elder" v-model="currentRecord.elder">
                </div>
                <div class="form-field full-span">
                  <label for="description">健康描述</label>
                  <textarea id="description" v-model="currentRecord.description" rows="3"></textarea>
                </div>
                <div class="form-field full-span">
                  <label for="doctorNotes">医生建议</label>
                  <textarea id="doctorNotes" v-model="currentRecord.doctorNotes" rows="3"></textarea>
                </div>
                <div class="form-field full-span">
                  <label for="notes">备注</label>
                  <div class="input-wrapper"><i class="fas fa-pen"></i><textarea id="notes" v-model="currentRecord.notes" rows="3" placeholder="请输入备注信息"></textarea></div>
                </div>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" @click="closeModal" class="btn btn-secondary"><span>取消</span></button>
          <button type="button" @click="handleSave" class="btn btn-danger">
            <i class="fas fa-check"></i>
            <span>{{ isEditing ? '保存更新' : '确认新增' }}</span>
          </button>
        </div>
      </div>
    </div>
    
    <!-- 详情弹窗 -->
    <div v-if="isDetailModalVisible" class="modal-backdrop" @click="closeDetailModal">
      <div class="modal-content detail-modal" @click.stop>
        <div class="modal-header">
          <h3>健康记录详情</h3>
          <button @click="closeDetailModal" class="close-btn" title="关闭">×</button>
        </div>
        <div v-if="detailRecord" class="modal-body">
          <div class="detail-grid">
            <div class="detail-item"><div class="item-label"><i class="fas fa-id-card"></i> ID</div><div class="item-value">{{ detailRecord.id }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user-tie"></i> 老人姓名</div><div class="item-value">{{ detailRecord.elderName }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-heartbeat"></i> 记录类型</div><div class="item-value"><span class="tag" :class="getRecordTypeClass(detailRecord.recordType)">{{ detailRecord.recordType }}</span></div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-ruler-combined"></i> 测量值</div><div class="item-value font-medium">{{ detailRecord.value }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-calendar-alt"></i> 记录日期</div><div class="item-value">{{ formatDate(detailRecord.recordDate) }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user-md"></i> 记录护工</div><div class="item-value">{{ detailRecord.careworkerName || '未记录' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-file-medical-alt"></i> 健康描述</div><div class="item-value note-value">{{ detailRecord.description || '无' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-stethoscope"></i> 医生建议</div><div class="item-value note-value">{{ detailRecord.doctorNotes || '无' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-link"></i> 关联老人</div><div class="item-value">{{ detailRecord.elder || '无' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-pen"></i> 备注</div><div class="item-value note-value">{{ detailRecord.notes || '无' }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-clock"></i> 创建时间</div><div class="item-value">{{ formatDateTime(detailRecord.createdAt) }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-history"></i> 最后更新</div><div class="item-value">{{ formatDateTime(detailRecord.updatedAt) }}</div></div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" @click="closeDetailModal" class="btn btn-danger">
            <i class="fas fa-check"></i>
            <span>关闭</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="isDeleteDialogVisible" class="modal-backdrop" @click="isDeleteDialogVisible = false">
      <div class="confirm-modal" @click.stop>
        <div class="confirm-icon-wrapper"><div class="confirm-icon"><i class="fas fa-exclamation-triangle"></i></div></div>
        <div class="confirm-content">
            <h3 class="confirm-title">确认删除</h3>
            <p class="confirm-message">您确定要删除这条健康记录吗？</p>
            <p class="confirm-submessage">此操作将永久删除，无法恢复。</p>
            <div class="confirm-actions">
              <button type="button" class="btn btn-secondary" @click="isDeleteDialogVisible = false"><span>取消</span></button>
              <button type="button" class="btn btn-danger" @click="handleConfirmDelete">
                <i class="fas fa-trash"></i>
                <span>确认删除</span>
              </button>
            </div>
        </div>
      </div>
    </div>

    <!-- 全局通知 -->
    <div v-if="notification.show" class="notification" :class="`is-${notification.type}`">
      <i :class="notification.icon" class="notification-icon"></i>
      <span>{{ notification.message }}</span>
      <button @click="closeNotification" class="notification-close">×</button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import apiClient from '@/api/request'; // api客户端
import { ElMessage } from 'element-plus';

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
const notification = ref({ show: false, type: 'success', message: '', icon: '' });

// --- API 调用 ---
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
      showNotification('error', response.data.msg || '获取记录失败');
    }
  } catch (error) {
    console.error('获取健康记录错误:', error);
    showNotification('error', '网络错误，无法加载记录');
  } finally {
    loading.value = false;
  }
};

const handleSave = async () => {
  // --- 前端校验 ---
  if (!currentRecord.value.elderName?.trim()) {
    showNotification('warning', '请输入老人姓名');
    return;
  }
  if (!currentRecord.value.recordType) {
    showNotification('warning', '请选择记录类型');
    return;
  }
  if (!currentRecord.value.value?.trim()) {
    showNotification('warning', '请输入测量值');
    return;
  }
  if (!currentRecord.value.recordDate) {
    showNotification('warning', '请选择记录日期');
    return;
  }

  try {
    let response;
    if (isEditing.value) {
      response = await apiClient.put(`/api/healthRecords/${currentRecord.value.id}`, currentRecord.value);
    } else {
      response = await apiClient.post('/api/healthRecords', currentRecord.value);
    }

    if (response.data.code === 0) {
      showNotification('success', isEditing.value ? '更新成功！' : '新增成功！');
      closeModal();
      await fetchHealthRecords();
    } else {
      showNotification('error', response.data.msg || '操作失败');
    }
  } catch (error) {
    console.error('保存健康记录错误:', error);
    showNotification('error', '操作失败，请检查网络或联系管理员');
  }
};

const deleteRecord = async (id) => {
  try {
    const response = await apiClient.delete(`/api/healthRecords/${id}`);
    if (response.data.code === 0) {
      showNotification('success', '删除成功！');
      isDeleteDialogVisible.value = false;
      // 如果删除的是最后一页的唯一一条数据，返回上一页
      if (healthRecords.value.length === 1 && searchParams.page > 1) {
        searchParams.page--;
      }
      await fetchHealthRecords();
    } else {
      showNotification('error', response.data.msg || '删除失败');
    }
  } catch (error) {
    console.error('删除健康记录错误:', error);
    showNotification('error', '删除失败，请检查网络或联系管理员');
  }
};

// --- 弹窗控制 ---
const openModal = (record = null) => {
  if (record) {
    isEditing.value = true;
    currentRecord.value = { ...record, recordDate: formatDate(record.recordDate) };
  } else {
    isEditing.value = false;
    currentRecord.value = {
      careworkerName: '',
      elderName: '',
      notes: '',
      recordDate: formatDate(new Date()),
      recordType: '',
      value: '',
      description: '', 
      doctorNotes: '', 
      elder: ''        
    };
  }
  isModalVisible.value = true;
};
const closeModal = () => { isModalVisible.value = false; currentRecord.value = {}; };
const openDetailModal = (record) => { detailRecord.value = record; isDetailModalVisible.value = true; };
const closeDetailModal = () => { isDetailModalVisible.value = false; detailRecord.value = null; };
const confirmDelete = (id) => { deletingRecordId.value = id; isDeleteDialogVisible.value = true; };
const handleConfirmDelete = () => deleteRecord(deletingRecordId.value);

// --- 工具函数 ---
const formatDate = (dateString) => dateString ? new Date(dateString).toISOString().split('T')[0] : '';
const formatDateTime = (dateString) => dateString ? new Date(dateString).toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-') : '';
const resetSearch = () => { searchParams.searchTerm = ''; }; // watch会触发fetch

const getRecordTypeClass = (type) => {
  const map = {
    '血压': 'danger',
    '体温': 'warning',
    '血糖': 'info',
    '心率': 'success',
    '体重': 'primary'
  };
  return map[type] || 'default';
};

// --- 通知系统 ---
const showNotification = (type, message) => {
  const icons = {
    success: 'fas fa-check-circle',
    warning: 'fas fa-exclamation-triangle',
    error: 'fas fa-times-circle',
    info: 'fas fa-info-circle',
  };
  notification.value = { show: true, type, message, icon: icons[type] };
  setTimeout(() => {
    notification.value.show = false;
  }, 3000);
};
const closeNotification = () => { notification.value.show = false; };

// ✅✅✅ 处理分页器事件的方法 ✅✅✅

/**
 * 当每页显示条数改变时触发 (e.g., from 10 to 20)
 */
const handleSizeChange = (newPageSize) => {
  searchParams.pageSize = newPageSize;
  searchParams.page = 1; // 改变每页条数时，通常回到第一页
  fetchHealthRecords();
};

/**
 * 当页码改变时触发 (e.g., from page 1 to 2)
 */
const handleCurrentChange = (newPage) => {
  searchParams.page = newPage;
  fetchHealthRecords();
};

// --- 生命周期与监听 ---
onMounted(fetchHealthRecords);
watch(() => searchParams.searchTerm, () => {
  searchParams.page = 1;
  fetchHealthRecords();
});
watch(() => searchParams.page, fetchHealthRecords);
</script>

<style scoped>
/* --- 基础布局与头部 --- */
.expenses-view {
  padding: 25px;
  /* 背景色和字体将从 base.css 全局样式继承 */
}
.view-header {
  text-align: center;
  margin-bottom: 25px;
}
.view-title {
  font-size: 1.75rem;
  font-weight: 600;
  color: var(--text-primary);
}
.view-description {
  font-size: 1rem;
  color: var(--text-regular);
  margin-top: 8px;
}

/* --- 工具栏 --- */
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 1rem;
}
.search-container {
  position: relative;
  width: 100%;
  max-width: 400px;
}
.search-input-wrapper {
  display: flex;
  align-items: center;
}
.search-icon {
  position: absolute;
  left: 15px;
  color: var(--text-secondary);
  pointer-events: none;
}
.search-input-wrapper input {
  flex-grow: 1;
  padding: 10px 80px 10px 40px; /* 调整右边距以容纳两个按钮 */
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.2s;
  background-color: var(--bg-card);
  color: var(--text-primary);
}
.search-input-wrapper input:focus {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.15);
  outline: none;
}
.search-input-wrapper .action-btn {
  position: absolute;
  right: 6px;
  top: 50%;
  transform: translateY(-50%);
}
.clear-button {
  position: absolute;
  right: 46px; /* 调整位置 */
  top: 50%;
  transform: translateY(-50%);
  background: #c8c9cc;
  color: white;
  border: none;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  cursor: pointer;
  transition: background-color 0.2s;
}
.clear-button:hover {
  background-color: #909399;
}


/* --- 数据卡片与表格 --- */
.data-card {
  background: var(--bg-card);
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
  overflow: hidden;
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
  padding: 15px;
  text-align: left;
  border-bottom: 1px solid var(--border-color-light, #f0f2f5);
  white-space: nowrap;
}
.data-table thead th {
  background-color: var(--bg-page);
  font-weight: 600;
  color: var(--text-regular);
  position: sticky;
  top: 0;
  z-index: 1;
}
.table-row-hover {
  transition: background-color 0.3s ease;
}
.table-row-hover:hover {
  background-color: rgba(64, 158, 255, 0.05);
}

/* --- 状态标签 --- */
.tag {
  padding: 5px 12px;
  border-radius: 15px;
  font-size: 0.8rem;
  font-weight: 500;
  display: inline-block;
  text-transform: capitalize;
}
.tag.success { background-color: #f0f9eb; color: #67c23a; }
.tag.warning { background-color: #fdf6ec; color: #e6a23c; }
.tag.danger { background-color: #fef0f0; color: #f56c6c; }
.tag.info { background-color: #f4f4f5; color: #909399; }
.tag.default { background-color: #e9e9eb; color: #909399; }

/* --- 表格内操作按钮 --- */
.actions-cell {
  display: flex;
  gap: 10px;
}
.action-btn {
  width: 36px;
  height: 36px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  border: none;
  background-color: var(--bg-page);
  color: var(--text-secondary);
  cursor: pointer;
  transition: all 0.2s;
  font-size: 1rem;
}
.action-btn:hover {
  color: #fff;
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}
.action-btn[title="搜索"]:hover,
.action-btn[title="查看详情"]:hover {
  background-color: var(--primary-color);
}
.action-btn[title="编辑"]:hover {
  background-color: #e6a23c; /* warning-color */
}
.action-btn[title="删除"]:hover {
  background-color: var(--danger-color);
}


/* --- 弹窗通用样式 --- */
.modal-backdrop {
  position: fixed;
  inset: 0; /* top, right, bottom, left = 0 */
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
  backdrop-filter: blur(3px);
  animation: fadeIn 0.3s;
}
.modal-content {
  background: var(--bg-card);
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  width: 95%;
  max-width: 700px;
  display: flex;
  flex-direction: column;
  max-height: 90vh;
  animation: slideUp 0.3s;
}
.modal-header {
  padding: 20px 25px;
  border-bottom: 1px solid var(--border-color-light, #f0f2f5);
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.modal-header h3 {
  margin: 0;
  font-size: 1.2rem;
  font-weight: 600;
  color: var(--text-primary);
}
.close-btn {
  background: none;
  border: none;
  font-size: 1.5rem;
  color: var(--text-secondary);
  cursor: pointer;
}
.modal-body {
  padding: 25px;
  overflow-y: auto;
}
.modal-footer {
  padding: 20px 25px;
  border-top: 1px solid var(--border-color-light, #f0f2f5);
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}


/* --- 表单样式 --- */
.form-section {
  padding-bottom: 1.5rem;
  margin-bottom: 1.5rem;
  border-bottom: 1px dashed var(--border-color-light, #e4e7ed);
}
.form-section:last-child {
  margin-bottom: 0;
  border-bottom: none;
}
.section-title {
  font-size: 1.1rem;
  font-weight: 600;
  color: var(--text-primary);
  margin-bottom: 1.25rem;
}
.form-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 18px 25px;
}
.form-field {
  display: flex;
  flex-direction: column;
}
.form-field.full-span {
  grid-column: 1 / -1;
}
.form-field label {
  margin-bottom: 8px;
  font-weight: 500;
  color: var(--text-regular);
}
.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
}
.input-wrapper .fas,
.input-wrapper > span {
  position: absolute;
  left: 15px;
  color: var(--text-secondary);
  font-size: 1rem;
}
.form-field input,
.form-field select,
.form-field textarea {
  width: 100%;
  padding: 10px 15px 10px 40px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.2s;
}
.input-wrapper > span + input {
  padding-left: 30px;
}
.form-field select {
  appearance: none;
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16'%3e%3cpath fill='none' stroke='%23343a40' stroke-linecap='round' stroke-linejoin='round' stroke-width='2' d='M2 5l6 6 6-6'/%3e%3c/svg%3e");
  background-repeat: no-repeat;
  background-position: right 1rem center;
  background-size: 0.8em;
  padding-right: 2.5rem;
}
.form-field textarea {
  resize: vertical;
  min-height: 80px;
  padding: 10px 15px;
}
.input-wrapper textarea ~ .fas {
  top: 15px;
  transform: none;
}
.form-field input:focus,
.form-field select:focus,
.form-field textarea:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(64, 158, 255, 0.15);
}

/* --- 特定弹窗样式 --- */
.confirm-modal {
  background-color: var(--bg-card);
  width: 420px;
  text-align: center;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid var(--danger-color);
}
.confirm-icon-wrapper {
  background-color: #fef0f0;
  padding: 30px;
}
.confirm-icon {
  color: var(--danger-color);
  font-size: 2.5rem;
}
.confirm-content {
  padding: 25px;
}
.confirm-title {
  font-size: 1.5rem;
  font-weight: 600;
  color: var(--text-primary);
}
.confirm-message {
  font-size: 1rem;
  color: var(--text-regular);
  margin-top: 10px;
}
.confirm-submessage {
  font-size: 0.9rem;
  color: var(--text-secondary);
  margin-top: 5px;
  margin-bottom: 25px;
}
.confirm-actions {
  display: flex;
  justify-content: center;
  gap: 15px;
}

.detail-modal .item-label {
  color: var(--text-secondary);
  font-size: 0.9rem;
  margin-bottom: 6px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.detail-modal .item-value {
  font-size: 1rem;
  padding: 10px 15px;
  background-color: var(--bg-page);
  border-radius: 8px;
  min-height: 20px;
  word-break: break-all;
}
.detail-modal .note-value {
  white-space: pre-wrap;
}
.detail-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px 30px;
}
.detail-item {
  display: flex;
  flex-direction: column;
}
.detail-item.full-span {
  grid-column: 1 / -1;
}

/* --- 加载与空状态 --- */
.empty-content, .loading-placeholder {
  padding: 60px 0;
  text-align: center;
  color: var(--text-secondary);
}
.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f0f2f5;
  border-top-color: var(--primary-color);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 15px;
}

.pagination-container {
  padding: 20px 0;
  display: flex;
  justify-content: center;
}

/* --- 动画 --- */
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
@keyframes spin { to { transform: rotate(360deg); } }
</style>