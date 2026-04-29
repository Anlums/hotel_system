<template>
  <el-container class="layout-container">
    <el-aside width="220px">
      <el-menu default-active="1" class="side-menu">
        <h3 class="logo-title">🏨 酒店后台系统</h3>
        <el-menu-item index="1">📊 数据概览</el-menu-item>
        <el-menu-item index="2">🛏️ 房间管理</el-menu-item>
        <el-menu-item index="3">📋 订单管理</el-menu-item>
        <el-menu-item index="4">🤖 AI 日志分析</el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">订单管理控制台</div>
        <div class="header-right">
          <el-avatar size="small" src="https://github.com/Anlums.png" />
          <span class="user-name">Anlums</span>
        </div>
      </el-header>

      <el-main>
        <div class="content-box">
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>