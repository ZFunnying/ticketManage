
<template>
  <div class="login-page">
    <img src="../assets/logo.png" class="logo" />
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
          v-model="formState.username"
          label="用户名"
          placeholder="请输入用户名"
          clearable
          :rules="[{ required: true, message: '请填写用户名' }]"
        >
        </van-field>
        <van-field
          v-model="formState.password"
          type="password"
          clearable
          label="密码"
          placeholder="请输入密码"
          :rules="[{ required: true, message: '请输入密码' }]"
        />
        <van-field
          v-model="formState.kaptcha"
          clearable
          label="验证码"
          placeholder="请输入验证码"
          :rules="[{ required: true, message: '请输入验证码' }]"
        >
          <template #button>
            <div>
              <div class="verifyCode">
                <img
                  :src="imageUrl"
                  style="cursor: pointer"
                  @click="changeImg"
                />
              </div>
            </div>
          </template>
        </van-field>
      </van-cell-group>

      <div class="loginBtn">
        <van-button round block type="primary" native-type="submit"
          >登录</van-button
        >
      </div>
    </van-form>
    <van-picker
      v-show="selectAreaModal"
      title="请选择区域"
      :columns="columns"
      @confirm="onConfirm"
      @cancel="onCancel"
      @change="onChange"
    />
  </div>
</template>

<script setup>
import { defineComponent, reactive, ref, createApp } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { Toast } from "vant";
import URL from "../api/api";
import sha256 from "js-sha256";

const router = useRouter();
const store = useStore();
const formRef = ref();
const formState = reactive({
  username: "",
  password: "",
  kaptcha: "",
});

const onSubmit = () => {
  console.log(import.meta.env.VITE_APP_BASE_API);
  const params = Object.assign({}, formState);
  params.password = sha256(formState.password);
  store
    .dispatch("user/login", params)
    .then(() => {
      Toast.success("登录成功");
      router.push("/main");
    })
    .catch((res) => {
      if (res.message !== "") {
        Toast.fail(res.message);
        changeImg();
      }
    });
};

const resetForm = () => {
  formRef.value.resetFields();
};

const imageUrl = ref("");
const changeImg = () => {
  var timestamp = Date.parse(new Date());
  imageUrl.value = URL + "/defaultKaptcha?timestamp=" + timestamp;
};
changeImg();
const selectArea = () => {
  selectAreaModal.value = true;
};
</script>

<style scoped>
.login-page {
  background: #ececec;
  background-size: cover;
  overflow: hidden;
  height: 100vh;
  width: 100%;
}
.logo {
  height: 60px;
  margin-top: 30px;
  margin-bottom: 60px;
}
.verifyCode {
  width: 20%;
}
.loginBtn {
  width: 90%;
  margin: 80px auto;
}
.van-nav-bar {
  background: unset;
}
.verifyCode {
  width: 20%;
}
</style>