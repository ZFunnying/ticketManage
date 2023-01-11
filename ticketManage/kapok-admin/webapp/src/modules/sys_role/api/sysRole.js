import request from '@/utils/request'

class SysRoleApi {
  query(data) {
    return request({
      url: '/sysrole/list',
      method: 'get',
      data
    })
  }

  queryByCond(data, pageSize, pageNum) {
    return request({
      url: `/sysrole/queryByCond?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: 'post',
      data
    })
  }
  queryUserRole(id) {
    return request({
      url: `/sysrole/queryRoleById?id=${id}`,
      method: 'get'
    })
  }

  add(data) {
    return request({
      url: '/sysrole/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/sysrole/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/sysrole/delete',
      method: 'post',
      data
    })
  }
}

export default new SysRoleApi()
