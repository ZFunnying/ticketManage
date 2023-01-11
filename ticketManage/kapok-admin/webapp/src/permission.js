import router from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken, removeToken } from '@/utils/auth' // get token from cookie
import { Loading } from 'element-ui'
NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect', '/register'] // no redirect whitelist
let loading
router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()
  console.log('to', to.path)
  console.log('from', from.path)
  if (to.query.kapokssotoken) {
    console.log('to', to)
    removeToken()
    await store
      .dispatch('user/ssoLogin', { token: to.query.kapokssotoken })
      .then(() => {
        // console.log('query', to.query)
        delete to.query.kapokssotoken
        if (to.path) {
          next({ path: to.path, query: to.query })
        } else {
          next({ path: '/dashboard', query: to.query })
        }
        NProgress.done()
      })
      .catch((res) => {
        if (res.message !== '') {
          Message.error(res.message)
        }
        next({ path: '/500' })
        NProgress.done()
      })
  } else {
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
            loading = Loading.service({
              lock: true,
              text: '系统资源加载中',
              spinner: 'el-icon-loading',
              background: 'rgba(0, 0, 0, 0.8)'
            })
            // get user info
            // note: roles must be a object array! such as: ['admin'] or ,['developer','editor']
            const res = await store.dispatch('user/getInfo')
            const roles = res.data.data.roles
            // generate accessible routes map based on roles
            await store.dispatch('dict/getAllDictMap')
            const accessRoutes = await store.dispatch('permission/generateRoutes', roles)
            // dynamically add accessible routes
            router.addRoutes(accessRoutes)

            // hack method to ensure that addRoutes is complete
            // set the replace: true, so the navigation will not leave a history record
            next({ ...to, replace: true })
          } catch (error) {
            // remove token and go to login page to re-login
            await store.dispatch('user/resetToken')
            Message.error(error || 'Has Error')
            next(`/login?redirect=${to.path}`)
            NProgress.done()
            loading.close()
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
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
  if (loading !== undefined) {
    loading.close()
  }
})
