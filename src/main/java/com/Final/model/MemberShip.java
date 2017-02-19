package com.Final.model;

/**
 * Created by tb on 2016/10/30.
 */
public class MemberShip {


    private User user; // 用户
    private Group group; // 角色

    public MemberShip() {
    }

    public MemberShip(User user, Group group) {
        this.user = user;
        this.group = group;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
