package com.caiyuan.httputils.controller;

import com.alibaba.fastjson.JSON;
import com.caiyuan.httputils.entity.request.Request;
import com.caiyuan.httputils.entity.response.Response;
import com.caiyuan.httputils.util.HttpUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/http")
public class TestController {

    @Value("${url}")
    private String url;

    private Request request;

    @PostConstruct
    private void init() {
        request = new Request();
        Integer[] nums = {10,4,3,1,7,2,9,5,6,8};
        List<Integer> unsorted = Arrays.asList(nums);
        request.setUnsortedList(unsorted);
    }

    @RequestMapping("/test")
    @ResponseBody
    public String testHttp(){
        String responseBody = HttpUtils.getNormalResponse(url, request, null);
        Response response = JSON.parseObject(responseBody, Response.class);
        if(response != null ) {
            List<Integer> sorted = response.getSortedList();
            return JSON.toJSONString(sorted);
        }
        return "bad response";
    }
}
