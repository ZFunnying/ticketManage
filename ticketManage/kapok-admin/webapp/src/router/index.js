import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noredirect           if `redirect:noredirect` will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    noCache: true                if set true, the page will no be cached(default is false)
    affix: true                  if set true, the tag will affix in the tags-view
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/redirect',
    component: Layout,
    hidden: true,
    children: [
      {
        path: '/redirect/:path*',
        component: () => import('@/views/redirect/index')
      }
    ]
  },
  {
    path: '/login',
    component: () => import('@/views/login/login'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/authRedirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/errorPage/404'),
    hidden: true
  },
  {
    path: '/register',
    component: () => import('@/modules/register/views/register'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/errorPage/401'),
    hidden: true
  },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [
      {
        path: 'dashboard',
        component: () => import('@/views/dashboard/index'),
        name: 'Dashboard',
        meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
      }
    ]
  }
]

/**
 * asyncRoutes
 * the routes that need to be dynamically loaded based on user roles
 */
export const asyncRoutes = [
  // {
  //   path: '/system',
  //   component: Layout,
  //   redirect: '/system/index',
  //   alwaysShow: true, // will always show the root menu
  //   meta: {
  //     title: '系统设置',
  //     icon: 'system',
  //     roles: ['super_admin'] // you can set roles in root nav
  //   },
  //   children: [
  //     {
  //       path: '/system/user',
  //       component: () => import('@/modules/user/views/sysUserList'),
  //       name: 'user',
  //       meta: {
  //         title: '用户设置',
  //         roles: ['super_admin'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: '/system/role',
  //       component: () => import('@/modules/sys_role/views/sysRoleList'),
  //       name: 'role',
  //       meta: {
  //         title: '组别设置',
  //         roles: ['super_admin'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: '/system/menu',
  //       component: () => import('@/modules/menu/views/sysMenuList'),
  //       name: 'sysMenuList',
  //       meta: {
  //         title: '菜单设置'
  //         // roles: ['super_admin'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: '/system/h5menu',
  //       component: () => import('@/modules/menu/views/h5MenuList'),
  //       name: 'h5MenuList',
  //       meta: {
  //         title: '移动端菜单设置'
  //         // roles: ['super_admin'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: '/system/permission',
  //       component: () => import('@/modules/permission/views/permissionList'),
  //       name: 'permission',
  //       meta: {
  //         title: '权限设置',
  //         roles: ['super_admin'] // or you can only set roles in sub nav
  //       }
  //     },
  //     {
  //       path: '/system/itemGroup',
  //       component: () => import('@/modules/item_group/views/groupItemList'),
  //       name: 'groupItemList',
  //       meta: { title: '数据字典' }
  //     },
  //     {
  //       path: '/logs',
  //       component: () => import('@/modules/system_logger/views/loggingEventList'),
  //       name: '系统日志',
  //       meta: {
  //         title: '系统日志',
  //         roles: ['super_admin'] // or you can only set roles in sub nav
  //       }
  //     }
  //   ]
  // },
  // {
  //   path: '/icon',
  //   component: Layout,
  //   children: [
  //     {
  //       path: 'index',
  //       component: () => import('@/views/svg-icons/index'),
  //       name: 'Icons',
  //       meta: { title: 'icons', icon: 'icon', noCache: true, roles: ['super_admin'] }
  //     }
  //   ]
  // },
  { path: '*', redirect: '/404', hidden: true }
  // 如果匹配不到路径，默认跳转到首页
  // { path: '*', redirect: '/dashboard', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history', // require service support
  base: process.env.VUE_APP_BASE_URI || '/',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
