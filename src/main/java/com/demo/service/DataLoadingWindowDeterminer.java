package com.demo.service;

import org.springframework.stereotype.Service;

import com.demo.dao.DataDao;
import com.demo.dao.DataLoadingWindowRequest;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataLoadingWindowDeterminer {
    //Temporary for controller design
    public DataDao getDataLoadingWindow(DataLoadingWindowRequest timeWindow){
        return new DataDao();
    }
}
