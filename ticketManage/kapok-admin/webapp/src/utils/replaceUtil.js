/**
 * Created by RuanJunJie on 2019/1/7.
 */
export function replaceDoubleQuotationMarks(str) {
  if (!str) {
    return str
  } else {
    return str.replace(/(\")/g, '\\"')
  }
}

import Layout from '@/layout'

function getViews(path) {
  return resolve => {
    require.ensure([], (require) => {
      resolve(require('@/' + path + '.vue'))
    })
  }
}

export function changeToComponent(routeData) {
  const res = []
  for (let i = 0; i < routeData.length; i++) {
    const menu = Object.assign({}, routeData[i])
    if (menu.component === 'Layout') { // Layout组件特殊处理
      menu.component = Layout
    } else {
      menu.component = getViews(menu.component)
    }
    if (menu.children && menu.children.length) {
      menu.children = changeToComponent(menu.children)
    }

    res.push(menu)
  }
  return res
}

// import Layout from '@/views/layout/Layout' //Layout 是架构组件，不在后台返回，在文件里单独引入

// export function filterAsyncRouter(asyncRouterMap) { //遍历后台传来的路由字符串，转换为组件对象
//   const accessedRouters = asyncRouterMap.filter(route => {
//     if (route.component) {
//       if (route.component === 'Layout') {//Layout组件特殊处理
//         route.component = Layout
//       } else {
//         route.component =  () => import(route.component)
//       }
//     }

//     if (route.children && route.children.length) {
//       route.children = filterAsyncRouter(route.children)
//     }
//     return true
//   })
//   return accessedRouters
// }
