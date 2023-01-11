import request from '@/utils/request'

class RegisterApi {
  register(data) {
    return request({
      url: '/user/register',
      method: 'post',
      data
    })
  }
}

export default new RegisterApi()
