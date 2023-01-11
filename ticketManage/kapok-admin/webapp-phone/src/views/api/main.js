import request from "../../utils/request";

export function getPhoneRouterList(userId) {
  return request({
    url: `/h5menu/getRouters?userId=${userId}`,
    method: "get",
  });
}

export function getPhoneRouterChildrenList(parentId) {
  return request({
    url: `/h5menu/getChildrenNode?parentId=${parentId}`,
    method: "get",
  });
}
