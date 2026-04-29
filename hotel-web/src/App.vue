<template>
  <el-container class="layout-container" style="height: 100vh; background-color: #f5f7fa;">
    <el-aside width="220px" style="background-color: #304156;">
      <el-menu
          active-text-color="#409EFF"
          background-color="#304156"
          class="el-menu-vertical"
          :default-active="activeMenu"
          text-color="#bfcbd9"
          @select="handleMenuSelect"
      >
        <div style="padding: 20px; color: white; text-align: center; font-weight: bold; font-size: 18px;">
          🏨 酒店后台系统
        </div>
        <el-menu-item index="dashboard">📊 数据概览</el-menu-item>
        <el-menu-item index="rooms">🛏️ 房间管理</el-menu-item>
        <el-menu-item index="bookings">📋 订单管理</el-menu-item>
        <el-menu-item index="ai-logs">🤖 AI 过滤日志</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container class="right-box">
      <el-header style="background-color: white; border-bottom: 1px solid #dcdfe6; display: flex; align-items: center; justify-content: space-between;">
        <span style="font-weight: bold;">{{ pageTitle }}</span>
        <div style="display: flex; align-items: center;">
          <el-avatar size="small" src="https://github.com/Anlums.png" style="margin-right: 10px;" />
          <span>Anlums</span>
        </div>
      </el-header>

      <DataDashboard v-if="activeMenu === 'dashboard'" />
      <BookingManager v-else-if="activeMenu === 'bookings'" />
      <div v-else style="padding: 40px; text-align: center; color: #909399;">
        <el-empty description="功能开发中..." />
      </div>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed } from 'vue'
import BookingManager from './views/BookingManager.vue'
import DataDashboard from './views/DataDashboard.vue'

const activeMenu = ref('dashboard')

const pageTitle = computed(() => {
  const titles = {
    'dashboard': '数据概览',
    'rooms': '房间管理',
    'bookings': '订单管理控制台',
    'ai-logs': 'AI 过滤日志'
  }
  return titles[activeMenu.value] || '酒店后台系统'
})

const handleMenuSelect = (index) => {
  activeMenu.value = index
}
</script>

<style scoped>
.layout-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
}

.el-aside {
  background-color: #304156;
  flex-shrink: 0;
}

.right-box {
  display: flex;
  flex-direction: column;
  flex-grow: 1;
  overflow: hidden;
}

.el-header {
  height: 60px;
  background-color: white;
  border-bottom: 1px solid #dcdfe6;
  flex-shrink: 0;
}
</style>


<!--<template>-->
<!--  <el-container class="layout-container" style="height: 100vh; background-color: #f5f7fa;">-->
<!--    <el-aside width="220px" style="background-color: #304156;">-->
<!--      <el-menu-->
<!--          active-text-color="#409EFF"-->
<!--          background-color="#304156"-->
<!--          class="el-menu-vertical"-->
<!--          default-active="1"-->
<!--          text-color="#bfcbd9"-->
<!--      >-->
<!--        <div style="padding: 20px; color: white; text-align: center; font-weight: bold; font-size: 18px;">-->
<!--          🏨 酒店后台系统-->
<!--        </div>-->
<!--        <el-menu-item index="1">📊 数据概览</el-menu-item>-->
<!--        <el-menu-item index="2">🛏️ 房间管理</el-menu-item>-->
<!--        <el-menu-item index="3">📋 订单管理</el-menu-item>-->
<!--        <el-menu-item index="4">🤖 AI 过滤日志</el-menu-item>-->
<!--      </el-menu>-->
<!--    </el-aside>-->

<!--    <el-container class="right-box">-->
<!--      <el-header style="background-color: white; border-bottom: 1px solid #dcdfe6; display: flex; align-items: center; justify-content: space-between;">-->
<!--        <span style="font-weight: bold;">订单管理控制台</span>-->
<!--        <div style="display: flex; align-items: center;">-->
<!--          <el-avatar size="small" src="https://github.com/Anlums.png" style="margin-right: 10px;" />-->
<!--          <span>Anlums</span>-->
<!--        </div>-->
<!--      </el-header>-->

<!--      <BookingManager />-->
<!--    </el-container>-->
<!--  </el-container>-->
<!--</template>-->

<!--<script setup>-->
<!--import BookingManager from './views/BookingManager.vue'-->
<!--</script>-->

<!--<style scoped>-->
<!--.layout-container {-->
<!--  display: flex;-->
<!--  width: 100vw;-->
<!--  height: 100vh;-->
<!--  overflow: hidden;-->
<!--}-->

<!--.el-aside {-->
<!--  background-color: #304156;-->
<!--  flex-shrink: 0;-->
<!--}-->

<!--.right-box {-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  flex-grow: 1;-->
<!--  overflow: hidden;-->
<!--}-->

<!--.el-header {-->
<!--  height: 60px;-->
<!--  background-color: white;-->
<!--  border-bottom: 1px solid #dcdfe6;-->
<!--  flex-shrink: 0;-->
<!--}-->
<!--</style>-->




<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <el-container class="layout-container" style="height: 100vh; background-color: #f5f7fa;">&ndash;&gt;-->
<!--&lt;!&ndash;    <el-aside width="220px" style="background-color: #304156;">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-menu&ndash;&gt;-->
<!--&lt;!&ndash;          active-text-color="#409EFF"&ndash;&gt;-->
<!--&lt;!&ndash;          background-color="#304156"&ndash;&gt;-->
<!--&lt;!&ndash;          class="el-menu-vertical"&ndash;&gt;-->
<!--&lt;!&ndash;          default-active="1"&ndash;&gt;-->
<!--&lt;!&ndash;          text-color="#bfcbd9"&ndash;&gt;-->
<!--&lt;!&ndash;      >&ndash;&gt;-->
<!--&lt;!&ndash;        <div style="padding: 20px; color: white; text-align: center; font-weight: bold; font-size: 18px;">&ndash;&gt;-->
<!--&lt;!&ndash;          🏨 酒店后台系统&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-menu-item index="1">📊 数据概览</el-menu-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-menu-item index="2">🛏️ 房间管理</el-menu-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-menu-item index="3">📋 订单管理</el-menu-item>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-menu-item index="4">🤖 AI 过滤日志</el-menu-item>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-menu>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-aside>&ndash;&gt;-->

<!--&lt;!&ndash;    <el-container class="right-box">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-header style="background-color: white; border-bottom: 1px solid #dcdfe6; display: flex; align-items: center; justify-content: space-between;">&ndash;&gt;-->
<!--&lt;!&ndash;        <span style="font-weight: bold;">订单管理控制台</span>&ndash;&gt;-->
<!--&lt;!&ndash;        <div style="display: flex; align-items: center;">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-avatar size="small" src="https://github.com/Anlums.png" style="margin-right: 10px;" />&ndash;&gt;-->
<!--&lt;!&ndash;          <span>Anlums</span>&ndash;&gt;-->
<!--&lt;!&ndash;        </div>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-header>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-main>&ndash;&gt;-->
<!--&lt;!&ndash;        <el-card style="margin-bottom: 15px;">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-form :inline="true" :model="searchForm">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item label="入住人">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-input v-model="searchForm.guestName" placeholder="请输入姓名" clearable @keyup.enter="handleSearch" />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item label="房间号">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-input-number v-model="searchForm.roomNumber" :controls="false" placeholder="房间号" />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item label="状态">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 120px;">&ndash;&gt;-->
<!--&lt;!&ndash;                <el-option label="已预约" :value="1" />&ndash;&gt;-->
<!--&lt;!&ndash;                <el-option label="已入住" :value="2" />&ndash;&gt;-->
<!--&lt;!&ndash;                <el-option label="已退房" :value="3" />&ndash;&gt;-->
<!--&lt;!&ndash;                <el-option label="已取消" :value="4" />&ndash;&gt;-->
<!--&lt;!&ndash;              </el-select>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-button type="primary" @click="handleSearch">🔍 查询</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-button @click="resetSearch">重置</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-form>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-card>&ndash;&gt;-->

<!--&lt;!&ndash;        <el-card>&ndash;&gt;-->
<!--&lt;!&ndash;          <div style="margin-bottom: 15px; display: flex; justify-content: space-between;">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button type="success" @click="handleAdd">+ 办理新入住</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button type="info" plain @click="fetchData">刷新数据</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          </div>&ndash;&gt;-->

<!--&lt;!&ndash;          <el-table&ndash;&gt;-->
<!--&lt;!&ndash;              v-loading="loading"&ndash;&gt;-->
<!--&lt;!&ndash;              :data="tableData"&ndash;&gt;-->
<!--&lt;!&ndash;              border&ndash;&gt;-->
<!--&lt;!&ndash;              stripe&ndash;&gt;-->
<!--&lt;!&ndash;              style="width: 100%"&ndash;&gt;-->
<!--&lt;!&ndash;          >&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column prop="id" label="ID" width="70" align="center" />&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column prop="guestName" label="入住人" width="120" />&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column prop="roomNumber" label="房间号" width="100" align="center" />&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column prop="phone" label="联系电话" width="150" />&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column label="入住日期" width="180">&ndash;&gt;-->
<!--&lt;!&ndash;              <template #default="scope">&ndash;&gt;-->
<!--&lt;!&ndash;                {{ scope.row.checkInDate?.replace('T', ' ') }}&ndash;&gt;-->
<!--&lt;!&ndash;              </template>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column label="总金额" width="120">&ndash;&gt;-->
<!--&lt;!&ndash;              <template #default="scope">&ndash;&gt;-->
<!--&lt;!&ndash;                <span v-if="scope.row.totalAmount > 0" style="color: #f56c6c; font-weight: bold;">&ndash;&gt;-->
<!--&lt;!&ndash;                  ￥{{ scope.row.totalAmount }}&ndash;&gt;-->
<!--&lt;!&ndash;                </span>&ndash;&gt;-->
<!--&lt;!&ndash;                <el-tag v-else type="info">待结算</el-tag>&ndash;&gt;-->
<!--&lt;!&ndash;              </template>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column label="状态" align="center" width="100">&ndash;&gt;-->
<!--&lt;!&ndash;              <template #default="scope">&ndash;&gt;-->
<!--&lt;!&ndash;                <el-tag :type="getStatusType(scope.row.status)" effect="dark">&ndash;&gt;-->
<!--&lt;!&ndash;                  {{ getStatusLabel(scope.row.status) }}&ndash;&gt;-->
<!--&lt;!&ndash;                </el-tag>&ndash;&gt;-->
<!--&lt;!&ndash;              </template>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-table-column label="操作" min-width="200" fixed="right" align="center">&ndash;&gt;-->
<!--&lt;!&ndash;              <template #default="scope">&ndash;&gt;-->
<!--&lt;!&ndash;                <template v-if="scope.row.status === 1">&ndash;&gt;-->
<!--&lt;!&ndash;                  <el-button type="success" size="small" @click="handleCheckIn(scope.row.id)">办理入住</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                  <el-button type="danger" size="small" plain @click="handleCancel(scope.row.id)">取消预订</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                </template>&ndash;&gt;-->

<!--&lt;!&ndash;                <template v-else-if="scope.row.status === 2">&ndash;&gt;-->
<!--&lt;!&ndash;                  <el-button type="primary" size="small" @click="handleCheckout(scope.row.id)">退房结算</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;                </template>&ndash;&gt;-->

<!--&lt;!&ndash;                <span v-else-if="scope.row.status === 3 || scope.row.status === 4"&ndash;&gt;-->
<!--&lt;!&ndash;                      style="color: #909399; font-size: 12px;">- 流程结束 - &ndash;&gt;-->
<!--&lt;!&ndash;                </span>&ndash;&gt;-->

<!--&lt;!&ndash;                <el-button&ndash;&gt;-->
<!--&lt;!&ndash;                    v-if="scope.row.status === 1 || scope.row.status === 2" type="info"&ndash;&gt;-->
<!--&lt;!&ndash;                    size="small" plain  @click="handleEdit(scope.row)">修改&ndash;&gt;-->
<!--&lt;!&ndash;                </el-button>&ndash;&gt;-->

<!--&lt;!&ndash;              </template>&ndash;&gt;-->
<!--&lt;!&ndash;            </el-table-column>&ndash;&gt;-->
<!--&lt;!&ndash;            <template #empty>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-empty description="暂无符合条件的订单记录" />&ndash;&gt;-->
<!--&lt;!&ndash;            </template>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-table>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-card>&ndash;&gt;-->

<!--&lt;!&ndash;        <el-dialog v-model="dialogVisible" :title="isEdit ? '修改订单信息' : '新客入住登记'" width="450px">&ndash;&gt;-->
<!--&lt;!&ndash;          <el-form :model="form" label-width="100px">&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item label="姓名" required>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-input v-model="form.guestName" placeholder="入住人姓名" />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item label="房间号" required>&ndash;&gt;-->
<!--&lt;!&ndash;              <el-input-number v-model="form.roomNumber" :min="1" />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item label="电话">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-input v-model="form.phone" placeholder="联系方式" />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-form-item label="入住时间">&ndash;&gt;-->
<!--&lt;!&ndash;              <el-date-picker&ndash;&gt;-->
<!--&lt;!&ndash;                  v-model="form.checkInDate"&ndash;&gt;-->
<!--&lt;!&ndash;                  type="datetime"&ndash;&gt;-->
<!--&lt;!&ndash;                  value-format="YYYY-MM-DD HH:mm:ss"&ndash;&gt;-->
<!--&lt;!&ndash;                  placeholder="选择具体时间"&ndash;&gt;-->
<!--&lt;!&ndash;              />&ndash;&gt;-->
<!--&lt;!&ndash;            </el-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;          </el-form>&ndash;&gt;-->
<!--&lt;!&ndash;          <template #footer>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button @click="dialogVisible = false">取消</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;            <el-button type="primary" @click="submitForm">确认提交</el-button>&ndash;&gt;-->
<!--&lt;!&ndash;          </template>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-dialog>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-main>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-container>&ndash;&gt;-->

<!--&lt;!&ndash;  </el-container>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script setup>&ndash;&gt;-->
<!--&lt;!&ndash;import { ref, onMounted } from 'vue'&ndash;&gt;-->
<!--&lt;!&ndash;import axios from 'axios'&ndash;&gt;-->
<!--&lt;!&ndash;import { ElMessage, ElMessageBox } from 'element-plus'&ndash;&gt;-->

<!--&lt;!&ndash;// -&#45;&#45; 数据定义 &#45;&#45;&ndash;&gt;>-->
<!--&lt;!&ndash;const tableData = ref([])&ndash;&gt;-->
<!--&lt;!&ndash;const loading = ref(false)&ndash;&gt;-->
<!--&lt;!&ndash;const dialogVisible = ref(false)&ndash;&gt;-->
<!--&lt;!&ndash;// 1. 增加一个状态，用来记录是新增还是修改&ndash;&gt;-->
<!--&lt;!&ndash;const isEdit = ref(false)&ndash;&gt;-->

<!--&lt;!&ndash;// 状态样式映射：1-黄色, 2-绿色, 3-红色, 4-灰色&ndash;&gt;-->
<!--&lt;!&ndash;const getStatusType = (status) => {&ndash;&gt;-->
<!--&lt;!&ndash;  const map = {&ndash;&gt;-->
<!--&lt;!&ndash;    1: 'warning', // 已预约 -> 黄色 (Warning)&ndash;&gt;-->
<!--&lt;!&ndash;    2: 'success', // 已入住 -> 绿色 (Success - 对应你说的“率色”)&ndash;&gt;-->
<!--&lt;!&ndash;    3: 'danger',  // 已退房 -> 红色 (Danger - 对应你说的流程完成)&ndash;&gt;-->
<!--&lt;!&ndash;    4: 'info'     // 已取消 -> 灰色 (Info)&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;  return map[status] || 'info'&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;// 状态文字映射&ndash;&gt;-->
<!--&lt;!&ndash;const getStatusLabel = (status) => {&ndash;&gt;-->
<!--&lt;!&ndash;  const map = {&ndash;&gt;-->
<!--&lt;!&ndash;    1: '已预约',&ndash;&gt;-->
<!--&lt;!&ndash;    2: '已入住',&ndash;&gt;-->
<!--&lt;!&ndash;    3: '已退房',&ndash;&gt;-->
<!--&lt;!&ndash;    4: '已取消'&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;  return map[status] || '未知'&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;const searchForm = ref({&ndash;&gt;-->
<!--&lt;!&ndash;  guestName: '',&ndash;&gt;-->
<!--&lt;!&ndash;  roomNumber: null,&ndash;&gt;-->
<!--&lt;!&ndash;  status: null&ndash;&gt;-->
<!--&lt;!&ndash;})&ndash;&gt;-->

<!--&lt;!&ndash;const form = ref({&ndash;&gt;-->
<!--&lt;!&ndash;  guestName: '',&ndash;&gt;-->
<!--&lt;!&ndash;  roomNumber: null,&ndash;&gt;-->
<!--&lt;!&ndash;  phone: '',&ndash;&gt;-->
<!--&lt;!&ndash;  checkInDate: '',&ndash;&gt;-->
<!--&lt;!&ndash;  status: 1,&ndash;&gt;-->
<!--&lt;!&ndash;  totalAmount: 0&ndash;&gt;-->
<!--&lt;!&ndash;})&ndash;&gt;-->

<!--&lt;!&ndash;// -&#45;&#45; 业务方法 &#45;&#45;&ndash;&gt;>-->

<!--&lt;!&ndash;// 1. 获取/搜索列表&ndash;&gt;-->
<!--&lt;!&ndash;const handleSearch = async () => {&ndash;&gt;-->
<!--&lt;!&ndash;  loading.value = true;&ndash;&gt;-->
<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    const res = await axios.get('http://localhost:8080/api/bookings/search', {&ndash;&gt;-->
<!--&lt;!&ndash;      // 关键：params 会被 Axios 自动转换成 ?guestName=xxx&status=xxx&ndash;&gt;-->
<!--&lt;!&ndash;      params: searchForm.value&ndash;&gt;-->
<!--&lt;!&ndash;    });&ndash;&gt;-->
<!--&lt;!&ndash;    tableData.value = res.data.data;&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (e) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('搜索失败');&ndash;&gt;-->
<!--&lt;!&ndash;  } finally {&ndash;&gt;-->
<!--&lt;!&ndash;    loading.value = false;&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;};&ndash;&gt;-->
<!--&lt;!&ndash;// 2. 重置查询&ndash;&gt;-->
<!--&lt;!&ndash;const resetSearch = () => {&ndash;&gt;-->
<!--&lt;!&ndash;  searchForm.value = { guestName: '', roomNumber: null, status: null }&ndash;&gt;-->
<!--&lt;!&ndash;  handleSearch()&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;// 3. 提交新入住&ndash;&gt;-->
<!--&lt;!&ndash;// 修改“办理新入住”按钮绑定的方法&ndash;&gt;-->
<!--&lt;!&ndash;const handleAdd = () => {&ndash;&gt;-->
<!--&lt;!&ndash;  isEdit.value = false // 设为新增模式&ndash;&gt;-->
<!--&lt;!&ndash;  // 清空表单数据，防止看到之前修改留下的残余&ndash;&gt;-->
<!--&lt;!&ndash;  form.value = { guestName: '', roomNumber: null, phone: '', checkInDate: '', status: 1, totalAmount: 0 }&ndash;&gt;-->
<!--&lt;!&ndash;  dialogVisible.value = true&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;// 4. 核心：重构原有的 submitForm&ndash;&gt;-->
<!--&lt;!&ndash;const submitForm = async () => {&ndash;&gt;-->
<!--&lt;!&ndash;  if (!form.value.guestName || !form.value.roomNumber) {&ndash;&gt;-->
<!--&lt;!&ndash;    return ElMessage.warning('请填写必要信息')&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;  loading.value = true&ndash;&gt;-->
<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    let res;&ndash;&gt;-->
<!--&lt;!&ndash;    if (isEdit.value) {&ndash;&gt;-->
<!--&lt;!&ndash;      // 如果是修改模式，调用 PUT 接口&ndash;&gt;-->
<!--&lt;!&ndash;      res = await axios.put('http://localhost:8080/api/bookings/update', form.value)&ndash;&gt;-->
<!--&lt;!&ndash;    } else {&ndash;&gt;-->
<!--&lt;!&ndash;      // 如果是新增模式，调用 POST 接口&ndash;&gt;-->
<!--&lt;!&ndash;      res = await axios.post('http://localhost:8080/api/bookings/save', form.value)&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->

<!--&lt;!&ndash;    if (res.data.code === 200) {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.success(isEdit.value ? '订单修改成功' : '入住登记成功')&ndash;&gt;-->
<!--&lt;!&ndash;      dialogVisible.value = false&ndash;&gt;-->
<!--&lt;!&ndash;      handleSearch() // 刷新列表&ndash;&gt;-->
<!--&lt;!&ndash;    } else {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.error(res.data.msg || '操作失败')&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (e) {&ndash;&gt;-->
<!--&lt;!&ndash;    // 这里会捕获到你在 Service 实现类里抛出的“房间被占用”异常&ndash;&gt;-->
<!--&lt;!&ndash;    const errorMsg = e.response?.data?.msg || '服务器连接异常'&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error(errorMsg)&ndash;&gt;-->
<!--&lt;!&ndash;  } finally {&ndash;&gt;-->
<!--&lt;!&ndash;    loading.value = false&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;// const submitForm = async () => {&ndash;&gt;-->
<!--&lt;!&ndash;//   if (!form.value.guestName || !form.value.roomNumber) {&ndash;&gt;-->
<!--&lt;!&ndash;//     return ElMessage.warning('请填写必要信息')&ndash;&gt;-->
<!--&lt;!&ndash;//   }&ndash;&gt;-->
<!--&lt;!&ndash;//   try {&ndash;&gt;-->
<!--&lt;!&ndash;//     const res = await axios.post('http://localhost:8080/api/bookings/save', form.value)&ndash;&gt;-->
<!--&lt;!&ndash;//     if (res.data.code === 200) {&ndash;&gt;-->
<!--&lt;!&ndash;//       ElMessage.success('入住登记成功')&ndash;&gt;-->
<!--&lt;!&ndash;//       dialogVisible.value = false&ndash;&gt;-->
<!--&lt;!&ndash;//       handleSearch() // 刷新列表&ndash;&gt;-->
<!--&lt;!&ndash;//       form.value = { guestName: '', roomNumber: null, phone: '', status: 1, totalAmount: 0 }&ndash;&gt;-->
<!--&lt;!&ndash;//     }&ndash;&gt;-->
<!--&lt;!&ndash;//   } catch (e) {&ndash;&gt;-->
<!--&lt;!&ndash;//     ElMessage.error('提交失败，请检查网络')&ndash;&gt;-->
<!--&lt;!&ndash;//   }&ndash;&gt;-->
<!--&lt;!&ndash;// }&ndash;&gt;-->
<!--&lt;!&ndash;// 4. 退房结算&ndash;&gt;-->
<!--&lt;!&ndash;const handleCheckout = (id) => {&ndash;&gt;-->
<!--&lt;!&ndash;  ElMessageBox.confirm('确认要为该订单办理退房结算吗？', '提示').then(async () => {&ndash;&gt;-->
<!--&lt;!&ndash;    const res = await axios.put(`http://localhost:8080/api/bookings/checkOut?id=${id}`)&ndash;&gt;-->
<!--&lt;!&ndash;    if (res.data.code === 200) {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.success('结算成功')&ndash;&gt;-->
<!--&lt;!&ndash;      handleSearch()&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  }).catch(() => {})&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;// 5. 办理入住 (状态从 1 变为 2)&ndash;&gt;-->
<!--&lt;!&ndash;const handleCheckIn = (id) => {&ndash;&gt;-->
<!--&lt;!&ndash;  ElMessageBox.confirm('确认要为该客人办理入住吗？', '办理入住', {&ndash;&gt;-->
<!--&lt;!&ndash;    confirmButtonText: '确认入住',&ndash;&gt;-->
<!--&lt;!&ndash;    cancelButtonText: '取消',&ndash;&gt;-->
<!--&lt;!&ndash;    type: 'success'&ndash;&gt;-->
<!--&lt;!&ndash;  }).then(async () => {&ndash;&gt;-->
<!--&lt;!&ndash;    try {&ndash;&gt;-->
<!--&lt;!&ndash;      // 这里的 URL 对应你后端写的 /api/bookings/checkIn&ndash;&gt;-->
<!--&lt;!&ndash;      const res = await axios.put(`http://localhost:8080/api/bookings/checkIn?id=${id}`)&ndash;&gt;-->
<!--&lt;!&ndash;      if (res.data.code === 200) {&ndash;&gt;-->
<!--&lt;!&ndash;        ElMessage.success('入住手续办理成功！')&ndash;&gt;-->
<!--&lt;!&ndash;        handleSearch() // 刷新列表，此时状态变为 2 (绿色)&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    } catch (e) {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.error('操作失败，请检查后端接口')&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  })&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;// 6. 取消预订 (状态从 1 变为 4)&ndash;&gt;-->
<!--&lt;!&ndash;const handleCancel = (id) => {&ndash;&gt;-->
<!--&lt;!&ndash;  ElMessageBox.confirm('确认要取消该预订订单吗？此操作不可逆。', '取消预订', {&ndash;&gt;-->
<!--&lt;!&ndash;    confirmButtonText: '确认取消',&ndash;&gt;-->
<!--&lt;!&ndash;    cancelButtonText: '点错了',&ndash;&gt;-->
<!--&lt;!&ndash;    type: 'error'&ndash;&gt;-->
<!--&lt;!&ndash;  }).then(async () => {&ndash;&gt;-->
<!--&lt;!&ndash;    try {&ndash;&gt;-->
<!--&lt;!&ndash;      // 这里的 URL 对应你后端写的 /api/bookings/cancel&ndash;&gt;-->
<!--&lt;!&ndash;      const res = await axios.put(`http://localhost:8080/api/bookings/cancel?id=${id}`)&ndash;&gt;-->
<!--&lt;!&ndash;      if (res.data.code === 200) {&ndash;&gt;-->
<!--&lt;!&ndash;        ElMessage.warning('订单已取消')&ndash;&gt;-->
<!--&lt;!&ndash;        handleSearch() // 刷新列表，此时状态变为 4 (灰色)&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    } catch (e) {&ndash;&gt;-->
<!--&lt;!&ndash;      ElMessage.error('取消失败')&ndash;&gt;-->
<!--&lt;!&ndash;    }&ndash;&gt;-->
<!--&lt;!&ndash;  })&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;//  修改“修改”按钮绑定的方法&ndash;&gt;-->
<!--&lt;!&ndash;const handleEdit = (row) => {&ndash;&gt;-->
<!--&lt;!&ndash;  isEdit.value = true // 设为修改模式&ndash;&gt;-->
<!--&lt;!&ndash;  form.value = { ...row } // 回显当前行的数据&ndash;&gt;-->
<!--&lt;!&ndash;  dialogVisible.value = true&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;// 初始加载&ndash;&gt;-->
<!--&lt;!&ndash;onMounted(() => {&ndash;&gt;-->
<!--&lt;!&ndash;  handleSearch()&ndash;&gt;-->
<!--&lt;!&ndash;})&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;/* 侧边栏和主容器的布局 */&ndash;&gt;-->
<!--&lt;!&ndash;.layout-container {&ndash;&gt;-->
<!--&lt;!&ndash;  display: flex;&ndash;&gt;-->
<!--&lt;!&ndash;  width: 100vw;&ndash;&gt;-->
<!--&lt;!&ndash;  height: 100vh;&ndash;&gt;-->
<!--&lt;!&ndash;  overflow: hidden; /* 再次确认不出现横向溢出 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.el-aside {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #304156;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-shrink: 0; /* 禁止侧边栏被挤压 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;/* 右侧内容垂直排布：Header 在上，Main 在下 */&ndash;&gt;-->
<!--&lt;!&ndash;.right-box {&ndash;&gt;-->
<!--&lt;!&ndash;  display: flex;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-direction: column;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-grow: 1; /* 占据剩余的所有宽度 */&ndash;&gt;-->
<!--&lt;!&ndash;  overflow: hidden;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.el-header {&ndash;&gt;-->
<!--&lt;!&ndash;  height: 60px;&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: white;&ndash;&gt;-->
<!--&lt;!&ndash;  border-bottom: 1px solid #dcdfe6;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-shrink: 0; /* 保证高度固定为 60px */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.el-main {&ndash;&gt;-->
<!--&lt;!&ndash;  background-color: #f5f7fa;&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;  flex-grow: 1; /* 占据剩余的所有高度 */&ndash;&gt;-->
<!--&lt;!&ndash;  overflow-y: auto; /* 只有这里允许垂直滚动 */&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->