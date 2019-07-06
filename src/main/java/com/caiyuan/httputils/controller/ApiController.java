package com.caiyuan.httputils.controller;

import com.alibaba.fastjson.JSON;
import com.caiyuan.httputils.entity.request.Request;
import com.caiyuan.httputils.entity.response.Response;
import com.caiyuan.httputils.service.SortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private SortService sortService;

    @PostMapping("/service")
    public String serviceHttp(@RequestBody Request request) {
        Response response = new Response();
        Map map = new HashMap();
        List<Integer> sorted = sortService.sort(request.getUnsortedList());
        response.setSortedList(sorted);
        if(sorted == null || sorted.size() == 0){
            map.put("msg", "list has no data");
            response.setResult(map);
        } else {
            map.put("msg", "sort successfully");
            response.setResult(map);
        }
        return JSON.toJSONString(response);
    }
}
