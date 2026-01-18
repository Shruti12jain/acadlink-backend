package com.acadlink.backend.student.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BulkUploadResponse {
        private int totalRows;
    private int successful;
    private int failed;
    private int skipped;
    private List<BulkUploadError> errors = new ArrayList<>();
}
