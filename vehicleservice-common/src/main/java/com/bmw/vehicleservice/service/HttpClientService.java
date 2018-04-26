package com.bmw.vehicleservice.service;

import com.bmw.vehicleservice.model.HttpClentResponseModel;
import org.apache.http.impl.client.CloseableHttpClient;

import java.util.Map;

/**
 * Description: HttpClient服务
 * Copyright: Copyright (c) 2017
 * Company: BMS
 *
 * @author: hants
 * @created: 2018/4/25
 */
public interface HttpClientService {
    /**
     * 获取CloseableHttpClient对象
     * @return
     */
    public CloseableHttpClient getCloseableHttpClient();

    /**
     * Http Post方式，通过json格式的参数访问请求，参数放在body体中
     * @param url
     * @param jsonValue
     * @return
     */
    public HttpClentResponseModel httpPostByJson(String url, String jsonValue);

    /**
     * 有post参数的http post请求，参数在from中
     * @param url
     * @param paramMap
     * @return
     */
    public HttpClentResponseModel httpPost(String url, Map<String, String> paramMap);

    /**
     * 无post参数的http post请求
     * @param url
     * @return
     */
    public HttpClentResponseModel httpPost(String url);

    /**
     * http get请求方式
     * @param url
     * @return
     */
    public HttpClentResponseModel httpGet(String url);

}
