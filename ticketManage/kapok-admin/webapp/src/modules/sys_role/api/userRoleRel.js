import request from '@/utils/request'

class UserRoleRelApi {
  query(data) {
    return request({
      url: '/userrolerel/list',
      method: 'get',
      data
    })
  }

  queryByCond(data) {
    return request({
      url: '/userrolerel/queryByCond',
      method: 'post',
      data
    })
  }
  updateUserRoleRel(data) {
    return request({
      url: '/userrolerel/updateUserRoleRel',
      method: 'post',
      data
    })
  }
  add(data) {
    return request({
      url: '/userrolerel/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/userrolerel/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/userrolerel/delete',
      method: 'post',
      data
    })
  }
}

export default new UserRoleRelApi()
