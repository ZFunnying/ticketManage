package com.cmit.kapok.base.core;

public enum ErrorCode {

    TOKEN_EXPIRED(50081,"token expired"),
    TOKEN_ERROR(50082,"token eroor"),
    TOKEN_INVALID(50083,"token invalid"),
    SUCCESS(20000,"成功");

    private int value;
    private String desc;

    private ErrorCode(int value, String desc) {
        this.setValue(value);
        this.setDesc(desc);
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "[" + this.value + "]" + this.desc;
    }
}
