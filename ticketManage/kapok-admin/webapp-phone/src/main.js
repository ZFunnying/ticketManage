import { createApp } from "vue";
import App from "./App.vue";

import Antd from "ant-design-vue";
import "ant-design-vue/dist/antd.css";

import Vant from "vant";
import "vant/lib/index.css";
import '@vant/touch-emulator';

import router from "./router";
import store from "./store";
import { Toast } from "vant";
import { removeToken } from "./utils/auth"; // get token from cookie
import "./permission";

import Vconsole from "vconsole";
if (process.env.NODE_ENV !== "production") {
    new Vconsole()
}

createApp(App).use(Antd).use(router).use(store).use(Vant).mount("#app");
