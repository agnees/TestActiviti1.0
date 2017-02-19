package com.Final.model;

/**
 * Created by tb on 2016/10/30.
 */
public class User {

    private String id; // 主键 用户名
    private String firstName; // 姓
    private String lastName; // 名
    private String email; // 邮箱
    private String password; // 密码
    private String groups; // 用户拥有的角色集合 以逗号隔开

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getGroups() {
        return groups;
    }
    public void setGroups(String groups) {
        this.groups = groups;
    }

}
