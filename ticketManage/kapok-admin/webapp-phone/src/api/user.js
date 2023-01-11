import request from "../utils/request";

export function login(data) {
  return request({
    url: "/user/login",
    method: "post",
    data,
  });
}

export function loginByPhone(data) {
  return request({
    url: "/corpuser/login",
    method: "post",
    data,
  });
}

export function getInfo(token) {
  return request({
    url: "/user/info",
    method: "get",
  });
}

export function generateSSOToken(data) {
    return request({
        url: "/sysuser/generateSSOToken",
        method: "post",
        data
    });
}

export function logout(token) {
  return request({
    url: "/user/userLogout",
    method: "post",
    data: {
      token,
    },
  });
}

export function modifyPassword(data) {
  return request({
    url: "/user/changePassword",
    method: "post",
    data,
  });
}

export function zqtSsoLogin(data) {
  return request({
    url: "/user/zqtSsoLogin",
    method: "post",
    data
  });
}
