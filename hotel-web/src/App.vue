<template>
  <div style="padding: 30px;">
    <el-card>
      <h2>🏨 酒店管理后台</h2>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="id" label="订单ID" width="80" />
        <el-table-column prop="guestName" label="入住人" />
        <el-table-column prop="roomNumber" label="房间号" />
        <el-table-column label="总金额">
          <template #default="scope">
            <span v-if="scope.row.totalAmount" >￥{{ scope.row.totalAmount }}</span>
            <el-tag v-else type="info">待结算</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button type="danger" size="small" @click="handleCheckout(scope.row.id)">退房结算</el-button>
          </template>
        </el-table-column>
        <el-table-column label="订单状态">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'warning' : (scope.row.status === 2 ? 'success' : 'info')">
              {{ scope.row.status === 1 ? '已预约' : (scope.row.status === 2 ? '已入住' : '已退房') }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

      <el-button type="success" @click="dialogVisible = true" style="margin-bottom: 20px;">
        + 办理新入住
      </el-button>

      <el-dialog v-model="dialogVisible" title="新客入住登记" width="500px">
        <el-form :model="form" label-width="100px">
          <el-form-item label="入住人姓名">
            <el-input v-model="form.guestName" />
          </el-form-item>
          <el-form-item label="房间编号">
            <el-input-number v-model="form.roomNumber" />
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.phone" />
          </el-form-item>
          <el-form-item label="入住时间">
            <el-date-picker
                v-model="form.checkInDate"
                type="datetime"
                placeholder="选择日期时间"
                value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确认提交</el-button>
        </template>
      </el-dialog>

    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const tableData = ref([])

// 获取后端数据
const fetchData = async () => {
  try {
    const res = await axios.get('http://localhost:8080/api/bookings/list')
    // res.data.data 对应你后端的 Result.data
    tableData.value = res.data.data
  } catch (err) {
    ElMessage.error('连接后端失败，请检查后端是否启动')
  }
}

// 调用退房接口
const handleCheckout = async (id) => {
  try {
    // 修改点 1：改为 ?id= 格式，匹配后端的 @RequestParam
    const res = await axios.put(`http://localhost:8080/api/bookings/checkOut?id=${id}`)

    if (res.data.code === 200) {
      ElMessage.success('结算成功！')
      fetchData() // 刷新列表
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (err) {
    // 修改点 2：打印详细错误，方便调试
    console.error('退房失败:', err)
    // 尝试获取后端返回的具体错误信息
    const errorMsg = err.response?.data?.msg || err.message || '系统异常'
    ElMessage.error(errorMsg)
  }
}

// 新增订单
const dialogVisible = ref(false) // 控制弹窗显示
const form = ref({
  guestName: '',
  roomNumber: null,
  phone: '',
  checkInDate: '',
  status: 1, // 默认状态为已预约/已入住
  totalAmount: 0 // 初始金额为0
})
        // 提交表单到后端
const submitForm = async () => {
  try {
    // 这里的 URL 要和你后端的 @PostMapping 路径一致
    const res = await axios.post('http://localhost:8080/api/bookings/save', form.value)

    if (res.data.code === 200) {
      ElMessage.success('入住登记成功！')
      dialogVisible.value = false // 关闭弹窗
      fetchData() // 刷新列表，看到新数据
      // 重置表单，方便下次输入
      form.value = { guestName: '', roomNumber: null, phone: '', status: 1, totalAmount: 0 }
    } else {
      ElMessage.error(res.data.msg)
    }
  } catch (error) {
    ElMessage.error('提交失败，请检查网络或后端')
  }
}

onMounted(fetchData)
</script>