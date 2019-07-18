package com.gongxin.mobilecommand.domain;

import java.io.Serializable;

/**
 *
 */
public class LoginUser implements Serializable {
    /*
     {
  "authFlag": "string",
  "createTime": "2019-07-15T06:25:15.567Z",
  "createUser": "string",
  "dept": "string",
  "email": "string",
  "flag": "string",
  "head": "string",
  "id": 0,
  "loginName": "string",
  "nickName": "string",
  "password": "string",
  "phone": "string",
  "status": "string",
  "updateTime": "2019-07-15T06:25:15.567Z",
  "updateUser": "string"
}
     * */

    private String loginName;
    private String token;
    private String appUserId;
    private String dept;
    private String email;
    private String head;
    private String id;
    private String  nickName;
    private String phone;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
