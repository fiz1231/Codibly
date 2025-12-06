package com.demo.service;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.demo.api.ApiRepository;
import com.demo.dao.DataDao;
@Service
@AllArgsConstructor
public class DataGetterExternalApi {
    private final ApiRepository apiRepository;
    public DataDao getData(){
        //temporary for controller design
        return new DataDao();
    }
}
