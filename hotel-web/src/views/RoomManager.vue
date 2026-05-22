NEW_FILE_CODE
<template>
  <div class="room-manager-container">
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="房间号">
          <el-input-number v-model="searchForm.roomNumber" :controls="false" placeholder="房间号" />
        </el-form-item>
        <el-form-item label="房间类型">
          <el-select v-model="searchForm.type" placeholder="全部类型" clearable style="width: 150px;">
            <el-option label="标准间" value="标准间" />
            <el-option label="大床房" value="大床房" />
            <el-option label="豪华套房" value="豪华套房" />
            <el-option label="总统套房" value="总统套房" />
            <el-option label="爱情房间" value="爱情房间" />
            <el-option label="亲子房" value="亲子房" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="全部状态" clearable style="width: 120px;">
            <el-option label="空闲" :value="0" />
            <el-option label="已预订" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">🔍 查询</el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <div class="table-header">
        <el-button type="success" @click="handleAdd">+ 添加房间</el-button>
        <el-button type="info" plain @click="fetchData">刷新数据</el-button>
      </div>

      <el-table
          v-loading="loading"
          :data="tableData"
          border
          stripe
          style="width: 100%"
          max-height="calc(100vh - 280px)"
      >
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="roomNumber" label="房间号" width="100" align="center" />
        <el-table-column prop="type" label="房间类型" width="150" align="center" />
        <el-table-column prop="price" label="价格(元/晚)" width="120" align="center">
          <template #default="scope">
            <span v-if="scope.row.price" style="color: #f56c6c; font-weight: bold;">
              ￥{{ scope.row.price }}
            </span>
            <el-tag v-else type="info">未设置</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 0 ? 'success' : 'warning'" effect="dark">
              {{ scope.row.status === 0 ? '空闲' : '已占用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" width="180">
          <template #default="scope">
            {{ scope.row.createTime?.replace('T', ' ') }}
          </template>
        </el-table-column>
        <el-table-column label="操作" min-width="200" fixed="right" align="center">
          <template #default="scope">
            <el-button type="primary" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button
                v-if="scope.row.status === 0"
                type="danger"
                size="small"
                plain
                @click="handleDelete(scope.row)"
            >
              删除
            </el-button>
            <span v-else style="color: #909399; font-size: 12px;margin-left: 20px;">使用中</span>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="暂无房间记录" />
        </template>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑房间信息' : '添加新房间'" width="450px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="房间号" required>
          <el-input-number v-model="form.roomNumber" :min="1" :max="9999" placeholder="请输入房间号" />
        </el-form-item>
        <el-form-item label="房间类型" required>
          <el-select v-model="form.type" placeholder="请选择房间类型" style="width: 100%;">
            <el-option label="标准间" value="标准间" />
            <el-option label="大床房" value="大床房" />
            <el-option label="豪华套房" value="豪华套房" />
            <el-option label="总统套房" value="总统套房" />
            <el-option label="爱情房间" value="爱情房间" />
            <el-option label="亲子房" value="亲子房" />
          </el-select>
        </el-form-item>
        <el-form-item label="价格" required>
          <el-input-number v-model="form.price" :min="0" :precision="2" placeholder="请输入价格" style="width: 100%;" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="0">空闲</el-radio>
            <el-radio :label="1">已预订</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确认提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>


import { ref, onMounted } from 'vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

const tableData = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)

const searchForm = ref({
  roomNumber: null,
  type: '',
  status: null
})

const form = ref({
  id: null,
  roomNumber: null,
  type: '',
  price: 0,
  status: 0
})

const handleSearch = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/api/rooms/allRoom')
    let data = res.data.data

    if (searchForm.value.roomNumber) {
      data = data.filter(item => item.roomNumber === searchForm.value.roomNumber)
    }
    if (searchForm.value.type) {
      data = data.filter(item => item.type === searchForm.value.type)
    }
    if (searchForm.value.status !== null && searchForm.value.status !== '') {
      data = data.filter(item => item.status === searchForm.value.status)
    }

    tableData.value = data
  } catch (e) {
    ElMessage.error('搜索失败')
  } finally {
    loading.value = false
  }
}

const resetSearch = () => {
  searchForm.value = { roomNumber: null, type: '', status: null }
  fetchData()
}

const fetchData = async () => {
  loading.value = true
  try {
    const res = await axios.get('http://localhost:8080/api/rooms/allRoom')
    tableData.value = res.data.data
  } catch (e) {
    ElMessage.error('获取房间数据失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    id: null,
    roomNumber: null,
    type: '',
    price: 0,
    status: 0
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  form.value = { ...row }
  dialogVisible.value = true
}

const handleDelete = (row) => {
  ElMessageBox.confirm('确认要删除该房间吗？此操作不可逆。', '删除房间', {
    confirmButtonText: '确认删除',
    cancelButtonText: '取消',
    type: 'error'
  }).then(async () => {
    try {
      const res = await axios.delete(`http://localhost:8080/api/rooms/id=${row.id}`)
      if (res.data.code === 200) {
        ElMessage.success('删除成功')
        fetchData();
      }
    } catch (e) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

const submitForm = async () => {
  if (!form.value.roomNumber || !form.value.type || !form.value.price) {
    return ElMessage.warning('请填写必要信息')
  }

  loading.value = true
  try {
    let res
    if (isEdit.value) {
      res = await axios.put('http://localhost:8080/api/rooms/update', form.value)
    } else {
      res = await axios.post('http://localhost:8080/api/rooms/insertRoom', form.value)
    }

    if (res.data.code === 200) {
      ElMessage.success(isEdit.value ? '房间信息修改成功' : '房间添加成功')
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

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.room-manager-container {
  padding: 20px;
  background-color: #f5f7fa;
  height: calc(100vh - 60px);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.search-card {
  flex-shrink: 0;
  margin-bottom: 15px;
}

.table-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.table-header {
  margin-bottom: 15px;
  display: flex;
  justify-content: space-between;
  flex-shrink: 0;
}
</style>
