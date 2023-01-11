<template>
  <div class="register-container">

    <el-form ref="registerForm" :model="registerForm" :rules="registerRules" class="register-form" auto-complete="on" label-position="left">
      <el-card class="box-card">
        <div class="title-container">
          <h3 class="title">系统注册</h3>
        </div>
        <div class="text item">
          <el-form-item prop="username">
            <span class="svg-container">
              <svg-icon icon-class="user" />
            </span>
            <el-input
              v-model="registerForm.username"
              placeholder="请输入账号"
              name="username"
              type="text"
              auto-complete="on"
            />
          </el-form-item>
          <el-form-item prop="realName">
            <span class="svg-container">
              <svg-icon icon-class="user" />
            </span>
            <el-input
              v-model="registerForm.realName"
              placeholder="请输入真实姓名"
              name="realName"
              type="text"
              auto-complete="on"
            />
          </el-form-item>
          <el-form-item prop="email">
            <span class="svg-container">
              <svg-icon icon-class="email" />
            </span>
            <el-input
              v-model="registerForm.email"
              placeholder="请输入电子邮箱"
              name="email"
              type="text"
              auto-complete="on"
            />
          </el-form-item>

          <!--<el-form-item prop="password">-->
          <!--<span class="svg-container">-->
          <!--<svg-icon icon-class="password" />-->
          <!--</span>-->
          <!--<el-input-->
          <!--:type="passwordType"-->
          <!--v-model="registerForm.password"-->
          <!--placeholder="请输入密码"-->
          <!--name="password"-->
          <!--auto-complete="on"/>-->
          <!--<span class="show-pwd" @click="showPwd">-->
          <!--<svg-icon icon-class="eye" />-->
          <!--</span>-->
          <!--</el-form-item>-->
          <el-tooltip v-model="capsTooltip" content="大写输入已锁定" placement="right" manual>
            <el-form-item prop="password">
              <span class="svg-container">
                <svg-icon icon-class="password" />
              </span>
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
            </el-form-item>
          </el-tooltip>
          <el-form-item prop="confirmPassword">
            <span class="svg-container">
              <svg-icon icon-class="password" />
            </span>
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
          </el-form-item>
          <!--<el-form-item prop="confirmPassword">-->
          <!--<span class="svg-container">-->
          <!--<svg-icon icon-class="password" />-->
          <!--</span>-->
          <!--<el-input-->
          <!--v-model="registerForm.confirmPassword"-->
          <!--:type="passwordType"-->
          <!--placeholder="请再次输入密码"-->
          <!--name="confirmPassword"-->
          <!--auto-complete="on"-->
          <!--@keyup.enter.native="handleRegister" />-->
          <!--<span class="show-pwd" @click="showPwd">-->
          <!--<svg-icon icon-class="eye" />-->
          <!--</span>-->
          <!--</el-form-item>-->

          <div class="button-container">
            <el-button :loading="loading" type="primary" style="width:100%;height:50px" @click.native.prevent="handleRegister">注册</el-button>
          </div>
          <div class="tips">
            <span>已有账号?</span>
            <a @click="goLogin">登陆</a>
          </div>
        </div>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import { validateAlphabets, validEmail } from '@/utils/validate'
import RegisterApi from '../api/register'
import { Message } from 'element-ui'
import sha256 from 'js-sha256'
export default {
  name: 'Register',
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
      passwordType: 'password',
      loading: false,
      showDialog: false,
      redirect: undefined,
      capsTooltip: false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    // window.addEventListener('hashchange', this.afterQRScan)
  },
  destroyed() {
    // window.removeEventListener('hashchange', this.afterQRScan)
  },
  methods: {
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
    goLogin() {
      this.$router.push({ path: '/login' })
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
                  this.$router.push({ path: this.redirect || '/login' })
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
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  /* 修复input 背景不协调 和光标变色 */
  /* Detail see https://github.com/PanJiaChen/vue-element-admin/pull/927 */

  $bg:#e5e5e5;
  $light_gray: #7a7a7a;
  $cursor: #333;

  @supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
    .register-container .el-input input{
      color: $cursor;
      &::first-line {
        color: #7a7a7a;
      }
    }
  }
  .text {
    font-size: 14px;
  }

  .item {
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }
  .clearfix:after {
    clear: both
  }

  .register-container .box-card {
    width: 480px;
  }
  /* reset element-ui css */
  .register-container {
    .el-input {
      display: inline-block;
      height: 47px;
      width: 85%;
      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: $light_gray;
        height: 47px;
        caret-color: $cursor;
        &:-webkit-autofill {
          -webkit-box-shadow: 0 0 0px 1000px $bg inset !important;
          -webkit-text-fill-color: $cursor !important;
        }
      }
    }
    .el-form-item {
      border: 1px solid rgba(255, 255, 255, 0.1);
      background: rgba(0, 0, 0, 0.1);
      border-radius: 5px;
      color: #454545;
    }
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
  $bg:#2d3a4b;
  $dark_gray:#889aa4;

  .register-container {
    position: fixed;
    height: 100%;
    width: 100%;
    background-color: $bg;
    .register-form {
      position: absolute;
      left: 0;
      right: 0;
      width: 520px;
      max-width: 100%;
      padding: 35px 35px 15px 35px;
      margin: 120px auto;
    }
    .tips {
      font-size: 14px;
      color: #464646;
      margin-top: 18px;
      text-align: center;
    }
    .svg-container {
      padding: 6px 5px 6px 15px;
      color: $dark_gray;
      vertical-align: middle;
      width: 30px;
      display: inline-block;
    }
    .title-container {
      position: relative;
      .title {
        font-size: 26px;
        color: #4b4b4b;
        margin: 0px auto 40px auto;
        text-align: center;
        font-weight: bold;
      }
      .set-language {
        color: #fff;
        position: absolute;
        top: 5px;
        right: 0px;
      }
    }
    .button-container{
      margin-top: 30px;
      position: relative;
      text-align: center;
    }
    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: $dark_gray;
      cursor: pointer;
      user-select: none;
    }
  }
</style>
