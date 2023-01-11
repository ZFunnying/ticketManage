import request from '@/utils/request'

class LoggingEventApi {
  query(data) {
    return request({
      url: '/loggingevent/list',
      method: 'get',
      data
    })
  }
  getClassList() {
    return request({
      url: '/loggingevent/classList',
      method: 'get'
    })
  }
  getLogFileNameOptions() {
    return request({
      url: '/loggingevent/getLogFileNameOptions',
      method: 'get'
    })
  }
  getLogFileContext(logName,start,end,ReverseView,DisplayAll,keyword) {
    return request({
      url: `/loggingevent/getLogFileContext?fileName=${logName}&startLine=${start}
      &endLine=${end}&ReverseView=${ReverseView}&DisplayAll=${DisplayAll}&keyword=${keyword}`,
      method: 'get'
    })
  }
  getLogFile(logName) {
    return request({
      url: `/loggingevent/getLogFile?fileName=${logName}`,
      method: 'get',
      responseType: 'blob'
    })
  }
  getMethodList() {
    return request({
      url: '/loggingevent/methodList',
      method: 'get'
    })
  }

  queryByCond(data, pageSize, pageNum) {
    return request({
      url: `/loggingevent/queryByCond?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: 'post',
      data
    })
  }
  getException(eventId) {
    return request({
      url: `/loggingeventexception/getException?eventId=${eventId}`,
      method: 'get'
    })
  }
  add(data) {
    return request({
      url: '/loggingevent/add',
      method: 'post',
      data
    })
  }
  update(data) {
    return request({
      url: '/loggingevent/update',
      method: 'post',
      data
    })
  }
  delete(data) {
    return request({
      url: '/loggingevent/delete',
      method: 'post',
      data
    })
  }
}

export default new LoggingEventApi()
