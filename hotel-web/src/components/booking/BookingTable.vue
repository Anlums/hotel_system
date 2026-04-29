NEW_FILE_CODE
2️⃣ BookingTable.vue - 数据表格
职责： 只负责展示数据和触发操作按钮
<template>
  <el-card>
    <div style="margin-bottom: 15px; display: flex; justify-content: space-between;">
      <el-button type="success" @click="$emit('add')">+ 办理新入住</el-button>
      <el-button type="info" plain @click="$emit('refresh')">刷新数据</el-button>
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
            <el-button type="success" size="small" @click="$emit('check-in', scope.row.id)">办理入住</el-button>
            <el-button type="danger" size="small" plain @click="$emit('cancel', scope.row.id)">取消预订</el-button>
          </template>

          <template v-else-if="scope.row.status === 2">
            <el-button type="primary" size="small" @click="$emit('checkout', scope.row.id)">退房结算</el-button>
          </template>

          <span v-else-if="scope.row.status === 3 || scope.row.status === 4"
                style="color: #909399; font-size: 12px;">- 流程结束 -
          </span>

          <el-button
              v-if="scope.row.status === 1 || scope.row.status === 2"
              type="info"
              size="small"
              plain
              @click="$emit('edit', scope.row)"
          >
            修改
          </el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="暂无符合条件的订单记录" />
      </template>
    </el-table>
  </el-card>
</template>

<script setup>
defineProps({
  tableData: {
    type: Array,
    required: true
  },
  loading: {
    type: Boolean,
    default: false
  }
})

defineEmits(['add', 'refresh', 'check-in', 'cancel', 'checkout', 'edit'])

const getStatusType = (status) => {
  const map = {
    1: 'warning',
    2: 'success',
    3: 'danger',
    4: 'info'
  }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = {
    1: '已预约',
    2: '已入住',
    3: '已退房',
    4: '已取消'
  }
  return map[status] || '未知'
}
</script>
