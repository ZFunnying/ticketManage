<script>
import { defineComponent, ref, reactive } from 'vue';
import { DownOutlined,SettingOutlined } from '@ant-design/icons-vue';
import { useStore } from 'vuex'
import { message } from 'ant-design-vue';
import sha256 from 'js-sha256'

// const routes = [{
//   path: '',
//   breadcrumbName: 'kapok',
// }, {
//   path: 'dashboard',
//   breadcrumbName: '首页',
// }];
export default defineComponent({

  components: {
    DownOutlined,
    SettingOutlined
  },

  setup() {

    const formRef = ref()
    const formState = reactive({
      originPassword: '',
      pass: '',
      checkPass: ''
    })
    const visible = ref(false)
    const store = useStore()

    const validatePassword = (_rule, value) => {
      if (value.length < 6) {
        return Promise.reject('输入的密码不少于6位');
      } else {
        if (formState.checkPass !== '') {
          formRef.value.validateFields('checkPass')
        }
      }
      return Promise.resolve()

    }

    const validateConfirmPassword = (_rule, value) => {
      if (value === '') {
        return Promise.reject('请再次确认密码');
      } else if (value !== formState.pass) {
        return Promise.reject('两次输入密码不一样')
      } else {
        return Promise.resolve();
      }
    }

    const rules = {
      originPassword: [{
        required: true,
        message: '请输入原密码',
        trigger: 'blur'
      }],
      pass: [{
        required: true,
        validator: validatePassword,
        trigger: 'blur',
      }],
      checkPass: [{
        required: true,
        validator: validateConfirmPassword,
        trigger: 'blur',
      }],
    }

    const handleOk = () => {
      formRef.value.validate().then(() => {
        const params = Object.assign({}, formState)
        params.originPassword = sha256(formState.originPassword)
        params.pass = sha256(formState.pass)
        store.dispatch('user/ModifyPassword', params).then(() => {
          message.success('修改成功！')
          visible.value = false
          resetForm()
        }).catch(error => {
          if (error.message !== '') {
            message.error(error.message)
          }
        })

      }).catch(error => {
        console.log('error', error);
      });
    }

    const resetForm = () => {
      formRef.value.resetFields();
    };

    return {
      formRef,
      formState,
      rules,
      visible,
      labelCol: {
        span: 4,
      },
      wrapperCol: {
        span: 14,
      },
      handleOk
    };
  },
  mounted() {
    // console.log('mouted', this.$route)
    this.loadRoutes(this.$route.matched)
  },
  data() {
    return {
      routes: []
    }
  },
  watch: {
    $route(to, from) {
      // console.log(to);
      this.loadRoutes(to.matched)
    }
  },

  methods: {
    loadRoutes(matched = []) {
      let list = []
      matched.map(item => {
        if (item.path) {
          list.push(item)
        }
      })
      this.routes = list
    },
    async logout() {
      await this.$store.dispatch('user/logout')
      this.$router.push(`/login?redirect=${this.$route.fullPath}`)
    },
    goHome() {
      this.$router.push('/dashboard')
    }
  }

});
</script>

<template>
  <div class="nav-bar">
    <div>
      <div class="kapok-page-header">
        <!-- <span>一个灵活的前端框架</span> -->
      </div>
      <!-- <a-breadcrumb class="kapok-breadcrumb" :routes="routes">
        <template #itemRender="{ route }">
          <span
            v-if="routes.indexOf(route) === routes.length - 1"
            style="color:#fff"
          >{{ route.meta.title }}</span>
          <router-link v-else :to="route.path">{{ route.meta.title }}</router-link>
        </template>
      </a-breadcrumb> -->
    </div>

    <div class="tool-tip">
      <a-dropdown>
        <a class="ant-dropdown-link" @click.prevent>
          <a-avatar src="https://img2.baidu.com/it/u=3363874973,1210127772&fm=253&fmt=auto&app=138&f=JPEG?w=400&h=400" />
          <!-- <a-avatar>
            <template #icon>
              <UserOutlined />
            </template>
          </a-avatar> -->
          <SettingOutlined :style="{fontSize: '20px'}" />
          <!-- <DownOutlined /> -->
        </a>
        <template #overlay>
          <a-menu>
            <a-menu-item>
              <a href="javascript:;" @click="goHome">首页</a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;" @click="() => { visible = true }">修改密码</a>
            </a-menu-item>
            <a-menu-item>
              <a href="javascript:;" @click="logout">退出登录</a>
            </a-menu-item>
          </a-menu>
        </template>
      </a-dropdown>
    </div>

    <!-- 修改密码   start -->
    <a-modal v-model:visible="visible" title="修改密码" ok-text="确认" cancel-text="取消" @ok="handleOk">
      <a-form
        ref="formRef"
        :model="formState"
        :rules="rules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
      >
        <a-form-item has-feedback label="原密码" name="originPassword" placeholder="请输入原密码">
          <a-input v-model:value="formState.originPassword" />
        </a-form-item>
        <a-form-item has-feedback label="密码" name="pass" placeholder="请输入密码">
          <a-input-password v-model:value="formState.pass" />
        </a-form-item>
        <a-form-item has-feedback label="确认密码" name="checkPass" placeholder="请再次确认密码">
          <a-input-password v-model:value="formState.checkPass" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<style scoped>
.nav-bar {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  padding: 0 24px 0 0;
}
deep .ant-breadcrumb {
  text-align: left;
}
.kapok-page-header {
  text-align: left;
  font-size: 18px;
  height: 40px;
  font-weight: 500;
  font-family: -apple-system,BlinkMacSystemFont,Segoe UI,Roboto,Helvetica Neue,Arial,Noto Sans,sans-serif,"Apple Color Emoji","Segoe UI Emoji",Segoe UI Symbol,"Noto Color Emoji";
}
deep .ant-page-header-heading-sub-title {
  font-size: 16px;
}
/* deep .ant-breadcrumb a {
  color: #fff;
}
deep .ant-breadcrumb .ant-dropdown-open a {
  color: #40a9ff;
} */
.kapok-breadcrumb {
  text-align: left;
  padding: 12px 0;
  font-size: 16px;
  color: #fff;
}
.tool-tip {
  padding: 12px 0;
}
.tool-tip a {
  color: #fff;
}
.ant-avatar-image{
  margin-right: 10px;
}
</style>