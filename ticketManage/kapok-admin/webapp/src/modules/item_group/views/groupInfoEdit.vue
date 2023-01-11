<template>
  <div>
    <el-form
      ref="editForm"
      :model="record"
      :rules="formRules"
    >
      <el-form-item
        label="ID"
        prop="id"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input
            v-model="record.id"
            disabled
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label="名称"
        prop="groupName"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input v-model="record.groupName" />
        </el-col>
      </el-form-item>
      <el-form-item
        label="编码"
        prop="groupCode"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input v-model="record.groupCode" />
        </el-col>
      </el-form-item>
      <el-form-item
        label="排序"
        prop="sort"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input v-model="record.sort" />
        </el-col>
      </el-form-item>
      <el-form-item
        label="备注"
        prop="remarks"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input v-model="record.remark" />
        </el-col>
      </el-form-item>
      <el-form-item align="center">
        <el-col :span="24">
          <el-button
            type="primary"
            :loading="submitLoading"
            @click.native.prevent="handleSubmit"
          >
            <i class="fa fa-save" /> 保存</el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import GroupInfoApi from '../api/groupInfo'
export default {
  name: 'GroupInfoEdit',
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
      formRules: {
        groupName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        groupCode: [{ required: true, message: '请输入编码', trigger: 'blur' }]
      },
      submitLoading: false
    }
  },
  methods: {
    handleSubmit() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.mode === 'add') {
            GroupInfoApi.add(this.record).then(rsp => {
              if (rsp.data.code === 200) {
                this.$emit('after-edit')
              } else {
                this.$message.error(rsp.data.message)
              }
              this.submitLoading = false
            })
          } else {
            GroupInfoApi.update(this.record).then(rsp => {
              if (rsp.data.code === 200) {
                this.$emit('after-edit')
              } else {
                this.$message.error(rsp.data.message)
              }
              this.submitLoading = false
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
