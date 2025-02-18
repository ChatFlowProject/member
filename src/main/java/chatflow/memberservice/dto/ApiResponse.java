package chatflow.memberservice.dto;

import org.springframework.http.HttpStatus;

public record ApiResponse(
        int status,
        String message,
        Object data
) {
    public static ApiResponse success(Object data) {
        return new ApiResponse(HttpStatus.OK.value(), null, data);
    }

    public static ApiResponse success() {
        return new ApiResponse(HttpStatus.OK.value(), null, null);
    }

    public static ApiResponse error(String message) {
        return new ApiResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message, null);
    }
}
