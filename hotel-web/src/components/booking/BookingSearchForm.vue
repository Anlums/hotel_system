NEW_FILE_CODE
1️⃣ BookingSearchForm.vue - 搜索表单
<template>
  <el-card style="margin-bottom: 15px;">
    <el-form :inline="true" :model="searchForm">
      <el-form-item label="入住人">
        <el-input
            v-model="searchForm.guestName"
            placeholder="请输入姓名"
            clearable
            @keyup.enter="handleSearch"
        />
      </el-form-item>
      <el-form-item label="房间号">
        <el-input-number
            v-model="searchForm.roomNumber"
            :controls="false"
            placeholder="房间号"
        />
      </el-form-item>
      <el-form-item label="状态">
        <el-select
            v-model="searchForm.status"
            placeholder="全部状态"
            clearable
            style="width: 120px;"
        >
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
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['search', 'reset'])

const searchForm = ref({
  guestName: '',
  roomNumber: null,
  status: null
})

const handleSearch = () => {
  emit('search', searchForm.value)
}

const resetSearch = () => {
  searchForm.value = { guestName: '', roomNumber: null, status: null }
  emit('reset')
}

defineExpose({
  searchForm
})
</script>
