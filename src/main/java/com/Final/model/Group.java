package com.Final.model;

/**
 * Created by tb on 2016/10/29.
 */
public class Group {

    private String id;
    private String name;

    public Group() {
    }

    public Group(String id, String name) {

        this.id = id;
        this.name = name;
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
}
