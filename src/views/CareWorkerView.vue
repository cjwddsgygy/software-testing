<!-- frontend/src/views/CareWorkerView.vue -->
<template>
  <div class="main-content">
    <!-- 操作区 -->
    <div class="operation-container">
      <el-input v-model="searchKeyword" placeholder="请输入护工姓名搜索" clearable @clear="fetchData" @keyup.enter="fetchData" style="width: 300px; margin-right: 10px;" />
      <el-button type="primary" :icon="Search" @click="fetchData">搜索</el-button>
      <el-button type="success" :icon="Plus" @click="handleAdd">新增护工</el-button>
    </div>

    <!-- 表格区 -->
    <el-table :data="tableData" v-loading="loading" border style="width: 100%">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="id" label="ID" width="80" sortable />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="gender" label="性别" />
      <el-table-column prop="age" label="年龄" />
      <el-table-column prop="phone" label="联系电话" />
      <el-table-column prop="hireDate" label="入职日期" />
      <el-table-column prop="level" label="护理级别" />
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="scope">
          <el-button type="primary" size="small" :icon="Edit" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="danger" size="small" :icon="Delete" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页区 -->
    <el-pagination background layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.pageSize" :current-page="pagination.pageNum" @size-change="handleSizeChange" @current-change="handleCurrentChange" style="margin-top: 20px; text-align: right;" />

    <!-- 新增/编辑 对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="50%">
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-form-item label="姓名" prop="name" required><el-input v-model="form.name" /></el-form-item>
        <el-form-item label="性别" prop="gender" required>
          <el-radio-group v-model="form.gender"><el-radio label="男" /><el-radio label="女" /></el-radio-group>
        </el-form-item>
        <el-form-item label="年龄" prop="age" required><el-input-number v-model="form.age" :min="18" /></el-form-item>
        <el-form-item label="联系电话" prop="phone"><el-input v-model="form.phone" /></el-form-item>
        <el-form-item label="入职日期" prop="hireDate" required>
            <el-date-picker v-model="form.hireDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
        </el-form-item>
        <el-form-item label="护理级别" prop="level">
          <el-select v-model="form.level" placeholder="请选择级别">
            <el-option label="一级护理" value="一级护理"></el-option>
            <el-option label="二级护理" value="二级护理"></el-option>
            <el-option label="三级护理" value="三级护理"></el-option>
            <el-option label="特级护理" value="特级护理"></el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSave">确 定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Search, Plus, Edit, Delete } from '@element-plus/icons-vue';
import apiClient from '@/api';

// --- State ---
const tableData = ref([]);
const loading = ref(false);
const searchKeyword = ref('');
const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 });
const dialogVisible = ref(false);
const dialogTitle = ref('');
const form = ref({});
const formRef = ref(null);

// --- API Methods ---
const fetchData = async () => {
  loading.value = true;
  try {
    const response = await apiClient.get('/care-workers', { // API路径
      params: {
        pageNum: pagination.pageNum,
        pageSize: pagination.pageSize,
        search: searchKeyword.value,
      },
    });
    if (response.data.code === 1) {
      tableData.value = response.data.data.records;
      pagination.total = response.data.data.total;
    }
  } catch (error) { ElMessage.error('数据加载失败'); }
  finally { loading.value = false; }
};

// --- CRUD Handlers ---
const handleAdd = () => {
  form.value = { gender: '男', level: '一级护理' };
  dialogTitle.value = '新增护工信息';
  dialogVisible.value = true;
};
const handleEdit = (row) => {
  form.value = { ...row };
  dialogTitle.value = '编辑护工信息';
  dialogVisible.value = true;
};
const handleSave = async () => {
  try {
    if (form.value.id) { // Update
      await apiClient.put('/care-workers', form.value); // API路径
    } else { // Create
      await apiClient.post('/care-workers', form.value); // API路径
    }
    ElMessage.success('操作成功');
    dialogVisible.value = false;
    fetchData();
  } catch (error) { ElMessage.error('操作失败'); }
};
const handleDelete = (id) => {
  ElMessageBox.confirm('确定要删除这条信息吗?', '警告', { type: 'warning' })
    .then(async () => {
      try {
        await apiClient.delete(`/care-workers/${id}`); // API路径
        ElMessage.success('删除成功');
        fetchData();
      } catch (error) { ElMessage.error('删除失败'); }
    })
    .catch(() => ElMessage.info('已取消删除'));
};

// --- Pagination Handlers ---
const handleSizeChange = (newSize) => { pagination.pageSize = newSize; fetchData(); };
const handleCurrentChange = (newPage) => { pagination.pageNum = newPage; fetchData(); };

// --- Lifecycle ---
onMounted(fetchData);
</script>

<style scoped>
.main-content { padding: 20px; }
.operation-container { margin-bottom: 20px; }
</style>