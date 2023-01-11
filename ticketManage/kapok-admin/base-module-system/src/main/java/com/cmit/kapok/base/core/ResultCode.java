package com.cmit.kapok.base.core;

/**
 * 响应码枚举，参考HTTP状态码的语义
 */
public enum ResultCode {
    SUCCESS(200),//成功
    FAIL(400),//失败
    BAD_REQUEST(400),//坏请求
    TOKEN_EXPIRED(50014),//失败
    RELOGIN(50015),//失败
    UNAUTHORIZED(401),//未认证（签名错误）
    FORBIDDEN(403),//无权限（签名错误）
    NOT_FOUND(404),//接口不存在
    INTERNAL_SERVER_ERROR(500);//服务器内部错误

    public int code;

    ResultCode(int code) {
        this.code = code;
    }
}
