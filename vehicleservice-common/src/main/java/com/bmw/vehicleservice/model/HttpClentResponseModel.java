package com.bmw.vehicleservice.model;

/**
 * Description: 所有第三方接口返回的实体类
 * Copyright: Copyright (c) 2017
 * Company: BMW
 *
 * @author: hants
 * @created: 2018/4/25
 */
public class HttpClentResponseModel {

    private int statusCode;
    private String content;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
