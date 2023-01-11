<template>
  <div>
    <el-form
      ref="editForm"
      :model="currentRecord"
      :rules="formRules"
    >
      <el-form-item
        label="编号"
        prop="id"
        label-width="80px"
        hidden="hidden"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.id"
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
            v-model="currentRecord.url"
            :disabled="this.mode=='details'"
            placeholder="请输入接口"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label="备注"
        prop="remark"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.remark"
            :disabled="this.mode=='details'"
            placeholder="请输入备注"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label="权限"
        prop="checkedPart"
        label-width="80px"
      >
        <el-col :span="22" class="checkbox-group">
          <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
          <el-checkbox-group v-model="checkedPart" size="mini" @change="handlecheckedPartChange">
            <el-checkbox v-for="role in rolesOptions" :key="role['id']" :label="role['role']" name="checkedPart" class="checkboxContent" border>{{ role['description'] }}</el-checkbox>
          </el-checkbox-group>
        </el-col>
      </el-form-item>
      <el-form-item align="center">
        <el-col :span="24">
          <el-button
            type="primary"
            @click.native.prevent="handleSubmit"
          >
            <i class="fa fa-save" /> 保存
          </el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import PermissionApi from '../api/permission'
import SysRoleApi from '@/modules/sys_role/api/sysRole'
export default {
  name: 'PermissionEdit',
  props: {
    mode: {
      type: String
    },
    record: {
      type: Object
    }
  },
  data() {
    return {
      checkAll: false,
      isIndeterminate: true,
      currentRecord: this.record,
      rolesOptions: [],
      // 角色列表
      rolesList: [],
      checkedPart: [],
      formRules: { }
    }
  },
  watch: {
    record: function(oldValue, newValue) {
      this.currentRecord = this.record
      this.checkedPart = this.currentRecord.roles.split(',')
      // this.getDetail()
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    // 页面初始化
    init() {
      this.getAllRole()
      this.checkedPart = this.currentRecord.roles.split(',')
    },
    handleCheckAllChange(val) {
      this.checkedPart = val ? this.rolesList : []
      this.isIndeterminate = false
    },
    handlecheckedPartChange(value) {
      const checkedCount = value.length
      this.checkAll = checkedCount === this.rolesList.length
      this.isIndeterminate = checkedCount > 0 && checkedCount < this.rolesList.length
    },
    getAllRole() {
      return new Promise((resolve, reject) => {
        SysRoleApi
          .query()
          .then(response => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              this.rolesOptions = data.list
              for (var role in data.list) {
                this.rolesList.push(data.list[role].role)
              }
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
    handleSubmit() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          this.checkedPart = this.checkedPart.filter(function(e) { return e })
          this.currentRecord.roles = this.checkedPart.join(',')
          const newRecord = Object.assign({}, this.currentRecord)
          if (this.mode === 'add') {
            PermissionApi.add(newRecord).then(rspData => {
              if (rspData.data.code === 200) {
                PermissionApi.updatePermission().then(rs => {
                  if (rs.data.code === 200) {
                    Message.success('新增成功')
                  } else {
                    Message.error(rs.data.message)
                  }
                })
              } else {
                Message.error(rspData.data.message)
              }
              this.$emit('after-edit')
            })
          } else if (this.mode === 'details') {
            this.$emit('after-edit')
          } else {
            PermissionApi.update(newRecord).then(rspData => {
              if (rspData.data.code === 200) {
                PermissionApi.updatePermission().then(rs => {
                  if (rs.data.code === 200) {
                    Message.success('修改成功')
                  } else {
                    Message.error(rs.data.message)
                  }
                })
              } else {
                Message.error(rspData.data.message)
              }
              this.$emit('after-edit')
            })
          }
        } else {
          return false
        }
      })
    },
    resetFields() {
      this.$refs['editForm'].resetFields()
    }
  }
}
</script>
<style>
.checkbox-group .el-checkbox.is-bordered + .el-checkbox.is-bordered {
  margin-left: 0px;
}
</style>
