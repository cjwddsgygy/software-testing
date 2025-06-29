<template>
  <div class="expenses-view">
    <div class="view-header">
      <h2 class="view-title">消费记录管理</h2>
      <p class="view-description">查看、添加和管理老人的消费记录</p>
    </div>

    <!-- 工具栏 -->
    <div class="toolbar">
      <div class="search-container">
        <div class="search-input-wrapper">
          <i class="fas fa-search search-icon"></i>
          <input type="text" v-model="searchParams.searchTerm" @keyup.enter="fetchExpenses" placeholder="按老人姓名或项目搜索..."/>
          <!-- 修复 #8: 搜索按钮样式 -->
          <button @click="fetchExpenses" class="action-btn" title="搜索"><i class="fas fa-search"></i></button>
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
              <th>项目名称</th>
              <th>支付状态</th>
              <th>金额</th>
              <th>消费日期</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody v-if="!loading && expenses.length > 0">
            <tr v-for="expense in expenses" :key="expense.id" class="table-row-hover">
              <td>{{ expense.id }}</td>
              <td>{{ expense.elderName }}</td>
              <td>{{ expense.item }}</td>
              <td><span class="tag" :class="getStatusClass(expense.status)">{{ expense.status || '未设置' }}</span></td>
              <td class="text-right font-medium">{{ expense.amount?.toFixed(2) }} 元</td>
              <td>{{ formatDate(expense.date) }}</td>
              <td class="actions-cell">
                <button @click="openDetailModal(expense)" class="action-btn" title="查看详情"><i class="fas fa-eye"></i></button>
                <button @click="openModal(expense)" class="action-btn" title="编辑"><i class="fas fa-pencil-alt"></i></button>
                <button @click="confirmDelete(expense.id)" class="action-btn" title="删除"><i class="fas fa-trash"></i></button>
              </td>
            </tr>
          </tbody>
        </table>
        <div v-if="loading" class="loading-placeholder"><div class="loading-spinner"></div><p>正在加载...</p></div>
        <div v-if="!loading && expenses.length === 0" class="empty-content"><i class="fas fa-inbox"></i><p>暂无消费记录</p></div>
      </div>
    </div>

    <!-- 新增/编辑弹窗 -->
    <div v-if="isModalVisible" class="modal-backdrop" @click="closeModal">
      <div class="modal-content" @click.stop>
        <div class="modal-header">
          <h3>{{ isEditing ? '编辑消费记录' : '新增消费记录' }}</h3>
          <button @click="closeModal" class="close-btn" title="关闭">×</button>
        </div>
        <div class="modal-body">
          <form @submit.prevent="handleSave">
            <div class="form-section">
              <h4 class="section-title">基础信息</h4>
              <div class="form-grid">
                <div class="form-field"><label for="elderName">老人姓名</label><div class="input-wrapper"><i class="fas fa-user"></i><input id="elderName" v-model="currentExpense.elderName" type="text" required placeholder="请输入老人姓名"/></div></div>
                <div class="form-field"><label for="item">项目名称</label><div class="input-wrapper"><i class="fas fa-shopping-bag"></i><input id="item" v-model="currentExpense.item" type="text" required placeholder="例如：住宿费、护理费"/></div></div>
                <div class="form-field"><label for="amount">金额 (元)</label><div class="input-wrapper"><span>¥</span><input id="amount" v-model.number="currentExpense.amount" type="number" step="0.01" required placeholder="请输入金额"/></div></div>
                <div class="form-field"><label for="date">消费日期</label><div class="input-wrapper"><i class="fas fa-calendar-alt"></i><input id="date" v-model="currentExpense.date" type="date" required/></div></div>
              </div>
            </div>
            <div class="form-section">
              <h4 class="section-title">附加信息 (可选)</h4>
              <div class="form-grid">
                <div class="form-field"><label for="status">支付状态</label><div class="input-wrapper"><i class="fas fa-credit-card"></i><select id="status" v-model="currentExpense.status"><option value="未支付">未支付</option><option value="已支付">已支付</option><option value="处理中">处理中</option><option value="已退款">已退款</option></select></div></div>
                <div class="form-field full-span"><label for="notes">备注</label><div class="input-wrapper"><i class="fas fa-pen"></i><textarea id="notes" v-model="currentExpense.notes" rows="3" placeholder="请输入备注信息"></textarea></div></div>
              </div>
            </div>
          </form>
        </div>
        <div class="modal-footer">
          <!-- 修复 #6: “取消”按钮边框 -->
          <button type="button" @click="closeModal" class="btn btn-secondary"><span>取消</span></button>
          <!-- 修复 #7: “保存更新”按钮边框 -->
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
          <h3>消费记录详情</h3>
          <button @click="closeDetailModal" class="close-btn" title="关闭">×</button>
        </div>
        <div v-if="detailExpense" class="modal-body">
          <div class="detail-grid">
            <div class="detail-item"><div class="item-label"><i class="fas fa-id-card-o"></i> ID</div><div class="item-value">{{ detailExpense.id }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-user"></i> 老人姓名</div><div class="item-value">{{ detailExpense.elderName }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-shopping-bag"></i> 项目名称</div><div class="item-value">{{ detailExpense.item }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-money-bill-wave"></i> 金额</div><div class="item-value font-medium">{{ detailExpense.amount.toFixed(2) }} 元</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-credit-card"></i> 支付状态</div><div class="item-value"><span class="tag" :class="getStatusClass(detailExpense.status)">{{ detailExpense.status || '未设置' }}</span></div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-calendar-alt"></i> 消费日期</div><div class="item-value">{{ formatDate(detailExpense.date) }}</div></div>
            <div class="detail-item full-span"><div class="item-label"><i class="fas fa-pen"></i> 备注</div><div class="item-value note-value">{{ detailExpense.notes || '无' }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-clock"></i> 创建时间</div><div class="item-value">{{ formatDateTime(detailExpense.createdAt) }}</div></div>
            <div class="detail-item"><div class="item-label"><i class="fas fa-history"></i> 最后更新</div><div class="item-value">{{ formatDateTime(detailExpense.updatedAt) }}</div></div>
          </div>
        </div>
        <div class="modal-footer">
          <!-- 修复 #4 & #5: 详情弹窗的“关闭”按钮 -->
          <button type="button" @click="closeDetailModal" class="btn btn-danger">
            <i class="fas fa-check"></i>
            <span>关闭</span>
          </button>
        </div>
      </div>
    </div>

    <!-- 删除确认弹窗 -->
    <div v-if="isDeleteDialogVisible" class="modal-backdrop" @click="isDeleteDialogVisible = false">
      <!-- 修复 #2: 弹窗边框 -->
      <div class="confirm-modal" @click.stop>
        <div class="confirm-icon-wrapper"><div class="confirm-icon"><i class="fas fa-exclamation-triangle"></i></div></div>
        <div class="confirm-content">
            <h3 class="confirm-title">确认删除</h3>
            <p class="confirm-message">您确定要删除这条消费记录吗？</p>
            <p class="confirm-submessage">此操作将永久删除，无法恢复。</p>
            <div class="confirm-actions">
              <!-- 修复 #2: “取消”按钮边框 -->
              <button type="button" class="btn btn-secondary" @click="isDeleteDialogVisible = false"><span>取消</span></button>
              <!-- 修复 #3: “确认删除”按钮文字颜色 -->
              <button type="button" class="btn btn-danger" @click="handleConfirmDelete">
                <i class="fas fa-trash"></i>
                <span>确认删除</span>
              </button>
            </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Script 部分无需修改，保持原样即可
import { ref, reactive, onMounted, watch } from 'vue';
import apiClient from '@/api/request';

const expenses = ref([]);
const loading = ref(true);
const searchParams = reactive({ searchTerm: '', page: 1, pageSize: 10, total: 0 });
const isModalVisible = ref(false);
const currentExpense = ref({});
const isEditing = ref(false);
const isDetailModalVisible = ref(false);
const detailExpense = ref(null);
const isDeleteDialogVisible = ref(false);
const deletingExpenseId = ref(null);

const fetchExpenses = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/api/expenses', { params: { search: searchParams.searchTerm, page: searchParams.page, pageSize: searchParams.pageSize } });
    if (response.data.code === 0) {
      expenses.value = response.data.data.list || [];
      searchParams.total = response.data.data.total || 0;
    }
  } catch (error) {
    console.error('获取消费记录错误:', error);
  } finally {
    loading.value = false;
  }
};
const handleSave = async () => {
  if (!currentExpense.value.elderName?.trim()) return;
  if (!currentExpense.value.item?.trim()) return;
  if (currentExpense.value.amount == null || isNaN(currentExpense.value.amount) || currentExpense.value.amount <= 0) return;
  if (!currentExpense.value.date) return;

  try {
    if (isEditing.value) {
      await apiClient.put(`/api/expenses/${currentExpense.value.id}`, currentExpense.value);
    } else {
      await apiClient.post('/api/expenses', currentExpense.value);
    }
    closeModal();
    await fetchExpenses();
  } catch (error) {
    console.error('保存消费记录错误:', error);
  }
};
const deleteExpense = async (id) => {
  try {
    await apiClient.delete(`/api/expenses/${id}`);
    isDeleteDialogVisible.value = false;
    await fetchExpenses();
  } catch (error) {
    console.error('删除消费记录错误:', error);
  }
};

const openModal = (expense = null) => {
  if (expense) {
    isEditing.value = true;
    currentExpense.value = { ...expense, date: formatDate(expense.date) };
  } else {
    isEditing.value = false;
    currentExpense.value = { elderName: '', item: '', amount: null, date: formatDate(new Date()), notes: '', status: '未支付' };
  }
  isModalVisible.value = true;
};
const closeModal = () => { isModalVisible.value = false; currentExpense.value = {}; };
const openDetailModal = (expense) => { detailExpense.value = expense; isDetailModalVisible.value = true; };
const closeDetailModal = () => { isDetailModalVisible.value = false; detailExpense.value = null; };
const confirmDelete = (id) => { deletingExpenseId.value = id; isDeleteDialogVisible.value = true; };
const handleConfirmDelete = () => deleteExpense(deletingExpenseId.value);

const formatDate = (dateString) => dateString ? new Date(dateString).toISOString().split('T')[0] : '';
const formatDateTime = (dateString) => dateString ? new Date(dateString).toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-') : '';
const resetSearch = () => { searchParams.searchTerm = ''; fetchExpenses(); };
const getStatusClass = (status) => ({ '已支付': 'success', '未支付': 'warning', '已退款': 'danger', '处理中': 'info' }[status] || 'default');

onMounted(fetchExpenses);
watch(() => searchParams.searchTerm, () => { searchParams.page = 1; fetchExpenses(); });
watch(() => searchParams.page, fetchExpenses);
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

/* --- 动画 --- */
@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
@keyframes spin { to { transform: rotate(360deg); } }
</style>