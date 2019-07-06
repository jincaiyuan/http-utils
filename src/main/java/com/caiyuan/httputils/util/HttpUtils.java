package com.caiyuan.httputils.util;

import com.alibaba.fastjson.JSON;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
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


    /**
     * 支持文件通过接口方式调用（还未测试）
     * @param url 调用url
     * @param file 文件对象
     * @param fileParamName 接口那边的文件参数名
     * @param params 其他参数
     * @return
     */
    public static String getMultipartResponse(String url, File file, String fileParamName, Map<String, String> params) {
        MultipartFile multipartFile = convertFile(file);
        String fileName = multipartFile.getOriginalFilename();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        //响应模型
        CloseableHttpResponse response = null;
        String responseBody = "";
        HttpPost httpPost = new HttpPost(url);
        try {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(Charset.forName("utf-8"));
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
            builder.addBinaryBody(fileParamName, multipartFile.getInputStream(), ContentType.MULTIPART_FORM_DATA, fileName);// 文件流
            for (Map.Entry<String, String> e : params.entrySet()) {
                builder.addTextBody(e.getKey(), e.getValue());// 类似浏览器表单提交，对应input的name和value
            }
            //响应模型
            HttpEntity entity = builder.build();
            httpPost.setEntity(entity);
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


    /**
     * 文件对象转成MultipartFile
     * @param file
     * @return
     */
    private static MultipartFile convertFile(File file) {
        return new CommonsMultipartFile(createFileItem(file));
    }

    private static FileItem createFileItem(File file) {
        FileItemFactory factory = new DiskFileItemFactory(16, null);
        String filePath = file.getPath();
        String textFieldName = "textField";
        int num = filePath.lastIndexOf(".");
        String extFile = filePath.substring(num);
        FileItem item = factory.createItem(textFieldName, "text/plain", true, "MyFileName");
        File newfile = new File(filePath);
        int bytesRead = 0;
        byte[] buffer = new byte[8192];
        try {
            FileInputStream fis = new FileInputStream(newfile);
            OutputStream os = item.getOutputStream();
            while ((bytesRead = fis.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return item;
    }
}
