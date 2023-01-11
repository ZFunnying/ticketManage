import { asyncRoutes, constantRoutes } from "../../router";
import MenuApi from "../../modules/menu/api/menu-api";
import { changeToComponent } from "../../utils/replaceUtil";
import store from "../../store";
/**
 * 通过meta.role判断是否与当前用户权限匹配
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some((role) => route.meta.roles.includes(role));
  } else {
    return true;
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = [];

  routes.forEach((route) => {
    const tmp = { ...route };
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles);
      }
      res.push(tmp);
    }
  });

  return res;
}

const state = {
  routes: [],
  addRoutes: [],
};

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes;
    state.routes = constantRoutes.concat(routes);
  },
  SET_ROLE_ROUTES: (state, routes) => {
    state.roleRoutes = routes;
  },
};

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise((resolve) => {
      let accessedRouters;
      // 拼接路由
      MenuApi.getRouters(store.getters.userid).then((response) => {
        const routerData = response.data;
        if (routerData.code === 200) {
          commit("SET_ROLE_ROUTES", routerData.data);
          const changeData = changeToComponent(routerData.data);
          // if (roles.includes('admin')) {
          //   accessedRouters = asyncRoutes.concat(changeData)
          // } else {
          //   accessedRouters = filterAsyncRoutes(asyncRoutes.concat(changeData), roles)
          // }
          accessedRouters = filterAsyncRoutes(
            asyncRoutes.concat(changeData),
            roles
          );
          console.log(accessedRouters);
          commit("SET_ROUTES", accessedRouters);
          resolve(accessedRouters);
        }
      });
    });
    // return new Promise(resolve => {
    //   let accessedRoutes
    //   if (roles.includes('admin')) {
    //     accessedRoutes = asyncRoutes
    //   } else {
    //     accessedRoutes = filterAsyncRoutes(asyncRoutes, roles)
    //   }
    //   commit('SET_ROUTES', accessedRoutes)
    //   resolve(accessedRoutes)
    // })
  },
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
