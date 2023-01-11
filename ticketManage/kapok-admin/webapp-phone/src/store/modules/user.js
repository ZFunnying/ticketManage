import {
  login,
  loginByPhone,
  logout,
  getInfo,
  modifyPassword,
  zqtSsoLogin
} from "../../api/user";
import {message} from "ant-design-vue"
import {
  getToken,
  setToken,
  removeToken,
  removeTokenExprieTime,
} from "../../utils/auth";
import router from "../../router";
import store from "../../store";
const state = {
  token: getToken(),
  name: "",
  avatar: "",
  introduction: "",
  roles: [],
  userid: "",
  rolesId: "",
};

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token;
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction;
  },
  SET_NAME: (state, name) => {
    state.name = name;
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar;
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles;
  },
  SET_ROLESID: (state, rolesId) => {
    state.rolesId = rolesId;
  },
  SET_USERID: (state, id) => {
    state.userid = id;
  },
};

const actions = {
  // user login
  login({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      login({
        username: userInfo.username.trim(),
        password: userInfo.password,
        kaptcha: userInfo.kaptcha,
      })
        .then((response) => {
          const data = response.data;
          if (data.code !== 200) {
            reject(data);
          }
          commit("SET_TOKEN", data.data.token);
          setToken(response.data.data.token);
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  loginByPhone({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      loginByPhone({
        username: userInfo.username.trim(),
        password: userInfo.password,
      })
        .then((response) => {
          console.log("res", response);
          const data = response.data;
          if (data.code !== 200) {
            reject(data);
          }
          commit("SET_TOKEN", data.data.token);
          setToken(response.data.data.token);
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  // 修改密码
  ModifyPassword({ commit }, userInfo) {
    return new Promise((resolve, reject) => {
      modifyPassword({
        id: store.getters.userid,
        originPassword: userInfo.originPassword,
        password: userInfo.pass,
      })
        .then((response) => {
          const data = response.data;
          if (data.code !== 200) {
            reject(data);
          }
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
  SetToken({ commit }, token) {
    return new Promise((resolve, reject) => {
        commit('SET_TOKEN', token)
        setToken(token)
        resolve();
    });
  },
  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token)
        .then((response) => {
          if (
            response.data.code === 50008 ||
            response.data.code === 50012 ||
            response.data.code === 50014
          ) {
            reject("你已被登出，请重新登录");
          }
          if (response.data.code !== 200) {
            reject(response.data.message);
          }
          const data = response.data.data;
          if (data.roles && data.roles.length > 0) {
            // 验证返回的roles是否是一个非空数组
            commit("SET_ROLES", data.roles);
            commit("SET_ROLESID", data.rolesId);
          } else {
            reject("getInfo: roles must be a non-null array !");
          }
          commit("SET_NAME", data.name);
          commit("SET_AVATAR", data.avatar);
          commit("SET_INTRODUCTION", data.introduction);
          commit("SET_USERID", data.id);
          resolve(response);
        })
        .catch((error) => {
          reject(error);
        });
    });
  },

  // user logout
  logout({ commit, state }) {
    return new Promise((resolve, reject) => {
      logout(state.token)
        .then(() => {
          commit("SET_TOKEN", "");
          commit("SET_ROLES", []);
          removeToken();
          removeTokenExprieTime();
          resolve();
        })
        .catch((error) => {
          reject(error);
        });
    });
  },

  // remove token
  resetToken({ commit }) {
    return new Promise((resolve) => {
      commit("SET_TOKEN", "");
      commit("SET_ROLES", []);
      removeToken();
      resolve();
    });
  },

  // Dynamically modify permissions
  changeRoles({ commit, dispatch }, role) {
    return new Promise(async (resolve) => {
      const token = role + "-token";

      commit("SET_TOKEN", token);
      setToken(token);

      const { roles } = await dispatch("getInfo");

      resetRouter();

      // generate accessible routes map based on roles
      const accessRoutes = await dispatch("permission/generateRoutes", roles, {
        root: true,
      });

      // dynamically add accessible routes
      // router.addRoutes(accessRoutes)
      accessRoutes.map((route) => {
        router.addRoute(route);
      });

      resolve();
    });
  },
  zqtLogin({ commit }){
    return new Promise((resolve, reject) => {
        let appId = 'CHUDAYI'
        window.ado.singleSignOn(appId).then(async function (res) {
            let data = res.data
            let token = data.token
            console.log('singleSignOn',res)
            await zqtSsoLogin({ token: token }).then(async response => {
                const data = response.data
                if (data.code === 200) {
                    console.log('zqtSsoLogin',data.data.token)
                    commit('SET_TOKEN', data.data.token)
                    setToken(data.data.token)
                    let params = {
                        appId: appId,
                        ssoTicket: data.data.token
                    }
                    window.ado.thirdLogin(params).then((res) => {
                        console.log('thirdLogin',res)
                    }).catch(error => {
                        console.log('thirdLogin',error)
                    });
                    resolve()
                } else {
                    message.error(data.message)
                    reject()
                }
            })
        }).catch(function (err) {
            console.log(err)
            reject(err)
        })
    })
},
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
};
