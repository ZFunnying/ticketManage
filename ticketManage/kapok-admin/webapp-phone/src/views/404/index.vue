<template>
  <!-- <van-nav-bar
    title="触达易"
    left-text="返回"
    left-arrow
    @click-left="backHome"
  ></van-nav-bar> -->
  <a-result status="404" title="404" sub-title="抱歉，你无权访问该页面！">
    <template #extra>
      <van-button type="danger" @click="backHome">返回首页</van-button>
    </template>
  </a-result>
</template>
<script setup>
import { useRouter } from "vue-router";

const router = useRouter();
const backHome = () => {
  router.push("/main");
};
</script>
