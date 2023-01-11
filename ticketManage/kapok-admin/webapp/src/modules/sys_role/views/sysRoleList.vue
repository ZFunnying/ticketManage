<template>
  <div class="container">
    <el-card class="table-container">
      <div style="margin: 0 10px 20px 10px">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item
            label="角色"
            prop="role"
            label-width="50px"
          >
            <el-col :span="22">
              <el-input v-model="searchForm.role" :rows="5" clearable type="text" placeholder="请输入角色名" />
            </el-col>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="queryLoading" @click="handleQuery">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleAdd">新增</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        v-loading="queryLoading"
        :data="tableData.list"
        :header-row-class-name="'table-head-th'"
        row-key="id"
        fit
        highlight-current-row
      >
        <el-table-column
          key="id"
          show-overflow-tooltip
          prop="id"
          label="编号"
        />
        <el-table-column
          key="role"
          show-overflow-tooltip
          prop="role"
          label="角色"
        />
        <el-table-column
          key="description"
          show-overflow-tooltip
          prop="description"
          label="角色描述"
        />
        <!-- 表格操作列 -->
        <el-table-column label="操作" fixed="right" width="150">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                title="编辑"
                type="primary"
                @click="handleEdit(scope.$index, scope.row)"
              >
                <i class="fa fa-pencil" />
              </el-button>
              <el-button
                size="mini"
                title="删除"
                type="danger"
                @click="handleDel(scope.$index, scope.row)"
              >
                <i class="fa fa-trash-o" />
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页信息 -->
      <div class="pagination-container">
        <el-pagination
          :current-page="pageNum"
          :total="tableData.total"
          background
          layout="total, prev, pager, next, jumper"
          @current-change="handlePageChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>

    <!-- 详情弹窗 -->
    <el-dialog :visible.sync="dialogFormVisible" :title="dialogType=='see'?'查看详情':'编辑'">
      <el-form :model="dialogForm" :rules="formRules">
        <el-form-item key="id" label="编号" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['id']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
          </el-col>
        </el-form-item>
        <el-form-item prop="role" label="角色" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['role']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
          </el-col>
        </el-form-item>
        <el-form-item prop="description" label="角色描述" label-width="100px">
          <el-col :span="22">
            <el-input v-model="dialogForm['description']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="SubmitCancel">取 消</el-button>
        <el-button type="primary" @click="Submit">确 定</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import { Message } from 'element-ui'
import SysRoleApi from '../api/sysRole'
export default {
  data() {
    return {
      /* 搜索面板数据*/
      searchConfig: [],
      /* 搜索请求数据*/
      searchForm: {},
      groupsearchForm: {},
      /* 表格面板数据*/
      tableConfig: [],
      pageSize: 10,
      pageNum: 1,
      // 表格数据
      tableData: {
        results: []
      },
      queryLoading: false,
      groupTableData: {
        results: []
      },
      dialogForm: {},
      groupDialogForm: {
        provinceList: [],
        cityList: [],
        id: '',
        remark: '',
        groupName: ''

      },
      dialogType: 'see',
      groupDialogType: 'edit',
      dialogFormVisible: false,
      formLabelWidth: '120px',
      formRules: {
        role: [{ required: true, message: '请输入角色', trigger: 'blur' }],
        description: [{ required: true, message: '请输入描述', trigger: 'blur' }]
      },
      groupFormRules: {
        groupName: [{ required: true, message: '请输入组名', trigger: 'blur' }]
      },
      groupDialogVisible: false

    }
  },
  mounted() {
    this.init()
  },
  methods: {
    // 页面初始化
    init() {
      this.handleQuery()
    },
    handleQuery() {
      this.pageNum = 1
      this.query()
    },

    query() {
      this.queryLoading = true
      return new Promise((resolve, reject) => {
        const param = Object.assign({}, this.searchForm)
        SysRoleApi
          .queryByCond(param, this.pageSize, this.pageNum)
          .then(response => {
            const data = response.data
            this.queryLoading = false
            if (data.code === 200) {
              const data = response.data.data
              this.tableData = data
              resolve()
            } else {
              Message.error(data.message)
              reject(data.message)
            }
          })
          .catch(error => {
            this.queryLoading = false
            if (error.message !== '') {
              Message.error(error.message)
            }
            reject(error)
          })
      })
    },
    handleAdd() {
      this.dialogType = 'add'
      this.dialogForm = {}
      this.dialogFormVisible = true
    },
    handleSee(index, row) {
      this.dialogType = 'see'
      this.dialogForm = row
      this.dialogFormVisible = true
    },
    handleEdit(index, row) {
      this.dialogType = 'edit'
      this.dialogForm = row
      this.dialogFormVisible = true
    },
    handleDel(index, row) {
      return new Promise((resolve, reject) => {
        const param = Object.assign({}, row)
        SysRoleApi
          .delete(param)
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              this.$message({
                message: '删除成功',
                type: 'success'
              })
              this.query()
            } else {
              Message.error(data.message)
              reject()
            }
          })
          .catch(error => {
            if (error.message !== '') {
              Message.error(error.message)
            }

            reject(error)
          })
      })
    },
    SubmitCancel() {
      this.dialogFormVisible = false
      this.query()
    },
    // 处理翻页事件
    handlePageChange(currentPage) {
      this.pageNum = currentPage
      this.query()
    },
    // 处理页面总条数事件
    handleSizeChange(val) {
      // console.log(`每页  条`);
      this.pageSize = val
      this.query()
    },
    Submit() {
      if (this.dialogType === 'see') {
        this.dialogFormVisible = false
      } else if (this.dialogType === 'add') {
        this.dialogFormVisible = false
        return new Promise((resolve, reject) => {
          const param = Object.assign({}, this.dialogForm)
          SysRoleApi
            .add(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: '新增成功',
                  type: 'success'
                })
                this.query()
              } else {
                Message.error(data.message)
                reject()
              }
            })
            .catch(error => {
              if (error.message !== '') {
                Message.error(error.message)
              }

              reject(error)
            })
        })
      } else if (this.dialogType === 'edit') {
        this.dialogFormVisible = false
        return new Promise((resolve, reject) => {
          const param = Object.assign({}, this.dialogForm)
          SysRoleApi
            .update(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: '修改成功',
                  type: 'success'
                })
                this.query()
              } else {
                Message.error(data.message)
                reject()
              }
            })
            .catch(error => {
              if (error.message !== '') {
                Message.error(error.message)
              }

              reject(error)
            })
        })
      }
    }

  }
}
</script>
<style>
  .container{
    margin: 10px;
  }

  .table-container{
    margin-top: 0px;
  }
  .pagination-container {
    margin-top: 30px;
    text-align: right;
  }
</style>

