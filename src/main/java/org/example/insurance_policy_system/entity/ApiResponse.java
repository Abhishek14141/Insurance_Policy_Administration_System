package org.example.insurance_policy_system.entity;

import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@Builder

public class ApiResponse<T>  {
    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;


}
