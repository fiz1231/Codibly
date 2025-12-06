package com.demo.service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.springframework.stereotype.Service;

import com.demo.api.ApiRepository;

import com.demo.dao.windonData.WindonDataOutput;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DataLoadingWindowDeterminer {
    private final ApiRepository apiRepository;
    public WindonDataOutput getDataLoadingWindow(int windowDurationHours){
        ZonedDateTime from =ZonedDateTime.now().withHour(0).withMinute(0).withNano(0).withZoneSameInstant(ZoneId.of("Z")).plusDays(1);
        ZonedDateTime to =from.plusDays(1);
        return (apiRepository.calculateOptimalChargingWindow(from, to,windowDurationHours));
    }
}
