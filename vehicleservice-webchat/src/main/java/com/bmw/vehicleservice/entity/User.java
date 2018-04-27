package com.bmw.vehicleservice.entity;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = -5164102540549630747L;

    private String userid;

    private String username;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }
}