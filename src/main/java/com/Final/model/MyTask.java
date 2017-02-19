package com.Final.model;

import java.util.Date;

/**
 * Created by tb on 2016/10/30.
 */
public class MyTask {

    private String id; // 任务id
    private String name; // 任务名称
    private Date createTime; // 创建时间
    private Date endTime; // 结束时间

    public MyTask(String id, String name, Date createTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.endTime = endTime;
    }

    public MyTask() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }


}
