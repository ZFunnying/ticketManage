<template>
  <van-nav-bar
    :title="title"
    left-text="返回"
    left-arrow
    @click-left="onClickLeft"
  ></van-nav-bar>
  <div class="appArea">
    <div class="apps">
      <div class="item" v-for="item in EAIApps.apps" :key="item.path">
        <div @click="jump(item)">
          <div class="circle">{{ item.title.substring(0, 1) }}</div>
          <div>
            <span class="listItem">{{ item.title }}</span>
          </div>
        </div>
      </div>
      <van-empty
        description="暂无菜单"
        v-if="EAIApps.apps.length == 0"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from "vue";
import { useRouter } from "vue-router";
import { Toast } from "vant";

onMounted(() => {
  init()
})
const init = () => {
  updateMenu()
}
const router = useRouter();

const EAIApps = ref({
  apps: [],
});
const title = ref("");

const updateMenu = ()=>{
  let routes = JSON.parse(sessionStorage.item);
  EAIApps.value.apps = routes.filter((item) => item.isShow);
  title.value = sessionStorage.title;
}

const jump = (item) => {
  if (item.children && item.children.length != 0) {
    let children = item.children;
    EAIApps.value.apps = children.filter((child) => child.isShow);
    title.value = item.title;
    // updateMenu()
  } else {
    router.push(item.path);
  }
};

const onClickLeft = () => {
  router.push("/main");
};

</script>

<style scoped>
.my-swipe .van-swipe-item {
  color: #fff;
  font-size: 20px;
  text-align: center;
}
.carousel {
  width: 100%;
  height: 180px;
}
.appArea {
  text-align: left;
  margin-left: 20px;
  margin-top: 10px;
  margin-bottom: 30px;
}
.circle {
  background: #5b7aff;
  border-radius: 50%;
  width: 36px;
  height: 36px;
  margin: 15px 20px 5px;
  line-height: 36px;
  color: white;
}
.listItem {
  font-size: 14px;
  margin-left: 5px;
  width: 20%;
}
.item {
  display: inline-block;
  list-style: none;
  text-align: center;
  margin-right: 10px;
  width: 20%;
  text-align: center;
  vertical-align: top;
}
</style>
