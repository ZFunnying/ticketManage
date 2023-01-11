<template>
  <div class="container">
    <el-card class="table-container">
      <div style="margin: 0 10px 20px 10px">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item
            label="编号"
            prop="id"
          >
            <el-col :span="22">
              <el-input
                v-model="searchForm.id"
                :rows="5"
                clearable
                type="text"
                placeholder="请输入编号"
              />
            </el-col>
          </el-form-item>
          <el-form-item
            label="接口"
            prop="url"
            label-width="80px"
          >
            <el-col :span="22">
              <el-input
                v-model="searchForm.url"
                :rows="5"
                clearable
                type="text"
                placeholder="请输入接口"
              />
            </el-col>
          </el-form-item>
          <el-form-item
            label="权限"
            prop="roles"
            label-width="80px"
          >
            <el-col :span="22">
              <el-input
                v-model="searchForm.roles"
                :rows="5"
                clearable
                type="text"
                placeholder="请输入权限"
              />
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
          key="url"
          show-overflow-tooltip
          prop="url"
          label="接口"
        />
        <el-table-column
          key="roles"
          show-overflow-tooltip
          prop="roles"
          label="权限"
        />
        <el-table-column
          key="remark"
          show-overflow-tooltip
          prop="remark"
          label="备注"
        />
        <!-- 表格操作列 -->
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                title="编辑"
                type="primary"
                @click="handleEdit(scope.row)"
              >
                <i class="fa fa-pencil" />
              </el-button>
              <el-button
                size="mini"
                title="删除"
                type="danger"
                @click="handleDel(scope.row)"
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

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="mode==='add'?'新增':'编辑'"
      :visible.sync="showEditDialog"
      :before-close="handleDialogClose"
    >
      <permission-edit
        ref="editForm"
        :mode="mode"
        :record="currentRecord"
        @after-edit="handleAfterEdit"
      />
    </el-dialog>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import PermissionApi from '../api/permission'
import permissionEdit from './permissionEdit'

export default {
  components: {
    permissionEdit
  },
  data() {
    return {
      /* 搜索请求数据*/
      searchForm: {
      },
      pageSize: 10,
      pageNum: 1,
      // 表格数据
      tableData: {
        results: []
      },
      mode: 'edit',
      // 是否显示添加/编辑对话框
      showEditDialog: false,
      queryLoading: false,
      // 当前处理行
      currentRecord: {}
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
        PermissionApi
          .queryByCond(param, this.pageSize, this.pageNum)
          .then(response => {
            const data = response.data
            this.queryLoading = false
            if (data.code === 200) {
              const data = response.data.data
              this.tableData = data
            } else {
              Message.error(data.message)
              reject()
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
      this.mode = 'add'
      this.currentRecord = {}
      this.showEditDialog = true
    },
    handleDetail(row) {
      this.mode = 'details'
      this.currentRecord = Object.assign({}, row)
      this.showEditDialog = true
    },
    handleEdit(row) {
      this.mode = 'edit'
      this.currentRecord = Object.assign({}, row)
      this.showEditDialog = true
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
    // 处理对话框关闭事件
    handleDialogClose(done) {
      this.$refs.editForm.resetFields()
      done()
    },
    handleDel(row) {
      this.$confirm('确定删除吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const param = Object.assign({}, row)
          PermissionApi
            .delete(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                PermissionApi.updatePermission().then(rs => {
                  if (rs.data.code === 200) {
                    this.$message({
                      message: '删除成功',
                      type: 'success'
                    })
                  }
                })
                this.handleQuery()
              } else {
                Message.error(data.message)
              }
            })
            .catch(error => {
              console.log(error)
              if (error.message !== '') {
                Message.error(error.message)
              }
            })
        })
        .catch(() => {
        })
    },
    // 处理编辑对话框关闭事件
    handleAfterEdit() {
      this.showEditDialog = false
      this.$refs.editForm.resetFields()
      this.query()
    }
  }
}
</script>
<style>
    .container {
        margin: 10px;
    }

    .table-container {
        margin-top: 0px;
    }
    .pagination-container {
        margin-top: 30px;
        text-align: right;
    }
</style>

