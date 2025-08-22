package com.kenny.xphrdemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReportRequestDTO {
    private String from;
    private String to;
}
