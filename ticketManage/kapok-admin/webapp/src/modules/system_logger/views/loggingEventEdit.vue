<template>
  <div>
    <el-form
      ref="editForm"
      :model="currentRecord"
      :rules="formRules"
    >
      <el-form-item
        label=""
        prop="timestmp"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.timestmp"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="formattedMessage"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.formattedMessage"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="loggerName"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.loggerName"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="levelString"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.levelString"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="threadName"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.threadName"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="referenceFlag"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.referenceFlag"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="arg0"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.arg0"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="arg1"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.arg1"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="arg2"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.arg2"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="arg3"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.arg3"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="callerFilename"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.callerFilename"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="callerClass"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.callerClass"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="callerMethod"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.callerMethod"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="callerLine"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.callerLine"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item
        label=""
        prop="eventId"
        label-width="80px"
      >
        <el-col :span="22">
          <el-input
            v-model="currentRecord.eventId"
            :disabled="this.mode=='details'"
            placeholder="������"
          />
        </el-col>
      </el-form-item>
      <el-form-item align="center">
        <el-col :span="24">
          <el-button
            type="primary"
            @click.native.prevent="handleSubmit"
          >
            <i class="fa fa-save" /> ����
          </el-button>
        </el-col>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { Message } from 'element-ui'
import LoggingEventApi from '../api/loggingEvent'

export default {
  name: 'LoggingEventEdit',
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
      currentRecord: this.record,
      formRules: {}
    }
  },
  watch: {
    record: function(oldValue, newValue) {
      this.currentRecord = this.record
      // this.getDetail()
    }
  },
  methods: {

    handleSubmit() {
      this.$refs['editForm'].validate(valid => {
        if (valid) {
          const newRecord = Object.assign({}, this.currentRecord)
          if (this.mode === 'add') {
            LoggingEventApi.add(newRecord).then(rspData => {
              if (rspData.data.code === 200) {
                Message.success('�����ɹ�')
              } else {
                Message.error(rspData.data.message)
              }
              this.$emit('after-edit')
            })
          } else if (this.mode === 'details') {
            this.$emit('after-edit')
          } else {
            LoggingEventApi.update(newRecord).then(rspData => {
              if (rspData.data.code === 200) {
                Message.success('�޸ĳɹ�')
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
