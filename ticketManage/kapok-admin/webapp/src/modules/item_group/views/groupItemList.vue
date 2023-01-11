<template>
  <div class="container">
    <el-card class="table-container">
      <div style="margin: 0 10px 20px 10px">
        <el-form :inline="true" :model="searchForm" class="demo-form-inline">
          <el-form-item
            label="组别"
            prop="groupName"
          >
            <el-col :span="22">
              <el-input v-model="searchForm.groupName" :rows="5" clearable type="text" placeholder="请输入组别" />
            </el-col>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :loading="queryLoading" @click="handleQueryGroupData">查询</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleAddGroupData">新增分组</el-button>
          </el-form-item>
        </el-form>
      </div>
      <!-- 数据表格 -->
      <el-table
        v-loading="queryLoading"
        :data="tableData.list"
        :header-row-class-name="'table-head-th'"
        row-key="id"
        fit
        highlight-current-row
        @expand-change="handleExpendRow"
      >
        <!-- 展开子表格 -->
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-table :data="props.row.items">
              <el-table-column
                prop="itemName"
                label="名称"
              />
              <el-table-column
                prop="itemCode"
                label="值"
              />
              <el-table-column
                prop="sort"
                label="排序"
              />
              <el-table-column
                prop="remark"
                show-overflow-tooltip
                label="备注"
              />
              <!-- 子表格操作列 -->
              <el-table-column
                header-align="center"
                align="center"
                width="160"
                label="操作"
              >
                <template slot-scope="scope">
                  <el-button-group>
                    <el-button
                      size="mini"
                      title="编辑"
                      type="primary"
                      @click="handleEditItem(scope.row)"
                    >
                      <i class="fa fa-pencil" />
                    </el-button>
                    <el-button
                      size="mini"
                      title="删除"
                      type="danger"
                      @click="handleDeleteItem(scope.row)"
                    >
                      <i class="fa fa-trash-o" />
                    </el-button>
                  </el-button-group>
                </template>
              </el-table-column>
            </el-table>
          </template>
        </el-table-column>
        <!-- 数据列 -->
        <el-table-column
          prop="groupName"
          label="名称"
        />
        <el-table-column
          prop="groupCode"
          label="编码"
        />
        <el-table-column
          prop="sort"
          label="排序"
        />
        <el-table-column
          prop="remark"
          show-overflow-tooltip
          label="备注"
        />
        <!-- 操作列 -->
        <el-table-column
          header-align="center"
          align="center"
          width="180"
          label="操作"
        >
          <template slot-scope="scope">
            <el-button-group>
              <el-button
                size="mini"
                title="添加字典"
                type="primary"
                @click="handleAddItem(scope.row)"
              >
                <i class="fa fa-plus" />
              </el-button>
              <el-button
                size="mini"
                title="编辑"
                type="primary"
                @click="handleEditGroup(scope.row)"
              >
                <i class="fa fa-pencil" />
              </el-button>
              <el-button
                size="mini"
                title="删除"
                type="danger"
                @click="handleDeleteGroup(scope.row)"
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
          :total="tableData.total"
          background
          layout="total, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>

    </el-card>
    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="showDialog"
      :before-close="handleDialogClose"
    >
      <!-- 字典编辑Form -->
      <group-item-edit
        v-if="dialogType === 'item'"
        ref="editForm"
        :mode="mode"
        :record="currentRecord"
        @after-edit="afterEditItem"
      />
      <!-- 字典组编辑Form -->
      <group-info-edit
        v-if="dialogType === 'itemGroup'"
        ref="editForm"
        :mode="mode"
        :record="currentRecord"
        @after-edit="afterEditItemGroup"
      />
    </el-dialog>
  </div>

</template>

<script>
import { Message } from 'element-ui'
import GroupItemApi from '../api/groupItem'
import GroupInfoApi from '../api/groupInfo'
import GroupInfoEdit from './groupInfoEdit'
import GroupItemEdit from './groupItemEdit'
export default {
  name: 'groupItemList',
  components: {
    GroupInfoEdit,
    GroupItemEdit
  },
  data() {
    return {
      tableData: {
        list: []
      },
      queryLoading: false,
      mode: 'edit', // or add
      dialogTitle: '',
      dialogType: 'itemGroup', // or dict
      currentRecord: {},
      showDialog: false,
      searchForm: {
        'name': ''
      },
      pageSize: 10,
      pageNum: 1
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    // 页面初始化
    init() {
      this.handleQueryGroupData()
    },
    handleQueryItemData(gid, groupCode) {
      GroupItemApi.queryAllByCode(groupCode).then(response => {
        const data = response.data
        if (data.code === 200) {
          const data = response.data.data
          const index = this.tableData.list.findIndex(data => data.id === gid)
          // 每个子元素塞进gid
          for (var i = 0, len = data.length; i < len; i++) {
            this.$set(data[i], 'gid', gid)
          }
          this.$set(this.tableData.list[index], 'items', data)
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
    },
    handleQueryGroupData() {
      const param = Object.assign({}, this.searchForm)
      this.queryLoading = true
      GroupInfoApi
        .queryByCond(param, this.pageSize, this.pageNum)
        .then(response => {
          const data = response.data
          if (data.code === 200) {
            const data = response.data.data
            this.tableData = data
          } else {
            Message.error(data.message)
          }
          this.queryLoading = false
        })
        .catch(error => {
          console.log(error)
          this.queryLoading = false
          if (error.message !== '') {
            Message.error(error.message)
          }
        })
    },
    handleAddGroupData() {
      this.mode = 'add'
      this.dialogTitle = '添加分组'
      this.dialogType = 'itemGroup'
      this.showDialog = true
      this.currentRecord = {}
    },
    handleExpendRow(row, expandedRows) {
      if (!row.items) {
        this.handleQueryItemData(row.id, row.groupCode)
      }
    },
    handleAddItem(row) {
      this.mode = 'add'
      this.dialogTitle = '添加项目'
      this.dialogType = 'item'
      this.showDialog = true
      this.currentRecord = { gid: row.id, groupCode: row.groupCode }
    },
    handleEditGroup(row) {
      this.mode = 'edit'
      this.dialogTitle = '编辑分组'
      this.dialogType = 'itemGroup'
      this.showDialog = true
      this.currentRecord = Object.assign({}, row)
    },
    handleDeleteGroup(row) {
      this.$confirm('确定删除吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const param = Object.assign({}, row)
          GroupInfoApi
            .delete(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                })
                this.handleQueryGroupData()
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
        .catch(() => {})
    },
    afterEditItem(row) {
      this.showDialog = false
      this.handleQueryItemData(row.gid, row.groupCode)
    },
    handleEditItem(row) {
      this.mode = 'edit'
      this.dialogTitle = '编辑项目'
      this.dialogType = 'item'
      this.showDialog = true
      this.currentRecord = Object.assign({}, row)
    },
    handleDeleteItem(row) {
      this.$confirm('确定删除吗？', '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          const param = Object.assign({}, row)
          GroupItemApi
            .delete(param)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                this.$message({
                  message: '删除成功',
                  type: 'success'
                })
                this.handleQueryItemData(row.gid, row.groupCode)
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
        .catch(() => {})
    },
    handleDialogClose(done) {
      this.$refs.editForm.resetFields()
      done()
    },
    afterEditItemGroup() {
      this.showDialog = false
      this.handleQueryGroupData()
    },
    // 处理翻页事件
    handlePageChange(currentPage) {
      this.pageNum = currentPage
      this.handleQueryGroupData()
    },
    // 处理页面总条数事件
    handleSizeChange(val) {
      this.pageSize = val
      this.handleQueryGroupData()
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
        GroupItemApi
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
    Submit() {
      if (this.dialogType === 'see') {
        this.dialogFormVisible = false
      } else if (this.dialogType === 'add') {
        this.dialogFormVisible = false
        return new Promise((resolve, reject) => {
          const param = Object.assign({}, this.dialogForm)
          GroupItemApi
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
          GroupItemApi
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

