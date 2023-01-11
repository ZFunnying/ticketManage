<template>
  <van-nav-bar title="Kapok"></van-nav-bar>
  <van-swipe class="my-swipe" :autoplay="3000" indicator-color="white">
    <van-swipe-item>
      <img src="../assets/img/chart.jpeg" alt="" class="carousel" />
    </van-swipe-item>
    <van-swipe-item>
      <img src="../assets/img/chudayi2.jpeg" alt="" class="carousel" />
    </van-swipe-item>
  </van-swipe>
  <div class="appArea" v-for="item in EAIApps.apps" :key="item.path">
    <span class="title">{{ item.title }}</span>
    <van-row class="apps" v-if="item.hasOwnProperty('children')">
      <van-col span="8" v-for="child in item.children" :key="child.path">
        <div class="item" @click="jump(child)">
          <div class="circle">
            <p>{{ child.title.substring(0, 1) }}</p>
          </div>
          <p class="listItem">{{ child.title }}</p>
        </div>
      </van-col>
      <van-empty description="暂无菜单" v-if="EAIApps.apps.length == 0" />
    </van-row>
  </div>
  <van-tabbar v-model="active">
    <van-tabbar-item icon="home-o" @click="goToPage('main')">首页</van-tabbar-item>
    <van-tabbar-item icon="setting-o" @click="goToPage('settings')">设置</van-tabbar-item>
  </van-tabbar>
</template>

<script setup>
import { getInfo } from "../api/user";
import { ref, toRaw } from "vue";
import { useRouter } from "vue-router";
import { Toast } from "vant";
import store from "../store";
defineProps({
  msg: String,
});

const count = ref(0);
const router = useRouter();
const jump = (item) => {
  if (item.children && item.children.length != 0) {
    sessionStorage.item = JSON.stringify(item.children);
    sessionStorage.title = item.title;
    router.push("/menu");
  } else {
    router.push(item.path);
  }
};
const res = ref({});
const onClickLeft = () =>
  window.ado
    .close()
    .then((res) => {
      console.log(res);
    })
    .catch((error) => {
      console.log(error);
    });
const EAIApps = ref({
  apps: [],
});

const getRouterList = () => {
  let routes = store.getters.roleRoutes;
  console.log("route", routes);
  routes = toRaw(routes);
  if (routes) {
    for (let key of Object.keys(routes)) {
      if (routes[key].hasOwnProperty("children")) {
        routes[key].children = routes[key].children.filter(
          (item) => item.isShow
        );
      }
    }
    EAIApps.value.apps = routes.filter((item) => item.isShow);
  }
};

getRouterList();

const active = ref(0);
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
  margin: 15px 0px 5px;
  line-height: 36px;
  color: white;
}

.listItem {
  font-size: 14px;
  /* margin-left: 5px;
  width: 20%; */
}

.item {
  display: flex;
  flex-direction: column;
  align-items: center;
  list-style: none;
  text-align: center;
}

.appArea:nth-last-child(2) {
  padding-bottom: 20px;
}

.title {
  font-weight: bold;
  font-size: 16px;
}
</style>
