<template>
  <div class="elder-view">
    <div class="view-header">
      <h2 class="view-title">老人信息管理</h2>
      <p class="view-description">查看、添加和管理所有在院老人的详细信息</p>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="search-container">
        <div class="search-input-wrapper">
          <i class="fas fa-search search-icon"></i>
          <input type="text" v-model="searchParams.searchTerm" @keyup.enter="fetchElders" placeholder="按姓名或ID搜索..."/>
          <button @click="fetchElders" class="action-btn" title="搜索"><i class="fas fa-search"></i></button>
          <button v-if="searchParams.searchTerm" @click="resetSearch" class="clear-button" title="清空">×</button>
        </div>
      </div>
      <div class="button-group">
        <button @click="openModal()" class="btn btn-primary add-btn">
          <i class="fas fa-plus"></i>
          <span>新增老人</span>
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
              <th>床位号</th>
              <th>护理级别</th>
              <th>家属联系方式</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody v-if="!loading && elders.length > 0">
            <tr v-for="elder in elders" :key="elder.id" class="table-row-hover">
              <td>{{ elder.id }}</td>
              <td>{{ elder.name }}</td>
              <td>{{ elder.age }}</td>
              <td>{{ elder.account }}</td>
              <td><span class="tag tag-bed">{{ elder.bedNumber || '未分配' }}</span></td>
              <td><span class="tag" :class="getCareLevelClass(elder.careLevel)">{{ elder.careLevel || '未评定' }}</span></td>
              <td class="notes-cell" :title="elder.relativeContact">{{ elder.relativeContact || '无' }}</td>
              <td class="actions-cell">
                <button @click="openDetailModal(elder)" class="action-btn" title="查看详情"><i class="fas fa-eye"></i></button>
                <button @click="openModal(elder)" class="action-btn" title="编辑"><i class="fas fa-pencil-alt"></i></button>
                <button @click="confirmDelete(elder.id)" class="action-btn" title="删除"><i class="fas fa-trash"></i></button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="loading" class="loading-placeholder"><div class="loading-spinner"></div><p>正在加载...</p></div>
        <div v-if="!loading && elders.length === 0" class="empty-content"><i class="fas fa-users"></i><p>暂无老人信息</p></div>
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
          <h3>{{ isEditing ? '编辑老人信息' : '新增老人' }}</h3>
          <button @click="closeModal" class="close-btn" title="关闭">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSave">
            <div class="form-section">
              <h4 class="section-title">基础信息 (必填)</h4>
              <div class="form-grid">
                <div class="form-field"><label for="name">姓名</label><div class="input-wrapper"><i class="fas fa-user"></i><input id="name" v-model="currentElder.name" type="text" required placeholder="请输入姓名"/></div></div>
                <div class="form-field"><label for="age">年龄</label><div class="input-wrapper"><i class="fas fa-birthday-cake"></i><input id="age" v-model.number="currentElder.age" type="number" required placeholder="请输入年龄"/></div></div>
                <div class="form-field"><label for="birthDate">出生日期</label><div class="input-wrapper"><i class="fas fa-calendar-alt"></i><input id="birthDate" v-model="currentElder.birthDate" type="date" required/></div></div>
                <div class="form-field"><label for="ethnicity">民族</label><div class="input-wrapper"><i class="fas fa-flag"></i><input id="ethnicity" v-model="currentElder.ethnicity" type="text" required placeholder="例如：汉族"/></div></div>
                <div class="form-field"><label for="account">账户名</label><div class="input-wrapper"><i class="fas fa-user-circle"></i><input id="account" v-model="currentElder.account" type="text" required placeholder="用于登录的唯一账号"/></div></div>
                <div class="form-field" v-if="!isEditing"><label for="password">初始密码</label><div class="input-wrapper"><i class="fas fa-lock"></i><input id="password" v-model="currentElder.password" type="password" required placeholder="为新用户设置密码"/></div></div>
              </div>
            </div>
            
            <div class="form-section">
              <h4 class="section-title">详细信息 (可选)</h4>
              <div class="form-grid">
                <div class="form-field"><label for="education">教育程度</label><div class="input-wrapper"><i class="fas fa-graduation-cap"></i><input id="education" v-model="currentElder.education" type="text" placeholder="例如：高中、本科"/></div></div>
                <div class="form-field"><label for="maritalStatus">婚姻状况</label><div class="input-wrapper"><i class="fas fa-heart"></i><input id="maritalStatus" v-model="currentElder.maritalStatus" type="text" placeholder="例如：已婚、丧偶"/></div></div>
                <div class="form-field"><label for="bedNumber">床位号</label><div class="input-wrapper"><i class="fas fa-bed"></i><input id="bedNumber" v-model="currentElder.bedNumber" type="text" placeholder="例如：A101"/></div></div>
                <div class="form-field"><label for="careLevel">护理级别</label><div class="input-wrapper"><i class="fas fa-user-md"></i><input id="careLevel" v-model="currentElder.careLevel" type="text" placeholder="例如：一级护理"/></div></div>
                <div class="form-field"><label for="feeType">费用类型</label><div class="input-wrapper"><i class="fas fa-credit-card"></i><input id="feeType" v-model="currentElder.feeType" type="text" placeholder="例如：月结、全包"/></div></div>
                <div class="form-field"><label for="expenses">月度费用 (元)</label><div class="input-wrapper"><span>¥</span><input id="expenses" v-model.number="currentElder.expenses" type="number" step="0.01" placeholder="请输入月度费用"/></div></div>
                <div class="form-field full-span"><label for="relativeContact">家属联系方式</label><div class="input-wrapper"><i class="fas fa-phone"></i><textarea id="relativeContact" v-model="currentElder.relativeContact" rows="2" placeholder="姓名 电话，多个请换行"></textarea></div></div>
                <div class="form-field full-span"><label for="hobbies">兴趣爱好</label><div class="input-wrapper"><i class="fas fa-palette"></i><textarea id="hobbies" v-model="currentElder.hobbies" rows="2" placeholder="例如：书法、听戏"></textarea></div></div>
                <div class="form-field full-span"><label for="medicalCare">医疗状况</label><div class="input-wrapper"><i class="fas fa-notes-medical"></i><textarea id="medicalCare" v-model="currentElder.medicalCare" rows="3" placeholder="记录老人的主要健康问题，如：高血压"></textarea></div></div>
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
          <h3>老人详细信息</h3>
          <button @click="closeDetailModal" class="close-btn" title="关闭">×</button>
        </div>
        <div v-if="detailElder" class="modal-body">
          <div class="detail-grid">
            <div class="detail-item"><div class="item-label"><i class="fas fa-id-card-o"></i> ID</div><div class="item-value">{{ detailElder.id }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user"></i> 姓名</div><div class="item-value">{{ detailElder.name }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user-circle"></i> 账户名</div><div class="item-value">{{ detailElder.account }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-birthday-cake"></i> 年龄</div><div class="item-value">{{ detailElder.age }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-flag"></i> 民族</div><div class="item-value">{{ detailElder.ethnicity }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-calendar-alt"></i> 出生日期</div><div class="item-value">{{ formatDate(detailElder.birthDate) }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-graduation-cap"></i> 教育程度</div><div class="item-value">{{ detailElder.education || '未填写' }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-heart"></i> 婚姻状况</div><div class="item-value">{{ detailElder.maritalStatus || '未填写' }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-bed"></i> 床位号</div><div class="item-value">{{ detailElder.bedNumber || '未分配' }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user-md"></i> 护理级别</div><div class="item-value">{{ detailElder.careLevel || '未评定' }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-credit-card"></i> 费用类型</div><div class="item-value">{{ detailElder.feeType || '未设置' }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-money-bill-wave"></i> 月度费用</div><div class="item-value">{{ detailElder.expenses ? detailElder.expenses + ' 元' : '未设置' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-phone"></i> 家属联系方式</div><div class="item-value note-value">{{ detailElder.relativeContact || '未填写' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-palette"></i> 兴趣爱好</div><div class="item-value note-value">{{ detailElder.hobbies || '未填写' }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-notes-medical"></i> 医疗状况</div><div class="item-value note-value">{{ detailElder.medicalCare || '未填写' }}</div></div>
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
            <p class="confirm-message">您确定要删除这位老人的信息吗？</p>
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
const elders = ref([]);
const loading = ref(true);

const searchParams = reactive({
  searchTerm: '',
  page: 1,       // 当前页码，默认为第1页
  pageSize: 10,  // 每页显示条数，默认为10条
  total: 0       // 总记录数，初始为0，由后端返回
});

const isModalVisible = ref(false);
const currentElder = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailElder = ref(null);
const isDeleteDialogVisible = ref(false);
const deletingElderId = ref(null);

// --- API 调用 ---
// ElderView.vue -> <script setup>

const fetchElders = async () => {
  loading.value = true;
  try {
    // ✅ 在 params 中加入所有需要的参数
    const response = await apiClient.get('/api/elders', { 
      params: { 
        searchTerm: searchParams.searchTerm,
        page: searchParams.page,
        pageSize: searchParams.pageSize
      } 
    });
    
    if (response.data.code === 0 && response.data.data) {
      // ✅ 后端返回的数据结构是 { list: [...], total: ... }
      elders.value = response.data.data.list || [];
      searchParams.total = response.data.data.total || 0;
    } else {
      // 清空数据，以防显示旧内容
      elders.value = [];
      searchParams.total = 0;
    }
  } catch (error) {
    console.error('获取老人信息失败:', error);
    elders.value = [];
    searchParams.total = 0;
  } finally {
    loading.value = false;
  }
};

const handleSave = async () => {
  const requiredFields = ['name', 'age', 'birthDate', 'ethnicity', 'account'];
  if (!isEditing.value) requiredFields.push('password');
  for (const field of requiredFields) {
    if (!currentElder.value[field] || String(currentElder.value[field]).trim() === '') {
      alert(`${field} 是必填项！`); 
      return;
    }
  }

  try {
    if (isEditing.value) {
      await apiClient.put(`/api/elders/${currentElder.value.elders_id}`, currentElder.value);
    } else {
      await apiClient.post('/api/elders', currentElder.value);
    }
    closeModal();
    await fetchElders();
  } catch (error) {
    console.error('保存老人信息失败:', error);
    alert(`操作失败: ${error.response?.data?.msg || error.message}`);
  }
};

const deleteElder = async (id) => {
  try {
    await apiClient.delete(`/api/elders/${id}`);
    isDeleteDialogVisible.value = false;
    await fetchElders();
  } catch (error) {
    console.error('删除老人信息失败:', error);
  }
};

// --- 弹窗控制 ---
const openModal = (elder = null) => {
  if (elder) {
    isEditing.value = true;
    currentElder.value = { ...elder, birthDate: formatDate(elder.birthDate) };
  } else {
    isEditing.value = false;
    currentElder.value = {
      name: '', age: null, birthDate: formatDate(new Date()), ethnicity: '', account: '', password: '',
      education: '', maritalStatus: '', hobbies: '', medicalCare: '',
      feeType: '月结', expenses: null, bedNumber: '', careLevel: '', relativeContact: ''
    };
  }
  isModalVisible.value = true;
};
const closeModal = () => { isModalVisible.value = false; currentElder.value = {}; };
const openDetailModal = (elder) => { detailElder.value = elder; isDetailModalVisible.value = true; };
const closeDetailModal = () => { isDetailModalVisible.value = false; detailElder.value = null; };
const confirmDelete = (id) => { deletingElderId.value = id; isDeleteDialogVisible.value = true; };
const handleConfirmDelete = () => deleteElder(deletingElderId.value);

// --- 工具函数 ---
const formatDate = (dateString) => dateString ? new Date(dateString).toISOString().split('T')[0] : '';
const resetSearch = () => { searchParams.searchTerm = ''; fetchElders(); };
const getCareLevelClass = (level) => {
  if (!level) return 'default';
  if (level.includes('一')) return 'danger';
  if (level.includes('二')) return 'warning';
  if (level.includes('三')) return 'success';
  return 'info';
};

// ✅✅✅ 处理分页器事件的方法 ✅✅✅

/**
 * 当每页显示条数改变时触发 (e.g., from 10 to 20)
 */
const handleSizeChange = (newPageSize) => {
  searchParams.pageSize = newPageSize;
  searchParams.page = 1; // 改变每页条数时，通常回到第一页
  fetchElders();
};

/**
 * 当页码改变时触发 (e.g., from page 1 to 2)
 */
const handleCurrentChange = (newPage) => {
  searchParams.page = newPage;
  fetchElders();
};

// --- 生命周期钩子 ---
onMounted(fetchElders);
watch(() => searchParams.searchTerm, () => {
  searchParams.page = 1;
  fetchElders();
});
watch(() => searchParams.page, fetchElders);
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