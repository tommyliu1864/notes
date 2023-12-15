<template>
  <div class="app-container">
    <!--查询表单-->
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名称">
              <el-input style="width: 100%" v-model="searchObj.roleName" placeholder="角色名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini"  @click="fetchData()" :disabled="$hasBP('bnt.sysRole.list')  === false">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        </el-row>
      </el-form>
    </div>
    <!-- 工具条 -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add" :disabled="$hasBP('bnt.sysRole.add') === false">添 加</el-button>
      <el-button class="btn-add" size="mini" @click="batchRemove()" :disabled="$hasBP('bnt.sysRole.remove')  === false">批量删除</el-button>
    </div>
    <!-- 表格 -->
    <el-table
      v-loading="listLoading"
      :data="list"
      stripe
      border
      style="width: 100%;margin-top: 10px;"
      @selection-change="handleSelectionChange">

      <el-table-column type="selection"/>

      <el-table-column
        label="序号"
        width="70"
        align="center">        
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column prop="roleName" label="角色名称" />
      <el-table-column prop="roleCode" label="角色编码" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="createTime" label="创建时间" width="160"/>
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row.id)" title="修改" :disabled="$hasBP('bnt.sysRole.update')  === false"/>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="删除" :disabled="$hasBP('bnt.sysRole.remove')  === false"/>
          <el-button type="warning" icon="el-icon-baseball" size="mini" @click="showAssignAuth(scope.row)" title="分配权限" :disabled="$hasBP('bnt.sysRole.assignAuth')  === false"/>
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

    <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%" >
      <el-form ref="dataForm" :model="sysRole" label-width="150px" size="small" style="padding-right: 40px;">
        <el-form-item label="角色名称">
          <el-input v-model="sysRole.roleName"/>
        </el-form-item>
        <el-form-item label="角色编码">
          <el-input v-model="sysRole.roleCode"/>
        </el-form-item>
        <el-form-item label="角色描述">
          <el-input v-model="sysRole.description"/>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>        
      </span>
    </el-dialog>
  </div>
  
</template>
<script>
import api from '@/api/system/sysRole'
export default {
  // 定义数据模型
  // 定义数据模型
  data() {
    return {
      listLoading:false, // 数据是否正在加载
      list: [], // 角色列表
      total: 0, // 总记录数
      page: 1, // 页码
      limit: 2, // 每页记录数
      searchObj: {}, // 查询条件

      dialogVisible: false,//弹出框
      sysRole:{}, //封装添加表单数据
      multipleSelection: [] // 批量删除选中的记录列表
    }
  },
  //页面渲染之前获取数据
  created() {
    this.fetchData()
  },
  // 定义方法
  methods: {    
    fetchData(pageNum=1) {
       this.page = pageNum
      // 调用api
      api.getPageList(this.page, this.limit, this.searchObj).then(response => {
        debugger
        this.listLoading = false
        this.list = response.data.records
        this.total = response.data.total
      })
    },
    // 重置表单
    resetData() {
        this.searchObj = {}
        this.fetchData()
    },
    // 根据id删除数据
    removeDataById(id) {
        // debugger
        this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
        }).then(() => { // promise
            // 点击确定，远程调用ajax
            return api.removeById(id)
        }).then((response) => {
            this.fetchData(this.page)
            this.$message.success(response.message || '删除成功')
        }).catch(() => {
            this.$message.info('取消删除')
        })
    },
    //弹出添加的表单
    add(){
      this.dialogVisible = true
      this.sysRole = {}
    },
    //添加或更新
    saveOrUpdate() {
      if (!this.sysRole.id) {
        this.save()
      } else {
        this.update()
      }
    },
    //添加
    save() {
      api.save(this.sysRole).then(response => {
        this.$message.success(response.message || '操作成功')
        this.dialogVisible = false
        this.fetchData(this.page)
      })
    },
    //编辑
    edit(id) {
      this.dialogVisible = true
      api.getById(id).then(response => {
        this.sysRole = response.data
      })
    },
    //更新
    update() {
      api.updateById(this.sysRole).then(response => {
        this.$message.success(response.message || '操作成功')
        this.dialogVisible = false
        this.fetchData(this.page)
      })
    },
    // 当多选选项发生变化的时候调用
    handleSelectionChange(selection) {
      console.log(selection)
      this.multipleSelection = selection
    },
    // 批量删除
    batchRemove() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的记录！')
        return
      }
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 点击确定，远程调用ajax
        // 遍历selection，将id取出放入id列表
        var idList = []
        this.multipleSelection.forEach(item => {
          idList.push(item.id)
        })
        // 调用api
        return api.batchRemove(idList)
      }).then((response) => {
        this.fetchData()
        this.$message.success(response.message)
      }).catch(error => {
        if (error === 'cancel') {
          this.$message.info('取消删除')
        }
      })
    },
    showAssignAuth(row) {
      this.$router.push('/system/assignAuth?id='+row.id+'&roleName='+row.roleName);
    }
  }
}
</script>