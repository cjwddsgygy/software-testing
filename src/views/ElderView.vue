<!-- 文件路径: src/views/ElderView.vue -->
<template>
  <div class="Elder-View">
    <h2 class="view-title">老人信息管理</h2>

    <!-- Toolbar: 搜索和新增按钮 -->
    <div class="toolbar">
      <input type="text" v-model="searchParams.name" @keyup.enter="fetchElders" placeholder="按姓名搜索..." class="search-input" />
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
            <th>账户名</th>
            <th>年龄</th>
            <th>床位号</th>
            <th>护理级别</th>
            <th>家属联系方式</th>
            <th>入院时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="elder in elders" :key="elder.eldersid">
            <td>{{ elder.eldersid }}</td>
            <td>{{ elder.name }}</td>
            <td>{{ elder.account }}</td>
            <td>{{ elder.age }}</td>
            <td>{{ elder.bedNumber }}</td>
            <td>{{ elder.careLevel }}</td>
            <td>{{ elder.relativeContact }}</td>
            <td>{{ formatDisplayDate(elder.checkInDate) }}</td>
            <td>
              <!-- ✅ 1. 新增“详情”按钮 -->
              <button @click="openDetailModal(elder)" class="btn-action detail">详情</button>
              <button @click="openModal(elder)" class="btn-action edit">编辑</button>
              <button @click="handleDelete(elder.eldersid)" class="btn-action delete">删除</button>
            </td>
          </tr>
          <tr v-if="!elders || elders.length === 0">
            <td colspan="9" class="empty-text">当前没有在院老人信息或未搜索到结果。</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 (保持原样) -->
    <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ isEditing ? '编辑老人信息' : '新增老人' }}</h3>
        <form @submit.prevent="handleSave">
          <div class="form-item"><label>姓名:</label><input v-model="currentElder.name" type="text" required></div>
          <div class="form-item"><label>账户名:</label><input v-model="currentElder.account" type="text" required></div>
          <div class="form-item"><label>年龄:</label><input v-model="currentElder.age" type="number" required></div>
          <div class="form-item"><label>床位号:</label><input v-model="currentElder.bedNumber" type="text"></div>
          <div class="form-item"><label>护理级别:</label><input v-model="currentElder.careLevel" type="text"></div>
          <div class="form-item"><label>家属联系方式:</label><input v-model="currentElder.relativeContact" type="text"></div>
          <div class="form-item"><label>入院时间:</label><input v-model="currentElder.checkInDate" type="date"></div>
          <div class="modal-footer">
            <button type="button" @click="closeModal" class="btn btn-secondary">取消</button>
            <button type="submit" class="btn btn-primary">{{ isEditing ? '保存更新' : '确认新增' }}</button>
          </div>
        </form>
      </div>
    </div>

    <!-- ✅ 2. 新增一个独立的“详情”弹窗 -->
    <div v-if="isDetailModalVisible" class="modal-overlay" @click="closeDetailModal">
      <div class="modal-content detail-modal" @click.stop>
        <h3>老人详细信息</h3>
        <div v-if="detailElder" class="detail-grid">
          <div class="detail-item"><strong>ID:</strong><span>{{ detailElder.eldersid }}</span></div>
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

  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage } from 'element-plus'; // 假设你已安装并配置Element Plus
import request from '@/api/request';

// --- 响应式状态 (编辑弹窗) ---
const elders = ref([]);
const loading = ref(true);
const searchParams = reactive({ name: '' });
const isModalVisible = ref(false); // 这是新增/编辑弹窗的开关
const currentElder = ref({});
const isEditing = computed(() => !!currentElder.value.id);
const defaultElder = {
  id: null, name: '', account: '', age: null, bedNumber: '',
  careLevel: '三级护理', relativeContact: '', checkInDate: new Date().toISOString().split('T')[0]
};

// ✅ 3. 新增用于“详情”弹窗的状态
const isDetailModalVisible = ref(false); // 这是详情弹窗的开关
const detailElder = ref(null); // 用于存放要显示详情的老人数据


// --- API 调用函数 ---
const fetchElders = async () => {
  loading.value = true;
  try {
    const response = await request.get('/elders', { params: searchParams });
    if (response.data.code === 1) {
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

// --- 保存和删除逻辑 (保持原样) ---
const handleSave = async () => {
  try {
    const apiCall = isEditing.value 
      ? request.put('/elders', currentElder.value)
      : request.post('/elders', currentElder.value);
    await apiCall;
    ElMessage.success(isEditing.value ? '更新成功！' : '新增成功！');
    closeModal();
    await fetchElders();
  } catch (error) {
    ElMessage.error('操作失败');
  }
};

const handleDelete = async (id) => {
  if (!confirm('确定要删除这位老人的信息吗?')) return;
  try {
    await request.delete(`/elders/${id}`);
    ElMessage.success('删除成功！');
    await fetchElders();
  } catch (error) {
    ElMessage.error('删除失败');
  }
};

// --- 新增/编辑弹窗控制 (保持原样) ---
const openModal = (elder = null) => {
  if (elder) {
    currentElder.value = { ...elder, checkInDate: elder.checkInDate ? elder.checkInDate.split('T')[0] : '' };
  } else {
    currentElder.value = { ...defaultElder };
  }
  isModalVisible.value = true;
};

const closeModal = () => {
  isModalVisible.value = false;
};

// ✅ 4. 新增“详情”弹窗的控制函数
const openDetailModal = (elder) => {
  detailElder.value = elder;
  isDetailModalVisible.value = true;
};

const closeDetailModal = () => {
  isDetailModalVisible.value = false;
  detailElder.value = null;
};

// --- 工具函数 ---
// ✅ 5. 增强日期格式化函数，使其能处理时间和日期
const formatDisplayDate = (dateString, showTime = false) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  if (isNaN(date.getTime())) return dateString; 
  
  const year = date.getFullYear();
  const month = (date.getMonth() + 1).toString().padStart(2, '0');
  const day = date.getDate().toString().padStart(2, '0');
  
  if (showTime) {
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  }
  return `${year}-${month}-${day}`;
};

// --- 生命周期钩子 ---
onMounted(fetchElders);
</script>

<style scoped>
/* ✅ 6. 新增和调整样式 */
.btn-action.detail { color: #409eff; } /* 详情按钮颜色 */

.detail-modal {
  width: 800px; /* 详情弹窗更宽 */
  max-width: 95%;
}

.detail-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr); /* 两列网格布局 */
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
  grid-column: 1 / -1; /* 占满整行 */
}


/* --- 以下是你原有的样式，保持不变 --- */
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.loading-text, .empty-text { font-size: 16px; color: #888; text-align: center; padding: 40px; }
.toolbar { display: flex; gap: 10px; margin-bottom: 20px; }
.search-input { padding: 8px 12px; border: 1px solid #ccc; border-radius: 4px; flex-grow: 1; max-width: 300px; }
.btn { padding: 8px 15px; border: none; border-radius: 4px; color: #fff; cursor: pointer; }
.btn-primary { background-color: #409eff; }
.btn-success { background-color: #67c23a; }
.btn-secondary { background-color: #909399; }
.table-container { background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eef1f6; }
th { background-color: #fafafa; font-weight: bold; color: #666; }
.btn-action { background: none; border: none; cursor: pointer; padding: 5px 8px; margin-right: 5px; }
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
</style>