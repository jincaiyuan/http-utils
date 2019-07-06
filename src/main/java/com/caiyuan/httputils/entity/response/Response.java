package com.caiyuan.httputils.entity.response;

import java.util.List;
import java.util.Map;

public class Response {

    private List<Integer> sortedList;
    Map result;

    public List<Integer> getSortedList() {
        return sortedList;
    }

    public void setSortedList(List<Integer> sortedList) {
        this.sortedList = sortedList;
    }

    public Map getResult() {
        return result;
    }

    public void setResult(Map result) {
        this.result = result;
    }
}
