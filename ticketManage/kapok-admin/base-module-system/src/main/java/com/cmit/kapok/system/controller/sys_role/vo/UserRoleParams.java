package com.cmit.kapok.system.controller.sys_role.vo;

import java.util.List;

public class UserRoleParams {
    private String id;
    private String userName;
    private List<String> checkedPart;

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setCheckedPart(List<String> checkedPart) {
        this.checkedPart = checkedPart;
    }

    public List<String> getCheckedPart() {
        return checkedPart;
    }
}
