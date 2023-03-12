package com.zlee.tofu.feign.Result;

/**
 * @author z-Lee
 * @date 2023-01-15-15:51
 */
public class ResponseData {

    public static <T> Result<T> success() {
        return ResponseData.success(ResultsEnum.SUCCESS);
    }

    public static <T> Result<T> success(T data) {
        return ResponseData.success(ResultsEnum.SUCCESS,data);
    }

    public static <T> Result<T> fail() {
        return ResponseData.fail(ResultsEnum.FAIL);
    }

    public static <T> Result<T> fail(T data) {
        return ResponseData.fail(ResultsEnum.FAIL,data);
    }

    public static <T> Result<T> authError() {
        return ResponseData.fail(ResultsEnum.AUTH_ERROR);
    }

    public static <T> Result<T> authError(T data) {
        return ResponseData.fail(ResultsEnum.AUTH_ERROR,data);
    }

    public static <T> Result<T> success(ResultsEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <T> Result<T> success(ResultsEnum resultEnum, T data) {
        return new Result<>(resultEnum, data);
    }

    public static <T> Result<T> fail(ResultsEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <T> Result<T> fail(ResultsEnum resultEnum,T data) {
        return new Result<>(resultEnum,data);
    }

    public static <T> Result<T> authError(ResultsEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static <T> Result<T> authError(ResultsEnum resultEnum,T data) {
        return new Result<>(resultEnum,data);
    }
}
