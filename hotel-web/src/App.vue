<template>
  <el-container class="layout-container" style="height: 100vh; background-color: #f5f7fa;">
    <el-aside width="220px" style="background-color: #304156;">
      <el-menu
          active-text-color="#409EFF"
          background-color="#304156"
          class="el-menu-vertical"
          default-active="1"
          text-color="#bfcbd9"
      >
        <div style="padding: 20px; color: white; text-align: center; font-weight: bold; font-size: 18px;">
          🏨 酒店后台系统
        </div>
        <el-menu-item index="1">📊 数据概览</el-menu-item>
        <el-menu-item index="2">🛏️ 房间管理</el-menu-item>
        <el-menu-item index="3">📋 订单管理</el-menu-item>
        <el-menu-item index="4">🤖 AI 过滤日志</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container class="right-box">
      <el-header style="background-color: white; border-bottom: 1px solid #dcdfe6; display: flex; align-items: center; justify-content: space-between;">
        <span style="font-weight: bold;">订单管理控制台</span>
        <div style="display: flex; align-items: center;">
          <el-avatar size="small" src="https://github.com/Anlums.png" style="margin-right: 10px;" />
          <span>Anlums</span>
        </div>
      </el-header>

      <el-main>
        <el-card style="margin-bottom: 15px;">
          <el-form :inline="true" :model="searchForm">
            <el-form-item label="入住人">
              <el-input v-model="searchForm.guestName" placeholder="请输入姓名" clearable @keyup.enter="handleSearch" />
            </el-form-item>
            <el-form-item label="房间号">
              <el-input-number v-model="searchForm.roomNumber" :controls="false" placeholder="房间号" />
            </el-form-item>
            <el-form-item label="状态">
              <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 120px;">
                <el-option label="已预约" :value="1" />
                <el-option label="已入住" :value="2" />
                <el-option label="已退房" :value="3" />
                <el-option label="已取消" :value="4" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSearch">🔍 查询</el-button>
              <el-button @click="resetSearch">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card>
          <div style="margin-bottom: 15px; display: flex; justify-content: space-between;">
            <el-button type="success" @click="handleAdd">+ 办理新入住</el-button>
            <el-button type="info" plain @click="fetchData">刷新数据</el-button>
          </div>

          <el-table
              v-loading="loading"
              :data="tableData"
              border
              stripe
              style="width: 100%"
          >
            <el-table-column prop="id" label="ID" width="70" align="center" />
            <el-table-column prop="guestName" label="入住人" width="120" />
            <el-table-column prop="roomNumber" label="房间号" width="100" align="center" />
            <el-table-column prop="phone" label="联系电话" width="150" />
            <el-table-column label="入住日期" width="180">
              <template #default="scope">
                {{ scope.row.checkInDate?.replace('T', ' ') }}
              </template>
            </el-table-column>
            <el-table-column label="总金额" width="120">
              <template #default="scope">
                <span v-if="scope.row.totalAmount > 0" style="color: #f56c6c; font-weight: bold;">
                  ￥{{ scope.row.totalAmount }}
                </span>
                <el-tag v-else type="info">待结算</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="状态" align="center" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)" effect="dark">
                  {{ getStatusLabel(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" min-width="200" fixed="right" align="center">
              <template #default="scope">
                <template v-if="scope.row.status === 1">
                  <el-button type="success" size="small" @click="handleCheckIn(scope.row.id)">办理入住</el-button>
                  <el-button type="danger" size="small" plain @click="handleCancel(scope.row.id)">取消预订</el-button>
                </template>

                <template v-else-if="scope.row.status === 2">
                  <el-button type="primary" size="small" @click="handleCheckout(scope.row.id)">退房结算</el-button>
                </template>

                <span v-else-if="scope.row.status === 3 || scope.row.status === 4"
                      style="color: #909399; font-size: 12px;">- 流程结束 -
                </span>

                <el-button
                    v-if="scope.row.status === 1 || scope.row.status === 2" type="info"
                    size="small" plain  @click="handleEdit(scope.row)">修改
                </el-button>

              </template>
            </el-table-column>
            <template #empty>
              <el-empty description="暂无符合条件的订单记录" />
            </template>
          </el-table>
        </el-card>

        <el-dialog v-model="dialogVisible" :title="isEdit ? '修改订单信息' : '新客入住登记'" width="450px">
          <el-form :model="form" label-width="100px">
            <el-form-item label="姓名" required>
              <el-input v-model="form.guestName" placeholder="入住人姓名" />
            </el-form-item>
            <el-form-item label="房间号" required>
              <el-input-number v-model="form.roomNumber" :min="1" />
            </el-form-item>
            <el-form-item label="电话">
              <el-input v-model="form.phone" placeholder="联系方式" />
            </el-form-item>
            <el-form-item label="入住时间">
              <el-date-picker
                  v-model="form.checkInDate"
                  type="datetime"
                  value-format="YYYY-MM-DD HH:mm:ss"
                  placeholder="选择具体时间"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitForm">确认提交</el-button>
          </template>
        </el-dialog>
      </el-main>
    </el-container>

  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

// --- 数据定义 ---
const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
// 1. 增加一个状态，用来记录是新增还是修改
const isEdit = ref(false)

// 状态样式映射：1-黄色, 2-绿色, 3-红色, 4-灰色
const getStatusType = (status) => {
  const map = {
    1: 'warning', // 已预约 -> 黄色 (Warning)
    2: 'success', // 已入住 -> 绿色 (Success - 对应你说的“率色”)
    3: 'danger',  // 已退房 -> 红色 (Danger - 对应你说的流程完成)
    4: 'info'     // 已取消 -> 灰色 (Info)
  }
  return map[status] || 'info'
}

// 状态文字映射
const getStatusLabel = (status) => {
  const map = {
    1: '已预约',
    2: '已入住',
    3: '已退房',
    4: '已取消'
  }
  return map[status] || '未知'
}

const searchForm = ref({
  guestName: '',
  roomNumber: null,
  status: null
})

const form = ref({
  guestName: '',
  roomNumber: null,
  phone: '',
  checkInDate: '',
  status: 1,
  totalAmount: 0
})

// --- 业务方法 ---

// 1. 获取/搜索列表
const handleSearch = async () => {
  loading.value = true;
  try {
    const res = await axios.get('http://localhost:8080/api/bookings/search', {
      // 关键：params 会被 Axios 自动转换成 ?guestName=xxx&status=xxx
      params: searchForm.value
    });
    tableData.value = res.data.data;
  } catch (e) {
    ElMessage.error('搜索失败');
  } finally {
    loading.value = false;
  }
};
// 2. 重置查询
const resetSearch = () => {
  searchForm.value = { guestName: '', roomNumber: null, status: null }
  handleSearch()
}
// 3. 提交新入住
// 修改“办理新入住”按钮绑定的方法
const handleAdd = () => {
  isEdit.value = false // 设为新增模式
  // 清空表单数据，防止看到之前修改留下的残余
  form.value = { guestName: '', roomNumber: null, phone: '', checkInDate: '', status: 1, totalAmount: 0 }
  dialogVisible.value = true
}
// 4. 核心：重构原有的 submitForm
const submitForm = async () => {
  if (!form.value.guestName || !form.value.roomNumber) {
    return ElMessage.warning('请填写必要信息')
  }
  loading.value = true
  try {
    let res;
    if (isEdit.value) {
      // 如果是修改模式，调用 PUT 接口
      res = await axios.put('http://localhost:8080/api/bookings/update', form.value)
    } else {
      // 如果是新增模式，调用 POST 接口
      res = await axios.post('http://localhost:8080/api/bookings/save', form.value)
    }

    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '订单修改成功' : '入住登记成功')
      dialogVisible.value = false
      handleSearch() // 刷新列表
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    // 这里会捕获到你在 Service 实现类里抛出的“房间被占用”异常
    const errorMsg = e.response?.data?.msg || '服务器连接异常'
    ElMessage.error(errorMsg)
  } finally {
    loading.value = false
  }
}
// const submitForm = async () => {
//   if (!form.value.guestName || !form.value.roomNumber) {
//     return ElMessage.warning('请填写必要信息')
//   }
//   try {
//     const res = await axios.post('http://localhost:8080/api/bookings/save', form.value)
//     if (res.data.code === 200) {
//       ElMessage.success('入住登记成功')
//       dialogVisible.value = false
//       handleSearch() // 刷新列表
//       form.value = { guestName: '', roomNumber: null, phone: '', status: 1, totalAmount: 0 }
//     }
//   } catch (e) {
//     ElMessage.error('提交失败，请检查网络')
//   }
// }
// 4. 退房结算
const handleCheckout = (id) => {
  ElMessageBox.confirm('确认要为该订单办理退房结算吗？', '提示').then(async () => {
    const res = await axios.put(`http://localhost:8080/api/bookings/checkOut?id=${id}`)
    if (res.data.code === 200) {
      ElMessage.success('结算成功')
      handleSearch()
    }
  }).catch(() => {})
}

// 5. 办理入住 (状态从 1 变为 2)
const handleCheckIn = (id) => {
  ElMessageBox.confirm('确认要为该客人办理入住吗？', '办理入住', {
    confirmButtonText: '确认入住',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    try {
      // 这里的 URL 对应你后端写的 /api/bookings/checkIn
      const res = await axios.put(`http://localhost:8080/api/bookings/checkIn?id=${id}`)
      if (res.data.code === 200) {
        ElMessage.success('入住手续办理成功！')
        handleSearch() // 刷新列表，此时状态变为 2 (绿色)
      }
    } catch (e) {
      ElMessage.error('操作失败，请检查后端接口')
    }
  })
}

// 6. 取消预订 (状态从 1 变为 4)
const handleCancel = (id) => {
  ElMessageBox.confirm('确认要取消该预订订单吗？此操作不可逆。', '取消预订', {
    confirmButtonText: '确认取消',
    cancelButtonText: '点错了',
    type: 'error'
  }).then(async () => {
    try {
      // 这里的 URL 对应你后端写的 /api/bookings/cancel
      const res = await axios.put(`http://localhost:8080/api/bookings/cancel?id=${id}`)
      if (res.data.code === 200) {
        ElMessage.warning('订单已取消')
        handleSearch() // 刷新列表，此时状态变为 4 (灰色)
      }
    } catch (e) {
      ElMessage.error('取消失败')
    }
  })
}

//  修改“修改”按钮绑定的方法
const handleEdit = (row) => {
  isEdit.value = true // 设为修改模式
  form.value = { ...row } // 回显当前行的数据
  dialogVisible.value = true
}

// 初始加载
onMounted(() => {
  handleSearch()
})
</script>

<style scoped>
/* 侧边栏和主容器的布局 */
.layout-container {
  display: flex;
  width: 100vw;
  height: 100vh;
  overflow: hidden; /* 再次确认不出现横向溢出 */
}

.el-aside {
  background-color: #304156;
  flex-shrink: 0; /* 禁止侧边栏被挤压 */
}

/* 右侧内容垂直排布：Header 在上，Main 在下 */
.right-box {
  display: flex;
  flex-direction: column;
  flex-grow: 1; /* 占据剩余的所有宽度 */
  overflow: hidden;
}

.el-header {
  height: 60px;
  background-color: white;
  border-bottom: 1px solid #dcdfe6;
  flex-shrink: 0; /* 保证高度固定为 60px */
}

.el-main {
  background-color: #f5f7fa;
  padding: 20px;
  flex-grow: 1; /* 占据剩余的所有高度 */
  overflow-y: auto; /* 只有这里允许垂直滚动 */
}
</style>