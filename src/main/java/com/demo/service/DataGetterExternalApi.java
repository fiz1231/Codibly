package com.demo.service;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import com.demo.dao.DataDao;
@Service
@AllArgsConstructor
public class DataGetterExternalApi {
    public DataDao getData(){
        //temporary for controller design
        return new DataDao();
    }
}
