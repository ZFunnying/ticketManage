<script>
import { defineComponent, reactive, ref, toRef, watch, toRefs, getCurrentInstance } from 'vue';
import { PlusOutlined, FormOutlined } from '@ant-design/icons-vue'
import { message } from "ant-design-vue"
import groupItemApi from '../api/groupItem'
import groupInfoApi from '../api/groupInfo'
import SysRoleApi from '../../sys_role/api/sysRole'

export default defineComponent({
  props: {
    formState: Object,
    mode: String,
    afterEdit: Function
  },

  setup(props, context) {
    const newFormRef = ref()
    const newFormState = toRef(props, 'formState')
    const { ctx }= getCurrentInstance()

    console.log(newFormState.value)

    const rules = {
      name: [
        { required: true, message: '请输入名称', trigger: 'blur' },
        {
          max: 100,
          message: '最大长度不能超过100个字符',
          trigger: 'blur'
        }
      ],
      title: [{
        required: true,
        message: '请输入菜单名称',
        trigger: 'blur',
      }],
      path: [
        {
          required: true,
          message: '请输入链接',
          trigger: 'blur'
        }, {
          max: 200,
          message: '最大长度不能超过200个字符',
          trigger: 'blur'
        }
      ],
      permission: [
        {
          max: 100,
          message: '最大长度不能超过100个字符',
          trigger: 'blur'
        }
      ],
      menuIcon: [
        {
          max: 255,
          message: '最大长度不能超过255个字符',
          trigger: 'blur'
        }
      ]
    }

    const rolesOptions = ref([])
    const rolesList = ref([])
    
    const state = reactive({
      indeterminate: true,
      checkAll: false
    });



    const sortItemInputOnChange = (e) =>{
      console.log(e)
      newFormState.value.sort = e
    }

    const onSubmit = () => {
      newFormRef.value.validate().then(() => {

        const param = Object.assign({}, newFormState.value)
        console.log(param)

        if(props.mode == 'add'){
          groupItemApi
            .add(param)
            .then((response) => {
              const data = response.data
              if (data.code === 200) {
                message.success('提交成功',3,);
                context.emit('afterEdit', { })

              } else {
                message.error(data.message)
              }
            })
            .catch(error => {
                message.warning(error.message,3,);
              
            })
        }else if(props.mode == 'edit'){
          groupItemApi
            .update(param)
            .then((response) => {
              const data = response.data
              if (data.code === 200) {
                const data = response.data.data
                message.success('修改成功',3,);
                context.emit('afterEdit', { })
              } else {
                message.error(data.message)
              }
            })
            .catch(error => {
                message.warning(error.message,3,);
              
            })
        }

      }).catch(error => {
        console.log('error', error);
      });
    }

    return {
      newFormRef,
      newFormState,
      rules,
      rolesOptions,
      rolesList,
      // checkedPart,
      labelCol: {
        span: 4,
      },
      wrapperCol: {
        span: 14,
      },
      ...toRefs(state),
      onSubmit,
      sortItemInputOnChange,
    }
  },

  created() {
    this.init()
  },

  methods: {
    init() {

    },

  }

})
</script>

<template>
  <div>
    <a-form
        ref="newFormRef"
        :model="newFormState"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-form-item has-feedback label="编码" name="groupCode" placeholder="请输入编码">
          <a-input v-model:value="newFormState.groupCode" disabled/>
        </a-form-item>
        <a-form-item has-feedback label="名称" name="itemName" placeholder="请输入名称">
          <a-input v-model:value="newFormState.itemName"/>
        </a-form-item>
        <a-form-item has-feedback label="值" name="itemCode" placeholder="">
          <a-input v-model:value="newFormState.itemCode"/>
        </a-form-item>
        <a-form-item has-feedback label="排序" name="sort" placeholder="0">
          <a-input-number id="inputNumber" v-model:value="newFormState.sort" :min="1" @change="sortItemInputOnChange" />
        </a-form-item>
        <a-form-item has-feedback label="备注" name="remark" placeholder="备注">
          <a-input v-model:value="newFormState.remark"/>
        </a-form-item>
        <a-form-item :wrapper-col="{ span: 14, offset: 4 }">
          <a-button type="primary" @click="onSubmit">保存</a-button>
        </a-form-item>
      </a-form>
  </div>
</template>