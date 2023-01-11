import store from '@/store'

export function getDicts(groupCode) {
  if (store.getters.dictsList[groupCode] && store.getters.dictsList[groupCode].length > 0) {
    return store.getters.dictsList[groupCode]
  } else {
    store.dispatch('dict/setDicts').then(() => {
      console.log('----> store ConditionType')
      return store.getters.dictsList[groupCode]
    })
  }
}
