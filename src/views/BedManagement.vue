<!-- frontend/src/views/BedManagement.vue -->
<template>
  <div class="main-content">
    <h2 class="view-title">床位管理</h2>

    <!-- 操作与筛选区 -->
    <div class="operation-container">
      <el-select v-model="filters.status" placeholder="按状态筛选" clearable @change="fetchBeds" style="width: 150px; margin-right: 10px;">
        <el-option label="空闲" value="空闲"></el-option>
        <el-option label="已入住" value="已入住"></el-option>
        <el-option label="维修中" value="维修中"></el-option>
      </el-select>
      <el-input v-model="filters.search" placeholder="按床位号搜索" clearable @clear="fetchBeds" @keyup.enter="fetchBeds" style="width: 200px; margin-right: 10px;" />
      <el-button type="primary" :icon="Search" @click="fetchBeds">搜索</el-button>
      <el-button type="success" :icon="Plus" @click="handleAddBed">新增床位</el-button>
    </div>

    <!-- 床位网格 -->
    <div v-if="loading" class="loading-text">正在加载床位信息...</div>
    <div v-else-if="bedList.length === 0" class="loading-text">没有找到符合条件的床位。</div>
    <div v-else class="bed-grid">
      <div 
        v-for="bed in bedList" 
        :key="bed.id" 
        :class="['bed', bed.status === '已入住' ? 'occupied' : 'available']"
        @click="openBedDialog(bed)"
      >
        <div class="bed-number">{{ bed.bedNumber }}</div>
        <div class="bed-occupant">{{ bed.elderName || '(空)' }}</div>
      </div>
    </div>

    <!-- 床位操作对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="40%">
      <!-- 新增床位表单 -->
      <el-form v-if="isAdding" :model="form" label-width="80px" ref="formRef">
        <el-form-item label="床位号" prop="bedNumber" required><el-input v-model="form.bedNumber" placeholder="例如：A-101" /></el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status"><el-option label="空闲" value="空闲"/><el-option label="维修中" value="维修中"/></el-select>
        </el-form-item>
      </el-form>
      
      <!-- 床位详情与操作 -->
      <div v-else-if="currentBed">
        <h3>床位详情 ({{ currentBed.bedNumber }})</h3>
        <!-- 空闲床位 -> 办理入住 -->
        <div v-if="currentBed.status === '空闲'">
          <el-form :model="checkinForm" label-width="100px">
            <el-form-item label="选择入住老人" required>
              <el-select v-model="checkinForm.elderId" placeholder="请选择未入住的老人" filterable style="width: 100%;">
                <el-option v-for="elder in unassignedElders" :key="elder.id" :label="`${elder.name} (ID: ${elder.id})`" :value="elder.id" />
              </el-select>
            </el-form-item>
          </el-form>
        </div>

        <!-- 已入住床位 -> 办理退住 -->
        <div v-if="currentBed.status === '已入住'">
          <p><strong>当前入住人：</strong> {{ currentBed.elderName }}</p>
          <p>您要为这位老人办理退住吗？此操作会将床位状态设置为空闲。</p>
        </div>
      </div>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button v-if="isAdding" type="primary" @click="handleSaveNewBed">确认新增</el-button>
        <el-button v-if="currentBed && currentBed.status === '空闲'" type="primary" @click="handleCheckin">办理入住</el-button>
        <el-button v-if="currentBed && currentBed.status === '已入住'" type="warning" @click="handleCheckout">办理退住</el-button>
        <el-button v-if="currentBed && !isAdding" type="danger" @click="handleDeleteBed(currentBed.id)">删除此床位</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Plus } from '@element-plus/icons-vue';
import apiClient from '@/api';

// --- State ---
const loading = ref(true);
const bedList = ref([]);
const filters = reactive({ status: '', search: '' });
const dialogVisible = ref(false);
//const dialogTitle = ref('');
const currentBed = ref(null);
const isAdding = ref(false);
const form = ref({});
const checkinForm = reactive({ elderId: null });
const unassignedElders = ref([]);

// --- Computed ---
const dialogTitle = computed(() => {
  if (isAdding.value) return '新增床位';
  if (currentBed.value) return `管理床位 - ${currentBed.value.bedNumber}`;
  return '床位管理';
});

// --- API Methods ---
const fetchBeds = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/beds', { params: filters });
    if (response.data.code === 1) {
      bedList.value = response.data.data.records;
    }
  } catch (error) {
    ElMessage.error('加载床位信息失败');
  } finally {
    loading.value = false;
  }
};

const fetchUnassignedElders = async () => {
  try {
    const response = await apiClient.get('/beds/unassigned-elders');
    if (response.data.code === 1) {
      unassignedElders.value = response.data.data;
    }
  } catch (error) {
    ElMessage.error('加载未入住老人列表失败');
  }
};

// --- Dialog & Form Handlers ---
const openBedDialog = async (bed) => {
  currentBed.value = { ...bed }; // Use a copy
  isAdding.value = false;
  // 如果是空闲床位，提前加载老人列表
  if (bed.status === '空闲') {
    await fetchUnassignedElders();
  }
  dialogVisible.value = true;
};

const handleAddBed = () => {
  isAdding.value = true;
  currentBed.value = null;
  form.value = { bedNumber: '', status: '空闲' };
  dialogVisible.value = true;
};

const handleSaveNewBed = async () => {
  if (!form.value.bedNumber) {
    ElMessage.warning('请输入床位号');
    return;
  }
  try {
    await apiClient.post('/beds', form.value);
    ElMessage.success('新增成功');
    dialogVisible.value = false;
    fetchBeds();
  } catch (error) {
    ElMessage.error('新增失败');
  }
};

// --- Check-in / Check-out Handlers ---
const handleCheckin = async () => {
  if (!checkinForm.elderId) {
    ElMessage.warning('请选择要入住的老人');
    return;
  }
  const updatedBed = {
    ...currentBed.value,
    elderId: checkinForm.elderId,
    status: '已入住',
  };
  try {
    await apiClient.put('/beds', updatedBed);
    ElMessage.success('办理入住成功');
    dialogVisible.value = false;
    fetchBeds();
  } catch (error) {
    ElMessage.error('操作失败');
  }
};

const handleCheckout = async () => {
  const updatedBed = {
    ...currentBed.value,
    elderId: null, // 清空老人ID
    status: '空闲',
  };
  try {
    await apiClient.put('/beds', updatedBed);
    ElMessage.success('办理退住成功');
    dialogVisible.value = false;
    fetchBeds();
  } catch (error) {
    ElMessage.error('操作失败');
  }
};

const handleDeleteBed = (id) => {
  ElMessageBox.confirm('此操作将永久删除该床位，是否继续?', '警告', { type: 'warning' })
    .then(async () => {
      try {
        await apiClient.delete(`/beds/${id}`);
        ElMessage.success('删除成功');
        dialogVisible.value = false;
        fetchBeds();
      } catch (error) {
        ElMessage.error('删除失败');
      }
    });
};

// --- Lifecycle ---
onMounted(fetchBeds);
</script>

<style scoped>
.main-content { padding: 20px; }
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.operation-container { margin-bottom: 20px; }
.loading-text { text-align: center; color: #999; font-size: 16px; padding: 40px; }

.bed-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(160px, 1fr)); gap: 20px; }
.bed { padding: 20px; border-radius: 8px; text-align: center; font-weight: bold; color: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.1); cursor: pointer; transition: all 0.2s ease-in-out; }
.bed:hover { transform: translateY(-5px); box-shadow: 0 4px 12px rgba(0,0,0,0.15); }
.bed.occupied { background: linear-gradient(135deg, #f87171, #ef4444); } /* 已入住 - 红色系 */
.bed.available { background: linear-gradient(135deg, #4ade80, #22c55e); } /* 空闲 - 绿色系 */
.bed-number { font-size: 22px; margin-bottom: 8px; }
.bed-occupant { font-size: 14px; font-weight: normal; }
</style>