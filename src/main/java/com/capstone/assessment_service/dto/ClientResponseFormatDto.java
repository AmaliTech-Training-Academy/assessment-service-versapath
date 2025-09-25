package com.capstone.assessment_service.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientResponseFormatDto {
    private Boolean success;
    private String message;
    private Object data;
    private List<String> errors;
}
