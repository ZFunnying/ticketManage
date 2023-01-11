import request from '@/utils/request'

class GroupInfoApi {
  query(data) {
    return request({
      url: '/groupinfo/list',
      method: 'get',
      data
    })
  }

  queryByCond(data, pageSize, pageNum) {
    return request({
      url: `/groupinfo/queryByCond?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: 'post',
      data
    })
  }

  add(data) {
    return request({
      url: '/groupinfo/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/groupinfo/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/groupinfo/delete',
      method: 'post',
      data
    })
  }
}

export default new GroupInfoApi()
