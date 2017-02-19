package com.Final.model;

import java.util.Date;

/**
 * Created by tb on 2016/10/30.
 */
public class Leave {

    private Integer id; // 编号
    private User user; // 请假人
    private Date leaveDate; // 请假日期
    private Integer leaveDays; // 请假天数
    private String leaveReason; // 请假原因
    private String state; // 审核状态  未提交  审核中 审核通过 审核未通过
    private String processInstanceId; // 流程实例id

    public Leave(Integer id, User user, Date leaveDate, Integer leaveDays, String leaveReason, String state, String processInstanceId) {
        this.id = id;
        this.user = user;
        this.leaveDate = leaveDate;
        this.leaveDays = leaveDays;
        this.leaveReason = leaveReason;
        this.state = state;
        this.processInstanceId = processInstanceId;
    }

    public Leave() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public Integer getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(Integer leaveDays) {
        this.leaveDays = leaveDays;
    }

    public String getLeaveReason() {
        return leaveReason;
    }

    public void setLeaveReason(String leaveReason) {
        this.leaveReason = leaveReason;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }
}
