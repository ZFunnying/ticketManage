<template>
  <div class="login-container">
    <div class="title-left">
      <div class="title-container">
        <h1 id="one" class="title2" />
      </div>
    </div>
    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <el-card class="box-card">
        <div class="title-container">
          <h3 class="title">系统登录</h3>
        </div>
        <div class="text item">
          <el-form-item prop="username">
            <span class="svg-container">
              <svg-icon icon-class="user" />
            </span>
            <el-input
              v-model="loginForm.username"
              placeholder="请输入账号"
              name="username"
              type="text"
              tabindex="1"
              auto-complete="on"
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>

          <el-tooltip v-model="capsTooltip" content="大写输入已锁定" placement="right" manual>
            <el-form-item prop="password">
              <span class="svg-container">
                <svg-icon icon-class="password" />
              </span>
              <el-input
                :key="passwordType"
                ref="password"
                v-model="loginForm.password"
                :type="passwordType"
                placeholder="请输入密码"
                name="password"
                tabindex="2"
                auto-complete="on"
                @keyup.native="checkCapslock"
                @blur="capsTooltip = false"
                @keyup.enter.native="handleLogin"
              />
              <span class="show-pwd" @click="showPwd">
                <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
              </span>
            </el-form-item>
          </el-tooltip>
          <el-form-item prop="kaptcha" class="kaptchaItem">
            <span class="svg-container">
              <svg-icon icon-class="kaptcha" />
            </span>
            <el-input
              v-model="loginForm.kaptcha"
              placeholder="请输入验证码"
              name="kaptcha"
              type="text"
              tabindex="3"
              auto-complete="on"
              @keyup.enter.native="handleLogin"
            />
          </el-form-item>
          <div class="verifyCode">
            <img :src="imageUrl" style="cursor:pointer" @click="changeImg">
          </div>

          <div class="button-container">
            <el-button :loading="loading" type="primary" style="width:100%;height:50px" @click.native.prevent="handleLogin">登录</el-button>
          </div>
          <div class="tips">
            <span>还没有账号?</span>
            <a @click="goRegister">注册</a>
          </div>
        </div>
      </el-card>
    </el-form>
  </div>
</template>

<script>
import { validateAlphabets } from '@/utils/validate'
import { Message } from 'element-ui'
import URL from '@/api/api'
import sha256 from 'js-sha256'
import { init } from 'ityped'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      if (!validateAlphabets(value)) {
        callback(new Error('请输入正确的用户名'))
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
    return {
      loginForm: {
        username: '',
        password: '',
        kaptcha: ''
      },
      imageUrl: '',
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }],
        kaptcha: [{ required: true, trigger: 'blur', message: '请输入验证码' }]
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
  mounted() {
    this.changeImg()
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
    goRegister() {
      this.$router.push({ path: '/register' })
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
    },
    changeImg() {
      var timestamp = Date.parse(new Date())
      this.imageUrl = URL + '/defaultKaptcha?timestamp=' + timestamp
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          const params = Object.assign({}, this.loginForm)
          params.password = sha256(this.loginForm.password)
          this.$store.dispatch('user/login', params).then(() => {
            this.loading = false
            this.$router.push({ path: '/' })
          }).catch((res) => {
            if (res.message !== '') {
              Message.error(res.message)
            }
            this.changeImg()
            this.loading = false
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
    .login-container .el-input input{
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

  .login-container .box-card {
    width: 480px;
  }
  /* reset element-ui css */
  .login-container {
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

  .verifyCode{
    display: inline-block;
    top: 5px;
    position: relative;
    margin-left: 10px;
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
$bg:#2d3a4b;
$dark_gray:#889aa4;
.login-container {
  position: fixed;
  height: 100%;
  width: 100%;
  background-color: $bg;
  background-repeat:no-repeat;
  background-attachment:fixed;
  background-size: cover;
  background-position:right top;
  background-image: url("../../assets/pic/homepage1.jpg") ;
  .login-form {
    position: absolute;
    left: 50%;
    right: 0;
    width: 520px;
    max-width: 100%;
    padding: 35px 35px 15px 35px;
    margin: 120px auto;
  }
  .title-left {
    position: absolute;
    left: 0;
    top: 10%;
    right: 50%;
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
    .title2 {
      font-size: 60px;
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
  .kaptchaItem {
    width: 70%;
    display: inline-block;
    position: relative;
    top: -10px;
  }
}
</style>
