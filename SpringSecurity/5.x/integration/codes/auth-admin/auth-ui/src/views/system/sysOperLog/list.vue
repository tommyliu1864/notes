<template>
  <div class="app-container">

    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="用户名">
              <el-input style="width: 95%" v-model="searchObj.username" placeholder="用户名"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作时间">
              <el-date-picker
                v-model="createTimes"
                type="datetimerange"
                range-separator="至"
                start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="margin-right: 10px;width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini"  @click="fetchData()" :disabled="$hasBP('bnt.sysUser.list')  === false">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        </el-row>
      </el-form>
    </div>

	<!-- 列表 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%;margin-top: 10px;">

      <el-table-column
        label="序号"
        width="70"
        align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="username" label="用户名" width="120"/>
      <el-table-column prop="module" label="业务模块" width="150"/>
      <el-table-column prop="title" label="功能" width="180"/>
      <el-table-column prop="method" label="方法名称" width="400"/>
      <el-table-column prop="ipaddr" label="IP地址" width="120"/>   
      <el-table-column label="执行状态" width="80">
        <template slot-scope="scope">
            <span v-if="scope.row.status === 1">成功</span>
            <span v-else>失败</span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="操作时间" width="180"/>      
      <el-table-column label="操作"  align="center" fixed="right">
        <template slot-scope="scope">
          <el-link type="info" @click="showDetails(scope.row)">详情<i class="el-icon-view el-icon--right"></i></el-link>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      :current-page="page"
      :total="total"
      :page-size="limit"
      style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper"
      @current-change="fetchData"
    />

    <el-dialog title="详情" :visible.sync="dialogVisible">
      <el-row>
        <el-col :span="2"><strong>操作模块</strong></el-col>
        <el-col :span="10">{{ sysOperLog.module }}</el-col>
        <el-col :span="2"><strong>功能名称</strong></el-col>
        <el-col :span="10">{{ sysOperLog.title }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="2"><strong>方法名称</strong></el-col>
        <el-col :span="10">{{ sysOperLog.method }}</el-col>
        <el-col :span="2"><strong>用户名</strong></el-col>
        <el-col :span="10">{{ sysOperLog.username }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="2"><strong>请求参数</strong></el-col>
        <el-col :span="22">{{ sysOperLog.param }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="2"><strong>返回结果</strong></el-col>
        <el-col :span="22">{{ sysOperLog.result }}</el-col>
      </el-row>
      <el-row>
        <el-col :span="2"><strong>执行状态</strong></el-col>
        <el-col :span="22" v-if="sysOperLog.status === '1'">成功</el-col>
        <el-col :span="22">
          <span v-if="sysOperLog.status === 1">操作成功</span>
          <span v-if="sysOperLog.status === 0">操作失败</span>
        </el-col>        
      </el-row>
      <el-row>
        <el-col :span="2"><strong>错误消息</strong></el-col>
        <el-col :span="22">{{ sysOperLog.errorMsg }}</el-col>
      </el-row>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<style>
  .el-row {
    margin-bottom: 30px;
  }
</style>
<script>

import api from '@/api/system/sysOperLog'

export default {
  data() {
    return {
      listLoading: true, // 数据是否正在加载
      list: null, // banner列表
      total: 0, // 数据库中的总记录数
      page: 1, // 默认页码
      limit: 10, // 每页记录数
      searchObj: {}, // 查询表单对象

      createTimes: [],
      sysOperLog: {},
      dialogVisible: false  // 详情对话框
    }
  },

  // 生命周期函数：内存准备完毕，页面尚未渲染
  created() {
    this.fetchData()
  },

  // 生命周期函数：内存准备完毕，页面渲染成功
  mounted() {
  },

  methods: {
    // 加载banner列表数据
    fetchData(page = 1) {
      this.page = page
      if(this.createTimes && this.createTimes.length ==2) {
        this.searchObj.createTimeBegin = this.createTimes[0]
        this.searchObj.createTimeEnd = this.createTimes[1]
      }

      api.getPageList(this.page, this.limit, this.searchObj).then(
        response => {
          //this.list = response.data.list
          this.list = response.data.records
          this.total = response.data.total

          // 数据加载并绑定成功
          this.listLoading = false
        }
      )
    },

    // 重置查询表单
    resetData() {
      this.searchObj = {}
      this.createTimes = []
      this.fetchData()
    },

    showDetails(row) {
      this.dialogVisible = true;
      this.sysOperLog = row;
    }
  }
}
</script>

