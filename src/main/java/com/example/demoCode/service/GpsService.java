package com.example.demoCode.service;

import com.example.demoCode.entity.GPSInfo;
import com.example.demoCode.repository.GPSInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GpsService {

    private final GPSInfoRepository gpsInfoRepository;


    public List<GPSInfo> getAllGPSInfo() {
        return gpsInfoRepository.findAll();
    }

}
