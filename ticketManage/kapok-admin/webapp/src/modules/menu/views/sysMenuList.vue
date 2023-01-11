<template>
  <div class="container">
    <!-- 数据表格 -->
    <el-card>
      <div slot="header">
        <span style="line-height: 36px;">菜单管理</span>
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
        v-loading="queryLoading"
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
            > 编辑
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
      :close-on-click-modal="false"
      :title="mode==='add'?'添加菜单':'编辑菜单'"
      :visible.sync="showDialog"
      :before-close="handleDialogClose"
    >
      <sys-menu-edit
        v-if="showDialog"
        ref="editForm"
        :record="currentRecord"
        :mode="mode"
        @after-edit="afterEdit"
      />
    </el-dialog>

  </div>

</template>

<script>
import sysMenuEdit from '@/modules/menu/views/sysMenuEdit'
import MenuApi from '@/modules/menu/api/menu-api'

import router from '@/router'
import store from '@/store'
// import { changeToComponent } from '@/utils/replaceUtil'
// import Layout from '@/views/layout/Layout'

export default {
  name: 'SysMenuList',
  components: {
    sysMenuEdit
  },
  data() {
    return {
      mode: 'edit',
      treePageData: [],
      currentRecord: {},
      showDialog: false,
      queryLoading: false,
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
    this.addRoutes()
  },
  methods: {
    // 获取菜单树初始数据
    handleQuery() {
      //      this.axios.get('static/json-data/menudemo/treeTable.json').then((response) => {
      //        this.treePageData = response.data
      //      }).catch((response) => {
      //        console.log(response)
      //      })
      this.queryLoading = true
      MenuApi.getParentNode().then(response => {
        const data = response.data
        if (data.code === 200) {
          this.treePageData = data.data
        } else {
          this.$message({
            message: data.message,
            type: 'danger'
          })
        }
        this.queryLoading = false
      })
    },
    // 动态更新路由表
    addRoutes() {
      // 插入路由表
      store.dispatch('user/getInfo').then(res => {
        // 拉取user_info
        const roles = res.data.data.roles
        store.dispatch('permission/generateRoutes', roles).then(() => {
          // 根据roles权限生成可访问的路由表
          router.addRoutes(store.getters.addRoutes)
        })
      })
    },
    handleExpandChange(record, expanded, resolve) {
      if (expanded) {
        MenuApi.getChildrenNode(record.id).then(response => {
          const data = response.data
          if (data.code === 200) {
            // console.log(data.data)
            // this.$set(record, 'children', data.data)
            resolve(data.data)
          }
        })
      }
    },
    afterEdit(row) {
      this.showDialog = false
      this.handleQuery()
      this.addRoutes()
      if (row.pid) {
        MenuApi.getChildrenNode(row.pid).then(response => {
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
      this.currentRecord = { isshow: true, component: 'Layout', name: '' }
    },
    handleAddChild(record) {
      this.mode = 'add'
      this.showDialog = true
      this.currentRecord = { parentId: record.id, isShow: true, noCache: false, breadcrumb: true, affix: false, component: '', name: '' }
    },
    handleEdit(record) {
      this.mode = 'edit'
      this.showDialog = true
      this.currentRecord = Object.assign({}, record,
        { parent: null,
          parentId: record.parentId,
          affix: record.meta.affix,
          noCache: record.meta.noCache,
          breadcrumb: record.meta.breadcrumb })
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
            const param = Object.assign({}, record)
            MenuApi.delete(param).then(response => {
              const data = response.data
              if (data.code === 200) {
                self.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                self.handleQuery()
                self.addRoutes()
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
            MenuApi.delete(param).then(response => {
              const data = response.data
              if (data.code === 200) {
                self.$message({
                  type: 'success',
                  message: '删除成功!'
                })
                self.handleQuery()
                self.addRoutes()
                if (record.parentId) {
                  MenuApi.getChildrenNode(record.parentId).then(response => {
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
      // this.currentRecord = {}
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

