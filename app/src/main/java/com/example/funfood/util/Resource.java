package com.example.funfood.util;
import com.example.funfood.data.remote.dto.ApiResponse;

public class Resource<T> {

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    private final Status status;
    private final T data;
    private final String message;

    private final ApiResponse<T> apiResponse;

    private Resource(Status status, T data, String message, ApiResponse<T> apiResponse) {
        this.status = status;
        this.data = data;
        this.message = message;
        this.apiResponse = apiResponse;
    }

    public static <T> Resource<T> success(T data) {
        return new Resource<>(Status.SUCCESS, data, null, null);
    }

    public static <T> Resource<T> error(String message, T data) {
        return new Resource<>(Status.ERROR, data, message, null);
    }

    public static <T> Resource<T> loading(T data) {
        return new Resource<>(Status.LOADING, data, null, null);
    }

    // Thêm các hàm static mới để chứa ApiResponse
    public static <T> Resource<T> success(T data, ApiResponse<T> apiResponse) {
        return new Resource<>(Status.SUCCESS, data, null, apiResponse);
    }

    public static <T> Resource<T> error(String message, T data, ApiResponse<T> apiResponse) {
        return new Resource<>(Status.ERROR, data, message, apiResponse);
    }

    // Thêm getter này
    public ApiResponse<T> getApiResponse() {
        return apiResponse;
    }

    public Status getStatus() {
        return status;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public boolean isError() {
        return status == Status.ERROR;
    }

    public boolean isLoading() {
        return status == Status.LOADING;
    }
}