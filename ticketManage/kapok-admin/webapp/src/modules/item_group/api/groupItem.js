import request from '@/utils/request'

class GroupItemApi {
  query(data) {
    return request({
      url: '/groupitem/list',
      method: 'get',
      data
    })
  }
  queryServiceType(data, pageSize, pageNum) {
    return request({
      url: `/groupitem/queryServiceType?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: 'post',
      data
    })
  }
  getAllDictMap() {
    return request({
      url: '/groupitem/getAllDictMap',
      method: 'post'
    })
  }
  queryByCond(data) {
    return request({
      url: '/groupitem/queryByCond',
      method: 'post',
      data
    })
  }
  queryAllByCode(groupCode) {
    return request({
      url: `/groupitem/queryAllByCode?groupCode=${groupCode}`,
      method: 'get'
    })
  }
  queryOptionByGroupCode(groupCode) {
    return request({
      url: `/groupitem/queryOptionByGroupCode?groupCode=${groupCode}`,
      method: 'get'
    })
  }
  add(data) {
    return request({
      url: '/groupitem/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/groupitem/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/groupitem/delete',
      method: 'post',
      data
    })
  }
}

export default new GroupItemApi()
