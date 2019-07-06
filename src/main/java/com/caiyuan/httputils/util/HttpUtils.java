package com.caiyuan.httputils.util;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils<K, V> {

    private static Logger logger = LoggerFactory.getLogger(HttpUtils.class);

    /**
     * 请求类参数和普通参数
     * @param url 请求url
     * @param request 请求实体
     * @param params 请求参数
     * @param <T> 请求实体的类型
     * @return
     */
    public static <T> String getNormalResponse(String url, T request, List<NameValuePair> params) {
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //响应模型
        CloseableHttpResponse response = null;
        String responseBody = "";
        HttpPost httpPost = new HttpPost(url);
        Map result = new HashMap();
        try {
            if(params != null) {
                URI uri = new URIBuilder().setParameters(params).build();
                httpPost.setURI(uri);
            }
            String requestJson = JSON.toJSONString(request);
            StringEntity entity = new StringEntity(requestJson, "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader("content-Type", "application/json;charset=utf8");

            //响应模型
            response = httpClient.execute(httpPost);
            HttpEntity responseEntity = response.getEntity();
            //响应的内容
            if(responseEntity != null) {
                responseBody = JSON.toJSON(EntityUtils.toString(responseEntity)).toString();

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if(response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return responseBody;
        }
    }
}
