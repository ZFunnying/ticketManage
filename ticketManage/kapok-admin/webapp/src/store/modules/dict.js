import GroupItemApi from '@/modules/item_group/api/groupItem'
const state = {
  dictsList: {},
  dictMap: []
}

const mutations = {
  SET_DICT_MAP: (state, dictMap) => {
    state.dictMap = dictMap
  },
  SET_DICTS: (state, dictsList) => {
    state.dictsList = dictsList
  }
}

const actions = {
  getAllDictMap({ commit }) {
    return new Promise(function(resolve, reject) {
      GroupItemApi
        .getAllDictMap()
        .then(rspData => {
          if (rspData.data.code === 200) {
            commit('SET_DICT_MAP', rspData.data.data)
            resolve()
          }
        })
        .catch(error => {
          console.log('加载字典数据失败')
          reject(error)
        })
    })
  },
  setDicts({ commit }, groupCode) {
    return new Promise((resolve, reject) => {
      GroupItemApi.queryOptionByGroupCode(groupCode).then(response => {
        const data = response.data
        if (data.code === 200) {
          const data = response.data.data
          const item = {}
          item[groupCode] = data
          commit('SET_DICTS', item)
        }
        resolve()
      })
        .catch(error => {
          reject(error)
        })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
