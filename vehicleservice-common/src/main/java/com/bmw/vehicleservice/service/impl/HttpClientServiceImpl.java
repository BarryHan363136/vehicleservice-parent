package com.bmw.vehicleservice.service.impl;

import com.bmw.vehicleservice.model.HttpClentResponseModel;
import com.bmw.vehicleservice.service.HttpClientService;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Description: HttpClient服务
 * Copyright: Copyright (c) 2017
 * Company: BMW
 *
 * @author: hants
 * @created: 2018/4/25
 */
@Service("httpClientService")
public class HttpClientServiceImpl implements HttpClientService {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientServiceImpl.class);

    private static final String CONTENT_TYPE_JSON = "application/json";
    /** 创建HttpClient */
    private CloseableHttpClient closeableHttpClient;

    @PostConstruct
    public void initialize() {
        if (logger.isDebugEnabled()) {
            logger.debug("HttpClientService initialize begin");
        }
        try {
            // 创建HttpClientBuilder
            HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
            // HttpClient
            closeableHttpClient = httpClientBuilder.build();
        } catch (Exception e) {
            logger.error("init http client error", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("HttpClientService initialize end");
        }
    }

    @PreDestroy
    public void destroy() {
        if (logger.isDebugEnabled()) {
            logger.debug("HttpClientService destroy begin");
        }
        // 释放资源
        try {
            if (null != closeableHttpClient) {
                closeableHttpClient.close();
            }
        } catch (IOException e) {
            logger.error("closeable http client error", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("HttpClientService destroy end");
        }
    }

    @Override
    public CloseableHttpClient getCloseableHttpClient() {
        return closeableHttpClient;
    }

    @Override
    public HttpClentResponseModel httpPostByJson(String url, String jsonValue) {
        logger.info("=====================>Http post url:"+ url+",jsonValue:"+jsonValue);
        HttpClentResponseModel responseModel = new HttpClentResponseModel();
        String httpEntityStr = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(RequestConfig.DEFAULT);
            httpPost.setHeader(HTTP.CONTENT_TYPE, CONTENT_TYPE_JSON);
            StringEntity stringEntity = this.getStringEntity(jsonValue);
            httpPost.setEntity(stringEntity);
            // post请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            // getEntity()
            HttpEntity httpEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("=====================>Http status code:" + statusCode);
            responseModel.setStatusCode(statusCode);
            if (httpEntity != null) {
                // 打印响应内容
                httpEntityStr = EntityUtils.toString(httpEntity, "UTF-8");
                logger.info("=====================>API返回值为:"+ httpEntityStr);
            }
        } catch (Exception e) {
            logger.error("httpPost error", e);
        }
        if(null != httpEntityStr){
            responseModel.setContent(httpEntityStr);
            logger.info("=====================>API返回值为:"+ httpEntityStr);
        }
        return responseModel;
    }

    @Override
    public HttpClentResponseModel httpPost(String url, Map<String, String> paramMap) {
        logger.info("=====================>Http post url:"+ url);
        HttpClentResponseModel responseModel = new HttpClentResponseModel();
        String httpEntityStr = null;
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(RequestConfig.DEFAULT);
            UrlEncodedFormEntity entity = this.getUrlEncodedFormEntity(paramMap);
            httpPost.setEntity(entity);
            // post请求
            HttpResponse httpResponse = closeableHttpClient.execute(httpPost);
            // getEntity()
            HttpEntity httpEntity = httpResponse.getEntity();
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("=====================>Http status code:" + statusCode);
            responseModel.setStatusCode(statusCode);
            if (httpEntity != null) {
                // 打印响应内容
                httpEntityStr = EntityUtils.toString(httpEntity, "UTF-8");
                logger.info("=====================>API返回值为:"+ httpEntityStr);
            }
        } catch (Exception e) {
            logger.error("httpPost error", e);
        }
        if(null != httpEntityStr){
            responseModel.setContent(httpEntityStr);
            logger.info("=====================>API返回值为:"+ httpEntityStr);
        }
        return responseModel;
    }

    @Override
    public HttpClentResponseModel httpPost(String url) {
        return this.httpPost(url,null);
    }

    @Override
    public HttpClentResponseModel httpGet(String url) {
        logger.info("=====================>Http get url:"+ url);
        HttpClentResponseModel responseModel = new HttpClentResponseModel();
        String httpEntityStr = null;
        try {
            HttpGet httpget = new HttpGet(url);
            CloseableHttpResponse httpResponse = closeableHttpClient.execute(httpget);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            logger.info("=====================>Http status code:" + httpResponse.getStatusLine().getStatusCode());
            responseModel.setStatusCode(httpResponse.getStatusLine().getStatusCode());
            HttpEntity entity = httpResponse.getEntity();
            if (entity != null) {
                httpEntityStr = EntityUtils.toString(entity, "UTF-8");
                logger.info("=====================>API返回值为:"+ httpEntityStr);
            }
        } catch (IOException e) {
            logger.error("httpGet error", e);
        }
        if(null != httpEntityStr){
            responseModel.setContent(httpEntityStr);
            logger.info("=====================>API返回值为:"+ httpEntityStr);
        }
        return responseModel;
    }

    public StringEntity getStringEntity(String jsonValue) throws Exception {
        return new StringEntity(jsonValue,"UTF-8");
    }

    public UrlEncodedFormEntity getUrlEncodedFormEntity(Map<String, String> paramMap) throws Exception {
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        if (null != paramMap) {
            Iterator<Map.Entry<String, String>> iter = paramMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry<String, String>) iter.next();
                formparams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                logger.info("=====================>paramName=" + entry.getKey() + ",paramValue=" + entry.getValue());
            }
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        return entity;
    }

}
