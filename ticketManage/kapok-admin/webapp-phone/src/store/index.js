import { createStore } from "vuex";
import getters from "./getters";
import user from "./modules/user";
import permission from "./modules/permission";
import dict from "./modules/dict";
import app from "./modules/app";

// vue3 vite直接使用require报错，官方推荐解决方式为使用import.meta.globEager
// const modulesFiles = import.meta.globEager("./modules/*.js")

// const modules = modulesFiles.keys().reduce((modules, modulePath) => {
//   // set './app.js' => 'app'
//   const moduleName = modulePath.replace(/^\.\/(.*)\.\w+$/, '$1')
//   const value = modulesFiles(modulePath)
//   modules[moduleName] = value.default
//   return modules
// }, {})

const store = createStore({
  modules: {
    app,
    user,
    permission,
    dict,
  },
  permission,
  user,
  getters,
});

export default store;
