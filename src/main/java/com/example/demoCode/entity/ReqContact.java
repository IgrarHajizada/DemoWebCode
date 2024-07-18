package com.example.demoCode.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data

public class ReqContact {

    private Long id;
    @JsonProperty("time_modified")
    private Long timeModified;
    private String name;
    private List<String> numbers;
}
