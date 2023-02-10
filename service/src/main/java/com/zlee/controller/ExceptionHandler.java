package com.zlee.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.zlee.Result.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author z-Lee
 * @date 2023-02-04-13:44
 */
@Controller
public class ExceptionHandler {

    @RequestMapping("/errorForward")
    public Result<Object> throwException(HttpServletRequest request) {
        Exception tokenException = (Exception) request.getAttribute("TokenException");
        throw new JWTVerificationException(tokenException.getMessage());

    }
}
