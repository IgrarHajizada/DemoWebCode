package com.example.demoCode.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data

public class ReqContact {

    private Long id;
    private Long time_modified;
    private String name;
    private List<String> numbers;


}
