<template>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />

    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    <div class="right-menu">
      <template v-if="device!=='mobile'">
        <search id="header-search" class="right-menu-item" />

        <error-log class="errLog-container right-menu-item hover-effect" />

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

      </template>
      <el-dropdown class="avatar-container right-menu-item" trigger="click">
        <div class="avatar-wrapper">
          <img :src="require('@/assets/pic/img.png')" class="user-avatar">
          <i class="el-icon-caret-bottom" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item>
            {{ name }}
          </el-dropdown-item>
          <router-link to="/">
            <el-dropdown-item>
              {{ $t('navbar.dashboard') }}
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item>
            <span style="display:block;" @click="handleShow">修改密码</span>
          </el-dropdown-item>
          <el-dropdown-item>
            <span style="display:block;" @click="logout">{{ $t('navbar.logOut') }}</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-dialog
      :visible.sync="dialogVisible"
      :before-close="handleClose"
      title="修改密码"
      width="30%"
    >
      <el-form ref="passwordForm" :model="passwordForm" :rules="rules2" status-icon label-width="80px">
        <el-form-item label="原密码" prop="originPassword">
          <el-input v-model="passwordForm.originPassword" type="password" />
        </el-form-item>
        <el-form-item label="密码" prop="pass">
          <el-input v-model="passwordForm.pass" type="password" autocomplete="off" />
        </el-form-item>
        <el-form-item label="确认密码" prop="checkPass">
          <el-input v-model="passwordForm.checkPass" type="password" autocomplete="off" />
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleModifyPassword">确 定</el-button>
      </span>
    </el-dialog>
    <!-- 新增/编辑对话框 -->
    <el-dialog :visible.sync="showSystemSettingDialog" title=" " width="40%" class="system-setting-dialog">
      <system-setting />
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import ErrorLog from '@/components/ErrorLog'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import LangSelect from '@/components/LangSelect'
import Search from '@/components/HeaderSearch'
import { Message } from 'element-ui'
import store from '@/store'
import sha256 from 'js-sha256'
export default {
  components: {
    Breadcrumb,
    Hamburger,
    ErrorLog,
    Screenfull,
    SizeSelect,
    LangSelect,
    Search
  },
  data() {
    var checkPassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入原密码'))
      }
      callback()
    }
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'))
      } else if (value.length < 6) {
        callback(new Error('输入的密码不少于6位'))
      } else {
        if (this.passwordForm.checkPass !== '') {
          this.$refs.passwordForm.validateField('checkPass')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.passwordForm.pass) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      dialogVisible: false,
      showSystemSettingDialog: false,
      passwordForm: {
        pass: '',
        checkPass: '',
        originPassword: ''
      },
      rules2: {
        pass: [
          { validator: validatePass, trigger: 'blur' }
        ],
        checkPass: [
          { validator: validatePass2, trigger: 'blur' }
        ],
        originPassword: [
          { validator: checkPassword, trigger: 'blur' }
        ]
      },
      options: [],
      isAdmin: false
    }
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'name',
      'avatar',
      'device',
      'roles'
    ])
  },
  mounted() {
    this.hasPermission()
  },
  methods: {
    handleShow() {
      this.dialogVisible = true
    },
    hasPermission() {
      if (this.$store.getters.roles.indexOf('superadmin') >= 0) {
        this.isAdmin = true
      }
    },
    handleModifyPassword() {
      this.$refs['passwordForm'].validate(valid => {
        if (valid) {
          const params = Object.assign({}, this.passwordForm)
          params.originPassword = sha256(this.passwordForm.originPassword)
          params.pass = sha256(this.passwordForm.pass)
          this.$store.dispatch('user/ModifyPassword', params).then(() => {
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.dialogVisible = false
            this.resetFields()
          }).catch(error => {
            if (error.message !== '') {
              Message.error(error.message)
            }
          })
        }
      })
    },
    handleClose(done) {
      this.$confirm('确认关闭？')
        .then(_ => {
          done()
          this.resetFields()
        })
        .catch(_ => {})
    },
    resetFields() {
      this.$refs['passwordForm'].resetFields()
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    }
  }
}
</script>

<style lang="scss">
  .system-setting-dialog .el-dialog .el-dialog__body{
    padding: 0px 20px 5px 20px;
  }
  .system-setting-dialog .el-dialog .el-dialog__header{
    padding: 20px;
  }
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
