<template>
  <div>
    <a-card class="ant-card">
      <!-- 头部搜索栏  start -->
      <a-form
        :layout="isDesktop()? 'inline' :'horizontal'"
        :model="formState"
        @finish="handleQuery"
        @finishFailed="handleFinishFailed"
      >
        <a-form-item>
          <a-input v-model:value="formState.groupName" placeholder="请输入组别">
            <template #prefix>
              <UserOutlined style="color: rgba(0, 0, 0, 0.25)" />
            </template>
          </a-input>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" style="margin-right:8px;">查询</a-button>
          <a-button type="primary" @click="handleNewGroup">新增分组</a-button>
        </a-form-item>
      </a-form>
      <!-- 头部搜索栏  end -->


      <!-- 表格  start-->
      <a-table
        :columns="columns"
        :data-source="tableData.list"
        :loading="loading"
        style="margin-top:24px;"
        :pagination="false"
        :expandedRowsChange="handleExpendRow"
        class="components-table-demo-nested"
        @expand="expandRow"
        :expandedRowKeys="expandedRowIds"
        :scroll="store.state.app.device == 'desktop'?{}:{ x: 1000 }"
        rowKey="id">
      >
        <template #action="{ record }" >
          <span>
            <a-button type="primary" style="margin-right:5px" @click="handleNewItem(record)">
              <template #icon>
                <plus-outlined />
              </template>
              添加
            </a-button>
            <a-button type="primary" style="margin-right:5px" @click="handleEditGroup(record)">
              <template #icon>
                <form-outlined />
              </template>
              编辑
            </a-button>
            <a-button type="primary" danger @click="handleDeleteGroup(record)">
              <template #icon>
                <delete-outlined />
              </template>
              删除
            </a-button>
          </span>
        </template>
        <template #expandedRowRender="{record, index, indent, expanded}" >
          <a-table :columns="innerColumns"
              :dataSource="record.innerData"
              :pagination="false" rowKey="id">
              <template #action="{ record }">
                <span>
                  <a-button type="primary" style="margin-right:5px" @click="handleEditItem(record)">
                    <template #icon>
                      <form-outlined />
                    </template>
                    编辑
                  </a-button>
                  <a-button type="primary" danger @click="handleDeleteItem(record)">
                    <template #icon>
                      <delete-outlined />
                    </template>
                    删除
                  </a-button>
                </span>
              </template>
          </a-table>
        </template>
      </a-table>
      <!-- 表格  end -->

      <!-- 分页 start -->
      <a-pagination
        class="pagination"
        size="small"
        v-model:current="pageNum"
        v-model:pageSize="pageSize"
        :total="tableData.total"
        :show-total="total => `共 ${total} 条`"
        show-size-changer
        @change="handlePageChange"
        @showSizeChange="handleSizeChange"
      />
      <!-- 分页 end -->
    </a-card>

    <!-- 组件化对话框 -->
    <a-modal
        v-model:visible="newVisible"
        :footer="null"
        :title="mode == 'add' ? '添加组别' : '编辑组别'"
        :width="800"
        @cancel="cancelGroup"
      >
      <edit-group :formState="newFormState" :mode="mode" @afterEdit="afterEdit"></edit-group>
    </a-modal>

    <a-modal
        v-model:visible="newItemVisible"
        :footer="null"
        :title="mode == 'add' ? '添加项目' : '编辑项目'"
        :width="800"
      >
      <edit-item :formState="newItemFormState" :mode="itemMode" @afterEdit="afterEditItem"></edit-item>
    </a-modal>
    <!-- 组件化对话框 -->

    <!-- 新增分组对话框  start -->
    <!-- <a-modal v-model:visible = "newVisible" title="新增分组" @ok="submitGroup">
      <a-form
        ref="newFormRef"
        :model="newFormState"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-form-item has-feedback label="ID" name="id" placeholder="">
          <a-input v-model:value="newFormState.id" disabled/>
        </a-form-item>
        <a-form-item has-feedback label="名称" name="groupName" placeholder="请输入名称">
          <a-input v-model:value="newFormState.groupName"/>
        </a-form-item>
        <a-form-item has-feedback label="编码" name="groupCode" placeholder="请输入编码">
          <a-input v-model:value="newFormState.groupCode"/>
        </a-form-item>
        <a-form-item has-feedback label="排序" name="sort" placeholder="0">
          <a-input-number id="inputNumber" v-model:value="newFormState.sort" :min="1" @change="sortGroupInputOnChange" />
        </a-form-item>
        <a-form-item has-feedback label="备注" name="remark" placeholder="备注">
          <a-input v-model:value="newFormState.remark"/>
        </a-form-item>
      </a-form>
    </a-modal> -->

    <!-- 添加项目对话框  start -->
    <!-- <a-modal v-model:visible = "newItemVisible" :title="itemMode == 'add'?'添加项目':'编辑项目'" @ok="submitItem">
      <a-form
        ref="newItemFormRef"
        :model="newItemFormState"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >

        <a-form-item has-feedback label="编码" name="groupCode" placeholder="请输入编码">
          <a-input v-model:value="newItemFormState.groupCode" disabled/>
        </a-form-item>
        <a-form-item has-feedback label="名称" name="itemName" placeholder="请输入名称">
          <a-input v-model:value="newItemFormState.itemName"/>
        </a-form-item>
        <a-form-item has-feedback label="值" name="itemCode" placeholder="">
          <a-input v-model:value="newItemFormState.itemCode"/>
        </a-form-item>
        <a-form-item has-feedback label="排序" name="sort" placeholder="0">
          <a-input-number id="inputNumber" v-model:value="newItemFormState.sort" :min="1" @change="sortItemInputOnChange" />
        </a-form-item>
        <a-form-item has-feedback label="备注" name="remark" placeholder="备注">
          <a-input v-model:value="newItemFormState.remark"/>
        </a-form-item>
      </a-form>
    </a-modal> -->
  </div>
</template>



<script>
  import { UserOutlined, MailOutlined, SmileOutlined, EyeOutlined, FormOutlined, DeleteOutlined, PlusOutlined, ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { defineComponent, reactive, ref ,createVNode } from 'vue';

  import groupItemApi from '../api/groupItem'
  import groupInfoApi from '../api/groupInfo'
  import { Modal,message } from 'ant-design-vue'
  import { validateAlphabets, validEmail } from '../../../utils/validate'
  import EditGroup from './groupEdit.vue'
  import EditItem from './groupItemEdit.vue'
  import _ from 'lodash'
  import { mixin, mixinDevice } from '../../../utils/mixin'
  import { useStore } from 'vuex'



  const rowSelection = {
    onChange: (selectedRowKeys, selectedRows) => {
      console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
    },
    onSelect: (record, selected, selectedRows) => {
      console.log(record, selected, selectedRows);
    },
    onSelectAll: (selected, selectedRows, changeRows) => {
      console.log(selected, selectedRows, changeRows);
    },
  };

export default defineComponent({
  components: {
    UserOutlined,
    MailOutlined,
    SmileOutlined,
    EyeOutlined,
    FormOutlined,
    DeleteOutlined,
    PlusOutlined,
    EditGroup,
    EditItem
  },
  mixins: [mixin, mixinDevice],

  setup() {
    // const expandedRowKeys = ref([]);
    const formState = reactive({
      // groupName: '',
    });

    const newFormRef = ref()

    const newFormState = ref({})

    const newItemFormRef = ref()

    const newItemFormState = ref({})


    // const newFormState = reactive({
    //   groupName: '',
    //   groupCode: '',
    //   sort: '',
    //   remark: '',
    // })

    // const newItemFormState = reactive({
    //   gid:'',
    //   groupCode: '',
    //   itemName: '',
    //   itemCode:'',
    //   remark: '',
    //   sort: '',
    // })
      

    const validateGroupName = (_rule, value) => {
      if (!validateAlphabets(value)) {
        return Promise.reject('请输入正确的分组名')
      } else {
        return Promise.resolve()
      }
    }

    const validateGroupCode = (_rule, value) => {
      if (!validateAlphabets(value)) {
        return Promise.reject('请输入正确的分组编码')
      } else {
        return Promise.resolve()
      }
    }

    const validateItemCode = (_rule, value) => {
      if (!validEmail(value)) {
        return Promise.reject('请输入正确的编码')
      } else {
        return Promise.resolve()
      }
    }

    const validateItemName = (_rule, value) => {
      if (!validateAlphabets(value)) {
        return Promise.reject('请输入正确的项目名')
      } else {
        return Promise.resolve()
      }
    }

    const validateSort = (_rule, value) => {
      if (!validateAlphabets(value)) {
        return Promise.reject('请输入正确的排序')
      } else {
        return Promise.resolve()
      }
    }

    

    const addItemRules = [{
      itemName: [{
        require: true,
        validator: validateItemName,
        trigger: 'blur',
      }],
      itemCode: [{
        require: true,
        validator: validateItemCode,
        trigger: 'blur',
      }],
      sort:[{
        require:false,
        validator: validateSort,
        trigger: 'blur',
      }]
    }]

    const addGroupRules = [{
      groupName: [{
        require: true,
        validator: validateGroupName,
        trigger: 'blur',
      }],
      groupCode: [{
        require: true,
        validator: validateGroupCode,
        trigger: 'blur',
      }],
    }]

    const columns = [{
      title: '名称',
      dataIndex: 'groupName',
      key: 'groupName'
    }, {
      title: '编码',
      dataIndex: 'groupCode',
      key: 'groupCode'
    }, {
      title: '排序',
      dataIndex: 'sort',
      key: 'sort'
    }, {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark'
    }, {
      title: '操作',
      key: 'action',
      slots: { customRender: 'action' },
      width:300
    }]

    const innerColumns = [{
      title: '名称',
      dataIndex: 'itemName',
      key: 'itemName'
    }, {
      title: '值',
      dataIndex: 'itemCode',
      key: 'itemCode'
    }, {
      title: '排序',
      dataIndex: 'sort',
      key: 'sort'
    }, {
      title: '备注',
      dataIndex: 'remark',
      key: 'remark'
    }, {
      title: '操作',
      key: 'action',
      slots: { customRender: 'action' },
      width:300
    }]


    
    

    const loading = ref(false)
    const rolesOptions = []
    const rolesList = []
    const tableData = {}
    const innerData = []
    const selectGroup = {}
    const selectItem = {}
    const mode = 'add'
    const itemMode = 'add'

    const pageSize = 10
    const pageNum = 1

    const newVisible = ref(false);
    const newItemVisible = ref(false);

    const store = useStore()

    

    return {
      formState,

      //表格数据
      columns,
      tableData,
      innerColumns,
      innerData,
      rowSelection,
      selectGroup,
      selectItem,
      mode,
      itemMode,
      newFormRef,
      newFormState,
      newItemFormRef,
      newItemFormState,
      // expandedRowKeys:[],
      expandedRowIds:[],
      //表格数据


      loading,
      pageNum,
      pageSize,
      newVisible,
      newItemVisible,
      rolesOptions,
      rolesList,


      addItemRules,
      addGroupRules,
      labelCol: {
        span: 4,
      },
      wrapperCol: {
        span: 14,
      },
      store

    };
  },

  mounted() {
    this.init()
  },


  methods: {
    //初始化函数
    init() {
      this.handleQueryGroupData()
    },
    handleQuery() {
      this.handleQueryGroupData()
    },
    handleFinishFailed(errors) {
      console.log(errors);
    },
    handlePageChange(page, pageSize) {
      console.log(page, pageSize)
    },
    handleSizeChange(current, size) {
      console.log(current, size)
    },

    // -------------功能函数------------------
    //打开新建组别弹窗
    handleNewGroup(e) {
      console.log(e);
      this.mode = 'add'
      this.newVisible = true
      this.newFormState = {}
    },
    //打开编辑组别弹窗
    handleEditGroup(row){
      console.log(row)
      //函数
      this.mode = 'edit'
      this.selectGroup = _.pick(row,'groupCode','groupName','id','remark','sort','state')
      this.newFormState = row
      this.newVisible = true
      console.log(this.selectGroup)
    },
    cancelGroup(e){
      var that = this
      console.log(that.selectGroup)
      console.log(this.tableData.list)
      var index = this.tableData.list.findIndex(function(item){
        return item.id == that.selectGroup.id; //如果返回true，那么findIndex方法会将这个item对应的id返回到外面接受
      });
      console.log(index)
      that.tableData.list[index] = that.selectGroup
    },
    //删除组别
    handleDeleteGroup(row){
      console.log(row)
      //函数
      var vm = this
      console.log(row);
      this.selectGroup = row

      //函数
      const modal = Modal.confirm()
      modal.update({
        title: '确认删除该项目吗?',
        icon: createVNode(ExclamationCircleOutlined),
        content: '组别名：' + row.groupName,
        okText: '删除',
        okType: 'danger',
        cancelText: '取消',
        closable:true,
        maskClosable:true,
        onOk(e) {
          console.log(vm.selectGroup)
          const param = Object.assign({}, vm.selectGroup)

          groupInfoApi
            .delete(param)
            .then((response) => {
              const data = response.data
              console.log(response)
              if (data.code === 200) {
                message.success('删除成功',3,);
                vm.handleQuery()
                modal.destroy();
              } else {
                message.warning(data.message,3,);
              }
            })
            .catch(error => {
              
            })
        },
        onCancel() {
          console.log('Cancel');
          modal.destroy();
        },
      });

    },

    //打开新建项目弹窗
    handleNewItem(row) {
      console.log(row);
      // this.resetForm();
      this.itemMode = 'add'
      this.newItemVisible = true
      this.newItemFormState.groupCode = row.groupCode
      this.newItemFormState.gid = row.id
    },
    //打开编辑项目弹窗
    handleEditItem(row){
      console.log(row);
      this.selectItem = row
      //函数
      this.newItemVisible = true
      this.itemMode = 'edit'
      const data = row
      this.newItemFormState = data
    },
    //删除项目
    handleDeleteItem(row) {
      var vm = this
      console.log(row);
      this.selectItem = row

      //函数
      const modal = Modal.confirm()
      modal.update({
        title: '确认删除该项目吗?',
        icon: createVNode(ExclamationCircleOutlined),
        content: '项目名：' + row.itemName,
        okText: '删除',
        okType: 'danger',
        cancelText: '取消',
        closable:true,
        maskClosable:true,
        onOk(e) {
          console.log(vm.selectItem)
          const param = Object.assign({}, vm.selectItem)

          groupItemApi
            .delete(param)
            .then((response) => {
              const data = response.data
              console.log(response)
              if (data.code === 200) {
                message.success('删除成功',3,);
                vm.handleQuery()
                modal.destroy();
              } else {
                message.warning(data.message,3,);
              }
            })
            .catch(error => {
              
            })
        },
        onCancel() {
          console.log('Cancel');
          modal.destroy();
        },
      });
    },
    

    //展开子表格
    expandRow(expanded, record){
      console.log(this.tableData.list)
      console.log(record)
      console.log(expanded)
      var index = this.tableData.list.findIndex(function(item){
        return item.id == record.id; //如果返回true，那么findIndex方法会将这个item对应的id返回到外面接受
      });
      record = this.tableData.list[index]

      if(expanded){
        this.handleQueryItemData(record.id,record.groupCode)

        if (this.expandedRowIds.length > 0) { //进这个判断说明当前已经有展开的了
         //返回某个指定的字符串值在字符串中首次出现的位置，下标为0
          let index = this.expandedRowIds.indexOf(record.id);
          if (index > -1) { //如果出现则截取这个id,1d到1相当于0，针对重复点击一个
            this.expandedRowIds.splice(index, 1);
          } else {
          //如果没出现则截取所有id,添加点击id，0到1，针对已经有一个展开，点另一个会进入判断
            this.expandedRowIds.push(record.id);
          }
        } else {
          //数组长度小于0，说明都没展开，第一次点击，id添加到数组，数组有谁的id谁就展开
          this.expandedRowIds.push(record.id);  
        }
      }
      // this.expandedRowIds
      this.$forceUpdate()
      console.log(this.expandedRowIds)

      // if (this.expandedRowKeys.length > 0) { //进这个判断说明当前已经有展开的了
      //  //返回某个指定的字符串值在字符串中首次出现的位置，下标为0
      //   let index = this.expandedRowKeys.indexOf(record.id);
      //   if (index > -1) { //如果出现则截取这个id,1d到1相当于0，针对重复点击一个
      //     this.expandedRowKeys.splice(index, 1);
      //   } else {
      //   //如果没出现则截取所有id,添加点击id，0到1，针对已经有一个展开，点另一个会进入判断
      //     this.expandedRowKeys.push(record.id);
      //   }
      // } else {
      //   //数组长度小于0，说明都没展开，第一次点击，id添加到数组，数组有谁的id谁就展开
      //   this.expandedRowKeys.push(record.id);  
      // }
    },
    //展开行（备用）
    handleExpendRow(row, expandedRows){
      console.log("c" + row, expandedRows)
      // if(!row.items){
      //   this.handleQueryItemData(row.id,row.groupCode)
      // }
    },


    // 组件化弹窗函数
    afterEdit(row){
      this.newVisible = false
      this.handleQueryGroupData()
      this.expandedRowIds = []
      console.log(row)
    },
    afterEditItem(row){
      this.newItemVisible = false
      this.handleQueryGroupData()
      this.expandedRowIds = []
      console.log(this.tableData.list)
      console.log(this.selectItem.groupCode)

      // let i =  this.tableData.list.findIndex (item => item.groupCode == this.selectItem.groupCode );
      // console.log(i)
      // this.handleQueryItemData(this.tableData.list[i].id,this.tableData.list[i].groupCode)

    },
    
    //查询组别数据
    handleQueryGroupData(){
      this.loading = true
      return new Promise((resolve, reject) => {
        const param = Object.assign({}, this.formState)
        groupInfoApi
          .queryByCond(param, this.pageSize, this.pageNum)
          .then(response => {
            const data = response.data
            this.loading = false
            if (data.code === 200) {
              const data = response.data.data
              this.tableData = data
              console.log(this.tableData)
            } else {
              message.error(data.message)
              reject()
            }
          })
          .catch(error => {
            this.loading = false
            reject(error)
          })
      })
    },
    
    
    //查询子表格（项目）数据
    handleQueryItemData(gid,groupCode) {
      console.log(gid,groupCode)

      this.loading = true
      const param = Object.assign({}, this.formState)
      groupItemApi
        .queryAllByCode(groupCode)
        .then((response) => {
          const data = response.data
          this.loading = false
          if (data.code === 200) {
            const data = response.data.data
            this.innerData = data

            let ace = this.tableData.list
            let i =  ( ace ). findIndex ((ace) => ace.id == gid );  // 根据条件查找符号条件对象下标索引
            this.tableData.list[i].innerData = data

          } else {
            message.error(data.message)
          }
        })
        .catch(error => {
          this.loading = false
        })
    },
    //提交项目
    submitItem(){
      console.log(this.newItemFormState)
      console.log(this.itemMode)
      const param = Object.assign({}, this.newItemFormState)
      if(this.itemMode == 'add'){
        groupItemApi
          .add(param)
          .then((response) => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              message.success('提交成功',3,);
              this.newItemVisible = false
              this.handleQuery()
            } else {
              message.error(data.message)
            }
          })
          .catch(error => {
              message.warning(error.message,3,);
              this.newItemVisible = false
            
          })
      }else if(this.itemMode == 'edit'){
        groupItemApi
          .update(param)
          .then((response) => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              message.success('提交成功',3,);
              this.newItemVisible = false
              this.handleQuery()
            } else {
              message.error(data.message)
            }
          })
          .catch(error => {
              message.warning(error.message,3,);
              this.newItemVisible = false
            
          })
      }
      
    },
    // 提交组别
    submitGroup(){
      console.log(this.newFormState)
      console.log(this.mode)

      const param = Object.assign({}, this.newFormState)
      if(this.mode == 'add'){
        groupInfoApi
          .add(param)
          .then((response) => {
            const data = response.data
            if (data.code === 200) {
              message.success('提交成功',3,);
              this.newItemVisible = false
              this.handleQuery()
            } else {
              message.error(data.message)
            }
          })
          .catch(error => {
              message.warning(error.message,3,);
              this.newItemVisible = false
            
          })
      }else if(this.mode == 'edit'){
        groupItemApi
          .update(param)
          .then((response) => {
            const data = response.data
            if (data.code === 200) {
              const data = response.data.data
              message.success('提交成功',3,);
              this.newVisible = false
              this.handleQuery()
            } else {
              message.error(data.message)
            }
          })
          .catch(error => {
              message.warning(error.message,3,);
              this.newVisible = false
            
          })
      }
    },

    sortGroupInputOnChange(e){
      console.log(e)
      this.newFormState.sort = e
    },
    sortItemInputOnChange(e){
      console.log(e)
      this.newItemFormState.sort = e
    },
  }
})
</script>

<style scoped>
.ant-card {
  margin: 24px;
  border-radius: 8px;
}
.pagination {
  text-align: right;
  margin: 12px;
}
</style>