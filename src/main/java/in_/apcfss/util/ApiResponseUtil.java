package in_.apcfss.util;


import in_.apcfss.dto.ApiResponse;

import java.util.Collections;
import java.util.List;

public class ApiResponseUtil {

    private ApiResponseUtil() {
    }

    public static <T> ApiResponse<T> success(T data, String message, String path) {
//        path = Optional.ofNullable(path).orElse(UriUtil.getFullRequestURI());
        return ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .uri(path)
                .errors(null)
                .errorCode(0)
                .build();
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return success(data, message, null);
    }

    public static <T> ApiResponse<T> error(List<String> errors, String message, int errorCode, String path) {
        return ApiResponse.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .timestamp(System.currentTimeMillis())
                .uri(path)
                .errors(errors)
                .errorCode(errorCode)
                .build();
    }

    public static <T> ApiResponse<T> error(String error, String message, int errorCode, String path) {
        return error(Collections.singletonList(error), message, errorCode, path);
    }

}