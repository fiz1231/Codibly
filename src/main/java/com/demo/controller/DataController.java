package com.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;

import com.demo.service.Facade;
import com.demo.dao.DataDao;
import com.demo.dao.DataLoadingWindowRequest;

@RestController
@RequestMapping("/v1/codibly")
@AllArgsConstructor
public class DataController {
    private final Facade facade;

    @CrossOrigin
    @GetMapping()
    public ResponseEntity<? super DataDao> getDataOnEnergyMix(){

        DataDao body = facade.getData();
        return ResponseEntity.ok(body);
    }

    
    @CrossOrigin
    @PostMapping
    public ResponseEntity<? super DataDao> getLoadingWindow(@RequestBody DataLoadingWindowRequest timeWindow){
        DataDao body = facade.getLoadinfWindown(timeWindow);
        return ResponseEntity.ok(body);
    }
}
