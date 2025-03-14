package scdy.userservice.common.advice;

import lombok.Getter;

@Getter
public class ApiResponse<T>{
    private String status;
    private String message;
    private T data;

    public ApiResponse(String message, T data) {
        this.status = "success"; // 고정된 성공 상태
        this.message = message;
        this.data = data;
    }

    public ApiResponse(String message) {
        this.status = "success";
        this.message = message;
    }

    public ApiResponse(String error, String message, Object o) {
    }


    // 성공 응답을 쉽게 만들기 위한 static 메서드
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(message, data);
    }

    // 성공 응답을 쉽게 만들기 위한 static 메서드
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("error", message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("error", message, data);
    }


}