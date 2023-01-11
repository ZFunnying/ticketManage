import axios from "axios";
import store from "../store";
import {
  getToken,
  setToken,
  removeToken,
  getTokenExprieTime,
  setTokenExprieTime,
} from "./auth";
import { uuid } from "vue-uuid";
import { Toast } from "vant";
// create an axios instance
const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_API, // api 的 base_url
  withCredentials: true, // 跨域请求时发送 cookies
  // timeout: 5000 // request timeout
});
// request interceptor
service.interceptors.request.use(
  (config) => {
    // Do something before request is sent
    if (store.getters.token) {
      // 让每个请求携带token-- ['Authorization']为自定义key 请根据实际情况自行修改
      config.headers["Authorization"] = getToken();
    }
    // TODO: 待修改：现在依赖Sentry自动附加相关HTTP头，无需添加
    config.headers["trace-id"] = uuid.v1();
    config.headers["trace-user"] = store.getters.userid;
    return config;
  },
  (error) => {
    // Do something with request error
    console.log(error); // for debug
    return Promise.reject(error);
  }
);

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get information such as headers or status
   * Please return  response => response
   */
  /**
   * 下面的注释为通过在response里，自定义code来标示请求状态
   * 当code返回如下情况则说明权限有问题，登出并返回到登录页
   * 如想通过 XMLHttpRequest 来状态码标识 逻辑可写在下面error中
   * 以下代码均为样例，请结合自生需求加以修改，若不需要，则可删除
   */
  (response) => {
    if (response.headers.authorization !== undefined) {
      setToken(response.headers.authorization);
      store.dispatch("user/SetToken", response.headers.authorization);
    }
    const res = response.data;
    if (res.code !== 200) {
      if (res.message === "") {
        res.message === "返回错误为空";
      }
      // 50008:非法的token; 50012:其他客户端登录了;  50014:Token 过期了;
      if (res.code === 50082) {
        // 请自行在引入 MessageBox
        // import { Message, MessageBox } from 'element-ui'
        Toast.fail("你已被登出，可以取消继续留在该页面，或者重新登录");
        store.dispatch("user/logout").then(() => {
          location.href = location.href.split('#')[0] + "#/"
          location.reload(); // 为了重新实例化vue-router对象 避免bug
        });
      }
      if (res.code === 50083) {
        // 请自行在引入 MessageBox
        // import { Message, MessageBox } from 'element-ui'
        Toast.fail(
          "您的账号已从别处登录，可以取消继续留在该页面，或者重新登录"
        );
        store.dispatch("user/logout").then(() => {
          location.href = location.href.split('#')[0] + "#/"
          location.reload(); // 为了重新实例化vue-router对象 避免bug
        });
      }
      if (res.code === 50081 || res.code === 50014 || res.code === 50015) {
        // 请自行在引入 MessageBox
        // import { Message, MessageBox } from 'element-ui'
        Toast.fail("安全认证过期，可以取消继续留在该页面，或者重新登录");
        store.dispatch("user/logout").then(() => {
          location.href = location.href.split('#')[0] + "#/"
          location.reload(); // 为了重新实例化vue-router对象 避免bug
        });
      }
      return response;
    } else {
      return response;
    }
  },
  (error) => {
    console.log("err" + error); // for debug
    if (error.message !== "") {
      Toast.fail(error.message);
    }
    return Promise.reject(error);
  }
);
export default service;
