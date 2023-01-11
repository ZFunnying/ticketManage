<template>
  <div class="container">
    <!-- 数据表格 -->
    <el-card>
      <div slot="header">
        <span style="line-height: 36px;">移动端菜单管理</span>
        <el-button
          type="primary"
          style="float: right;"
          @click="handleAdd"
        >
          <i class="fa fa-plus" /> 添加
        </el-button>
      </div>

      <el-table
        ref="treeTable"
        :columns="columns"
        :data="treePageData"
        :load="handleExpandChange"
        row-key="id"
        lazy
      >
        <el-table-column
          key="id"
          prop="id"
          label="ID"
        />
        <el-table-column
          key="title"
          prop="title"
          label="标题"
        />
        <el-table-column
          key="path"
          prop="path"
          label="链接"
        />
        <el-table-column
          key="component"
          prop="component"
          label="文件路径"
        />
        <el-table-column
          key="sort"
          prop="sort"
          label="排序"
        />
        <el-table-column
          key="isShowFlag"
          prop="isShowFlag"
          label="是否显示"
        />
        <!-- 操作列 -->
        <el-table-column
          header-align="center"
          align="center"
          width="200"
          label="操作"
        >
          <template slot-scope="scope">
            <el-dropdown
              split-button
              size="small"
              type="primary"
              @click="handleEdit(scope.row)"
            >
              编辑
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item><span @click="handleAddChild(scope.row)"><i class="fa fa-indent" /> 添加下级</span></el-dropdown-item>
                <!-- <el-dropdown-item><span @click="handleMenuPermission(scope.row)"><i class="fa fa-th-large"></i> 功能权限</span></el-dropdown-item> -->
                <el-dropdown-item><span @click="handleDelete(scope.row)"><i class="fa fa-trash-o" /> 删除菜单</span></el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="mode==='add'?'添加菜单':'编辑菜单'"
      :visible.sync="showDialog"
      :close-on-click-modal="false"
    >
      <h5-menu-edit
        ref="editForm"
        :record="currentRecord"
        :mode="mode"
        @after-edit="afterEdit"
      />
    </el-dialog>

  </div>

</template>

<script>
import h5MenuEdit from '@/modules/menu/views/h5MenuEdit'
import H5MenuApi from '@/modules/menu/api/h5Menu'
export default {
  name: 'H5MenuList',
  components: {
    h5MenuEdit
  },
  data() {
    return {
      mode: 'edit',
      treePageData: [],
      currentRecord: {},
      showDialog: false,
      columns: [
        {
          value: 'name',
          text: '名称'
        }
      ]
    }
  },
  mounted() {
    this.handleQuery()
  },
  methods: {
    // 获取菜单树初始数据
    handleQuery() {
      H5MenuApi.getParentNode().then(response => {
        const data = response.data
        if (data.code === 200) {
          this.treePageData = data.data
        } else {
          this.$message({
            message: data.message,
            type: 'danger'
          })
        }
      })
    },

    handleExpandChange(record, expanded, resolve) {
      if (expanded) {
        H5MenuApi.getChildrenNode(record.id).then(response => {
          const data = response.data
          if (data.code === 200) {
            resolve(data.data)
          }
        })
      }
    },
    afterEdit(row) {
      this.showDialog = false
      this.handleQuery()
      if (row.pid) {
        H5MenuApi.getChildrenNode(row.pid).then(response => {
          const data = response.data
          if (data.code === 200) {
            this.$set(this.$refs.treeTable.store.states.lazyTreeNodeMap, row.pid, data.data)
          }
        })
      }
    },
    handleAdd() {
      this.mode = 'add'
      this.showDialog = true
      this.currentRecord = { isshow: 1 }
    },
    handleAddChild(record) {
      this.mode = 'add'
      this.showDialog = true
      this.currentRecord = { parentId: record.id, isshow: 1 }
    },
    handleEdit(record) {
      this.mode = 'edit'
      this.showDialog = true
      this.currentRecord = Object.assign({}, record, { parent: null, parentId: record.parentId })
    },
    handleDelete(record) {
      const self = this
      if (record.hasChildren) {
        this.$confirm('确定删除此菜单及其下面的子菜单吗？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            const param = Object.assign({}, { id: record.id })
            H5MenuApi.delete(param).then(response => {
              const data = response.data
              if (data.code === 200) {
                self.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                self.handleQuery()
              } else {
                self.$message({
                  type: 'danger',
                  message: data.message
                })
              }
            })
          })
          .catch(() => {})
      } else {
        this.$confirm('确定删除此菜单吗？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            const param = Object.assign({}, record)
            H5MenuApi.delete(param).then(response => {
              const data = response.data
              if (data.code === 200) {
                self.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                self.handleQuery()
                if (record.parentId) {
                  H5MenuApi.getChildrenNode(record.parentId).then(response => {
                    const data = response.data
                    if (data.code === 200) {
                      self.$set(self.$refs.treeTable.store.states.lazyTreeNodeMap, record.parentId, data.data)
                    }
                  })
                }
              } else {
                self.$message({
                  type: 'danger',
                  message: data.message
                })
              }
            })
          })
          .catch(() => {})
      }
    },
    handleDialogClose(done) {
      this.currentRecord = {}
      this.$refs.editForm.resetFields()
      done()
    }
  }
}
</script>
<style>
  .container {
    margin: 10px;
  }
</style>

