package com.zlee.tofu.feign.Result;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author z-Lee
 * @date 2023-01-15-15:44
 */
@Data
public class Result<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public int code;

    private String msg;

    private T data;

    public Result(ResultsEnum resultsEnum) {
        this.code = resultsEnum.code;
        this.msg = resultsEnum.message;
    }

    public Result(ResultsEnum resultsEnum, T data){
        this.code = resultsEnum.code;
        this.msg = resultsEnum.message;
        this.data = data;
    }

}