<template>
    <div class="container">
  
      <el-card class="table-container">
        <div style="margin: 0 10px 20px 10px">
          <el-form :inline="true" :model="searchForm" class="demo-form-inline">
            <el-form-item
              label="用户名"
              prop="userName"
            >
              <el-col :span="22">
                <el-input v-model="searchForm.userName" :rows="5" clearable type="text" placeholder="请输入用户名" />
              </el-col>
            </el-form-item>
            <el-form-item
              label="电子邮件"
              prop="email"
              label-width="80px"
            >
              <el-col :span="22">
                <el-input v-model="searchForm.email" :rows="5" clearable type="text" placeholder="请输入电子邮件" />
              </el-col>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" :loading="queryLoading" @click="handleQuery">查询</el-button>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="addUserDialog = true">新增用户</el-button>
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
            key="userName"
            show-overflow-tooltip
            prop="userName"
            label="用户名"
          />
          <el-table-column
            key="email"
            show-overflow-tooltip
            prop="email"
            label="电子邮件"
          />
          <el-table-column
            key="introduction"
            show-overflow-tooltip
            prop="introduction"
            label="介绍"
          />
          <el-table-column
            key="state"
            show-overflow-tooltip
            prop="state"
            label="状态"
          >
            <template slot-scope="scope">
              <el-tag :type="scope.row.state | statusFilter">
                <div v-if="scope.row.state === true">激活</div>
                <div v-else>删除</div></el-tag>
            </template>
          </el-table-column>
          <!-- 表格操作列 -->
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button-group>
                <el-button
                  size="mini"
                  title="权限管理"
                  type="primary"
                  @click="handlePerm(scope.$index, scope.row)"
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
  
      <!-- 详情弹窗 -->
      <el-dialog :visible.sync="dialogFormVisible" :title="dialogType=='see'?'查看详情':'编辑'">
        <el-form :model="dialogForm">
          <el-form-item key="id" label="编号" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['id']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="userName" label="用户名" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['userName']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="email" label="电子邮件" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['email']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="password" label="密码" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['password']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="salt" label="盐" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['salt']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="token" label="用户令牌" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['token']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="avatar" label="头像" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['avatar']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="introduction" label="介绍" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['introduction']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item key="state" label="状态" label-width="100px">
            <el-col :span="22">
              <el-input v-model="dialogForm['state']" :disabled="dialogType=='see'" :rows="5" auto-complete="off" />
            </el-col>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="SubmitCancel">取 消</el-button>
        </div>
      </el-dialog>
  
      <!-- 详情弹窗 -->
      <el-dialog :visible.sync="PermFormVisible" title="权限管理">
        <el-form ref="PermForm" :model="PermForm" :rules="formRules">
          <el-form-item key="userName" label="用户名" label-width="100px">
            <el-col :span="22">
              <el-input v-model="PermForm['userName']" :rows="5" :disabled="true" auto-complete="off" />
            </el-col>
          </el-form-item>
          <el-form-item
            label="用户角色"
            prop="checkedPart"
            label-width="100px"
          >
            <el-col :span="22" class="checkbox-group">
              <el-checkbox v-model="checkAll" :indeterminate="isIndeterminate" @change="handleCheckAllChange">全选</el-checkbox>
              <el-checkbox-group v-model="PermForm['checkedPart']" size="mini" @change="handlecheckedPartChange">
                <el-checkbox v-for="role in rolesOptions" v-if="role['role'] !== 'superadmin'" :key="role['id']" :label="role['role']" name="checkedPart" class="checkboxContent" border>{{ role['description'] }}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="SubmitCancel">取 消</el-button>
          <el-button type="primary" @click="SubmitPerForm">确 定</el-button>
        </div>
      </el-dialog>
      <el-dialog :visible.sync="addUserDialog" title="新建用户">
        <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form" label-width="100px" label-position="left">
          <div class="text item">
            <el-form-item prop="username" label="用户名">
              <el-col :span="22">
                <el-input
                  v-model="registerForm.username"
                  placeholder="请输入账号"
                  name="username"
                  type="text"
                  auto-complete="on"
                />
              </el-col>
            </el-form-item>
            <el-form-item prop="realName" label="真实姓名">
              <el-col :span="22">
                <el-input
                  v-model="registerForm.realName"
                  placeholder="请输入真实姓名"
                  name="realName"
                  type="text"
                  auto-complete="on"
                />
              </el-col>
            </el-form-item>
            <el-form-item prop="email" label="邮件">
              <el-col :span="22">
                <el-input
                  v-model="registerForm.email"
                  placeholder="请输入电子邮箱"
                  name="email"
                  type="text"
                  auto-complete="on"
                />
              </el-col>
            </el-form-item>
            <el-form-item prop="introduction" label="介绍">
              <el-col :span="22">
                <el-input
                  v-model="registerForm.introduction"
                  placeholder="请输入个人介绍"
                />
              </el-col>
            </el-form-item>
            <el-tooltip v-model="capsTooltip" content="大写输入已锁定" placement="right" manual>
              <el-form-item prop="password" label="密码">
                <el-col :span="22">
                  <el-input
                    :key="passwordType"
                    ref="password"
                    v-model="registerForm.password"
                    :type="passwordType"
                    placeholder="请输入密码"
                    name="password"
                    auto-complete="on"
                    @keyup.native="checkCapslock"
                    @blur="capsTooltip = false"
                  />
                  <span class="show-pwd" @click="showPwd">
                    <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                  </span>
                </el-col>
              </el-form-item>
            </el-tooltip>
            <el-form-item prop="confirmPassword" label="确认密码">
              <el-col :span="22">
                <el-input
                  :key="passwordType"
                  ref="confirmPassword"
                  v-model="registerForm.confirmPassword"
                  :type="passwordType"
                  placeholder="请再次输入密码"
                  name="confirmPassword"
                  auto-complete="on"
                  @keyup.native="checkCapslock"
                />
                <span class="show-pwd" @click="showPwd">
                  <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
                </span>
              </el-col>
            </el-form-item>
            <div class="button-container" style="text-align: center">
              <el-button :loading="loading" type="primary" @click.native.prevent="handleRegister">注册</el-button>
            </div>
          </div>
        </el-form>
      </el-dialog>
    </div>
  
  </template>
  
  <script>
  import { Message } from 'element-ui'
  import SysUserApi from '../api/sysUser'
  import SysRoleApi from '@/modules/sys_role/api/sysRole'
  import UserRoleRelApi from '@/modules/sys_role/api/userRoleRel'
  import { validateAlphabets, validEmail } from '@/utils/validate'
  import RegisterApi from '@/modules/register/api/register'
  import sha256 from 'js-sha256'
  
  export default {
    filters: {
      statusFilter(status) {
        const statusMap = {
          true: 'success',
          false: 'danger'
        }
        return statusMap[status]
      }
    },
    data() {
      const validateUsername = (rule, value, callback) => {
        if (!validateAlphabets(value)) {
          callback(new Error('请输入正确的用户名'))
        } else {
          callback()
        }
      }
      const validateUserEmail = (rule, value, callback) => {
        if (!validEmail(value)) {
          callback(new Error('请输入正确的email'))
        } else {
          callback()
        }
      }
      const validatePassword = (rule, value, callback) => {
        if (value.length < 6) {
          callback(new Error('输入的密码不少于6位'))
        } else {
          callback()
        }
      }
      const validateConfirmPassword = (rule, value, callback) => {
        if (value !== this.registerForm.password) {
          callback(new Error('两次输入密码不一样'))
        } else {
          callback()
        }
      }
      return {
        checkAll: false,
        isIndeterminate: true,
        queryLoading: false,
        /* 搜索面板数据*/
        searchConfig: [],
        /* 搜索请求数据*/
        searchForm: {},
        pageSize: 10,
        pageNum: 1,
        /* 表格面板数据*/
        tableConfig: [],
        // 表格数据
        tableData: {
          results: []
        },
        dialogForm: {},
        PermForm: {
          id: '',
          userName: '',
          checkedPart: []
        },
        dialogType: 'see',
        dialogFormVisible: false,
        PermFormVisible: false,
        addUserDialog: false,
        formLabelWidth: '120px',
        rolesOptions: [],
        passwordType: 'password',
        registerForm: {
          username: '',
          password: '',
          confirmPassword: '',
          email: ''
        },
        registerRules: {
          realName: [{ required: true, trigger: 'blur', message: '请输入真实姓名' }],
          username: [{ required: true, trigger: 'blur', validator: validateUsername }],
          password: [{ required: true, trigger: 'blur', validator: validatePassword }],
          confirmPassword: [{ required: true, trigger: 'blur', validator: validateConfirmPassword }],
          email: [{ required: true, trigger: 'blur', validator: validateUserEmail }]
        },
        // 角色列表
        rolesList: [],
        loading: false,
        showDialog: false,
        redirect: undefined,
        capsTooltip: false,
        formRules: {
          checkedPart: [{ type: 'array', required: true, message: '至少选择一个部分', trigger: 'change' }]
        }
      }
    },
    mounted() {
      this.init()
    },
    methods: {
      // 页面初始化
      init() {
        this.getAllRole()
        this.query()
      },
      checkCapslock({ shiftKey, key } = {}) {
        if (key && key.length === 1) {
          if (shiftKey && (key >= 'a' && key <= 'z') || !shiftKey && (key >= 'A' && key <= 'Z')) {
            this.capsTooltip = true
          } else {
            this.capsTooltip = false
          }
        }
        if (key === 'CapsLock' && this.capsTooltip === true) {
          this.capsTooltip = false
        }
      },
      handleQuery() {
        this.query()
      },
      handleCheckAllChange(val) {
        this.PermForm.checkedPart = val ? this.rolesList : []
        this.isIndeterminate = false
      },
      handlecheckedPartChange(value) {
        const checkedCount = value.length
        this.checkAll = checkedCount === this.rolesList.length
        this.isIndeterminate = checkedCount > 0 && checkedCount < this.rolesList.length
      },
      getThisUserRole(id) {
        return new Promise((resolve, reject) => {
          SysRoleApi
            .queryUserRole(id)
            .then(response => {
              const data = response.data
              if (data.code === 200) {
                const data = response.data.data
                this.PermForm.checkedPart = data
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
      query() {
        this.queryLoading = true
        return new Promise((resolve, reject) => {
          const param = Object.assign({}, this.searchForm)
          SysUserApi
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
              reject(error)
            })
        })
      },
      showPwd() {
        if (this.passwordType === 'password') {
          this.passwordType = ''
        } else {
          this.passwordType = 'password'
        }
      },
      handleRegister() {
        this.$refs.registerForm.validate(valid => {
          if (valid) {
            this.loading = true
            const params = Object.assign({}, this.registerForm)
            params.password = sha256(this.registerForm.password)
            return new Promise((resolve, reject) => {
              RegisterApi
                .register(params)
                .then(response => {
                  const data = response.data
                  this.loading = false
                  if (data.code === 200) {
                    Message.success('注册成功..')
                    this.addUserDialog = false
                    this.query()
                  } else {
                    Message.error(data.message)
                    return
                  }
                  resolve(data)
                })
                .catch(error => {
                  this.loading = false
                  if (error.message !== '') {
                    Message.error(error.message)
                  }
  
                  reject(error)
                })
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
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
      handlePerm(index, row) {
        this.PermForm.id = row.id
        this.PermForm.userName = row.userName
        this.getThisUserRole(row.id)
        this.PermFormVisible = true
      },
  
      SubmitCancel() {
        this.dialogFormVisible = false
        this.PermFormVisible = false
        this.query()
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
      handleDel(row) {
        this.$confirm('确定删除吗？', '警告', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
          .then(() => {
            const param = Object.assign({}, row)
            SysUserApi
              .delete(param)
              .then(response => {
                const data = response.data
                if (data.code === 200) {
                  this.$message({
                    message: '删除成功',
                    type: 'success'
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
      SubmitPerForm(row) {
        this.loading = true
        this.$refs['PermForm'].validate(valid => {
          if (valid) {
            const params = Object.assign({}, this.PermForm)
            return new Promise((resolve, reject) => {
              UserRoleRelApi
                .updateUserRoleRel(params)
                .then(response => {
                  this.loading = false
                  const data = response.data
                  if (data.code === 200) {
                    this.$message({
                      message: '修改成功',
                      type: 'success'
                    })
                    this.PermFormVisible = false
                    this.loading = false
                    this.query()
                  } else {
                    Message.error(data.message)
                    reject()
                  }
                })
                .catch(error => {
                  this.loading = false
                  if (error.message !== '') {
                    Message.error(error.message)
                  }
  
                  reject(error)
                })
            })
          } else {
            this.loading = false
            return false
          }
        })
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
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: #889aa4;
      cursor: pointer;
      user-select: none;
    }
  </style>
  
  