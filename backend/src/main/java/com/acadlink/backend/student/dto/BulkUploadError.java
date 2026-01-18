package com.acadlink.backend.student.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkUploadError {
     private int row;
    private String email;
    private String reason;
}
