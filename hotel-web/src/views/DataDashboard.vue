<template>
  <div class="dashboard-container">
    <!-- 1. 顶部视觉大图：展示智慧酒店形象 -->
    <el-card class="banner-card" :body-style="{ padding: '0px' }">
      <div class="banner-wrapper">
        <img src="@/assets/酒店照片/天玺尊邸酒店（顶级私宅 + 圈层感）.png" class="banner-img" />
        <div class="banner-mask">
          <h2 class="title">天玺尊邸酒店管理系统</h2>
          <p class="subtitle">
            <el-icon><Monitor /></el-icon> 实时数据监控 ·
            <el-icon><MagicStick /></el-icon> AI 驱动决策 ·
            <el-icon><Star /></el-icon> 极致服务体验
          </p>
          <!-- AI 唤起按钮 -->
          <div class="banner-actions">
            <el-button type="warning" size="large" @click="aiDrawer = true" round shadow>
              <el-icon><Cpu /></el-icon> 呼叫 AI 智能管家
            </el-button>
          </div>
        </div>
      </div>
    </el-card>

    <!-- 2. 中间：数据统计卡片（横向平铺） -->
    <el-row :gutter="20" class="stat-row">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #f0f9eb; color: #67c23a;">🛏️</div>
            <div class="stat-info">
              <div class="stat-value">{{ roomStatus.available || 0 }}</div>
              <div class="stat-label">空闲房间</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #fdf6ec; color: #e6a23c;">📋</div>
            <div class="stat-info">
              <div class="stat-value">{{ roomStatus.booked || 0 }}</div>
              <div class="stat-label">已预订房间</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #ecf5ff; color: #409eff;">✅</div>
            <div class="stat-info">
              <div class="stat-value">{{ roomStatus.occupied || 0 }}</div>
              <div class="stat-label">已入住房间</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 3. 底部：数据图表展示 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="10">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">📊 房间状态分布</div>
          </template>
          <div ref="roomStatusChart" class="chart"></div>
        </el-card>
      </el-col>
      <el-col :span="14">
        <el-card class="chart-card" shadow="hover">
          <template #header>
            <div class="card-header">💰 近7天营业额趋势</div>
          </template>
          <div ref="revenueChart" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 4. AI 助手抽屉 -->
    <el-drawer
        v-model="aiDrawer"
        title="天玺尊邸 · AI 智慧管家 (DeepSeek-V4-Pro)"
        direction="rtl"
        size="450px"
        custom-class="ai-drawer"
    >
      <div class="ai-chat-box">
        <div class="ai-header">
          <p>您好，我是您的 AI 助理。您可以询问排房建议、经营分析或系统操作。</p>
        </div>
        <el-input
            v-model="aiInput"
            type="textarea"
            :rows="4"
            placeholder="请输入您的需求，例如：帮我分析本周营业额下滑的原因，并给出排房策略建议..."
        />
        <el-button
            type="primary"
            class="ask-btn"
            @click="handleAISearch"
            :loading="aiLoading"
        >
          {{ aiLoading ? '正在进行 V4 深度思考...' : '开始智能分析' }}
        </el-button>

        <el-divider v-if="aiResult">分析结论</el-divider>
        <div v-if="aiResult" class="ai-result-content">
          <div class="result-text">{{ aiResult }}</div>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { Monitor, MagicStick, Star, Cpu } from '@element-plus/icons-vue'

// --- 状态数据 ---
const roomStatusChart = ref(null)
const revenueChart = ref(null)
const roomStatus = ref({})
let roomChartInstance = null
let revenueChartInstance = null

// --- AI 相关 ---
const aiDrawer = ref(false)
const aiInput = ref('')
const aiLoading = ref(false)
const aiResult = ref('')

// 获取房间数据
const fetchRoomStatus = async () => {
  try {
    const res = await axios.get('/api/rooms/statusCount')
    roomStatus.value = res.data.data
    initRoomStatusChart(res.data.data)
  } catch (e) {
    ElMessage.error('获取房间状态失败')
  }
}

// 获取营收数据
const fetchRevenue = async () => {
  try {
    const res = await axios.get('/api/bookings/revenue?days=7')
    initRevenueChart(res.data.data)
  } catch (e) {
    ElMessage.error('获取营业额数据失败')
  }
}

// AI 接口调用逻辑
const handleAISearch = async () => {
  if (!aiInput.value) return ElMessage.warning('请输入咨询内容')
  aiLoading.value = true
  aiResult.value = ''
  try {
    const res = await axios.post('/api/ai/chat', { prompt: aiInput.value })
    aiResult.value = res.data.content // 对应后端返回的内容
  } catch (e) {
    ElMessage.error('AI 服务暂时不可用')
  } finally {
    aiLoading.value = false
  }
}

// 图表初始化逻辑... (保持原有的 echarts 配置不变)
const initRoomStatusChart = (data) => {
  if (!roomChartInstance) roomChartInstance = echarts.init(roomStatusChart.value)
  roomChartInstance.setOption({
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      type: 'pie',
      radius: ['50%', '75%'],
      itemStyle: { borderRadius: 10, borderColor: '#fff', borderWidth: 2 },
      data: [
        { value: data.available || 0, name: '空闲', itemStyle: { color: '#67C23A' } },
        { value: data.booked || 0, name: '已预订', itemStyle: { color: '#E6A23C' } },
        { value: data.occupied || 0, name: '已入住', itemStyle: { color: '#409EFF' } }
      ]
    }]
  })
}

const initRevenueChart = (data) => {
  if (!revenueChartInstance) revenueChartInstance = echarts.init(revenueChart.value)
  const dates = data.map(item => item.date)
  const revenues = data.map(item => item.revenue || 0)
  revenueChartInstance.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', boundaryGap: false, data: dates },
    yAxis: { type: 'value', axisLabel: { formatter: '¥{value}' } },
    series: [{
      type: 'line',
      smooth: true,
      data: revenues,
      areaStyle: { color: 'rgba(64, 158, 255, 0.2)' },
      itemStyle: { color: '#409EFF' }
    }]
  })
}

onMounted(() => {
  fetchRoomStatus()
  fetchRevenue()
  window.addEventListener('resize', handleResize)
})

const handleResize = () => {
  roomChartInstance?.resize()
  revenueChartInstance?.resize()
}

onBeforeUnmount(() => {
  roomChartInstance?.dispose()
  revenueChartInstance?.dispose()
  window.removeEventListener('resize', handleResize)
})
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

/* 1. 顶部大图样式 */
.banner-card {
  margin-bottom: 25px;
  border: none;
  border-radius: 16px;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(0,0,0,0.1);
}

.banner-wrapper {
  position: relative;
  height: 350px;
  overflow: hidden;
}

.banner-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 1s;
}

.banner-card:hover .banner-img {
  transform: scale(1.05); /* 悬停微缩放效果 */
}

.banner-mask {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, rgba(0, 0, 0, 0.8) 0%, rgba(0, 0, 0, 0.3) 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding-left: 60px;
  color: white;
}

/* 琥珀金立体字 */
.banner-mask .title {
  font-size: 48px;
  font-weight: bold;
  margin: 0;
  letter-spacing: 5px;
  color: #FFD700;
  text-shadow:
      1px 1px 0px #B38B2D,
      2px 2px 0px #B38B2D,
      3px 3px 0px #B38B2D,
      0 5px 10px rgba(0,0,0,0.5);
  filter: drop-shadow(0px 2px 2px rgba(0,0,0,0.4));
}

.banner-mask .subtitle {
  font-size: 18px;
  margin-top: 20px;
  letter-spacing: 1px;
  opacity: 0.95;
  display: flex;
  align-items: center;
  gap: 15px;
}

.banner-actions {
  margin-top: 30px;
}

/* 2. 统计卡片 */
.stat-row {
  margin-bottom: 25px;
}
.stat-card {
  height: 120px;
  border-radius: 12px;
  border: none;
}
.stat-card:hover { transform: translateY(-5px); }
.stat-content { display: flex; align-items: center; padding: 10px; }
.stat-icon {
  width: 60px; height: 60px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  font-size: 30px; margin-right: 20px;
}
.stat-value { font-size: 32px; font-weight: bold; color: #303133; }
.stat-label { color: #909399; font-size: 14px; margin-top: 4px; }

/* 3. 图表 */
.chart-card { height: 450px; border-radius: 12px; }
.chart { width: 100%; height: 380px; }

/* 4. AI 抽屉样式 */
.ai-chat-box { display: flex; flex-direction: column; gap: 20px; }
.ai-header { font-size: 14px; color: #606266; background: #f0f2f5; padding: 15px; border-radius: 8px; }
.ask-btn { height: 45px; font-weight: bold; letter-spacing: 1px; }
.ai-result-content {
  background: #fdf6ec;
  border: 1px solid #faecd8;
  padding: 20px;
  border-radius: 12px;
  min-height: 200px;
  white-space: pre-wrap; /* 保持 AI 的换行格式 */
  line-height: 1.8;
  color: #606266;
}
</style>

<!--<template>-->
<!--  <div class="dashboard-container">-->
<!--    &lt;!&ndash; 1. 顶部视觉大图：展示智慧酒店形象 &ndash;&gt;-->
<!--    <el-card class="banner-card" :body-style="{ padding: '0px' }">-->
<!--      <div class="banner-wrapper">-->
<!--        <img src="@/assets/酒店照片/天玺尊邸酒店（顶级私宅 + 圈层感）.png" class="banner-img" />-->
<!--        <div class="banner-mask">-->
<!--          <h2 class="title">天玺尊邸酒店管理系统</h2>-->
<!--          <p class="subtitle">实时数据监控 · AI 驱动决策 · 极致服务体验</p>-->
<!--        </div>-->
<!--      </div>-->
<!--    </el-card>-->

<!--    &lt;!&ndash; 2. 中间：数据统计卡片（横向平铺） &ndash;&gt;-->
<!--    <el-row :gutter="20" class="stat-row">-->
<!--      <el-col :span="8">-->
<!--        <el-card shadow="hover" class="stat-card">-->
<!--          <div class="stat-content">-->
<!--            <div class="stat-icon" style="background-color: #f0f9eb; color: #67c23a;">🛏️</div>-->
<!--            <div class="stat-info">-->
<!--              <div class="stat-value">{{ roomStatus.available || 0 }}</div>-->
<!--              <div class="stat-label">空闲房间</div>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--      <el-col :span="8">-->
<!--        <el-card shadow="hover" class="stat-card">-->
<!--          <div class="stat-content">-->
<!--            <div class="stat-icon" style="background-color: #fdf6ec; color: #e6a23c;">📋</div>-->
<!--            <div class="stat-info">-->
<!--              <div class="stat-value">{{ roomStatus.booked || 0 }}</div>-->
<!--              <div class="stat-label">已预订房间</div>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--      <el-col :span="8">-->
<!--        <el-card shadow="hover" class="stat-card">-->
<!--          <div class="stat-content">-->
<!--            <div class="stat-icon" style="background-color: #ecf5ff; color: #409eff;">✅</div>-->
<!--            <div class="stat-info">-->
<!--              <div class="stat-value">{{ roomStatus.occupied || 0 }}</div>-->
<!--              <div class="stat-label">已入住房间</div>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--    </el-row>-->

<!--    &lt;!&ndash; 3. 底部：数据图表展示（横向平铺） &ndash;&gt;-->
<!--    <el-row :gutter="20" class="chart-row">-->
<!--      <el-col :span="10">-->
<!--        <el-card class="chart-card" shadow="hover">-->
<!--          <template #header>-->
<!--            <div class="card-header">📊 房间状态分布</div>-->
<!--          </template>-->
<!--          <div ref="roomStatusChart" class="chart"></div>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--      <el-col :span="14">-->
<!--        <el-card class="chart-card" shadow="hover">-->
<!--          <template #header>-->
<!--            <div class="card-header">💰 近7天营业额趋势</div>-->
<!--          </template>-->
<!--          <div ref="revenueChart" class="chart"></div>-->
<!--        </el-card>-->
<!--      </el-col>-->
<!--    </el-row>-->
<!--  </div>-->
<!--</template>-->

<!--<script setup>-->
<!--import { ref, onMounted, onBeforeUnmount } from 'vue'-->
<!--import * as echarts from 'echarts'-->
<!--import axios from 'axios'-->
<!--import { ElMessage } from 'element-plus'-->

<!--// -&#45;&#45; 逻辑部分保持不变 -&#45;&#45;-->
<!--const roomStatusChart = ref(null)-->
<!--const revenueChart = ref(null)-->
<!--const roomStatus = ref({})-->
<!--let roomChartInstance = null-->
<!--let revenueChartInstance = null-->

<!--const fetchRoomStatus = async () => {-->
<!--  try {-->
<!--    const res = await axios.get('/api/rooms/statusCount')-->
<!--    roomStatus.value = res.data.data-->
<!--    initRoomStatusChart(res.data.data)-->
<!--  } catch (e) {-->
<!--    ElMessage.error('获取房间状态失败')-->
<!--  }-->
<!--}-->

<!--const fetchRevenue = async () => {-->
<!--  try {-->
<!--    const res = await axios.get('/api/bookings/revenue?days=7')-->
<!--    initRevenueChart(res.data.data)-->
<!--  } catch (e) {-->
<!--    ElMessage.error('获取营业额数据失败')-->
<!--  }-->
<!--}-->

<!--const initRoomStatusChart = (data) => {-->
<!--  if (!roomChartInstance) roomChartInstance = echarts.init(roomStatusChart.value)-->
<!--  const option = {-->
<!--    tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },-->
<!--    legend: { orient: 'vertical', left: 'left' },-->
<!--    series: [{-->
<!--      name: '房间状态',-->
<!--      type: 'pie',-->
<!--      radius: ['45%', '75%'],-->
<!--      avoidLabelOverlap: false,-->
<!--      itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },-->
<!--      label: { show: true, formatter: '{b}: {c}' },-->
<!--      data: [-->
<!--        { value: data.available || 0, name: '空闲', itemStyle: { color: '#67C23A' } },-->
<!--        { value: data.booked || 0, name: '已预订', itemStyle: { color: '#E6A23C' } },-->
<!--        { value: data.occupied || 0, name: '已入住', itemStyle: { color: '#409EFF' } }-->
<!--      ]-->
<!--    }]-->
<!--  }-->
<!--  roomChartInstance.setOption(option)-->
<!--}-->

<!--const initRevenueChart = (data) => {-->
<!--  if (!revenueChartInstance) revenueChartInstance = echarts.init(revenueChart.value)-->
<!--  const dates = data.map(item => item.date)-->
<!--  const revenues = data.map(item => item.revenue || 0)-->
<!--  const option = {-->
<!--    tooltip: { trigger: 'axis', formatter: '{b}<br/>营业额: ¥{c}' },-->
<!--    grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },-->
<!--    xAxis: { type: 'category', boundaryGap: false, data: dates },-->
<!--    yAxis: { type: 'value', name: '元', axisLabel: { formatter: '¥{value}' } },-->
<!--    series: [{-->
<!--      name: '营业额',-->
<!--      type: 'line',-->
<!--      smooth: true,-->
<!--      data: revenues,-->
<!--      areaStyle: {-->
<!--        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [-->
<!--          { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },-->
<!--          { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }-->
<!--        ])-->
<!--      },-->
<!--      itemStyle: { color: '#409EFF' },-->
<!--      lineStyle: { width: 3 }-->
<!--    }]-->
<!--  }-->
<!--  revenueChartInstance.setOption(option)-->
<!--}-->

<!--onMounted(() => {-->
<!--  fetchRoomStatus()-->
<!--  fetchRevenue()-->
<!--  window.addEventListener('resize', handleResize)-->
<!--})-->

<!--const handleResize = () => {-->
<!--  roomChartInstance?.resize()-->
<!--  revenueChartInstance?.resize()-->
<!--}-->

<!--onBeforeUnmount(() => {-->
<!--  roomChartInstance?.dispose()-->
<!--  revenueChartInstance?.dispose()-->
<!--  window.removeEventListener('resize', handleResize)-->
<!--})-->
<!--</script>-->

<!--<style scoped>-->
<!--.dashboard-container {-->
<!--  padding: 20px;-->
<!--  background-color: #f5f7fa; /* 浅灰色背景衬托卡片 */-->
<!--  min-height: 100vh;-->
<!--}-->

<!--/* 顶部大图样式 */-->
<!--.banner-card {-->
<!--  margin-bottom: 25px;-->
<!--  border: none;-->
<!--  border-radius: 12px;-->
<!--}-->

<!--.banner-wrapper {-->
<!--  position: relative;-->
<!--  height: 300px;-->
<!--  overflow: hidden;-->
<!--}-->

<!--.banner-img {-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  /* 必须有这一行，防止图片被压扁或拉长 */-->
<!--  object-fit: cover;-->
<!--  /* 如果你想让图片显示靠上一点，可以加这行（可选） */-->
<!--  object-position: center center;-->
<!--}-->
<!--.banner-mask {-->
<!--  position: absolute;-->
<!--  top: 0;-->
<!--  left: 0;-->
<!--  width: 100%;-->
<!--  height: 100%;-->
<!--  background: linear-gradient(90deg, rgba(0, 0, 0, 0.7) 0%, rgba(0, 0, 0, 0.2) 100%);-->
<!--  display: flex;-->
<!--  flex-direction: column;-->
<!--  justify-content: center;-->
<!--  padding-left: 50px;-->
<!--  color: white;-->
<!--}-->

<!--/* 调大文字以适应大图 */-->
<!--.banner-mask .title {-->
<!--  font-size: 42px; /* 更大的标题 */-->
<!--  font-weight: bold;-->
<!--  margin: 0;-->
<!--  letter-spacing: 4px;-->
<!--  /* 调整颜色 */-->
<!--  color: #FFD700; /* 纯白色，最经典 */-->
<!--  /* 或者使用 Element Plus 的主题色 */-->
<!--  /* color: #409EFF; */-->
<!--  /* 利用阴影制造光影对比 */-->
<!--  text-shadow:-->
<!--      rgba(255, 255, 255, 0.5) 0px 3px 3px;-->
<!--  filter: drop-shadow(0px -1px 1px rgba(0,0,0,0.8));-->

<!--  /* 添加阴影：水平 2px, 垂直 2px, 模糊距离 4px, 半透明黑色 */-->
<!--  //text-shadow:-->
<!--  //    1px 1px 0px #B38B2D,-->
<!--  //    2px 2px 0px #B38B2D,-->
<!--  //    3px 3px 0px #B38B2D,-->
<!--  //    4px 4px 0px #B38B2D,-->
<!--  //    5px 5px 5px rgba(0, 0, 0, 0.5); /* 最后一层是模糊阴影，增加悬浮感 */-->
<!--}-->

<!--.banner-mask .subtitle {-->
<!--  font-size: 20px; /* 更大的副标题 */-->
<!--  margin-top: 15px;-->
<!--  opacity: 0.9;-->
<!--}-->

<!--/* 统计卡片样式 */-->
<!--.stat-row {-->
<!--  margin-bottom: 25px;-->
<!--}-->

<!--.stat-card {-->
<!--  height: 110px;-->
<!--  border-radius: 10px;-->
<!--  transition: transform 0.3s;-->
<!--}-->

<!--.stat-card:hover {-->
<!--  transform: translateY(-5px);-->
<!--}-->

<!--.stat-content {-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--}-->

<!--.stat-icon {-->
<!--  width: 54px;-->
<!--  height: 54px;-->
<!--  border-radius: 12px;-->
<!--  display: flex;-->
<!--  align-items: center;-->
<!--  justify-content: center;-->
<!--  font-size: 24px;-->
<!--  margin-right: 15px;-->
<!--}-->

<!--.stat-value {-->
<!--  font-size: 28px;-->
<!--  font-weight: bold;-->
<!--  color: #303133;-->
<!--}-->

<!--.stat-label {-->
<!--  font-size: 14px;-->
<!--  color: #909399;-->
<!--}-->

<!--/* 图表卡片样式 */-->
<!--.chart-card {-->
<!--  height: 420px;-->
<!--  border-radius: 10px;-->
<!--}-->

<!--.card-header {-->
<!--  font-weight: bold;-->
<!--  color: #606266;-->
<!--}-->

<!--.chart {-->
<!--  width: 100%;-->
<!--  height: 340px;-->
<!--}-->
<!--</style>-->
<!--&lt;!&ndash;<template>&ndash;&gt;-->
<!--&lt;!&ndash;  <div class="dashboard-container">&ndash;&gt;-->
<!--&lt;!&ndash;    &ndash;&gt;-->
<!--&lt;!&ndash;    <el-row :gutter="20">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-col :span="12">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-card class="chart-card">&ndash;&gt;-->
<!--&lt;!&ndash;          <template #header>&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="card-header">&ndash;&gt;-->
<!--&lt;!&ndash;              <span>📊 房间状态分布</span>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;          </template>&ndash;&gt;-->
<!--&lt;!&ndash;          <div ref="roomStatusChart" class="chart"></div>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-col :span="12">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-card class="chart-card">&ndash;&gt;-->
<!--&lt;!&ndash;          <template #header>&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="card-header">&ndash;&gt;-->
<!--&lt;!&ndash;              <span>💰 近7天营业额趋势</span>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;          </template>&ndash;&gt;-->
<!--&lt;!&ndash;          <div ref="revenueChart" class="chart"></div>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-row>&ndash;&gt;-->

<!--&lt;!&ndash;    <el-row :gutter="20" style="margin-top: 20px;">&ndash;&gt;-->
<!--&lt;!&ndash;      <el-col :span="8">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-card class="stat-card">&ndash;&gt;-->
<!--&lt;!&ndash;          <div class="stat-content">&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="stat-icon" style="background-color: #67C23A;">🛏️</div>&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="stat-info">&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="stat-value">{{ roomStatus.available || 0 }}</div>&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="stat-label">空闲房间</div>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;          </div>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-col :span="8">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-card class="stat-card">&ndash;&gt;-->
<!--&lt;!&ndash;          <div class="stat-content">&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="stat-icon" style="background-color: #E6A23C;">📋</div>&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="stat-info">&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="stat-value">{{ roomStatus.booked || 0 }}</div>&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="stat-label">已预订房间</div>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;          </div>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->

<!--&lt;!&ndash;      <el-col :span="8">&ndash;&gt;-->
<!--&lt;!&ndash;        <el-card class="stat-card">&ndash;&gt;-->
<!--&lt;!&ndash;          <div class="stat-content">&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="stat-icon" style="background-color: #409EFF;">✅</div>&ndash;&gt;-->
<!--&lt;!&ndash;            <div class="stat-info">&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="stat-value">{{ roomStatus.occupied || 0 }}</div>&ndash;&gt;-->
<!--&lt;!&ndash;              <div class="stat-label">已入住房间</div>&ndash;&gt;-->
<!--&lt;!&ndash;            </div>&ndash;&gt;-->
<!--&lt;!&ndash;          </div>&ndash;&gt;-->
<!--&lt;!&ndash;        </el-card>&ndash;&gt;-->
<!--&lt;!&ndash;      </el-col>&ndash;&gt;-->
<!--&lt;!&ndash;    </el-row>&ndash;&gt;-->
<!--&lt;!&ndash;  </div>&ndash;&gt;-->
<!--&lt;!&ndash;</template>&ndash;&gt;-->

<!--&lt;!&ndash;<script setup>&ndash;&gt;-->
<!--&lt;!&ndash;import { ref, onMounted, onBeforeUnmount } from 'vue'&ndash;&gt;-->
<!--&lt;!&ndash;import * as echarts from 'echarts'&ndash;&gt;-->
<!--&lt;!&ndash;import axios from 'axios'&ndash;&gt;-->
<!--&lt;!&ndash;import { ElMessage } from 'element-plus'&ndash;&gt;-->

<!--&lt;!&ndash;const roomStatusChart = ref(null)&ndash;&gt;-->
<!--&lt;!&ndash;const revenueChart = ref(null)&ndash;&gt;-->
<!--&lt;!&ndash;const roomStatus = ref({})&ndash;&gt;-->

<!--&lt;!&ndash;let roomChartInstance = null&ndash;&gt;-->
<!--&lt;!&ndash;let revenueChartInstance = null&ndash;&gt;-->

<!--&lt;!&ndash;const fetchRoomStatus = async () => {&ndash;&gt;-->
<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    const res = await axios.get('/api/rooms/statusCount')&ndash;&gt;-->
<!--&lt;!&ndash;    roomStatus.value = res.data.data&ndash;&gt;-->
<!--&lt;!&ndash;    initRoomStatusChart(res.data.data)&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (e) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('获取房间状态失败')&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;const fetchRevenue = async () => {&ndash;&gt;-->
<!--&lt;!&ndash;  try {&ndash;&gt;-->
<!--&lt;!&ndash;    const res = await axios.get('/api/bookings/revenue?days=7')&ndash;&gt;-->
<!--&lt;!&ndash;    initRevenueChart(res.data.data)&ndash;&gt;-->
<!--&lt;!&ndash;  } catch (e) {&ndash;&gt;-->
<!--&lt;!&ndash;    ElMessage.error('获取营业额数据失败')&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;const initRoomStatusChart = (data) => {&ndash;&gt;-->
<!--&lt;!&ndash;  if (!roomChartInstance) {&ndash;&gt;-->
<!--&lt;!&ndash;    roomChartInstance = echarts.init(roomStatusChart.value)&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->

<!--&lt;!&ndash;  const option = {&ndash;&gt;-->
<!--&lt;!&ndash;    tooltip: {&ndash;&gt;-->
<!--&lt;!&ndash;      trigger: 'item',&ndash;&gt;-->
<!--&lt;!&ndash;      formatter: '{b}: {c} ({d}%)'&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    legend: {&ndash;&gt;-->
<!--&lt;!&ndash;      orient: 'vertical',&ndash;&gt;-->
<!--&lt;!&ndash;      left: 'left'&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    series: [&ndash;&gt;-->
<!--&lt;!&ndash;      {&ndash;&gt;-->
<!--&lt;!&ndash;        name: '房间状态',&ndash;&gt;-->
<!--&lt;!&ndash;        type: 'pie',&ndash;&gt;-->
<!--&lt;!&ndash;        radius: ['40%', '70%'],&ndash;&gt;-->
<!--&lt;!&ndash;        avoidLabelOverlap: false,&ndash;&gt;-->
<!--&lt;!&ndash;        itemStyle: {&ndash;&gt;-->
<!--&lt;!&ndash;          borderRadius: 10,&ndash;&gt;-->
<!--&lt;!&ndash;          borderColor: '#fff',&ndash;&gt;-->
<!--&lt;!&ndash;          borderWidth: 2&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        label: {&ndash;&gt;-->
<!--&lt;!&ndash;          show: true,&ndash;&gt;-->
<!--&lt;!&ndash;          formatter: '{b}: {c}间'&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        emphasis: {&ndash;&gt;-->
<!--&lt;!&ndash;          label: {&ndash;&gt;-->
<!--&lt;!&ndash;            show: true,&ndash;&gt;-->
<!--&lt;!&ndash;            fontSize: 16,&ndash;&gt;-->
<!--&lt;!&ndash;            fontWeight: 'bold'&ndash;&gt;-->
<!--&lt;!&ndash;          }&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        data: [&ndash;&gt;-->
<!--&lt;!&ndash;          { value: data.available || 0, name: '空闲', itemStyle: { color: '#67C23A' } },&ndash;&gt;-->
<!--&lt;!&ndash;          { value: data.booked || 0, name: '已预订', itemStyle: { color: '#E6A23C' } },&ndash;&gt;-->
<!--&lt;!&ndash;          { value: data.occupied || 0, name: '已入住', itemStyle: { color: '#409EFF' } }&ndash;&gt;-->
<!--&lt;!&ndash;        ]&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    ]&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->

<!--&lt;!&ndash;  roomChartInstance.setOption(option)&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;const initRevenueChart = (data) => {&ndash;&gt;-->
<!--&lt;!&ndash;  if (!revenueChartInstance) {&ndash;&gt;-->
<!--&lt;!&ndash;    revenueChartInstance = echarts.init(revenueChart.value)&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->

<!--&lt;!&ndash;  const dates = data.map(item => item.date)&ndash;&gt;-->
<!--&lt;!&ndash;  const revenues = data.map(item => item.revenue || 0)&ndash;&gt;-->

<!--&lt;!&ndash;  const option = {&ndash;&gt;-->
<!--&lt;!&ndash;    tooltip: {&ndash;&gt;-->
<!--&lt;!&ndash;      trigger: 'axis',&ndash;&gt;-->
<!--&lt;!&ndash;      formatter: '{b}<br/>营业额: ¥{c}'&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    grid: {&ndash;&gt;-->
<!--&lt;!&ndash;      left: '3%',&ndash;&gt;-->
<!--&lt;!&ndash;      right: '4%',&ndash;&gt;-->
<!--&lt;!&ndash;      bottom: '3%',&ndash;&gt;-->
<!--&lt;!&ndash;      containLabel: true&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    xAxis: {&ndash;&gt;-->
<!--&lt;!&ndash;      type: 'category',&ndash;&gt;-->
<!--&lt;!&ndash;      boundaryGap: false,&ndash;&gt;-->
<!--&lt;!&ndash;      data: dates,&ndash;&gt;-->
<!--&lt;!&ndash;      axisLabel: {&ndash;&gt;-->
<!--&lt;!&ndash;        rotate: 45&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    yAxis: {&ndash;&gt;-->
<!--&lt;!&ndash;      type: 'value',&ndash;&gt;-->
<!--&lt;!&ndash;      name: '营业额 (元)',&ndash;&gt;-->
<!--&lt;!&ndash;      axisLabel: {&ndash;&gt;-->
<!--&lt;!&ndash;        formatter: '¥{value}'&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    },&ndash;&gt;-->
<!--&lt;!&ndash;    series: [&ndash;&gt;-->
<!--&lt;!&ndash;      {&ndash;&gt;-->
<!--&lt;!&ndash;        name: '营业额',&ndash;&gt;-->
<!--&lt;!&ndash;        type: 'line',&ndash;&gt;-->
<!--&lt;!&ndash;        smooth: true,&ndash;&gt;-->
<!--&lt;!&ndash;        data: revenues,&ndash;&gt;-->
<!--&lt;!&ndash;        areaStyle: {&ndash;&gt;-->
<!--&lt;!&ndash;          color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [&ndash;&gt;-->
<!--&lt;!&ndash;            { offset: 0, color: 'rgba(64, 158, 255, 0.5)' },&ndash;&gt;-->
<!--&lt;!&ndash;            { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }&ndash;&gt;-->
<!--&lt;!&ndash;          ])&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        itemStyle: {&ndash;&gt;-->
<!--&lt;!&ndash;          color: '#409EFF'&ndash;&gt;-->
<!--&lt;!&ndash;        },&ndash;&gt;-->
<!--&lt;!&ndash;        lineStyle: {&ndash;&gt;-->
<!--&lt;!&ndash;          width: 3&ndash;&gt;-->
<!--&lt;!&ndash;        }&ndash;&gt;-->
<!--&lt;!&ndash;      }&ndash;&gt;-->
<!--&lt;!&ndash;    ]&ndash;&gt;-->
<!--&lt;!&ndash;  }&ndash;&gt;-->

<!--&lt;!&ndash;  revenueChartInstance.setOption(option)&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;onMounted(() => {&ndash;&gt;-->
<!--&lt;!&ndash;  fetchRoomStatus()&ndash;&gt;-->
<!--&lt;!&ndash;  fetchRevenue()&ndash;&gt;-->

<!--&lt;!&ndash;  window.addEventListener('resize', () => {&ndash;&gt;-->
<!--&lt;!&ndash;    roomChartInstance?.resize()&ndash;&gt;-->
<!--&lt;!&ndash;    revenueChartInstance?.resize()&ndash;&gt;-->
<!--&lt;!&ndash;  })&ndash;&gt;-->
<!--&lt;!&ndash;})&ndash;&gt;-->

<!--&lt;!&ndash;onBeforeUnmount(() => {&ndash;&gt;-->
<!--&lt;!&ndash;  roomChartInstance?.dispose()&ndash;&gt;-->
<!--&lt;!&ndash;  revenueChartInstance?.dispose()&ndash;&gt;-->
<!--&lt;!&ndash;  window.removeEventListener('resize', () => {&ndash;&gt;-->
<!--&lt;!&ndash;    roomChartInstance?.resize()&ndash;&gt;-->
<!--&lt;!&ndash;    revenueChartInstance?.resize()&ndash;&gt;-->
<!--&lt;!&ndash;  })&ndash;&gt;-->
<!--&lt;!&ndash;})&ndash;&gt;-->
<!--&lt;!&ndash;</script>&ndash;&gt;-->

<!--&lt;!&ndash;<style scoped>&ndash;&gt;-->
<!--&lt;!&ndash;.dashboard-container {&ndash;&gt;-->
<!--&lt;!&ndash;  padding: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.chart-card {&ndash;&gt;-->
<!--&lt;!&ndash;  height: 400px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.card-header {&ndash;&gt;-->
<!--&lt;!&ndash;  font-weight: bold;&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 16px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.chart {&ndash;&gt;-->
<!--&lt;!&ndash;  width: 100%;&ndash;&gt;-->
<!--&lt;!&ndash;  height: 320px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.stat-card {&ndash;&gt;-->
<!--&lt;!&ndash;  height: 120px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.stat-content {&ndash;&gt;-->
<!--&lt;!&ndash;  display: flex;&ndash;&gt;-->
<!--&lt;!&ndash;  align-items: center;&ndash;&gt;-->
<!--&lt;!&ndash;  height: 100%;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.stat-icon {&ndash;&gt;-->
<!--&lt;!&ndash;  width: 60px;&ndash;&gt;-->
<!--&lt;!&ndash;  height: 60px;&ndash;&gt;-->
<!--&lt;!&ndash;  border-radius: 12px;&ndash;&gt;-->
<!--&lt;!&ndash;  display: flex;&ndash;&gt;-->
<!--&lt;!&ndash;  align-items: center;&ndash;&gt;-->
<!--&lt;!&ndash;  justify-content: center;&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 28px;&ndash;&gt;-->
<!--&lt;!&ndash;  margin-right: 20px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.stat-info {&ndash;&gt;-->
<!--&lt;!&ndash;  flex: 1;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.stat-value {&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 32px;&ndash;&gt;-->
<!--&lt;!&ndash;  font-weight: bold;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #303133;&ndash;&gt;-->
<!--&lt;!&ndash;  line-height: 1;&ndash;&gt;-->
<!--&lt;!&ndash;  margin-bottom: 8px;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->

<!--&lt;!&ndash;.stat-label {&ndash;&gt;-->
<!--&lt;!&ndash;  font-size: 14px;&ndash;&gt;-->
<!--&lt;!&ndash;  color: #909399;&ndash;&gt;-->
<!--&lt;!&ndash;}&ndash;&gt;-->
<!--&lt;!&ndash;</style>&ndash;&gt;-->
