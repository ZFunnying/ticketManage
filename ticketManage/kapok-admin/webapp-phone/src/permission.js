import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from './utils/auth' // get token from cookie
import { message } from 'ant-design-vue'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect', '/register'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // determine whether the user has logged in
  const hasToken = getToken()
  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done()
    } else {
      // determine whether the user has obtained his permission roles through getInfo
      const hasRoles = store.getters.roles && store.getters.roles.length > 0
      if (hasRoles) {
        next()
      } else {
        try {
          
          // get user info
          // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
          const res = await store.dispatch('user/getInfo')
          const roles = res.data.data.roles
          // generate accessible routes map based on roles
          await store.dispatch('dict/getAllDictMap')
          const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
          // dynamically add accessible routes，vue-touter4取消了addRoutes方法，必须逐个添加
          accessRoutes.map((route) => {
            router.addRoute(route)
          })
          

          // hack method to ensure that addRoutes is complete
          // set the replace: true, so the navigation will not leave a history record
          next({ ...to, replace: true })
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()

        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
  // if(loading !== undefined){
  //   loading.close();
  // }
})
