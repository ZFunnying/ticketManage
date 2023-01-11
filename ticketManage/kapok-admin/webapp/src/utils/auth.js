import Cookies from 'js-cookie'

const TokenKey = 'Kapok-Admin-Admin-Token'
const TokenExpireTimeKey = 'Kapok-Admin-Token-Expire-Time'
export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
export function removeTokenExprieTime() {
  return Cookies.remove(TokenExpireTimeKey)
}
export function setTokenExprieTime(tokenExpireTime) {
  return Cookies.set(TokenExpireTimeKey, tokenExpireTime)
}

export function getTokenExprieTime() {
  return Cookies.get(TokenExpireTimeKey)
}
