NEW_FILE_CODE
<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>📊 房间状态分布</span>
            </div>
          </template>
          <div ref="roomStatusChart" class="chart"></div>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>💰 近7天营业额趋势</span>
            </div>
          </template>
          <div ref="revenueChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67C23A;">🛏️</div>
            <div class="stat-info">
              <div class="stat-value">{{ roomStatus.available || 0 }}</div>
              <div class="stat-label">空闲房间</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #E6A23C;">📋</div>
            <div class="stat-info">
              <div class="stat-value">{{ roomStatus.booked || 0 }}</div>
              <div class="stat-label">已预订房间</div>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409EFF;">✅</div>
            <div class="stat-info">
              <div class="stat-value">{{ roomStatus.occupied || 0 }}</div>
              <div class="stat-label">已入住房间</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'

const roomStatusChart = ref(null)
const revenueChart = ref(null)
const roomStatus = ref({})

let roomChartInstance = null
let revenueChartInstance = null

const fetchRoomStatus = async () => {
  try {
    const res = await axios.get('/api/rooms/statusCount')
    roomStatus.value = res.data.data
    initRoomStatusChart(res.data.data)
  } catch (e) {
    ElMessage.error('获取房间状态失败')
  }
}

const fetchRevenue = async () => {
  try {
    const res = await axios.get('/api/bookings/revenue?days=7')
    initRevenueChart(res.data.data)
  } catch (e) {
    ElMessage.error('获取营业额数据失败')
  }
}

const initRoomStatusChart = (data) => {
  if (!roomChartInstance) {
    roomChartInstance = echarts.init(roomStatusChart.value)
  }

  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c} ({d}%)'
    },
    legend: {
      orient: 'vertical',
      left: 'left'
    },
    series: [
      {
        name: '房间状态',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: true,
          formatter: '{b}: {c}间'
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        data: [
          { value: data.available || 0, name: '空闲', itemStyle: { color: '#67C23A' } },
          { value: data.booked || 0, name: '已预订', itemStyle: { color: '#E6A23C' } },
          { value: data.occupied || 0, name: '已入住', itemStyle: { color: '#409EFF' } }
        ]
      }
    ]
  }

  roomChartInstance.setOption(option)
}

const initRevenueChart = (data) => {
  if (!revenueChartInstance) {
    revenueChartInstance = echarts.init(revenueChart.value)
  }

  const dates = data.map(item => item.date)
  const revenues = data.map(item => item.revenue || 0)

  const option = {
    tooltip: {
      trigger: 'axis',
      formatter: '{b}<br/>营业额: ¥{c}'
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates,
      axisLabel: {
        rotate: 45
      }
    },
    yAxis: {
      type: 'value',
      name: '营业额 (元)',
      axisLabel: {
        formatter: '¥{value}'
      }
    },
    series: [
      {
        name: '营业额',
        type: 'line',
        smooth: true,
        data: revenues,
        areaStyle: {
          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
          ])
        },
        itemStyle: {
          color: '#409EFF'
        },
        lineStyle: {
          width: 3
        }
      }
    ]
  }

  revenueChartInstance.setOption(option)
}

onMounted(() => {
  fetchRoomStatus()
  fetchRevenue()

  window.addEventListener('resize', () => {
    roomChartInstance?.resize()
    revenueChartInstance?.resize()
  })
})

onBeforeUnmount(() => {
  roomChartInstance?.dispose()
  revenueChartInstance?.dispose()
  window.removeEventListener('resize', () => {
    roomChartInstance?.resize()
    revenueChartInstance?.resize()
  })
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.chart-card {
  height: 400px;
}

.card-header {
  font-weight: bold;
  font-size: 16px;
}

.chart {
  width: 100%;
  height: 320px;
}

.stat-card {
  height: 120px;
}

.stat-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  margin-right: 20px;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}
</style>
