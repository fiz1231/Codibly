package com.demo.service;

import com.demo.service.DataGetterExternalApi;
import com.demo.dao.DataDao;
import com.demo.dao.DataLoadingWindowRequest;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class Facade {
    private final DataGetterExternalApi dataGetterExternalApi;
    private final DataLoadingWindowDeterminer dataLoadingWindowDeterminer;

    public DataDao getData(){
        return dataGetterExternalApi.getData();
    }
    public DataDao getLoadinfWindown(DataLoadingWindowRequest timeWindow){
        return dataLoadingWindowDeterminer.getDataLoadingWindow(timeWindow);
        
    }
}
