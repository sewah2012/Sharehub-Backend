package com.luslusdawmpfe.PFEBackent.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponseDto {
    private Map<String, Object> response;
}
