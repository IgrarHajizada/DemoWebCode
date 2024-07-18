package com.example.demoCode.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data

public class DataInfo {
    private List<ReqContact> contacts;
    private List<Images> images;
    @JsonProperty("gps_info")
    private List<GPSInfo> gpsInfo;
    private List<Records> records;
}
