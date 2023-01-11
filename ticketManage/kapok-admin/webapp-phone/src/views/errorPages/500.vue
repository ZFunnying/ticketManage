<template>
  <!-- <van-nav-bar
    title="触达易"
    left-text="返回"
    left-arrow
    @click-left="backHome"
  ></van-nav-bar> -->
  <a-result status="500" title="500" sub-title="对不起，服务器出错了">
    <template #extra>
      <van-button type="danger" @click="backHome">关闭</van-button>
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

<style scoped>
</style>
