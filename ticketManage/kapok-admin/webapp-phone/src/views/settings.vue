<template>
  <van-nav-bar title="设置"></van-nav-bar>
  <img :src="store.getters.avatar" alt="" width="150" height="150" class="avatar">
  <div class="name">{{store.getters.name}}</div>
  <div>
    <van-button type="primary" @click="logout" class="logout"
      >退出登录</van-button
    >
  </div>
  <van-tabbar v-model="active">
    <van-tabbar-item icon="home-o" @click="goToPage('main')"
      >首页</van-tabbar-item
    >
    <van-tabbar-item icon="setting-o" @click="goToPage('settings')"
      >设置</van-tabbar-item
    >
  </van-tabbar>
</template>

<script setup>
import { useRouter } from "vue-router";
import { ref } from "vue";
import store from "@/store";
import { Toast } from "vant";

const router = useRouter();

let logout = async () => {
  await store.dispatch("user/logout");
  Toast.success("退出登录成功");
  router.push(`/login?redirect=${router.fullPath}`);
};

const active = ref(1);
const goToPage = (name) => {
  switch (name) {
    case "main":
      router.push("Main");
      break;
    case "settings":
      router.push("Settings");
      break;
  }
};
</script>

<style scoped>
.avatar {
  border: 1px solid blue;
  margin-top: 20px;
  border-radius: 50%;
}
.name {
  margin-top: 20px;
  font-weight: bold;
}
.logout {
  margin-top: 20px;
}
</style>
