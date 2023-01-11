import * as VueRouter from "vue-router"; //使用Vue-router@4

export const constantRoutes = [
  {
    name: "Login",
    path: "/login",
    hidden: true,
    component: (res) => import("../views/login.vue"),
  },
  {
    name: "Main",
    path: "/main",
    component: (res) => import("../views/main.vue"),
  },
  {
    name: "Menu",
    path: "/menu",
    component: (res) => import("../views/menu.vue"),
  },
  {
    name: "Settings",
    path: "/settings",
    component: (res) => import("../views/settings.vue"),
  },
  {
    name: "404",
    path: "/404",
    // 在vite中引入文件后缀不可省略，否则编译报错
    component: () => import("../views/404/index.vue"),
  },
  {
    path: "/500",
    component: () => import("../views/errorPages/500.vue"),
    hidden: true,
  },
  {
    path: '',
    component: import('../views/main.vue'),
    redirect: 'dashboard',
    children: [
      {
        path: '/dashboard',
        component: () => import('../views/main.vue'),
        name: 'Dashboard',
        meta: { title: '首页', icon: 'HomeOutlined', noCache: true }
      }
    ]
  }
];

export const asyncRoutes = [
  {
    path: '\/*',   // *不能直接定义在路由路径，需要加转义字符
    redirect: '/404', 
    hidden: true
  }
]

const router = VueRouter.createRouter({
  history: VueRouter.createWebHashHistory(),
  routes: constantRoutes
})

export default router;
