import request from '@/utils/request'

class SysUserApi {
  query(data) {
    return request({
      url: '/sysuser/list',
      method: 'get',
      data
    })
  }

  queryByCond(data, pageSize, pageNum) {
    return request({
      url: `/sysuser/queryByCond?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: 'post',
      data
    })
  }

  add(data) {
    return request({
      url: '/sysuser/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/sysuser/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/sysuser/delete',
      method: 'post',
      data
    })
  }
  queryIdAndUsername() {
    return request({
      url: '/sysuser/queryIdAndUsername',
      method: 'post'
    })
  }
  getName() {
    return request({
      url: '/sysuser/getName',
      method: 'get'
    })
  }
}

export default new SysUserApi()
