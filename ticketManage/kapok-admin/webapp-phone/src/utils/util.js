export function filterObj(obj) {
  if (!(typeof obj == "object")) {
    return;
  }

  for (let key in obj) {
    if (
      obj.hasOwnProperty(key) &&
      (obj[key] == null || obj[key] == undefined || obj[key] === "")
    ) {
      delete obj[key];
    }
  }
  return obj;
}

export function processTime(str) {
  if (str) return str.replace("T", " ");
  return "无";
}

export function processText(str) {
  if (!str || str == "") return "无";
  else return str;
}

export function processTimeStamp(timestamp, number) {
  var date = new Date(timestamp);
  //时间戳为10位需*1000，时间戳为13位的话不需乘1000
  var Y = date.getFullYear() + "-";
  var M =
    (date.getMonth() + 1 < 10
      ? "0" + (date.getMonth() + 1)
      : date.getMonth() + 1) + "-";
  var D = (date.getDate() < 10 ? "0" + date.getDate() : date.getDate()) + " ";
  var h =
    (date.getHours() < 10 ? "0" + date.getHours() : date.getHours()) + ":";
  var m =
    (date.getMinutes() < 10 ? "0" + date.getMinutes() : date.getMinutes()) +
    ":";
  var s = date.getSeconds() < 10 ? "0" + date.getSeconds() : date.getSeconds();

  var res = Y + M + D + h + m + s;
  if (number) {
    return res.substring(0, number);
  }
  return res;
}
