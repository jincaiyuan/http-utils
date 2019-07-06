package com.caiyuan.httputils.service.impl;

import com.caiyuan.httputils.service.SortService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SortServiceImpl implements SortService {
    @Override
    public List<Integer> sort(List<Integer> list) {
        Collections.sort(list);
        return list;
    }
}
