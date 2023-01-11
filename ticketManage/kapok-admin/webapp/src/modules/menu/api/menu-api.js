import request from '@/utils/request'

class MenuApi {
  // 获取路由表
  getRouters() {
    return request({
      url: '/sysmenu/getRouters',
      method: 'get'
    })
  }

  // 获取父菜单
  getParentNode() {
    return request({
      url: '/sysmenu/getParentNode',
      method: 'get'
    })
  }

  // 根据id获取子菜单
  getChildrenNode(parentId) {
    return request({
      url: `/sysmenu/getChildrenNode?parentId=${parentId}`,
      method: 'get'
    })
  }
  getVueModuleDirTree() {
    return request({
      url: '/sysmenu/getVueModuleDirTree',
      method: 'get'
    })
  }

  // 添加菜单
  add(data) {
    return request({
      url: '/sysmenu/add',
      method: 'post',
      data
    })
  }

  // 修改菜单信息
  update(data) {
    return request({
      url: '/sysmenu/update',
      method: 'post',
      data
    })
  }

  // 根据id删除菜单
  delete(data) {
    return request({
      url: '/sysmenu/delete',
      method: 'post',
      data
    })
  }
}

export default new MenuApi()
