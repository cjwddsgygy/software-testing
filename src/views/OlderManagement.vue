<!-- START OF FILE OlderManagement.vue -->
<template>
  <div class="older-management-view">
    <h2 class="view-title">老人信息管理</h2>

    <!-- Toolbar -->
    <div class="toolbar">
      <input type="text" placeholder="按姓名搜索..." class="search-input" />
      <button class="btn btn-primary">搜索</button>
      <button class="btn btn-success">新增老人</button>
    </div>

    <!-- Data Table -->
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>姓名</th>
            <th>年龄</th>
            <th>护理级别</th>
            <th>床位号</th>
            <th>入院时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <!-- Sample Data Row -->
          <tr>
            <td>张三</td>
            <td>82</td>
            <td>一级护理</td>
            <td>A-101</td>
            <td>2023-05-10</td>
            <td>
              <button class="btn-action view" @click="showDetails = true">查看</button>
              <button class="btn-action edit">编辑</button>
              <button class="btn-action delete">删除</button>
            </td>
          </tr>
          <!-- Add more rows as needed -->
          <tr>
            <td>李四</td>
            <td>78</td>
            <td>二级护理</td>
            <td>B-203</td>
            <td>2024-01-15</td>
            <td>
              <button class="btn-action view" @click="showDetails = true">查看</button>
              <button class="btn-action edit">编辑</button>
              <button class="btn-action delete">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Details Modal (Pop-up) -->
    <div v-if="showDetails" class="modal-overlay" @click="showDetails = false">
      <div class="modal-content" @click.stop>
        <h3>老人详细信息</h3>
        <div class="details-grid">
          <p><strong>姓名:</strong> {{ selectedOlder.name }}</p>
          <p><strong>年龄:</strong> {{ selectedOlder.age }}</p>
          <p><strong>出生日期:</strong> {{ selectedOlder.birthDate }}</p>
          <p><strong>民族:</strong> {{ selectedOlder.nation }}</p>
          <p><strong>账号:</strong> {{ selectedOlder.account }}</p>
          <p><strong>密码:</strong> ●●●●●●●●</p>
          <p><strong>学历:</strong> {{ selectedOlder.education }}</p>
          <p><strong>婚姻状况:</strong> {{ selectedOlder.maritalStatus }}</p>
          <p><strong>爱好:</strong> {{ selectedOlder.hobbies }}</p>
          <p><strong>护理级别:</strong> {{ selectedOlder.careLevel }}</p>
          <p><strong>医疗管理:</strong> {{ selectedOlder.medicalManagement }}</p>
          <p><strong>收费类型:</strong> {{ selectedOlder.chargeType }}</p>
          <p><strong>费用支出:</strong> {{ selectedOlder.expenses }}</p>
          <p><strong>亲属联系方式:</strong> {{ selectedOlder.relativeContact }}</p>
          <p><strong>床位统计:</strong> {{ selectedOlder.bedCount }}</p>
          <p><strong>入院时间:</strong> {{ selectedOlder.admissionDate }}</p>
          <p><strong>出院时间:</strong> {{ selectedOlder.dischargeDate }}</p>
        </div>
        <button class="btn btn-secondary close-btn" @click="showDetails = false">关闭</button>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';

const showDetails = ref(false);

const selectedOlder = reactive({
  name: '张三',
  age: 82,
  birthDate: '1942-08-15',
  nation: '汉族',
  account: 'zhangsan',
  password: 'hashed_password',
  education: '高中',
  maritalStatus: '丧偶',
  hobbies: '听戏, 下棋',
  careLevel: '一级护理',
  medicalManagement: '高血压, 糖尿病',
  chargeType: '月度缴费',
  expenses: '3500元/月',
  relativeContact: '张小明 - 13812345678',
  bedCount: 'A-101',
  admissionDate: '2023-05-10',
  dischargeDate: 'N/A',
});
</script>

<style scoped>
.view-title { font-size: 24px; margin-bottom: 20px; color: #333; }
.toolbar { display: flex; gap: 10px; margin-bottom: 20px; }
.search-input { padding: 8px 12px; border: 1px solid #ccc; border-radius: 4px; flex-grow: 1; max-width: 300px; }
.btn { padding: 8px 15px; border: none; border-radius: 4px; color: #fff; cursor: pointer; transition: opacity 0.3s; }
.btn:hover { opacity: 0.8; }
.btn-primary { background-color: #409eff; }
.btn-success { background-color: #67c23a; }
.btn-secondary { background-color: #909399; }

.table-container { background: #fff; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); overflow: hidden; }
table { width: 100%; border-collapse: collapse; }
th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eef1f6; }
th { background-color: #fafafa; font-weight: bold; color: #666; }
.btn-action { background: none; border: none; cursor: pointer; padding: 5px 8px; margin-right: 5px; }
.btn-action.view { color: #409eff; }
.btn-action.edit { color: #e6a23c; }
.btn-action.delete { color: #f56c6c; }

/* Modal Styles */
.modal-overlay { position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0, 0, 0, 0.5); display: flex; justify-content: center; align-items: center; z-index: 1000; }
.modal-content { background: #fff; padding: 30px; border-radius: 8px; width: 800px; max-width: 90%; box-shadow: 0 5px 15px rgba(0,0,0,0.3); }
.modal-content h3 { margin-top: 0; margin-bottom: 20px; text-align: center; }
.details-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; margin-bottom: 25px; }
.details-grid p { margin: 0; }
.close-btn { display: block; margin: 0 auto; }
</style>
<!-- END OF FILE OlderManagement.vue -->