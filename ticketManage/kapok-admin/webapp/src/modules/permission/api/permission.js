import request from '@/utils/request'

class PermissionApi {
  query(data) {
    return request({
      url: '/permission/list',
      method: 'get',
      data
    })
  }
  updatePermission() {
    return request({
      url: 'permission/updatePermisstion',
      method: 'get'
    })
  }
  queryByCond(data, pageSize, pageNum) {
    return request({
      url: `/permission/queryByCond?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: 'post',
      data
    })
  }
  add(data) {
    return request({
      url: '/permission/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/permission/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/permission/delete',
      method: 'post',
      data
    })
  }
}

export default new PermissionApi()
