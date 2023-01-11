<template>
  <div>
    <el-form
      ref="editForm"
      :model="record"
      :rules="formRules"
    >
      <el-form-item
        label="编码"
        prop="groupCode"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input
            v-model="record.groupCode"
            disabled
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label="名称"
        prop="itemName"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input v-model="record.itemName" />
        </el-col>
      </el-form-item>
      <el-form-item
        label="值"
        prop="itemCode"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input v-model="record.itemCode" />
        </el-col>
      </el-form-item>
      <el-form-item
        label="排序"
        prop="sort"
        label-width="60px"
      >
        <el-col :span="22">
          <el-input-number
            v-model="record.sort"
            controls-position="right"
            class="ef-input-number"
          />
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
import GroupItemApi from '../api/groupItem'
export default {
  name: 'GroupItemEdit',
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
        itemName: [{ required: true, message: '请输入名称', trigger: 'blur' }],
        itemCode: [{ required: true, message: '请输入值', trigger: 'blur' }]
      },
      submitLoading: false
    }
  },
  methods: {
    handleSubmit() {
      const { gid, groupCode } = this.record
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          this.submitLoading = true
          if (this.mode === 'add') {
            GroupItemApi.add(this.record).then(rsp => {
              if (rsp.data.code === 200) {
                this.$emit('after-edit', { gid, groupCode })
              } else {
                this.$message.error(rsp.data.message)
              }
              this.submitLoading = false
            })
          } else {
            GroupItemApi.update(this.record).then(rsp => {
              if (rsp.data.code === 200) {
                this.$emit('after-edit', { gid, groupCode })
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
