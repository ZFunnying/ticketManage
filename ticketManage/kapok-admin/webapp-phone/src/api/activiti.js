import request from "../utils/request";

class ActivitiApi {
  getModelListData(data) {
    return request({
      url: "/activiti/models/modelListData",
      method: "get",
      data,
    });
  }
  getActivitiProcessList(data) {
    return request({
      url: `/activiti_process/listData?${data}`,
      method: "get",
    });
  }
  modelsDelete(data) {
    return request({
      url: "/activiti/models/delete/" + data,
      method: "get",
    });
  }
  activitiModelDeployment(data) {
    return request({
      url: "/activiti/models/deployment/" + data,
      method: "get",
    });
  }
  actTaskHistoricFlow(data) {
    return request({
      url: "/actTask/historicFlow/" + data,
      method: "get",
    });
  }
  actProcessInsGetFinishedProcess(data) {
    return request({
      url: `/actProcessIns/getFinishedProcess?${data}`,
      method: "get",
    });
  }
  actTaskDoneList(data) {
    return request({
      url: `/actTask/doneList?${data}`,
      method: "get",
    });
  }
  actProcessInsGetRunningProcess(data) {
    return request({
      url: "/actProcessIns/getRunningProcess",
      method: "get",
    });
  }
  actProcessInsDelHistoricInsByIds(data) {
    return request({
      url: `/actProcessIns/delHistoricInsByIds/${data}`,
      method: "post",
    });
  }
  actTaskDeleteHistoric(data) {
    return request({
      url: `/actTask/deleteHistoric/${data}`,
      method: "post",
    });
  }
  actTaskTodoList(data) {
    return request({
      url: `/actTask/todoList?${data}`,
      method: "get",
    });
  }
  activitiProcessGetNextNode(data) {
    return request({
      url: `/activiti_process/getNextNode?${data}`,
      method: "get",
    });
  }
  activitiProcessGetNode(data) {
    return request({
      url: `/activiti_process/getNode?${data}`,
      method: "get",
    });
  }
  actTaskGetBackList(data) {
    return request({
      url: `/actTask/getBackList/${data}`,
      method: "get",
    });
  }
  actTaskPass(data) {
    return request({
      url: "/actTask/pass",
      method: "post",
      data,
    });
  }
  actTaskPassAll(data) {
    return request({
      url: "/actTask/passAll",
      method: "post",
      data,
    });
  }
  actTaskBackAll(data) {
    return request({
      url: "/actTask/backAll",
      method: "post",
      data,
    });
  }
  actProcessInsUpdateInsStatus(data) {
    return request({
      url: "/actProcessIns/updateInsStatus/",
      method: "post",
      data,
    });
  }
  actTaskBackToTask(data) {
    return request({
      url: "/actTask/backToTask",
      method: "post",
      data,
    });
  }
  actTaskBack(data) {
    return request({
      url: "/actTask/back",
      method: "post",
      data,
    });
  }
  actTaskDelegate(data) {
    return request({
      url: "/actTask/delegate",
      method: "post",
      data,
    });
  }
  actProcessInsDelInsByIds(id, data) {
    return request({
      url: `/actProcessIns/delInsByIds/${data}`,
      method: "post",
      data,
    });
  }
  editNodeUser(data) {
    return request({
      url: "/activiti_process/editNodeUser",
      method: "post",
      data,
    });
  }
  getProcessNode(data) {
    return request({
      url: "/activiti_process/getProcessNode",
      method: "post",
      data,
    });
  }
  updateInfo(data) {
    return request({
      url: "/activiti_process/updateInfo",
      method: "post",
      data,
    });
  }
  convertToModel(data) {
    return request({
      url: "/activiti_process/convertToModel",
      method: "post",
      data,
    });
  }
  actProcessDelByIds(data) {
    return request({
      url: "/activiti_process/delByIds",
      method: "post",
      data,
    });
  }
  actProcessUpdateStatus(data) {
    return request({
      url: "/activiti_process/updateStatus",
      method: "post",
      data,
    });
  }
  getActBusinessData(data) {
    return request({
      url: "/actBusiness/listData",
      method: "post",
      data,
    });
  }
  actBusinessDelByIds(data) {
    return request({
      url: "/actBusiness/delByIds",
      method: "post",
      data,
    });
  }
  actProcessInsGetFirstNode(data) {
    return request({
      url: `/actProcessIns/getFirstNode?${data}`,
      method: "get",
    });
  }
  actKapProcessInsGetFirstNode(data) {
    return request({
      url: `/actProcessIns/getKapFirstNode?${data}`,
      method: "get",
    });
  }
  applyBusiness(data) {
    return request({
      url: "/actBusiness/apply",
      method: "post",
      data,
    });
  }
  startProcessBusiness(data) {
        return request({
            url: "/mobile/process/startProcess",
            method: "post",
            data,
        });
    }
  cancelBusinessApply(data) {
    return request({
      url: "/actBusiness/cancel",
      method: "post",
      data,
    });
  }
  actBusinessGetForm(data) {
    return request({
      url: `/actBusiness/getForm?${data}`,
      method: "get",
    });
  }
  actBusinessAddApply(data) {
    return request({
      url: "/actBusiness/add",
      method: "post",
      data,
    });
  }
  actBusinessEditForm(data) {
    return request({
      url: "/actBusiness/editForm",
      method: "post",
      data,
    });
  }
  getFormDetail(pageCode) {
    return request({
      url: `/kapform/workPage?pageCode=${pageCode}`,
      method: "get",
    });
  }
  formOrderData(pageCode) {
    return request({
      url: `/mobile/work/formOrderData?pageCode=${pageCode}`,
      method: "get",
    });
  }
  addFormRecord(url, data) {
    return request({
      url: url,
      method: "post",
      data,
    });
  }
  getFormList(searchWord) {
    return request({
      url: `/actkapform/formlist?searchWord=${searchWord}`,
      method: "get",
    });
  }
  addPageRel(data) {
    return request({
      url: "/actkapform/addpagerel",
      method: "post",
      data,
    });
  }
  getPagerelList(procDefId) {
    return request({
      url: `/actkapform/listpagerel?procDefId=${procDefId}`,
      method: "get",
    });
  }
  delPageRel(data) {
    return request({
      url: "/actkapform/delpagerel",
      method: "post",
      data,
    });
  }
  getAssignees(orderId) {
    return request({
      url: `/actProcessIns/getKapFirstNode?orderId=${orderId}`,
      method: "get",
    });
  }
  getMyPageList() {
    return request({
      url: `/actkapform/mypagelist`,
      method: "get",
    });
  }
  delByIds(data) {
    return request({
      url: `/activiti_process/delByIds`,
      method: "post",
      data
    });
  }
  myWaitWork(pageSize, pageNum) {
    return request({
      url: `/mobile/work/myWaitWork?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: "post",
    });
  }
  myFinishWork(pageSize, pageNum) {
    return request({
      url: `/mobile/work/myFinishWork?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: "post",
    });
  }
  getMyMsgList(pageSize, pageNum) {
    return request({
      url: `/mobile/work/myPage?pageSize=${pageSize}&pageNum=${pageNum}`,
      method: "post",
    });
  }
  uploadFile(data,fileName){
    return request({
      url: `/minio/upload?fileName=${fileName}`,
      method: "post",
      data,
    });
  }
}

export default new ActivitiApi();
