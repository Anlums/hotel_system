NEW_FILE_CODE
4️⃣ BookingManager.vue - 总控制器（父组件）
职责： 协调所有子组件，处理业务逻辑


<template>
  <el-main>
    <BookingSearchForm
        ref="searchFormRef"
        @search="handleSearch"
        @reset="handleReset"
    />

    <BookingTable
        :table-data="tableData"
        :loading="loading"
        @add="handleAdd"
        @refresh="fetchData"
        @check-in="handleCheckIn"
        @cancel="handleCancel"
        @checkout="handleCheckout"
        @edit="handleEdit"
    />

    <BookingDialog
        v-model="dialogVisible"
        :is-edit="isEdit"
        :form-data="form"
        @submit="submitForm"
    />
  </el-main>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'
import BookingSearchForm from '@/components/booking/BookingSearchForm.vue'
import BookingTable from '@/components/booking/BookingTable.vue'
import BookingDialog from '@/components/booking/BookingDialog.vue'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const searchFormRef = ref(null)

const form = ref({
  guestName: '',
  roomNumber: null,
  phone: '',
  checkInDate: '',
  status: 1,
  totalAmount: 0
})

const handleSearch = async (searchParams) => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/api/bookings/search', {
      params: searchParams
    })
    tableData.value = res.data.data
  } catch (e) {
    ElMessage.error('搜索失败')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  fetchData()
}

const fetchData = async () => {
  if (searchFormRef.value) {
    handleSearch(searchFormRef.value.searchForm)
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    guestName: '',
    roomNumber: null,
    phone: '',
    checkInDate: '',
    status: 1,
    totalAmount: 0
  }
  dialogVisible.value = true
}

const submitForm = async (formData) => {
  if (!formData.guestName || !formData.roomNumber) {
    return ElMessage.warning('请填写必要信息')
  }

  loading.value = true
  try {
    let res
    if (isEdit.value) {
      res = await axios.put('http://localhost:8080/api/bookings/update', formData)
    } else {
      res = await axios.post('http://localhost:8080/api/bookings/save', formData)
    }

    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '订单修改成功' : '入住登记成功')
      dialogVisible.value = false
      fetchData()
    } else {
      ElMessage.error(res.data.msg || '操作失败')
    }
  } catch (e) {
    const errorMsg = e.response?.data?.msg || '服务器连接异常'
    ElMessage.error(errorMsg)
  } finally {
    loading.value = false
  }
}

const handleCheckout = (id) => {
  ElMessageBox.confirm('确认要为该订单办理退房结算吗？', '提示').then(async () => {
    const res = await axios.put(`http://localhost:8080/api/bookings/checkOut?id=${id}`)
    if (res.data.code === 200) {
      ElMessage.success('结算成功')
      fetchData()
    }
  }).catch(() => {})
}

const handleCheckIn = (id) => {
  ElMessageBox.confirm('确认要为该客人办理入住吗？', '办理入住', {
    confirmButtonText: '确认入住',
    cancelButtonText: '取消',
    type: 'success'
  }).then(async () => {
    try {
      const res = await axios.put(`http://localhost:8080/api/bookings/checkIn?id=${id}`)
      if (res.data.code === 200) {
        ElMessage.success('入住手续办理成功！')
        fetchData()
      }
    } catch (e) {
      ElMessage.error('操作失败，请检查后端接口')
    }
  })
}

const handleCancel = (id) => {
  ElMessageBox.confirm('确认要取消该预订订单吗？此操作不可逆。', '取消预订', {
    confirmButtonText: '确认取消',
    cancelButtonText: '点错了',
    type: 'error'
  }).then(async () => {
    try {
      const res = await axios.put(`http://localhost:8080/api/bookings/cancel?id=${id}`)
      if (res.data.code === 200) {
        ElMessage.warning('订单已取消')
        fetchData()
      }
    } catch (e) {
      ElMessage.error('取消失败')
    }
  })
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

onMounted(() => {
  fetchData()
})
</script>
