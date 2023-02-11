package com.zlee.advice;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zlee.Result.ResponseData;
import com.zlee.Result.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 * @author Administrator
 */
@RestControllerAdvice
public class ExceptionControllerAdvice {
    /** 专门用来捕获和处理Controller层的异常 */
    @ExceptionHandler(Exception.class)
    public Result<Object> customException(Exception e) {

        return ResponseData.fail(e.getMessage());
    }

    @ExceptionHandler(JWTVerificationException.class)
    public Result<Object> tokenExpiredException(JWTVerificationException e) {
        return ResponseData.authError(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public Result<Object> tokenExpiredException(RuntimeException e) {
        return ResponseData.fail(e.getMessage());
    }

    /** 专门用来捕获和处理Controller层的空指针异常 */
    @ExceptionHandler(NullPointerException.class)
    public ModelAndView nullPointerExceptionHandler(NullPointerException e)
    {
        ModelAndView mv = new ModelAndView(new MappingJackson2JsonView());
        mv.addObject("success",false);
        mv.addObject("mesg","请求发生了空指针异常，请稍后再试");
        return mv;
   }
}