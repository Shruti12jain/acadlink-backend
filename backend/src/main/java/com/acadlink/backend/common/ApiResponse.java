package com.acadlink.backend.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private ErrorCode errorCode;
    private Instant timestamp = Instant.now();

    public ApiResponse(boolean success, String message, T data, ErrorCode errorCode) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.errorCode = errorCode;
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null);
    }

    public static <T> ApiResponse<T> error(String message, ErrorCode errorCode) {
        return new ApiResponse<>(false, message, null, errorCode);
    }

    // getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public ErrorCode getErrorCode() { return errorCode; }
    public Instant getTimestamp() { return timestamp; }
}
