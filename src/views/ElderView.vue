<!-- 文件路径: src/views/ElderView.vue -->
<template>
  <div class="Elder-View">
    <h2 class="view-title">老人信息管理</h2>

    <!-- Toolbar: 统一搜索框和新增按钮 -->
    <div class="toolbar">
      <!-- 关键修改: v-model 绑定到 searchParams.searchTerm -->
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
          <!-- 注意: :key 应该使用唯一的ID, 比如 edler.id -->
          <tr v-for="elder in elders" :key="elder.id">
            <td>{{ elder.id }}</td>
            <td>{{ elder.name }}</td>
            <td>{{ elder.account }}</td>
            <td>{{ elder.age }}</td>
            <td>{{ elder.bedNumber }}</td>
            <td>{{ elder.careLevel }}</td>
            <td>{{ elder.relativeContact }}</td>
            <td>{{ formatDisplayDate(elder.checkInDate) }}</td>
            <td>
              <button @click="openDetailModal(elder)" class="btn-action detail">详情</button>
              <button @click="openModal(elder)" class="btn-action edit">编辑</button>
              <button @click="handleDelete(elder.id)" class="btn-action delete">删除</button>
            </td>
          </tr>
          <tr v-if="!elders || elders.length === 0">
            <td colspan="9" class="empty-text">当前没有在院老人信息或未搜索到结果。</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 新增/编辑弹窗 -->
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

  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import apiClient from '@/api'; // 确保这是你配置好的 Axios 实例

// --- 响应式状态 ---
const elders = ref([]);
const loading = ref(true);

// 关键修改: 使用统一的 searchTerm
const searchParams = reactive({
  searchTerm: ''
});

// 新增/编辑弹窗状态
const isModalVisible = ref(false);
const currentElder = ref({});
const isEditing = ref(false);

// 详情弹窗状态
const isDetailModalVisible = ref(false);
const detailElder = ref(null);

// --- API 调用函数 ---
const fetchElders = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/elders', {
      params: { // 只发送 searchTerm
        searchTerm: searchParams.searchTerm 
      }
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

const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('您确定要删除这位老人的信息吗?', '警告', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    });
    
    // 使用RESTful风格的DELETE请求
    await apiClient.delete(`/elders/${id}`);

    ElMessage.success('删除成功！');
    await fetchElders();
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.response?.data?.msg || '删除操作失败');
    }
  }
};

// --- 弹窗控制 ---
const openModal = (elder = null) => {
  if (elder) {
    isEditing.value = true;
    currentElder.value = { 
      ...elder, 
      checkInDate: elder.checkInDate ? elder.checkInDate.split('T')[0] : ''
    };
  } else {
    isEditing.value = false;
    currentElder.value = {
        name: '', account: '', age: null, bedNumber: '',
        careLevel: '三级护理', relativeContact: '', 
        checkInDate: new Date().toISOString().split('T')[0]
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
</style>