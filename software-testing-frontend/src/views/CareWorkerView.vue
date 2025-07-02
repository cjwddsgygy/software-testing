<template>
  <div class="care-worker-view">
    <div class="view-header">
      <h2 class="view-title">护工信息管理</h2>
      <p class="view-description">查看、添加和管理所有护工的详细信息</p>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="search-container">
        <div class="search-input-wrapper">
          <i class="fas fa-search search-icon"></i>
          <input type="text" v-model="searchParams.searchTerm" @keyup.enter="fetchCareWorkers" placeholder="按ID或姓名搜索..."/>
		            <button @click="fetchExpenses" class="action-btn" title="搜索"><i class="fas fa-search"></i></button>
          <button v-if="searchParams.searchTerm" @click="resetSearch" class="clear-button" title="清空">×</button>
        </div>
      </div>
      <div class="button-group">
        <button @click="openModal()" class="btn btn-primary add-btn">
          <i class="fas fa-plus"></i>
          <span>新增护工</span>
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
              <th>姓名</th>
              <th>年龄</th>
              <th>账户名</th>
              <th>状态</th>
              <th>学历</th>
              <th>特长</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody v-if="!loading && careWorkers.length > 0">
            <tr v-for="worker in careWorkers" :key="worker.id" class="table-row-hover">
              <td>{{ worker.id }}</td>
              <td>{{ worker.name }}</td>
              <td>{{ worker.age }}</td>
              <td>{{ worker.account }}</td>
              <td><span class="tag" :class="getStatusClass(worker.status)">{{ getStatusText(worker.status) }}</span></td>
              <td>{{ worker.education || '未填写' }}</td>
              <td class="notes-cell" :title="worker.specialties">{{ worker.specialties || '无' }}</td>
              <td class="actions-cell">
                <button @click="openDetailModal(worker)" class="action-btn" title="查看详情"><i class="fas fa-eye"></i></button>
                <button @click="openModal(worker)" class="action-btn" title="编辑"><i class="fas fa-pencil-alt"></i></button>
                <button @click="confirmDelete(worker.id)" class="action-btn" title="删除"><i class="fas fa-trash"></i></button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="loading" class="loading-placeholder"><div class="loading-spinner"></div><p>正在加载...</p></div>
        <div v-if="!loading && careWorkers.length === 0" class="empty-content"><i class="fas fa-user-md"></i><p>暂无护工信息</p></div>
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
          <h3>{{ isEditing ? '编辑护工信息' : '新增护工' }}</h3>
          <button @click="closeModal" class="close-btn" title="关闭">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSave">
            <div class="form-section">
              <h4 class="section-title">基础信息 (必填)</h4>
              <div class="form-grid">
                <div class="form-field"><label for="name">姓名</label><div class="input-wrapper"><i class="fas fa-user"></i><input id="name" v-model="currentCareWorker.name" type="text" required placeholder="请输入姓名"/></div></div>
                <div class="form-field"><label for="age">年龄</label><div class="input-wrapper"><i class="fas fa-birthday-cake"></i><input id="age" v-model.number="currentCareWorker.age" type="number" required placeholder="请输入年龄"/></div></div>
                <div class="form-field"><label for="birthDate">出生日期</label><div class="input-wrapper"><i class="fas fa-calendar-alt"></i><input id="birthDate" v-model="currentCareWorker.birthDate" type="date" required/></div></div>
                <div class="form-field"><label for="ethnicity">民族</label><div class="input-wrapper"><i class="fas fa-flag"></i><input id="ethnicity" v-model="currentCareWorker.ethnicity" type="text" required placeholder="例如：汉族"/></div></div>
                <div class="form-field"><label for="account">账户名</label><div class="input-wrapper"><i class="fas fa-user-circle"></i><input id="account" v-model="currentCareWorker.account" type="text" required placeholder="用于登录的唯一账号"/></div></div>
                <div class="form-field"><label for="status">状态</label><div class="input-wrapper"><i class="fas fa-check-circle"></i>
                  <select id="status" v-model="currentCareWorker.status" required>
                    <option disabled value="">请选择状态</option>
      <option value="在职">在职</option>
      <option value="休假">休假</option>
                  </select>
                </div></div>
                <div class="form-field full-span" v-if="!isEditing"><label for="password">初始密码</label><div class="input-wrapper"><i class="fas fa-lock"></i><input id="password" v-model="currentCareWorker.password" type="password" required placeholder="为新护工设置登录密码"/></div></div>
              </div>
            </div>
            
            <div class="form-section">
              <h4 class="section-title">附加信息 (可选)</h4>
              <div class="form-grid">
                <div class="form-field"><label for="education">学历</label><div class="input-wrapper"><i class="fas fa-graduation-cap"></i><input id="education" v-model="currentCareWorker.education" type="text" placeholder="例如：大专、本科"/></div></div>
                <div class="form-field full-span"><label for="experience">工作经验</label><div class="input-wrapper"><i class="fas fa-briefcase"></i><textarea id="experience" v-model="currentCareWorker.experience" rows="3" placeholder="请描述护工的工作经历"></textarea></div></div>
                <div class="form-field full-span"><label for="specialties">特长</label><div class="input-wrapper"><i class="fas fa-star"></i><textarea id="specialties" v-model="currentCareWorker.specialties" rows="3" placeholder="例如：康复训练、心理疏导"></textarea></div></div>
                <div class="form-field full-span"><label for="assignedElders">负责老人</label><div class="input-wrapper"><i class="fas fa-users"></i><textarea id="assignedElders" v-model="currentCareWorker.assignedElders" rows="2" placeholder="输入负责的老人姓名，用逗号分隔"></textarea></div></div>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <button type="button" @click="closeModal" class="btn btn-secondary"><span>取消</span></button>
          <button type="button" @click="handleSave" class="btn btn-danger"><i class="fas fa-check"></i><span>{{ isEditing ? '保存更新' : '确认新增' }}</span></button>
        </div>
      </div>
    </div>
    
    <!-- 详情弹窗 -->
    <div v-if="isDetailModalVisible" class="modal-backdrop" @click="closeDetailModal">
      <div class="modal-content detail-modal" @click.stop>
        <div class="modal-header">
          <h3>护工详细信息</h3>
          <button @click="closeDetailModal" class="close-btn" title="关闭">×</button>
        </div>
        <div v-if="detailCareWorker" class="modal-body">
          <div class="detail-grid">
            <div class="detail-item"><div class="item-label"><i class="fas fa-id-card-o"></i> ID</div><div class="item-value">{{ detailCareWorker.id }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user"></i> 姓名</div><div class="item-value">{{ detailCareWorker.name }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user-circle"></i> 账户名</div><div class="item-value">{{ detailCareWorker.account }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-birthday-cake"></i> 年龄</div><div class="item-value">{{ detailCareWorker.age }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-flag"></i> 民族</div><div class="item-value">{{ detailCareWorker.ethnicity }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-calendar-alt"></i> 出生日期</div><div class="item-value">{{ formatDate(detailCareWorker.birthDate) }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-check-circle"></i> 状态</div><div class="item-value"><span class="tag" :class="getStatusClass(detailCareWorker.status)">{{ getStatusText(detailCareWorker.status) }}</span></div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-graduation-cap"></i> 学历</div><div class="item-value">{{ detailCareWorker.education || '未填写' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-briefcase"></i> 工作经验</div><div class="item-value note-value">{{ detailCareWorker.experience || '无' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-star"></i> 特长</div><div class="item-value note-value">{{ detailCareWorker.specialties || '无' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-users"></i> 负责老人</div><div class="item-value note-value">{{ detailCareWorker.assignedElders || '暂无' }}</div></div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" @click="closeDetailModal" class="btn btn-danger"><span>关闭</span></button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="isDeleteDialogVisible" class="modal-backdrop" @click="isDeleteDialogVisible = false">
      <div class="confirm-modal" @click.stop>
        <div class="confirm-icon-wrapper"><div class="confirm-icon"><i class="fas fa-exclamation-triangle"></i></div></div>
        <div class="confirm-content">
            <h3 class="confirm-title">确认删除</h3>
            <p class="confirm-message">您确定要删除这位护工的信息吗？</p>
            <p class="confirm-submessage">此操作将永久删除，无法恢复。</p>
            <div class="confirm-actions">
              <button type="button" class="btn btn-secondary" @click="isDeleteDialogVisible = false"><span>取消</span></button>
              <button type="button" class="btn btn-danger" @click="handleConfirmDelete"><i class="fas fa-trash"></i><span>确认删除</span></button>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, watch } from 'vue';
import apiClient from '@/api/request';
import { ElMessage } from 'element-plus'; 

// --- 响应式状态 ---
const careWorkers = ref([]);
const loading = ref(true);

const searchParams = reactive({
  searchTerm: '',
  page: 1,       // 当前页码，默认为第1页
  pageSize: 10,  // 每页显示条数，默认为10条
  total: 0       // 总记录数，初始为0，由后端返回
});

const isModalVisible = ref(false);
const currentCareWorker = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailCareWorker = ref(null);
const isDeleteDialogVisible = ref(false);
const deletingCareWorkerId = ref(null);

// --- API 调用 ---
const fetchCareWorkers = async () => {
  loading.value = true;
  try {
    // ✅ 在 params 中加入了分页参数 page 和 pageSize
    const response = await apiClient.get('/api/careWorkers', { 
      params: { 
        searchTerm: searchParams.searchTerm,
        page: searchParams.page,
        pageSize: searchParams.pageSize
      } 
    });
    
    // ✅ 确保后端返回的数据结构是 { code: 0, data: { list: [...], total: ... } }
    if (response.data.code === 0 && response.data.data) {
      // ✅ 将返回的数据列表赋值给 careWorkers
      careWorkers.value = response.data.data.list || [];
      // ✅ 将返回的总记录数赋值给 searchParams.total，用于驱动分页器
      searchParams.total = response.data.data.total || 0;
    } else {
      // 如果请求成功但业务码不为0，或data为空，清空现有数据
      careWorkers.value = [];
      searchParams.total = 0;
      console.error('获取护工信息失败:', response.data.msg || '服务器返回数据格式不正确');
    }
  } catch (error) {
    // 如果请求直接失败（如网络错误，40x, 50x），也清空数据
    console.error('获取护工信息失败:', error);
    careWorkers.value = [];
    searchParams.total = 0;
  } finally {
    loading.value = false;
  }
};

const handleSave = async () => {
  const requiredFields = ['name', 'age', 'birthDate', 'ethnicity', 'account', 'status'];
  if (!isEditing.value) { requiredFields.push('password'); }
  for (const field of requiredFields) {
    if (currentCareWorker.value[field] === null || currentCareWorker.value[field] === undefined || String(currentCareWorker.value[field]).trim() === '') {
      alert(`${field} 是必填项！`); 
      return;
    }
  }

  try {
    if (isEditing.value) {
      await apiClient.put(`/api/careWorkers/${currentCareWorker.value.id}`, currentCareWorker.value);
    } else {
      await apiClient.post('/api/careWorkers', currentCareWorker.value);
    }
    closeModal();
    await fetchCareWorkers();
  } catch (error) {
    console.error('保存护工信息失败:', error);
    alert(`操作失败: ${error.response?.data?.msg || error.message}`);
  }
};

const deleteCareWorker = async (id) => {
  try {
    await apiClient.delete(`/api/careWorkers/${id}`);
    isDeleteDialogVisible.value = false;
    await fetchCareWorkers();
  } catch (error) {
    console.error('删除护工信息失败:', error);
  }
};

// --- 弹窗控制 ---
const openModal = (worker = null) => {
  if (worker) {
    isEditing.value = true;
    

    // 复制 worker 对象，并确保 status 字段是字符串，以匹配 <select> 的 <option> value
    currentCareWorker.value = { 
      ...worker, 
      status: String(worker.status), // 强制转换为字符串
      birthDate: formatDate(worker.birthDate) 
    };

  } else {
    isEditing.value = false;
    // 新增时，默认为 '1' (在职)，已经是字符串，无需修改
    currentCareWorker.value = {
      status: '1' ,
      birthDate: formatDate(new Date())
    }; 
  }
  isModalVisible.value = true;
};

const closeModal = () => { isModalVisible.value = false; currentCareWorker.value = {}; };
const openDetailModal = (worker) => { detailCareWorker.value = worker; isDetailModalVisible.value = true; };
const closeDetailModal = () => { isDetailModalVisible.value = false; detailCareWorker.value = null; };
const confirmDelete = (id) => { deletingCareWorkerId.value = id; isDeleteDialogVisible.value = true; };
const handleConfirmDelete = () => deleteCareWorker(deletingCareWorkerId.value);

// --- 工具函数 ---
const formatDate = (dateString) => dateString ? new Date(dateString).toISOString().split('T')[0] : '';
const resetSearch = () => { searchParams.searchTerm = ''; fetchCareWorkers(); };
const getStatusText = (status) => {
  // 现在可以直接返回 status，或者提供一个默认值
  return status || '未定义'; 
};

const getStatusClass = (status) => {
  // 根据中文状态返回对应的 CSS 类名
  switch (status) {
    case '在职':
      return 'success';
    case '休假':
      return 'info';
    default:
      return 'default';
  }
};

// ✅✅✅ 处理分页器事件的方法 ✅✅✅

/**
 * 当每页显示条数改变时触发 (e.g., from 10 to 20)
 */
const handleSizeChange = (newPageSize) => {
  searchParams.pageSize = newPageSize;
  searchParams.page = 1; // 改变每页条数时，通常回到第一页
  fetchCareWorkers();
};

/**
 * 当页码改变时触发 (e.g., from page 1 to 2)
 */
const handleCurrentChange = (newPage) => {
  searchParams.page = newPage;
  fetchCareWorkers();
};

// --- 生命周期钩子 ---
onMounted(fetchCareWorkers);
watch(() => searchParams.searchTerm, () => {
  searchParams.page = 1;
  fetchCareWorkers();
});
watch(() => searchParams.page, fetchCareWorkers);
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