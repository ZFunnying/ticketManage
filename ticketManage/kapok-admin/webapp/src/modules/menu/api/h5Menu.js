import request from '@/utils/request'

class H5MenuApi {
  query(data) {
    return request({
      url: '/h5menu/list',
      method: 'get',
      data
    })
  }

  queryByCond(data, pageSize, pageNum) {
    return request({
      url: `/h5menu/queryByCond?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: 'post',
      data
    })
  }
  add(data) {
    return request({
      url: '/h5menu/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/h5menu/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/h5menu/delete',
      method: 'post',
      data
    })
  }
  // 获取父菜单
  getParentNode() {
    return request({
      url: '/h5menu/getParentNode',
      method: 'get'
    })
  }
  // 根据id获取子菜单
  getChildrenNode(parentId) {
    return request({
      url: `/h5menu/getChildrenNode?parentId=${parentId}`,
      method: 'get'
    })
  }
}

export default new H5MenuApi()
