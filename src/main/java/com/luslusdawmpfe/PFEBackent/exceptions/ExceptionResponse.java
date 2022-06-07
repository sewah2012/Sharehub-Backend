package com.luslusdawmpfe.PFEBackent.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
    private String message;
    private HttpStatus status;
}
